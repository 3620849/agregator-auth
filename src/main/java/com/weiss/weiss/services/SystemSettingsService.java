package com.weiss.weiss.services;

import com.weiss.weiss.model.ClientId;
import com.weiss.weiss.model.SystemSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SystemSettingsService {
    @Autowired
    SystemSettings systemSettings;

    public ClientId getSettings() {
        UUID uuid = UUID.randomUUID();
        ClientId clientId = new ClientId(systemSettings);
        clientId.setClientId(uuid.toString());
        return clientId;
    }
}
