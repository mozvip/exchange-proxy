package com.github.mozvip.exchange.core;

import com.github.mozvip.exchange.ExchangeProxyConfiguration;
import com.github.mozvip.exchange.devices.DeviceManager;
import com.github.mozvip.exchange.wbxml.WbxmlUtils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import okhttp3.*;
import okio.ByteString;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.kxml2.kdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActiveSyncHttpHandler implements HttpHandler {

    private AtomicLong exchangeId = new AtomicLong(0);

    private final static Logger LOGGER = LoggerFactory.getLogger(ActiveSyncHttpHandler.class);

    final OkHttpClient client;
    final ExchangeProxyConfiguration configuration;
    final String exchangeBaseURL;
    final DeviceManager deviceManager;
    final ActiveSyncQueryBuilder builder;

    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public ActiveSyncHttpHandler(ExchangeProxyConfiguration configuration, DeviceManager deviceManager, ActiveSyncQueryBuilder builder) {
        this.configuration = configuration;
        this.deviceManager = deviceManager;
        this.builder = builder;

        this.exchangeBaseURL = String.format("https://%s", configuration.getServerHost());

        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(360, TimeUnit.SECONDS);
        b.connectTimeout(360, TimeUnit.SECONDS);
        client = b.build();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        long requestId = exchangeId.getAndIncrement();

        LOGGER.info("ID {} ================================================== New request", requestId );

        ActiveSyncRequest request = buildMirrorRequest(requestId, exchange);

        long contentLength = 0;
        String contentType = null;
        Response response = null;

        if (request == null) {

            exchange.setStatusCode(403);

        } else {

            Call call = client.newCall(request.getRequest());
            response = call.execute();

            if (!response.isSuccessful()) {
                LOGGER.warn("ID {} ==== Response is not successful : {}", requestId, response.toString());
            }

            okhttp3.Headers responseHeaders = response.headers();
            for (String name : responseHeaders.names()) {
                LOGGER.info("ID {} Response Header : {}={}", requestId, HttpString.tryFromString(name), responseHeaders.get(name));

                if (name.equals("Content-Length")) {
                    contentLength = Long.parseLong(responseHeaders.get(name));
                } else {
                    exchange.getResponseHeaders().put(HttpString.tryFromString(name), responseHeaders.get(name));
                }

                if (name.equals("Content-Type")) {
                    contentType = responseHeaders.get("Content-Type");
                    LOGGER.info("ID {} ==== Response Content-Type : {}", requestId, contentType);
                }

            }

            exchange.setStatusCode(response.code());

        }

        if (contentLength > 0) {

            try (ResponseBody body = response.body()) {
                if (request.getQuery() != null && request.getQuery().command != null && (request.getQuery().command.isResponseMustBeDumped() || request.getQuery().command.isResponseMustBeModified())) {
                    byte[] originalContent = body.bytes();

                    contentLength = originalContent.length;

                    String str ;
                    if (contentType.startsWith("application/vnd.ms-sync")) {
                        if (request.getQuery().command.isResponseMustBeDumped()) {
                            dumpToFile(request.getQuery().originalDeviceId, "Response", request.getQuery().command, originalContent, contentType);
                        }
                        if (request.getQuery().command.isResponseMustBeModified()) {
                            byte[] content = injectValues(requestId, originalContent, null);
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

        LOGGER.info("ID {} ==== Exchange Closed", requestId);
    }

    private ActiveSyncRequest buildMirrorRequest(Long id, HttpServerExchange exchange) throws IOException {

        String requestURI = exchange.getRequestURI();
        String originalQueryString = exchange.getQueryString();

        AuthorizedDevice device = null;

        ActiveSyncQueryString query = null;

        String userAgent = null;
        HeaderValues userAgents = exchange.getRequestHeaders().get("User-Agent");
        if (userAgents == null ||userAgents.size() == 0) {
            LOGGER.warn("ID {} - Could not determine User-Agent", id);
        } else {
            userAgent = userAgents.getFirst();
        }

        String url;
        SocketAddress peerAddress = exchange.getConnection().getPeerAddress();
        String requestMethod = exchange.getRequestMethod().toString();
        if (StringUtils.isNotEmpty(originalQueryString)) {
            LOGGER.info( "ID {} - ORIGINAL {} - {} {}?{}", id, peerAddress, requestMethod, requestURI, originalQueryString );

            query = builder.build(originalQueryString, userAgent);
            device = query.getDevice();

            if (device == null) {
                LOGGER.warn("ID {} - Device is not authorized", id);
                return null;
            }

            originalQueryString = query.encode(device);
            url = String.format("%s%s", exchangeBaseURL, exchange.getRequestURI() + "?" + originalQueryString );
            LOGGER.info( "ID {} - MODIFIED {} - {} {}?{}", id, peerAddress, requestMethod, requestURI, originalQueryString );
        } else {
            LOGGER.info( "ID {} - ORIGINAL {} - {} {}", id, peerAddress, requestMethod, requestURI );
            url = String.format("%s%s", exchangeBaseURL, exchange.getRequestURI());
        }


        Request.Builder builder = new Request.Builder().url(url);

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


            String headerNameString = headerName.toString();
            if (headerNameString.equals("Content-Type")) {
                contentType = value;
            }

            if (device != null && device.getOverrides() != null && device.getOverrides().containsKey(headerNameString)) {
                value = device.getOverrides().get(headerNameString);
            }

            LOGGER.debug("ID {} Request Header : {}={}", id, headerNameString, value);
            builder.header(headerNameString, value);
        }

        if (device != null && device.getOverrides() != null && device.getOverrides().containsKey(ActiveSyncProxy.USER_AGENT)) {
            builder.removeHeader(ActiveSyncProxy.USER_AGENT);
            builder.addHeader(ActiveSyncProxy.USER_AGENT, device.getOverrides().get(ActiveSyncProxy.USER_AGENT));
        } else {

            // FIXME: use a default User-Agent in this case ?

        }

        builder.header("Host", configuration.getServerHost());

        // copy cookies
        Map<String, io.undertow.server.handlers.Cookie> requestCookies = exchange.getRequestCookies();
        for (Map.Entry<String, Cookie> cookie : requestCookies.entrySet()) {
            builder.addHeader("Cookie", String.format("%s=%s", cookie.getKey(), cookie.getValue()));
        }

        if (contentLength >= 0) {
            exchange.startBlocking();
            byte[] originalRequestContent = IOUtils.toByteArray(exchange.getInputStream());
            byte[] content = originalRequestContent;

            if (query != null && query.command != null) {
                if (contentType != null && contentType.startsWith("application/vnd.ms-sync")) {
                    if (query.command.isRequestMustBeModified()) {
                        try {
                            content = injectValues(id, originalRequestContent, device.getOverrides());
                        } catch (XmlPullParserException |IOException e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                    if (query.command.isRequestMustBeDumped()) {
                        try {
                            Document dom = WbxmlUtils.wbxmlToXml(content);
                            String xmlContent = WbxmlUtils.serializeToXml(dom);
                            dumpToFile(query.originalDeviceId != null ? query.originalDeviceId : "UNKNOWN", "Request", query.command, xmlContent.getBytes(), contentType);
                        } catch (XmlPullParserException e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                }
            }

            body = RequestBody.create(MediaType.parse(contentType != null ? contentType : ""), ByteString.of(content));
        }

        if (requestMethod.equals(HttpString.tryFromString("GET"))) {
            builder.get();
        } else {
            builder.method(requestMethod, body);
        }

        return new ActiveSyncRequest(query, builder.build());
    }

    private byte[] injectValues(long id, byte[] wbxmlContent, Map<String, String> valuesToInject) throws XmlPullParserException, IOException {
        byte[] content;

        Document dom = WbxmlUtils.wbxmlToXml(wbxmlContent);
        String xmlContent = WbxmlUtils.serializeToXml(dom);

        LOGGER.info("ID {} Original WBXML : {}", id, xmlContent);

        if (xmlContent.contains("<RemoteWipe")) {
            // TODO
            LOGGER.error("ID {} RemoteWipe was requested for device!!", id);
            LOGGER.error("ID {} {}", id, xmlContent);
            xmlContent= xmlContent.replaceAll("<RemoteWipe[\\s]*/>", "");
        }

        xmlContent = xmlContent.replaceAll("<EASProvidionDoc>.*</EASProvidionDoc>", "<EASProvidionDoc><DevicePasswordEnabled>0</DevicePasswordEnabled><AlphanumericDevicePasswordRequired>0</AlphanumericDevicePasswordRequired><PasswordRecoveryEnabled>0</PasswordRecoveryEnabled><DeviceEncryptionEnabled>0</DeviceEncryptionEnabled><AttachmentsEnabled>1</AttachmentsEnabled><MinDevicePasswordLength>4</MinDevicePasswordLength><MaxInactivityTimeDeviceLock>0</MaxInactivityTimeDeviceLock><MaxDevicePasswordFailedAttempts>5</MaxDevicePasswordFailedAttempts><MaxAttachmentSize /><AllowSimpleDevicePassword>1</AllowSimpleDevicePassword><DevicePasswordExpiration>0</DevicePasswordExpiration><DevicePasswordHistory>0</DevicePasswordHistory><AllowStorageCard>1</AllowStorageCard><AllowCamera>1</AllowCamera><RequireDeviceEncryption>1</RequireDeviceEncryption><AllowUnsignedApplications>1</AllowUnsignedApplications><AllowUnsignedInstallationPackages>1</AllowUnsignedInstallationPackages><MinDevicePasswordComplexCharacters>1</MinDevicePasswordComplexCharacters><AllowWiFi>1</AllowWiFi><AllowTextMessaging>1</AllowTextMessaging><AllowPOPIMAPEmail>1</AllowPOPIMAPEmail><AllowBluetooth>2</AllowBluetooth><AllowIrDA>1</AllowIrDA><RequireManualSyncWhenRoaming>0</RequireManualSyncWhenRoaming><AllowDesktopSync>1</AllowDesktopSync><MaxCalendarAgeFilder>0</MaxCalendarAgeFilder><AllowHTMLEmail>1</AllowHTMLEmail><MaxEmailAgeFilter>0</MaxEmailAgeFilter><MaxEmailBodyTruncationSize>-1</MaxEmailBodyTruncationSize><MaxEmailHTMLBodyTruncationSize>-1</MaxEmailHTMLBodyTruncationSize><RequireSignedSMIMEMessages>0</RequireSignedSMIMEMessages><RequireEncryptedSMIMEMessages>0</RequireEncryptedSMIMEMessages><RequireSignedSMIMEAlgorithm>0</RequireSignedSMIMEAlgorithm><RequireEncryptionSMIMEAlgorithm>0</RequireEncryptionSMIMEAlgorithm><AllowSMIMEEncryptionAlgorithmNegotiation>2</AllowSMIMEEncryptionAlgorithmNegotiation><AllowSMIMESoftCerts>1</AllowSMIMESoftCerts><AllowBrowser>1</AllowBrowser><AllowConsumerEmail>1</AllowConsumerEmail><AllowRemoteDesktop>1</AllowRemoteDesktop><AllowInternetSharing>1</AllowInternetSharing><UnapprovedInROMApplicationList /><ApprovedApplicationList /></EASProvidionDoc>");

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


    private void dumpToFile(String deviceId, String fileNamePrefix, ActiveSyncCommand command, byte[] originalContent, String contentType) {
        try {
            boolean xml = contentType.endsWith("xml");

            if (xml) {
                String filename = String.format("%s-%s-%d-%s.xml", deviceId, fileNamePrefix, System.currentTimeMillis(), command.name());
                Path outputFile = Paths.get( filename );

                String xmlString = new String(originalContent);

                try {

                    DocumentBuilder db = dbf.newDocumentBuilder();
                    org.w3c.dom.Document doc = db.parse(new ByteArrayInputStream(originalContent));

                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                    StreamResult result = new StreamResult(new StringWriter());
                    transformer.transform(new DOMSource(doc), result);
                    xmlString = result.getWriter().toString();

                } catch (Exception e) {

                }

                try (OutputStream out = Files.newOutputStream(outputFile)) {
                    out.write(xmlString.getBytes());
                }


            } else {
                String filename = String.format("%s-%s-%d-%s.bin", deviceId, fileNamePrefix, System.currentTimeMillis(), command.name());
                Path outputFile = Paths.get( filename );
                try (OutputStream out = Files.newOutputStream(outputFile)) {
                    out.write(originalContent);
                }
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
