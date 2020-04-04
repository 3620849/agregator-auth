package com.weiss.weiss.config.providers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.converters.UserConverter;
import com.weiss.weiss.dao.VkUserDao;
import com.weiss.weiss.model.Role;
import com.weiss.weiss.model.UserInfo;
import com.weiss.weiss.model.vk.VkUser;
import com.weiss.weiss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class VkAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    VkUserDao vkUserDao;
    @Autowired
    UserService userService;
    @Autowired
    UserConverter converter;
    @Value("${vk.prefix}")
    public String LOGIN_PREFIX;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication auth = (UserAuthentication) authentication;
        if (auth.getVkToken() == null || auth.getVkToken().isBlank()) {
            throw new BadCredentialsException("token is null");
        }
        VkUser vkUserData = vkUserDao.getVkUserData(auth.getVkToken());

        UserInfo userInfo = converter.convert(vkUserData);
        userInfo.setLogin(LOGIN_PREFIX+vkUserData.getId());
        userInfo.grantRole(Role.ROLE_USER);
        try {
            userInfo = userService.findUserByLogin(userInfo);
        } catch (UsernameNotFoundException e) {
            userService.addNewUser(userInfo);
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
