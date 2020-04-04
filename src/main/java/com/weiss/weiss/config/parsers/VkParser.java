package com.weiss.weiss.config.parsers;

import com.weiss.weiss.config.UserAuthentication;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public class VkParser extends TokenHeaderParser {
    @Override
    public void setToken(UserAuthentication auth, String header) {
        auth.setVkToken(header);
    }
}
