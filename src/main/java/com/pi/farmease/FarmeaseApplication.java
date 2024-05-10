package com.pi.farmease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class FarmeaseApplication {


    public static void main(String[] args) {
        SpringApplication.run(FarmeaseApplication.class, args);

    }


}
