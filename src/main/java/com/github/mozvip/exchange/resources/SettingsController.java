package com.github.mozvip.exchange.resources;

import com.github.mozvip.exchange.ExchangeProxyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SettingsController {

    @Autowired
    ExchangeProxyConfiguration exchangeProxyConfiguration;

    @PostMapping("/settings")
    public void saveSettings(@RequestBody ExchangeProxyConfiguration settings) {
        exchangeProxyConfiguration.setProxyPort(settings.getProxyPort());
        exchangeProxyConfiguration.setServerHost(settings.getServerHost());

        // TODO: how do we persist configuration ??

    }

    @RequestMapping("/settings")
    public ResponseEntity<ExchangeProxyConfiguration> getSettings() {
        return new ResponseEntity<>(exchangeProxyConfiguration, HttpStatus.OK);
    }

}
