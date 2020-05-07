package com.weiss.weiss.config.providers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.model.Role;
import com.weiss.weiss.model.UserInfo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AnonymousProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication auth = (UserAuthentication) authentication;
        if (auth.getClientId() == null || auth.getClientId().isBlank()) {
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            auth.setClientId("NO-CLIENT-"+id);
        }
        if(!auth.isAuth()){
            UserInfo userInfo = auth.getUserInfo();
            userInfo.grantRole(Role.ROLE_ANONYMOUS);
        }
        auth.setAuth(true);
        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}