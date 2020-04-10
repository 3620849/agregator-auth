package com.weiss.weiss.config.parsers;

import com.weiss.weiss.config.UserAuthentication;

public class DisParser extends TokenHeaderParser {
    @Override
    public void setToken(UserAuthentication auth, String header) {
        auth.setDisToken(header);
    }
}
