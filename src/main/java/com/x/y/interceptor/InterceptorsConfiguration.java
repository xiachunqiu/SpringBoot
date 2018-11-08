package com.x.y.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class InterceptorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludeUrls = {"/favicon.ico", "/**/js/**", "/error"};
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**").excludePathPatterns(excludeUrls);
    }
}