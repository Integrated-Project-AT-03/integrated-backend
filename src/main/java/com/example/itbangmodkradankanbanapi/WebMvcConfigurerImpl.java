package com.example.itbangmodkradankanbanapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // This annotation indicates that this class contains Spring configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {

    @Value("${value.url.cross.origin}")
   private String CORS_ORIGIN;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(CORS_ORIGIN)
                        .allowedMethods("GET","PATCH", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .exposedHeaders("Set-Cookie")
                        .allowCredentials(true);
            }
        };
    }
}
