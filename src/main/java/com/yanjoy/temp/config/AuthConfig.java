package com.yanjoy.temp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor initAuthInterceptor() {
        return new AuthInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initAuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("")
                .excludePathPatterns("/")
                .excludePathPatterns("/**/login/**")
                .excludePathPatterns("/**/pass/**")
                .excludePathPatterns("/**/excel/**")
                .excludePathPatterns("/**/org/**");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(initAuthInterceptor())
//                .excludePathPatterns("/**");
//    }
}