package com.github.mozvip.exchange.core;

import com.github.mozvip.exchange.devices.DeviceManager;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class ActiveSyncQueryBuilder {

    private DeviceManager deviceManager;

    public ActiveSyncQueryBuilder(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    public ActiveSyncQueryString build(String originalQueryString, String userAgent) throws IOException {

        ActiveSyncQueryString asQuery;

        try {
            Base64.getDecoder().decode(originalQueryString.getBytes());
            asQuery = new ASBinaryQueryString(deviceManager, originalQueryString, userAgent);
        } catch (IllegalArgumentException e) {
            asQuery = new ASQueryString(deviceManager, originalQueryString, userAgent);
        }

        return asQuery;

    }


}
