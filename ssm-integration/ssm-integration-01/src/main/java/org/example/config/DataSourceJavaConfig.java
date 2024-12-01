package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

@PropertySource("classpath:jdbc.properties")
@Configuration
public class DataSourceJavaConfig {
    @Bean
    public DataSource getDataSource(
            @Value("${jdbc.url}")String url,
            @Value("${jdbc.driver}")String driverName,
            @Value("${jdbc.user}")String user,
            @Value("${jdbc.password}")String password
    ) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setDriverClassName(driverName);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }
}
