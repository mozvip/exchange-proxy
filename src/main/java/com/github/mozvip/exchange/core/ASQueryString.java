package com.github.mozvip.exchange.core;

import com.github.mozvip.exchange.devices.DeviceManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 *
 */
public class ASQueryString extends ActiveSyncQueryString {

	public ASQueryString(DeviceManager deviceManager, String queryString, String userAgent) {

		super(deviceManager, userAgent);

		String[] parameters = queryString.split("&");
		for (String queryParameter : parameters) {
			String[] split = queryParameter.split("=");
			
			switch (split[0]) {
			case "DeviceId":
				originalDeviceId = split[1];
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
	public String encode(AuthorizedDevice auth) throws IOException {
		StringBuffer queryString = new StringBuffer();
		for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
			String valueToUse;
			if (entry.getKey().equalsIgnoreCase("DeviceId")) {
				valueToUse = auth.getOverridenId();
			} else {
				valueToUse = auth.getOverrides().getOrDefault(entry.getKey(), entry.getValue());
			}
			queryString.append(entry.getKey()).append("=").append(valueToUse);
			queryString.append("&");
		}
		queryString.deleteCharAt( queryString.length() - 1 );
		
		return queryString.toString();
	}

	@Override
	public String toString() {
		return "ASQueryString{" +
				"user='" + user + '\'' +
				", queryParameters=" + queryParameters +
				", command=" + command +
				", protocolVersion=" + protocolVersion +
				", locale=" + locale +
				", originalDeviceId='" + originalDeviceId + '\'' +
				", policyKey=" + Arrays.toString(policyKey) +
				", deviceType='" + deviceType + '\'' +
				'}';
	}
}
