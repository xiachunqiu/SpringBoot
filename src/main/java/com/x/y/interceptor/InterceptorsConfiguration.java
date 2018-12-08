package com.x.y.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class InterceptorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludeUrls = {"/static/js/**", "/error", "/page/**"};
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**").excludePathPatterns(excludeUrls);
    }
}