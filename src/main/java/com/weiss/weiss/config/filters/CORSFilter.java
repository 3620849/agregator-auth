package com.weiss.weiss.config.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {
    @Value("${clientUrl}")
    private String clientUrl;
    @Value("${allowedOrigins}")
    private String[] allowedOrigins;
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        response.setHeader("Access-Control-Allow-Origin", getAllowedOrigin(request.getHeader("origin")));
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS, POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers",  "X-Auth-Token, Access-Control-Expose-Headers,Authorization, Cache-Control, Content-Type, Access-Control-Allow-Origin, Access-Control-Allow-Headers, Origin");
        response.setHeader("Access-Control-Allow-Headers", "Dis-Oauth-Token, Ggl-Oauth-Token,Fb-Oauth-Token,Vk-Oauth-Token, Git-Hub-Oauth-Token, X-Auth-Token, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }

    }
    private String getAllowedOrigin(String origin){
        String result = clientUrl;
        for(String o : allowedOrigins){
            if(o.equalsIgnoreCase(origin)){
                result = o;
            };
        }
        return result;
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}