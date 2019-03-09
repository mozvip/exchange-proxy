package com.github.mozvip.exchange.core;

import com.github.mozvip.exchange.ExchangeProxyConfiguration;
import com.github.mozvip.exchange.devices.DeviceManager;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class ActiveSyncProxy {

	public static final String USER_AGENT = "User-Agent";

	private ExchangeProxyConfiguration configuration;
	private DeviceManager deviceManager;
	private final ActiveSyncQueryBuilder builder;

	public ActiveSyncProxy(ExchangeProxyConfiguration configuration, DeviceManager deviceManager, ActiveSyncQueryBuilder builder){
	    this.configuration = configuration;
	    this.deviceManager = deviceManager;
	    this.builder = builder;
	}

	private Undertow server;

	@PostConstruct
    public void start() {
		server = Undertow.builder()
				.setServerOption(UndertowOptions.IDLE_TIMEOUT, 360 * 1000)
				.setServerOption(UndertowOptions.REQUEST_PARSE_TIMEOUT, -1)
				.setServerOption(UndertowOptions.NO_REQUEST_TIMEOUT, 360 * 1000)
				.addHttpListener(configuration.getProxyPort(), "0.0.0.0").setHandler(new ActiveSyncHttpHandler(configuration, deviceManager, builder)
				).build();
		server.start();
	}

	@PreDestroy
    public void stop() {
        server.stop();
    }


}
