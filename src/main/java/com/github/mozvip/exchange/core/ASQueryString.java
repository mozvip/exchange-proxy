package com.github.mozvip.exchange.core;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class ASQueryString extends ActiveSyncQueryString {
	
	String user;

	public ASQueryString(String queryString) throws IOException {

		String[] parameters = queryString.split("&");
		for (String queryParameter : parameters) {
			String[] split = queryParameter.split("=");
			
			switch (split[0]) {
			case "DeviceId":
				deviceId = split[1];
				break;
			case "Cmd":
				command = ActiveSyncCommand.from(split[1]);
				break;
			case "User":
				user = split[1];
				break;
			case "DeviceType":
				deviceType = split[1];
				break;

			default:
				break;
			}
			
			queryParameters.put(split[0], split[1]);
		}

	}
	
	@Override
	public void overrideValues(Map<String, String> overrides) {
		super.overrideValues(overrides);
		queryParameters.put("DeviceId", deviceId);
		for (Map.Entry<String, String> entry : overrides.entrySet()) {
			if (queryParameters.containsKey(entry.getKey())) {
				queryParameters.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	@Override
	public String encode() throws IOException {
		StringBuffer queryString = new StringBuffer();
		for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
			queryString.append(entry.getKey()).append("=").append(entry.getValue());
			queryString.append("&");
		}
		queryString.deleteCharAt( queryString.length() - 1 );
		
		return queryString.toString();
	}

}
