package com.github.mozvip.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class EmulatedDevice {

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

    @JsonProperty
    public String getDefaultUserAgent() {
        return defaultUserAgent;
    }

    @JsonProperty
    public String getDefaultDeviceType() {
        return defaultDeviceType;
    }

    @JsonProperty
    public String getDefaultOs() {
        return defaultOs;
    }

    @JsonProperty
    public String getDefaultOsLanguage() {
        return defaultOsLanguage;
    }

    @JsonProperty
    public String getDefaultFriendlyName() {
        return defaultFriendlyName;
    }

    @JsonProperty
    public String getDefaultModel() {
        return defaultModel;
    }

    @JsonProperty
    public String getDefaultPhoneNumber() {
        return defaultPhoneNumber;
    }

    @JsonProperty
    public String getDefaultIMEI() {
        return defaultIMEI;
    }

    @JsonProperty
    public String getDefaultMobileOperator() {
        return defaultMobileOperator;
    }
}
