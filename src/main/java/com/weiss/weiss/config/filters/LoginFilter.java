package com.weiss.weiss.config.filters;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.model.UserInfo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(RequestMatcher requiresAuthenticationRequestMatcher){
      super(requiresAuthenticationRequestMatcher);

  }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request,response);

    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        //TODO check who is the user
        //get from header token or user name and password
        //if request has authorization
        //check if authentication is valid
        //set authentication to request
        String s = httpServletRequest.getRequestURL().toString();
        Authentication auth = new UserAuthentication(new UserInfo());
        if(s.equals("http://localhost:8080/api/authenticate")){
            boolean isA=true;
            auth.setAuthenticated(isA);
        }
        AuthenticationManager manager = this.getAuthenticationManager();
        return manager.authenticate(auth); }
}
