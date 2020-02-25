package com.weiss.weiss.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("we in");
        String name = authentication.getName();
       // String password = authentication.getCredentials().toString();

        throw new BadCredentialsException("fuck off");// new UsernamePasswordAuthenticationToken("HEllo world123", "43", Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
    }
