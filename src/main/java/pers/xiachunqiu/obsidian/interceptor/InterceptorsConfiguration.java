package pers.xiachunqiu.obsidian.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class InterceptorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] logInterceptorExcludeUrls = {"/static/**", "/error", "/page/**"};
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**").excludePathPatterns(logInterceptorExcludeUrls);
        String[] authInterceptorExcludeUrls = {"/", "/index/**", "/error", "/login/**", "/static/**", "/page/**"};
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**").excludePathPatterns(authInterceptorExcludeUrls);
    }
}