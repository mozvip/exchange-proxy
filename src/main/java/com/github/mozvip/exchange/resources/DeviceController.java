package com.github.mozvip.exchange.resources;

import com.github.mozvip.exchange.core.AuthorizedDevice;
import com.github.mozvip.exchange.core.NewDevice;
import com.github.mozvip.exchange.devices.DeviceDetails;
import com.github.mozvip.exchange.devices.DeviceManager;
import com.github.mozvip.exchange.devices.RandomDeviceIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DeviceController {

    @Autowired
    DeviceManager deviceManager;

    @RequestMapping("/devices")
    public ResponseEntity<Collection<AuthorizedDevice>> getAuthorized() {
        return new ResponseEntity<>(deviceManager.getAuthorizedDevices(), HttpStatus.OK);
    }

    @RequestMapping("/devices/new")
    public ResponseEntity<Collection<NewDevice>> getNew() {
        return new ResponseEntity<>(deviceManager.getNewDevices(), HttpStatus.OK);
    }

    @RequestMapping("/devices/unique-id")
    public ResponseEntity<String> generateUniqueId() {
        return new ResponseEntity<>(RandomDeviceIdGenerator.unique(), HttpStatus.OK);
    }

    @RequestMapping("/devices/templates")
    public ResponseEntity<Map<String, DeviceDetails>> getDeviceTemplates() {
        return new ResponseEntity<>(deviceManager.getDeviceTemplates(), HttpStatus.OK);
    }

    @PostMapping("/devices/authorize")
    public void authorizeDevice(@RequestBody DeviceAuthorization authorization) {
        deviceManager.authorize(authorization.getOriginalDeviceId(), authorization.getOverridenId(), authorization.getOriginalDeviceDetails(), authorization.getDeviceTemplate());
    }

}
