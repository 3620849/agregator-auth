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

@Service
public class AnonymouseProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication auth = (UserAuthentication) authentication;
        if (auth.getClientId() == null || auth.getClientId().isBlank()) {
            throw new BadCredentialsException("illegal argument exception");
        }
        if(!auth.isAuth()){
            UserInfo userInfo = auth.getUserInfo();
            userInfo.grantRole(Role.ROLE_ANONYMOUS);
        }
        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}