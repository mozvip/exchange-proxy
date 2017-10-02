package com.github.mozvip.exchange;

import com.codahale.metrics.health.HealthCheck;

public class ExchangeProxyHealthCheck extends HealthCheck {

    private String serverHost;

    public ExchangeProxyHealthCheck( String serverHost ) {
        this.serverHost = serverHost;
    }

    @Override
    protected Result check() throws Exception {
        return null;
    }
}
