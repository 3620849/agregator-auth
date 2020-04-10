package com.weiss.weiss.config.providers;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.converters.UserConverter;
import com.weiss.weiss.dao.GitHubUserDao;
import com.weiss.weiss.model.git.GitHubUser;
import com.weiss.weiss.model.Role;
import com.weiss.weiss.model.UserInfo;
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
import org.springframework.stereotype.Component;

@Component
public class GitHubAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    GitHubUserDao gitHubUserDao;
    @Autowired
    UserService userService;
    @Autowired
    UserConverter converter;
    @Value("${github.prefix}")
    public String LOGIN_PREFIX;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication auth = (UserAuthentication) authentication;
        if (auth.getGitHubToken() == null || auth.getGitHubToken().isBlank()) {
            throw new BadCredentialsException("token is null");
        }
        GitHubUser gitHubUser = gitHubUserDao.getGitHubUserData(auth.getGitHubToken());
        gitHubUser.setLogin(LOGIN_PREFIX + gitHubUser.getLogin());
        UserInfo userInfo = converter.convert(gitHubUser);
        userInfo.grantRole(Role.ROLE_USER);
        try {
            userInfo = userService.findUserByLogin(userInfo);
        } catch (UsernameNotFoundException e) {
            try {
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
