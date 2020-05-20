package com.weiss.weiss.model;



import com.weiss.weiss.model.forum.MyMark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserInfo implements UserDetails {
    private String id;
    private List<UserAutority> authorities;
    private String password;
    private String username;
    private String login;
    private String mail;
    private String photo;
    private List<String> clientIdList;
    private List<MyMark> markList;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    public void grantRole(Role r){
        if(authorities==null){
            authorities = new ArrayList<>();
        }
        if(authorities.size()>0){
            if(authorities.stream().anyMatch(g->g.getAuthority().equals(r.name()))){
                return;
            }
        };
        UserAutority userAutority = new UserAutority();
        userAutority.setAuthority(r.name());
        authorities.add(userAutority);
    }
    public void removeRole(Role role){
        if(authorities.size()>0) {
            List<UserAutority> list = authorities.stream().filter(r -> r.getAuthority().equals(role.name())).collect(Collectors.toList());
            this.authorities = list;
        }
    }
}
