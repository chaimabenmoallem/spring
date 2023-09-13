package com.epix.hawkadmin.config;/*package com.epix.hawkadmin.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, WebMvcAutoConfiguration.class })
public class WebMvcConfig {
    private final WebConfigProperties webConfigProperties;

    @Autowired
    public WebMvcConfig(WebConfigProperties webConfigProperties) {
        this.webConfigProperties = webConfigProperties;
    }
    @Bean
    public WebMvcConfigurer corsMappingConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // WebConfigProperties.Cors cors = webConfigProperties.getCors();
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .exposedHeaders("*");
            }
        };
    }
}*/