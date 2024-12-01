package org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableWebMvc
@ComponentScan(basePackages = {"org.example.service.impl"})
@Configuration
public class SpringJavaConfig {

}
