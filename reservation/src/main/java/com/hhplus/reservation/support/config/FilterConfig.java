package com.hhplus.reservation.support.config;

import com.hhplus.reservation.interfaces.api.filter.TokenChkFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TokenChkFilter> tokenCheckFilter() {
        FilterRegistrationBean<TokenChkFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenChkFilter());

        registrationBean.addUrlPatterns("/api/**");

        return registrationBean;
    }
}