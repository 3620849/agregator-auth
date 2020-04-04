package com.weiss.weiss.config.parsers;

import com.weiss.weiss.config.UserAuthentication;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthParser  {
     Authentication parseRequest(String searcHeader, UserAuthentication authentication, HttpServletRequest request) ;
}
