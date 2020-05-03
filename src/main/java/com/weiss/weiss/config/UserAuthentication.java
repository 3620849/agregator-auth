package com.weiss.weiss.config;

import com.weiss.weiss.model.UserInfo;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
@Data
public class UserAuthentication implements Authentication {
    private UserInfo userInfo;
    private boolean auth = false;
    private String accessToken;
    private String gitHubToken;
    private String vkToken;
    private String fbToken;
    private String gglToken;
    private String disToken;
    private String androidToken;

    public UserAuthentication(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userInfo.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userInfo.getPassword() ;
    }

    @Override
    public Object getDetails() {
        return userInfo;
    }

    @Override
    public Object getPrincipal() {
        return userInfo;
    }

    @Override
    public boolean isAuthenticated() {
        return auth;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        auth= isAuthenticated;
    }

    @Override
    public String getName() {
        return userInfo.getUsername();
    }
}