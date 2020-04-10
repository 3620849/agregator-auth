package com.weiss.weiss.config.providers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.services.TokenHandler;
import com.weiss.weiss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;
    @Autowired
    TokenHandler tokenHandlerService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication auth = (UserAuthentication) authentication;
        UserInfo details = (UserInfo) auth.getDetails();
        if (details.getLogin() == null || details.getPassword() == null) {
            throw new BadCredentialsException("fuck off");
        }
        UserInfo userInfo = userService.findUserByLogin(details.getLogin());

        if (details.getLogin().equals(userInfo.getLogin())) {
            if (passwordEncoder.matches(details.getPassword(), userInfo.getPassword())) {
                details.setAuthorities(userInfo.getAuthorities());
                auth.setAccessToken(tokenHandlerService.generateTokenId(userInfo.getId(), LocalDateTime.now().plusHours(24)));
                auth.setAuthenticated(true);
            } else {
                throw new BadCredentialsException("fuck off");
            }

        } else {
            throw new BadCredentialsException("fuck off");
        }
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
