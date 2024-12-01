package org.example.springbootheadlinepart;

import org.example.springbootheadlinepart.interceptor.LoginProtectInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@MapperScan("org.example.springbootheadlinepart.mapper")
@SpringBootApplication
public class SpringbootHeadlinePartApplication implements WebMvcConfigurer {
    @Autowired
    LoginProtectInterceptor loginProtectInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootHeadlinePartApplication.class, args);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginProtectInterceptor).addPathPatterns("/headline/**");
    }
}
