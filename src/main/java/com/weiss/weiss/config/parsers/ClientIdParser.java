package com.weiss.weiss.config.parsers;

import com.weiss.weiss.config.UserAuthentication;

public class ClientIdParser extends TokenHeaderParser {
    @Override
    public void setToken(UserAuthentication auth, String header) {
        auth.setClientId(header);
    }
}