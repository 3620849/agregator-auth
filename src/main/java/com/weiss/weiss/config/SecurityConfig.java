package com.weiss.weiss.config;

import com.weiss.weiss.config.filters.LoginFilter;
import com.weiss.weiss.config.providers.*;
import com.weiss.weiss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//define class as config bean
@Configuration
//to use secure annotation PreAuthorize i add this @EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
//it is trip automatic security settings
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    BasicAuthenticationProvider basicAuthProvider;
    @Autowired
    TokenAuthenticationProvider tokenAuthProvider;
    @Autowired
    GitHubAuthenticationProvider gitHubAuthenticationProvider;
    @Autowired
    VkAuthenticationProvider vkAuthenticationProvider;
    @Autowired
    FbAuthenticationProvider fbAuthenticationProvider;
    @Autowired
    GglAuthenticationProvider gglAuthenticationProvider;

    @Override
    public void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        //pay attention order is important
        auth.authenticationProvider(gglAuthenticationProvider);
        auth.authenticationProvider(fbAuthenticationProvider);
        auth.authenticationProvider(vkAuthenticationProvider);
        auth.authenticationProvider(gitHubAuthenticationProvider);
        auth.authenticationProvider(tokenAuthProvider);
        auth.authenticationProvider(basicAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/p/**","/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(getLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    public LoginFilter getLoginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(new AntPathRequestMatcher("/api/s/**"));
        loginFilter.setAuthenticationManager(this.authenticationManager());
        return loginFilter;
    }


}
