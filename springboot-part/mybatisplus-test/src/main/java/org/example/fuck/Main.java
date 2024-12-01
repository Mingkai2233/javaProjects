package org.example.fuck;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.example.fuck.mapper")
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // 运行 Spring Boot 应用
        SpringApplication.run(Main.class, args);
    }
}
