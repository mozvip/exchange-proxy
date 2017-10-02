package com.github.mozvip.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.glassfish.jersey.server.JSONP;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

public class ExchangeProxyConfiguration extends Configuration {

    @NotEmpty
    private String serverHost;

    @NotEmpty
    private String defaultUserAgent = "MSFT-WIN-4/10.0.15063";

    @NotEmpty
    private String defaultDeviceType = "WP8";

    @NotEmpty
    private String defaultOs = "Windows 10.0.15063";

    @NotEmpty
    private String defaultOsLanguage = "French";

    @NotEmpty
    private String defaultFriendlyName = "Lumia 950";

    @NotEmpty
    private String defaultModel = "RM-1104_12707";

    private String defaultPhoneNumber;

    private String defaultIMEI;

    private String defaultMobileOperator;

    @Min(1)
    private int proxyPort = 9090;

    @JsonProperty
    public String getServerHost() {
        return serverHost;
    }

    @JsonProperty
    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    @JsonProperty
    public String getDefaultUserAgent() {
        return defaultUserAgent;
    }

    @JsonProperty
    public void setDefaultUserAgent(String defaultUserAgent) {
        this.defaultUserAgent = defaultUserAgent;
    }

    @JsonProperty
    public String getDefaultDeviceType() {
        return defaultDeviceType;
    }

    @JsonProperty
    public void setDefaultDeviceType(String defaultDeviceType) {
        this.defaultDeviceType = defaultDeviceType;
    }

    @JsonProperty
    public String getDefaultOs() {
        return defaultOs;
    }

    @JsonProperty
    public void setDefaultOs(String defaultOs) {
        this.defaultOs = defaultOs;
    }

    @JsonProperty
    public String getDefaultFriendlyName() {
        return defaultFriendlyName;
    }

    @JsonProperty
    public void setDefaultFriendlyName(String defaultFriendlyName) {
        this.defaultFriendlyName = defaultFriendlyName;
    }

    @JsonProperty
    public int getProxyPort() {
        return proxyPort;
    }

    @JsonProperty
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    @JsonProperty
    public String getDefaultOsLanguage() {
        return defaultOsLanguage;
    }

    @JsonProperty
    public void setDefaultOsLanguage(String defaultOsLanguage) {
        this.defaultOsLanguage = defaultOsLanguage;
    }

    @JsonProperty
    public String getDefaultModel() {
        return defaultModel;
    }

    @JsonProperty
    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

    @JsonProperty
    public String getDefaultIMEI() {
        return defaultIMEI;
    }

    @JsonProperty
    public void setDefaultIMEI(String defaultIMEI) {
        this.defaultIMEI = defaultIMEI;
    }

    @JsonProperty
    public String getDefaultPhoneNumber() {
        return defaultPhoneNumber;
    }

    @JsonProperty
    public void setDefaultPhoneNumber(String defaultPhoneNumber) {
        this.defaultPhoneNumber = defaultPhoneNumber;
    }

    @JsonProperty
    public String getDefaultMobileOperator() {
        return defaultMobileOperator;
    }

    @JsonProperty
    public void setDefaultMobileOperator(String defaultMobileOperator) {
        this.defaultMobileOperator = defaultMobileOperator;
    }
}
