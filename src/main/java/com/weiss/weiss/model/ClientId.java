package com.weiss.weiss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientId extends  SystemSettings {
    String clientId;

    public ClientId(SystemSettings settings) {
        super(settings.getGit_userAuthorizationUrl(),
                settings.getVk_userAuthorizationUrl(),
                settings.getFb_userAuthorizationUrl(),
                settings.getGgl_userAuthorizationUrl(),
                settings.getDis_userAuthorizationUrl()
        );

    }

}
