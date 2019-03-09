package com.github.mozvip.exchange.devices;

import javax.validation.constraints.NotEmpty;

public class DeviceDetails {

    @NotEmpty
    private String userAgent;

    @NotEmpty
    private String deviceType;

    private String os;

    private String osLanguage;

    private String friendlyName;

    private String model;

    private String phoneNumber;

    private String iMEI;

    private String mobileOperator;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsLanguage() {
        return osLanguage;
    }

    public void setOsLanguage(String osLanguage) {
        this.osLanguage = osLanguage;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getiMEI() {
        return iMEI;
    }

    public void setiMEI(String iMEI) {
        this.iMEI = iMEI;
    }

    public String getMobileOperator() {
        return mobileOperator;
    }

    public void setMobileOperator(String mobileOperator) {
        this.mobileOperator = mobileOperator;
    }

    @Override
    public String toString() {
        return "DeviceDetails{" +
                "userAgent='" + userAgent + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", os='" + os + '\'' +
                ", osLanguage='" + osLanguage + '\'' +
                ", friendlyName='" + friendlyName + '\'' +
                ", model='" + model + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", iMEI='" + iMEI + '\'' +
                ", mobileOperator='" + mobileOperator + '\'' +
                '}';
    }
}
