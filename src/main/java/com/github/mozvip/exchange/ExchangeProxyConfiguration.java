package com.github.mozvip.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.glassfish.jersey.server.JSONP;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ExchangeProxyConfiguration extends Configuration {

    @NotEmpty
    private String serverHost;

    @NotNull
    private EmulatedDevice emulatedDevice;

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
    public int getProxyPort() {
        return proxyPort;
    }

    @JsonProperty
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    @JsonProperty
    public EmulatedDevice getEmulatedDevice() {
        return emulatedDevice;
    }
}
