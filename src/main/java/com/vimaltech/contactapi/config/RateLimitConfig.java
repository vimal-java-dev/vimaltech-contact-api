package com.vimaltech.contactapi.config;

import com.vimaltech.contactapi.security.IpRateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class RateLimitConfig {

    @Bean
    public FilterRegistrationBean<IpRateLimitFilter> rateLimitFilterRegistration(
            IpRateLimitFilter filter) {

        FilterRegistrationBean<IpRateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1);

        return registration;
    }
}