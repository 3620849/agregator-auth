package com.weiss.weiss.config.parsers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.model.UserInfo;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public class TokenHeaderParser implements AuthParser {
    @Override
    public Authentication parseRequest(String searchHeader, UserAuthentication auth, HttpServletRequest req){
        String header = req.getHeader(searchHeader);
        if (header == null) {
            return auth;
        }
        setToken ( auth, header);
        return auth;
    }
    public void setToken (UserAuthentication auth,String header){
        auth.setAccessToken(header);
    }
}
