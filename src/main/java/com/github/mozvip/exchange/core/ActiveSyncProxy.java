package com.github.mozvip.exchange.core;

import com.github.mozvip.exchange.ExchangeProxyConfiguration;
import com.github.mozvip.exchange.wbxml.WbxmlUtils;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import okhttp3.*;
import okhttp3.Request.Builder;
import okio.ByteString;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.kxml2.kdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActiveSyncProxy {

	private final static Logger LOGGER = LoggerFactory.getLogger(ActiveSyncProxy.class);

	private ExchangeProxyConfiguration configuration;

	private Map<String, String> overrides;
	private AtomicLong exchangeId = new AtomicLong(0);

	public ActiveSyncProxy(ExchangeProxyConfiguration configuration){
	    this.configuration = configuration;
	}
    
    public void start() {

		final String exchangeBaseURL = "https://" + configuration.getServerHost();
		
		overrides = new HashMap<>();
		overrides.put("DeviceType", configuration.getDefaultDeviceType());
		overrides.put("UserAgent", configuration.getDefaultUserAgent());
		overrides.put("User-Agent", configuration.getDefaultUserAgent());
		
		overrides.put("OS", configuration.getDefaultOs());
		overrides.put("OSLanguage", configuration.getDefaultOsLanguage());

		overrides.put("FriendlyName", configuration.getDefaultFriendlyName());
		overrides.put("Model", configuration.getDefaultModel());

        if (configuration.getDefaultPhoneNumber() != null) {
            overrides.put("PhoneNumber", configuration.getDefaultPhoneNumber());
        }
		if (configuration.getDefaultIMEI() != null) {
            overrides.put("IMEI", configuration.getDefaultIMEI());
        }
        if (configuration.getDefaultMobileOperator() != null) {
            overrides.put("MobileOperator", configuration.getDefaultMobileOperator());
        }

		OkHttpClient.Builder b = new OkHttpClient.Builder();
		b.readTimeout(360, TimeUnit.SECONDS);
		b.connectTimeout(360, TimeUnit.SECONDS);
		final OkHttpClient client = b.build();

		Undertow server = Undertow.builder()
				.setServerOption(UndertowOptions.IDLE_TIMEOUT, 360 * 1000)
				.setServerOption(UndertowOptions.REQUEST_PARSE_TIMEOUT, -1)
				.setServerOption(UndertowOptions.NO_REQUEST_TIMEOUT, 360 * 1000)
				.addHttpListener(configuration.getProxyPort(), "0.0.0.0").setHandler(new HttpHandler() {

			public void handleRequest(HttpServerExchange exchange) throws Exception {

				if (exchange.isInIoThread()) {
					exchange.dispatch(this);
					return;
				}
				
				long id = exchangeId.getAndIncrement();
				
				LOGGER.info("ID {} ================================================== New request", id );
				
				Map<String, String> originalValues = new HashMap<>();
				
				ActiveSyncRequest request = buildMirrorRequest(id, exchange, originalValues);
				Call call = client.newCall(request.getRequest());
				Response response = call.execute();
				
				LOGGER.info("ID {} ==== Response from {} : {}", id, configuration.getServerHost(), response.toString());
				
				okhttp3.Headers headers = response.headers();
				
				String contentType = null;
				
				long contentLength = 0;
				for (String name : headers.names()) {
					LOGGER.info("ID {} Response Header : {}={}", id, HttpString.tryFromString(name), headers.get(name));

					if (name.equals("Content-Length")) {
						contentLength = Long.parseLong( headers.get(name) );
					} else {
						exchange.getResponseHeaders().put(HttpString.tryFromString(name), headers.get(name));
					}
					
					if (name.equals("Content-Type")) {
						contentType = headers.get("Content-Type");
						LOGGER.info("ID {} ==== Response Content-Type : {}", id, contentType);
					}
				
				}
				
				exchange.setStatusCode( response.code() );
				
				if (contentLength > 0) {

					try (ResponseBody body = response.body()) {
						if (request.getQuery() != null && request.getQuery().command != null && (request.getQuery().command.isResponseMustBeDumped() || request.getQuery().command.isResponseMustBeModified())) {
							byte[] originalContent = body.bytes();

							contentLength = originalContent.length;

							String str ;
							if (contentType.startsWith("application/vnd.ms-sync")) {
								if (request.getQuery().command.isResponseMustBeDumped()) {
									dumpToFile(request.getQuery().deviceId, "Response", request.getQuery().command, originalContent, contentType);
								}
								if (request.getQuery().command.isResponseMustBeModified()) {
									byte[] content = injectValues(id, contentType, originalContent, originalValues);
									contentLength = content.length;
									str = new String(content, "UTF-8");
								} else {
									str = new String(originalContent);
								}
							} else {
								str = new String(originalContent);
							}
							exchange.getResponseHeaders().put(HttpString.tryFromString("Content-Length"), contentLength);
							exchange.getResponseSender().send(str);
						} else {
							ByteBuffer byteBuffer = ByteBuffer.wrap(body.bytes());
							exchange.getResponseSender().send(byteBuffer);
						}
					}
				} else {
					exchange.endExchange();
				}

				LOGGER.info("ID{} ==== Exchange Closed", id);
			}
			
			private ActiveSyncRequest buildMirrorRequest(Long id, HttpServerExchange exchange, Map<String, String> originalValues) throws IOException {

				SocketAddress peerAddress = exchange.getConnection().getPeerAddress();

				String requestURI = exchange.getRequestURI();
				String queryString = exchange.getQueryString();


				ActiveSyncQueryString query = null;

                String url;
				if (StringUtils.isNotEmpty(queryString)) {
					LOGGER.info( "ID {} - ORIGINAL {} - {} {}?{}", id, peerAddress, exchange.getRequestMethod().toString(), requestURI, queryString );
					query = ActiveSyncQueryString.build(queryString, overrides);
					queryString = query.encode();
					url = String.format("%s%s", exchangeBaseURL, exchange.getRequestURI() + "?" + queryString );
					LOGGER.info( "ID {} - MODIFIED {} - {} {}?{}", id, peerAddress, exchange.getRequestMethod().toString(), requestURI, queryString );
				} else {
					LOGGER.info( "ID {} - ORIGINAL {} - {} {}", id, peerAddress, exchange.getRequestMethod().toString(), requestURI );
					url = String.format("%s%s", exchangeBaseURL, exchange.getRequestURI());
				}
				

				Builder builder = new Request.Builder().url(url);

				RequestBody body = null;
				
				String contentType = null;

				long contentLength = 0;

				// copy headers
				HeaderMap requestHeaders = exchange.getRequestHeaders();
				for (HttpString headerName : requestHeaders.getHeaderNames()) {
					
					if (headerName.equalToString("Host")) {
						continue;
					}
					
					HeaderValues values = requestHeaders.get(headerName);
					String value = StringUtils.join(values, ";");

					if (headerName.equalToString("Content-Length")) {
						contentLength = Long.parseLong( value ); 
						continue;
					}
					
					
					if (headerName.toString().equals("Content-Type")) {
						contentType = value;
					}
					
					if (overrides.containsKey(headerName.toString())) {
						value = overrides.get(headerName.toString());
					}

					LOGGER.debug("ID {} Request Header : {}={}", id, headerName.toString(), value);
					builder.header(headerName.toString(), value);
				}
				
				if (overrides.containsKey("User-Agent")) {
					builder.removeHeader("User-Agent");
					builder.addHeader("User-Agent", overrides.get("User-Agent"));
				}

				builder.header("Host", configuration.getServerHost());

				// copy cookies
				Map<String, Cookie> requestCookies = exchange.getRequestCookies();
				for (Map.Entry<String, Cookie> cookie : requestCookies.entrySet()) {
					builder.addHeader("Cookie", String.format("%s=%s", cookie.getKey(), cookie.getValue()));
				}

				if (contentLength >= 0) {
					exchange.startBlocking();
					byte[] originalContent = IOUtils.toByteArray(exchange.getInputStream());
					byte[] content = originalContent;
					
					if (query != null && query.command != null) {
						if (contentType.startsWith("application/vnd.ms-sync")) {
							if (query.command.isRequestMustBeModified()) {
								try {
									content = injectValues(id, contentType, originalContent, overrides);
								} catch (XmlPullParserException|IOException e) {
									LOGGER.error(e.getMessage(), e);
								}
							}
							if (query.command.isRequestMustBeDumped()) {
								try {
									Document dom = WbxmlUtils.wbxmlToXml(content);
									String xmlContent = WbxmlUtils.serializeToXml(dom);								
									dumpToFile(query.deviceId != null ? query.deviceId : "UNKNOWN", "Request", query.command, xmlContent.getBytes(), contentType);
								} catch (XmlPullParserException e) {
									LOGGER.error(e.getMessage(), e);
								}
							}
						}
					}
				
					body = RequestBody.create(MediaType.parse(contentType != null ? contentType : ""), ByteString.of(content));
				}

				builder.method( exchange.getRequestMethod().toString(), body);

				return new ActiveSyncRequest(query, builder.build());
			}

			private byte[] injectValues(long id, String contentType, byte[] wbxmlContent, Map<String, String> valuesToInject) throws XmlPullParserException, IOException {
				byte[] content;
				
				String xmlContent = null;
				Document dom = WbxmlUtils.wbxmlToXml(wbxmlContent);
				xmlContent = WbxmlUtils.serializeToXml(dom);

				LOGGER.info("ID {} Original WBXML : {}", id, xmlContent);
				
				xmlContent = xmlContent.replaceAll("<EASProvidionDoc>.*</EASProvidionDoc>", "<EASProvidionDoc><DevicePasswordEnabled>0</DevicePasswordEnabled><AlphanumericDevicePasswordRequired>0</AlphanumericDevicePasswordRequired><PasswordRecoveryEnabled>0</PasswordRecoveryEnabled><DeviceEncryptionEnabled>0</DeviceEncryptionEnabled><AttachmentsEnabled>1</AttachmentsEnabled><MinDevicePasswordLength>4</MinDevicePasswordLength><MaxInactivityTimeDeviceLock>0</MaxInactivityTimeDeviceLock><MaxDevicePasswordFailedAttempts>5</MaxDevicePasswordFailedAttempts><MaxAttachmentSize /><AllowSimpleDevicePassword>1</AllowSimpleDevicePassword><DevicePasswordExpiration>0</DevicePasswordExpiration><DevicePasswordHistory>0</DevicePasswordHistory><AllowStorageCard>1</AllowStorageCard><AllowCamera>1</AllowCamera><RequireDeviceEncryption>1</RequireDeviceEncryption><AllowUnsignedApplications>1</AllowUnsignedApplications><AllowUnsignedInstallationPackages>1</AllowUnsignedInstallationPackages><MinDevicePasswordComplexCharacters>1</MinDevicePasswordComplexCharacters><AllowWiFi>1</AllowWiFi><AllowTextMessaging>1</AllowTextMessaging><AllowPOPIMAPEmail>1</AllowPOPIMAPEmail><AllowBluetooth>2</AllowBluetooth><AllowIrDA>1</AllowIrDA><RequireManualSyncWhenRoaming>0</RequireManualSyncWhenRoaming><AllowDesktopSync>1</AllowDesktopSync><MaxCalendarAgeFilder>0</MaxCalendarAgeFilder><AllowHTMLEmail>1</AllowHTMLEmail><MaxEmailAgeFilter>0</MaxEmailAgeFilter><MaxEmailBodyTruncationSize>-1</MaxEmailBodyTruncationSize><MaxEmailHTMLBodyTruncationSize>-1</MaxEmailHTMLBodyTruncationSize><RequireSignedSMIMEMessages>0</RequireSignedSMIMEMessages><RequireEncryptedSMIMEMessages>0</RequireEncryptedSMIMEMessages><RequireSignedSMIMEAlgorithm>0</RequireSignedSMIMEAlgorithm><RequireEncryptionSMIMEAlgorithm>0</RequireEncryptionSMIMEAlgorithm><AllowSMIMEEncryptionAlgorithmNegotiation>2</AllowSMIMEEncryptionAlgorithmNegotiation><AllowSMIMESoftCerts>1</AllowSMIMESoftCerts><AllowBrowser>1</AllowBrowser><AllowConsumerEmail>1</AllowConsumerEmail><AllowRemoteDesktop>1</AllowRemoteDesktop><AllowInternetSharing>1</AllowInternetSharing><UnapprovedInROMApplicationList /><ApprovedApplicationList /></EASProvidionDoc>");
				
				if (xmlContent.contains("<RemoteWipe")) {
					// TODO
					LOGGER.error("ID {} RemoteWipe was requested for device!!", id);
					xmlContent= xmlContent.replaceAll("<RemoteWipe[\\s]*/>", "");
				}

				if (valuesToInject != null) {
				    // Modify XML content
					for (Map.Entry<String, String> overrideEntry : valuesToInject.entrySet()) {

						// extract original value
						String valueExtractor = String.format(".*<%s>([^<]*)</%s>.*", overrideEntry.getKey(), overrideEntry.getKey());
						
						// FIXME : handle empty tags : <TagWithNoContent />
						Matcher matcher = Pattern.compile(valueExtractor).matcher(xmlContent);
						if (matcher.matches()) {
							String originalValue = matcher.group(1);
							if (!originalValue.equals(overrideEntry.getValue())) {
								String replacement = String.format("<%s>%s</%s>", overrideEntry.getKey(), overrideEntry.getValue(), overrideEntry.getKey());
								xmlContent = xmlContent.replaceAll(String.format("<%s>%s</%s>", overrideEntry.getKey(), originalValue, overrideEntry.getKey()), replacement);
							}
						}
						
					}
				}

				LOGGER.info("ID {} Modified WBXML : {}", id, xmlContent);

				// write XML back to WBML
				content = WbxmlUtils.serializeToWbxml(WbxmlUtils.parseXml(xmlContent), "UTF-8");
				
				return content;
			}


		}).build();
		server.start();
	}

	private void dumpToFile(String deviceId, String fileNamePrefix, ActiveSyncCommand command, byte[] originalContent, String contentType) {
		try {
			String contentTypeForFilename = contentType.replace("/", "_");
			String filename = String.format("%s-%s-%d-%s-%s.bin", deviceId, fileNamePrefix, System.currentTimeMillis(), command.name(), contentTypeForFilename);
			Path outputFile = Paths.get( filename );
			try (OutputStream out = Files.newOutputStream(outputFile)) {
				out.write(originalContent);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
