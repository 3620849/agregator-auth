package com.weiss.weiss.config.parsers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

public class BasicHeaderParser implements AuthParser {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserAuthentication parseRequest(String searchHeader, UserAuthentication authentication, HttpServletRequest req) {
        String header = req.getHeader(searchHeader);
        if (header == null) {
            return authentication;
        }
        UserAuthentication value = null;
        if (header.matches("^Basic (?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})$")) {
            String[] basci_s = header.split("Basic ");
            if (basci_s.length > 1) {
                String userCredentials = null;
                userCredentials = new String(Base64.getDecoder().decode(basci_s[1].getBytes()));
                String[] userCredentialsArr = userCredentials.split(":");
                UserInfo userInfo = UserInfo.builder().login(userCredentialsArr[0]).password(userCredentialsArr[1]).build();
                authentication.setUserInfo(userInfo);
            }
        }else {
            String error = "base64 error user password wrong format";
            LOGGER.info(error);
        }
        return authentication;
    }
}
