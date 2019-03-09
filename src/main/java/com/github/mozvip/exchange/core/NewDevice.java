package com.github.mozvip.exchange.core;

import com.github.mozvip.exchange.devices.DeviceDetails;

public class NewDevice {

    private String originalDeviceId;
    private String user;

    private DeviceDetails originalDeviceDetails;

    public String getOriginalDeviceId() {
        return originalDeviceId;
    }

    public void setOriginalDeviceId(String originalDeviceId) {
        this.originalDeviceId = originalDeviceId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public DeviceDetails getOriginalDeviceDetails() {
        return originalDeviceDetails;
    }

    public void setOriginalDeviceDetails(DeviceDetails originalDeviceDetails) {
        this.originalDeviceDetails = originalDeviceDetails;
    }

    public NewDevice(String originalDeviceId, String user, DeviceDetails originalDeviceDetails) {
        this.originalDeviceId = originalDeviceId;
        this.user = user;
        this.originalDeviceDetails = originalDeviceDetails;
    }

    public NewDevice() {
    }

    @Override
    public String toString() {
        return "NewDevice{" +
                "originalDeviceId='" + originalDeviceId + '\'' +
                ", user='" + user + '\'' +
                ", originalDeviceDetails=" + originalDeviceDetails +
                '}';
    }
}
