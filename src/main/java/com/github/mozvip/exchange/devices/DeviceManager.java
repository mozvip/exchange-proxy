package com.github.mozvip.exchange.devices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mozvip.exchange.DeviceTemplates;
import com.github.mozvip.exchange.core.AuthorizedDevice;
import com.github.mozvip.exchange.core.NewDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Component
public class DeviceManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceManager.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, AuthorizedDevice> authorizedDevices = new HashMap<>();
    private Map<String, NewDevice> newDevices = new HashMap<>();

    File authorizedDevicesFile = Paths.get("authorizedDevices.json").toFile();
    File newDevicesFile = Paths.get("newDevices.json").toFile();

    @Autowired
    DeviceTemplates deviceTemplates;

    @PostConstruct
    public void start() {
        try {
            if (authorizedDevicesFile.canRead()) {
                TypeReference<HashMap<String, AuthorizedDevice>> typeRef = new TypeReference<HashMap<String, AuthorizedDevice>>() {};
                authorizedDevices = objectMapper.readValue(authorizedDevicesFile, typeRef);
            }
            if (newDevicesFile.canRead()) {
                TypeReference<HashMap<String, NewDevice>> typeRef = new TypeReference<HashMap<String, NewDevice>>() {};
                newDevices = objectMapper.readValue(newDevicesFile, typeRef);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    protected synchronized void save() {
        try {
            objectMapper.writeValue(authorizedDevicesFile, authorizedDevices);
            objectMapper.writeValue(newDevicesFile, newDevices);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    public Collection<NewDevice> getNewDevices() {
        return newDevices.values();
    }
    
    public Collection<AuthorizedDevice> getAuthorizedDevices() {
        return authorizedDevices.values();
    }

    public void addNew(String deviceId, String user, DeviceDetails deviceDetails) {
        newDevices.put(deviceId, new NewDevice(deviceId, user, deviceDetails));
        save();
    }

    public void authorize(String originalDeviceId, String overridenId, DeviceDetails originalDeviceDetails, String deviceTemplate) {
        DeviceDetails templateDetails = getDeviceTemplate(deviceTemplate);
        AuthorizedDevice device = new AuthorizedDevice(originalDeviceId, overridenId, originalDeviceDetails, templateDetails);
        authorizedDevices.put(originalDeviceId, device);
        newDevices.remove(originalDeviceId);
        save();
    }

    private DeviceDetails getDeviceTemplate(String deviceTemplate) {
        return deviceTemplates.getDeviceTemplate(deviceTemplate);
    }

    public AuthorizedDevice getAuthorizedDevice(String originalDeviceId) {
        return authorizedDevices.get(originalDeviceId);
    }

    public Map<String, DeviceDetails> getDeviceTemplates() {
        return deviceTemplates.getDeviceTemplates();
    }

}
