package org.example.springbootbaselog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.io.ClassPathResource;

import javax.swing.*;

@Slf4j
@SpringBootApplication
public class SpringbootBaseLogApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringbootBaseLogApplication.class)
                .banner(new ResourceBanner(new ClassPathResource("banner.txt")))
                .run(args);
    }

}
