package com.github.mozvip.exchange.core;

import com.github.mozvip.exchange.devices.DeviceDetails;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

public class AuthorizedDevice {

    private String originalDeviceId;
    private String overridenId;

    private DeviceDetails originalDeviceDetails;
    private DeviceDetails templateDeviceDetails;

    public String getOriginalDeviceId() {
        return originalDeviceId;
    }

    public void setOriginalDeviceId(String originalDeviceId) {
        this.originalDeviceId = originalDeviceId;
    }

    public String getOverridenId() {
        return overridenId;
    }

    public void setOverridenId(String overridenId) {
        this.overridenId = overridenId;
    }

    public DeviceDetails getOriginalDeviceDetails() {
        return originalDeviceDetails;
    }

    public void setOriginalDeviceDetails(DeviceDetails originalDeviceDetails) {
        this.originalDeviceDetails = originalDeviceDetails;
    }

    public DeviceDetails getTemplateDeviceDetails() {
        return templateDeviceDetails;
    }

    public void setTemplateDeviceDetails(DeviceDetails templateDeviceDetails) {
        this.templateDeviceDetails = templateDeviceDetails;
    }

    public AuthorizedDevice(String originalDeviceId, String overridenId, DeviceDetails originalDeviceDetails, DeviceDetails templateDeviceDetails) {
        this.originalDeviceId = originalDeviceId;
        this.overridenId = overridenId;
        this.originalDeviceDetails = originalDeviceDetails;
        this.templateDeviceDetails = templateDeviceDetails;
    }

    public AuthorizedDevice() {
    }

    @Transient
    public Map<String, String> getOverrides() {
        Map<String, String> overrides = new HashMap<>();
        overrides.put("DeviceType", templateDeviceDetails.getDeviceType());
        overrides.put("UserAgent", templateDeviceDetails.getUserAgent());
        overrides.put(ActiveSyncProxy.USER_AGENT, templateDeviceDetails.getUserAgent());

        overrides.put("OS", templateDeviceDetails.getOs());
        overrides.put("OSLanguage", templateDeviceDetails.getOsLanguage());

        overrides.put("FriendlyName", templateDeviceDetails.getFriendlyName());
        overrides.put("Model", templateDeviceDetails.getModel());

        if (templateDeviceDetails.getPhoneNumber() != null) {
            overrides.put("PhoneNumber", templateDeviceDetails.getPhoneNumber());
        }
        if (templateDeviceDetails.getiMEI() != null) {
            overrides.put("IMEI", templateDeviceDetails.getiMEI());
        }
        if (templateDeviceDetails.getMobileOperator() != null) {
            overrides.put("MobileOperator", templateDeviceDetails.getMobileOperator());
        }
        return overrides;
    }

}
