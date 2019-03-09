package com.github.mozvip.exchange.core;

import com.github.mozvip.exchange.devices.DeviceDetails;
import com.github.mozvip.exchange.devices.DeviceManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ActiveSyncQueryString {

	DeviceManager deviceManager;

	Map<String, String> queryParameters = new HashMap<>();
	
	ActiveSyncCommand command;
	int protocolVersion;
	int locale;
	String originalDeviceId;
	byte[] policyKey = new byte[] {};
	String deviceType;
	String user;
	String userAgent;
	
	public abstract String encode(AuthorizedDevice auth) throws IOException;

	public ActiveSyncQueryString(DeviceManager deviceManager, String userAgent) {
		this.deviceManager = deviceManager;
		this.userAgent = userAgent;
	}

	public AuthorizedDevice getDevice() {
		AuthorizedDevice auth = deviceManager.getAuthorizedDevice(originalDeviceId);
		if (auth != null) {
			return auth;
		}

		DeviceDetails deviceDetails = new DeviceDetails();
		deviceDetails.setDeviceType(deviceType);
		deviceDetails.setUserAgent(userAgent);
		deviceManager.addNew(originalDeviceId, user, deviceDetails);
		return null;
	}

}
