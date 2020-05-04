package com.weiss.weiss.config.providers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.converters.UserConverter;
import com.weiss.weiss.dao.DisUserDao;
import com.weiss.weiss.model.Role;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.model.dis.DisqusResponse;
import com.weiss.weiss.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DisAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    DisUserDao disUserDao;
    @Autowired
    UserService userService;
    @Autowired
    UserConverter converter;
    @Value("${dis.prefix}")
    public String LOGIN_PREFIX;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication auth = (UserAuthentication) authentication;
        if (auth.getDisToken() == null || auth.getDisToken().isBlank()) {
            throw new BadCredentialsException("token is null");
        }
        DisqusResponse disData = disUserDao.getDisUserData(auth.getDisToken());
        UserInfo userInfo = converter.convert(disData);
        userInfo.setLogin(LOGIN_PREFIX + disData.getId());
        try {
            userInfo = userService.findUserByLogin(userInfo);
        } catch (UsernameNotFoundException e) {
            try {
                userInfo.grantRole(Role.ROLE_USER);
                userService.addNewUser(userInfo);
            } catch (IllegalArgumentException ex) {
                LOGGER.error("User wasn't added cause" + ex.getMessage());
            }
        }
        auth.setAuthenticated(true);
        ((UserAuthentication) authentication).setUserInfo(userInfo);
        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
