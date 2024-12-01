package org.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@PropertySource("classpath:jdbc.properties")
@Configuration
public class DataSourceJavaConfig {
    @Bean
    public DataSource dataSource(
           @Value("${jdbc.url}") String url,
           @Value("${jdbc.driver}") String driver,
           @Value("${jdbc.user}") String user,
           @Value("${jdbc.password}") String password
    ) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
}
