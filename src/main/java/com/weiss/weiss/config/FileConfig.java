package com.weiss.weiss.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.MultipartConfigElement;

@Configuration
public class FileConfig {
    @Bean(name = "commonsMultipartResolver")
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));

        return factory.createMultipartConfig();
    }
}
