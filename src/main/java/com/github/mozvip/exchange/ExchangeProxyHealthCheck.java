package com.github.mozvip.exchange;

import com.codahale.metrics.health.HealthCheck;

import java.io.IOException;
import java.net.Socket;

public class ExchangeProxyHealthCheck extends HealthCheck {

    private String serverHost;

    public ExchangeProxyHealthCheck( String serverHost ) {
        this.serverHost = serverHost;
    }

    @Override
    protected Result check() throws Exception {
        int port = 443;

        try (final Socket socket = createSocket(serverHost, port)) {
            return socket.isConnected()
                    ? Result.healthy()
                    : Result.unhealthy(String.format(
                    "Failed to connect to %s:%d", serverHost, port));
        }
    }

    /**
     * Creates a new {@link Socket} for the given {@code hostname} and {@code port}.
     *
     * @param hostname the remote hostname of the {@link Socket} to create.
     * @param port the remote port of the {@link Socket} to create.
     *
     * @return a new {@link Socket} for the given {@code hostname} and {@code port}.
     *
     * @throws IOException if an I/O error occurs when creating the {@link Socket} or connecting it.
     */
    private Socket createSocket(final String hostname, final int port) throws IOException {
        return new Socket(hostname, port);
    }
}
