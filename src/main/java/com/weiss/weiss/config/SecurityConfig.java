package com.weiss.weiss.config;

import com.weiss.weiss.config.filters.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.Arrays;

//define class as config bean
@Configuration
//to use secure annotation PreAuthorize i add this @EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
//it is trip automatic security settings
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomAuthenticationProvider authProvider;
    @Autowired
    CustomAuthenticationProvider2 authProvider2;
    @Override
    public void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(authProvider);
        auth.authenticationProvider(authProvider2);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests().antMatchers("/", "/api/authenticate").permitAll()
                .anyRequest().authenticated().and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(getLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private LoginFilter getLoginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(new AntPathRequestMatcher("/api/**"));
        loginFilter.setAuthenticationManager(this.authenticationManager());
        return loginFilter;
    }

}
