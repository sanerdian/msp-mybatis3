package com.jnetdata.msp.config;

import com.jnetdata.msp.core.filter.ActionContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableConfigurationProperties({CorsProperties.class})
public class FilterBeansConfig {

    @Autowired
    private CorsProperties corsProperties;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(corsProperties.getAllowCredentials());
        config.setAllowedOrigins(corsProperties.getAllowedOrigins());
        config.setAllowedHeaders(corsProperties.getAllowedHeaders());
        config.setAllowedMethods(corsProperties.getAllowedMethods());
        config.setMaxAge(corsProperties.getMaxAge());
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Bean
    public FilterRegistrationBean registCorsFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(corsFilter());
        //优先处理
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public ActionContextFilter actionContextFilter() {
        ActionContextFilter actionContextFilter = new ActionContextFilter();
        return actionContextFilter;
    }


    @Bean
    public FilterRegistrationBean registActionContextFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(actionContextFilter());
        registration.setOrder(2);
        return registration;
    }

}
