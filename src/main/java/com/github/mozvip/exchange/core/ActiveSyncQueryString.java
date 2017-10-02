package com.github.mozvip.exchange.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public abstract class ActiveSyncQueryString {
	
	private final static Logger LOGGER = LoggerFactory.getLogger( ActiveSyncQueryString.class );
	
	Map<String, String> queryParameters = new HashMap<>();
	
	ActiveSyncCommand command;
	int protocolVersion;
	int locale;
	String deviceId;
	byte[] policyKey = new byte[] {};
	String deviceType;
	
	private static Map<String, String> deviceIdMappings = new HashMap<String, String>();

	public abstract String encode() throws IOException;
	
	static {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Path deviceIdMappingsFile = Paths.get("deviceIdMappings.json");
			if (Files.exists(deviceIdMappingsFile)) {
				deviceIdMappings = objectMapper.readValue(deviceIdMappingsFile.toFile(), Map.class);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public void overrideValues( Map<String, String> overrides ) {

		if (!deviceIdMappings.containsKey(deviceId)) {
			synchronized (ActiveSyncQueryString.class) {
				String mappedDeviceId = RandomDeviceIdGenerator.unique();
				deviceIdMappings.put(deviceId, mappedDeviceId);

				ObjectMapper mapper = new ObjectMapper();
				
				Path deviceIdMappingsFile = Paths.get("deviceIdMappings.json");
				try {
					mapper.writeValue(deviceIdMappingsFile.toFile(), deviceIdMappings);
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}

		deviceId = deviceIdMappings.get(deviceId);

		if (overrides != null) {

			if (overrides.containsKey("PolicyKey")) {
				String policyKeyStr = overrides.get("PolicyKey");
				try {
					policyKey = Hex.decodeHex(policyKeyStr.toCharArray());
				} catch (DecoderException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			if (overrides.containsKey("DeviceType")) {
				deviceType = overrides.get("DeviceType");
			}
			
		}	

		LOGGER.info(
				"Modified Query String : ProtocolVersion={} Command={} Locale={} DeviceId=0x{} PolicyKey=0x{} DeviceType={}",
				protocolVersion, command != null ? command.getLabel() : "NULL", locale, deviceId, Hex.encodeHexString(policyKey),
				deviceType);
	}
	
	public static ActiveSyncQueryString build( String queryString, Map<String, String> overrides ) throws IOException {
		
		ActiveSyncQueryString asQuery ;
		
		try {
			Base64.getDecoder().decode(queryString.getBytes());
			asQuery = new ASBinaryQueryString(queryString);
		} catch (IllegalArgumentException e) {
			asQuery = new ASQueryString(queryString);
		}
		
		asQuery.overrideValues(overrides);
		
		return asQuery;

	}

}
