package com.weiss.weiss.config.filters;

import com.weiss.weiss.config.UserAuthentication;
import com.weiss.weiss.config.parsers.*;
import com.weiss.weiss.model.UserInfo;
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
        UserAuthentication auth = (UserAuthentication) authResult;
        SecurityContextHolder.getContext().setAuthentication(auth);
        response.setHeader("X-Auth-Token",auth.getAccessToken());
        chain.doFilter(request,response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
        UserAuthentication auth = new UserAuthentication(new UserInfo());
        new FbParser().parseRequest("Fb-Oauth-Token",auth,req);
        new VkParser().parseRequest("Vk-Oauth-Token",auth,req);
        new GitHubParser().parseRequest("Git-Hub-Oauth-Token",auth,req);
        new TokenHeaderParser().parseRequest("X-Auth-Token",auth,req);
        new BasicHeaderParser().parseRequest("Authorization",auth,req);
        return this.getAuthenticationManager().authenticate(auth);
        }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        System.out.println("error");
    }
}