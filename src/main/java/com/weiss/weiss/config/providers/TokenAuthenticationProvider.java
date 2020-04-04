package com.weiss.weiss.config.providers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.model.Role;
import com.weiss.weiss.model.UserAutority;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.services.TokenHandler;
import com.weiss.weiss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;
    @Autowired
    TokenHandler tokenHandlerService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserInfo details = (UserInfo)authentication.getDetails();
        UserAuthentication auth = (UserAuthentication)authentication;
        if (auth.getAccessToken()==null || auth.getAccessToken().isBlank()){
            throw new BadCredentialsException("token is null");
        }
        UserInfo userInfo = tokenHandlerService.
                extractUserId(auth.getAccessToken()).
                map(userService::findById).get();
        auth.setAuthenticated(true);
        ((UserAuthentication)authentication).setUserInfo(userInfo);
        return  auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
