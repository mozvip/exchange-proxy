package com.github.mozvip.exchange;

import com.github.mozvip.exchange.core.ActiveSyncProxy;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ExchangeProxyApplication extends Application<ExchangeProxyConfiguration> {

	public static void main(String[] args) throws Exception {
		new ExchangeProxyApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<ExchangeProxyConfiguration> bootstrap) {
		super.initialize(bootstrap);
	}

	@Override
	public void run(ExchangeProxyConfiguration configuration, Environment environment) throws Exception {

		final ExchangeProxyHealthCheck healthCheck =  new ExchangeProxyHealthCheck(configuration.getServerHost());
		environment.healthChecks().register("serverHost", healthCheck);

		ActiveSyncProxy proxy = new ActiveSyncProxy(configuration);
		proxy.start();

	}
}
