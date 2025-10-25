package com.dublefin.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class WebConfig {

    @Bean
    @RequestScope
    public HttpServletRequest httpServletRequest(HttpServletRequest request) {
        return request;
    }
}

