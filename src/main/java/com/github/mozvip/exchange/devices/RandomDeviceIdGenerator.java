package com.github.mozvip.exchange.devices;

import java.security.SecureRandom;

public class RandomDeviceIdGenerator {
    private static volatile SecureRandom numberGenerator = null;
    private static final long MSB = 0x8000000000000000L;

    public static String unique() {
        SecureRandom ng = numberGenerator;
        if (ng == null) {
            numberGenerator = ng = new SecureRandom();
        }

        String id = "FR" + Long.toHexString(MSB | ng.nextLong()).toUpperCase() + Long.toHexString(MSB | ng.nextLong()).toUpperCase();
        return id.substring(0, 26);
    }       
}