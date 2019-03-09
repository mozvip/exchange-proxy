package com.github.mozvip.exchange.resources;

import com.github.mozvip.exchange.devices.DeviceDetails;

import javax.validation.constraints.NotEmpty;

public class DeviceAuthorization {

    @NotEmpty
    String originalDeviceId;

    String overridenId;

    @NotEmpty
    DeviceDetails originalDeviceDetails;

    String deviceTemplate;

    public DeviceAuthorization(String originalDeviceId, String overridenId, DeviceDetails originalDeviceDetails, String deviceTemplate) {
        this.originalDeviceId = originalDeviceId;
        this.overridenId = overridenId;
        this.originalDeviceDetails = originalDeviceDetails;
        this.deviceTemplate = deviceTemplate;
    }

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

    public String getDeviceTemplate() {
        return deviceTemplate;
    }

    public void setDeviceTemplate(String deviceTemplate) {
        this.deviceTemplate = deviceTemplate;
    }

    @Override
    public String toString() {
        return "DeviceAuthorization{" +
                "originalDeviceId='" + originalDeviceId + '\'' +
                ", overridenId='" + overridenId + '\'' +
                ", originalDeviceDetails=" + originalDeviceDetails +
                ", deviceTemplate='" + deviceTemplate + '\'' +
                '}';
    }
}
