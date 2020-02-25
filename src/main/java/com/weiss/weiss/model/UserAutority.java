package com.weiss.weiss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAutority implements GrantedAuthority {

    @NotNull
    private String authority;
    @Override
    public String getAuthority() {
        return authority;
    }

}

