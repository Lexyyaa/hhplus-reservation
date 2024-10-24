package com.hhplus.reservation.support.config;

import com.hhplus.reservation.interfaces.api.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/concert/**")
                .addPathPatterns("/api/pay/**")
                .addPathPatterns("/api/reserve/**")
                .addPathPatterns("/api/point/**");
    }
}
