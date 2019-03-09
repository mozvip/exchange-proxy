package com.github.mozvip.exchange;

import com.github.mozvip.exchange.devices.DeviceDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "exchange")
public class DeviceTemplates {

    private Map<String, DeviceDetails> deviceTemplates;

    public Map<String, DeviceDetails> getDeviceTemplates() {
        return deviceTemplates;
    }

    public void setDeviceTemplates(Map<String, DeviceDetails> deviceTemplates) {
        this.deviceTemplates = deviceTemplates;
    }

    public DeviceDetails getDeviceTemplate(String deviceTemplate) {
        return deviceTemplates.get(deviceTemplate);
    }
}
