package com.weiss.weiss.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;
@Component
public class CustomAuthenticationProvider2 implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("we in 2");
         authentication.setAuthenticated(true);

        return  authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
