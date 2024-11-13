package org.example;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@PropertySource("classpath:jdbc.properties")
@ComponentScan("org.example")
@Configuration
public class StudentConfiguration {
    @Bean
    public DruidDataSource druidDataSource(@Value("${jdbc.url}")String url, @Value("${jdbc.driver}") String driverClassName,
                                           @Value("${jdbc.username}")String username, @Value("${jdbc.password}")String password) {
        System.out.println(url);
        System.out.println(driverClassName);
        System.out.println(username);
        System.out.println(password);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }


    @Bean
    public JdbcTemplate jdbcTemplate(DruidDataSource druidDataSource) {
        return new JdbcTemplate(druidDataSource);
    }
}
