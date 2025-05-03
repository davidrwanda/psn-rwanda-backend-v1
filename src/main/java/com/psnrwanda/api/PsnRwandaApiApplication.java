package com.psnrwanda.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application class for PSN Rwanda API
 */
@SpringBootApplication
@EnableJpaAuditing
public class PsnRwandaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsnRwandaApiApplication.class, args);
    }
} 