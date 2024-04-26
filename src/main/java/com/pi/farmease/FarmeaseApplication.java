package com.pi.farmease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class FarmeaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmeaseApplication.class, args);
    }

    // Configurer RestTemplate en tant que bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
