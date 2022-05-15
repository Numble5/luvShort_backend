package com.example.backend.config;

import com.example.backend.security.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    // HTTP 통신할 때 쓸 객체 만들어주는 간단한 템플릿 메서드
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:3000")
                .allowedOrigins("https://luvshort.netlify.app/")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }



    @Bean
    public FilterRegistrationBean filterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CorsFilter());
        return registrationBean;
    }




}
