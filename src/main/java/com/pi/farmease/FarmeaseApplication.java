package com.pi.farmease;

import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.enumerations.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
//@RequiredArgsConstructor
public class FarmeaseApplication {

//    private  final UserRepository userRepository ;
//    private final PasswordEncoder passwordEncoder ;
    public static void main(String[] args) {
        SpringApplication.run(FarmeaseApplication.class, args);

    }
//   @PostConstruct
//    public void createAdmin() {
//        User admin = User.builder().firstname("khalil").lastname("ben khoud").email("khalil@farmEase.com")
//                .password(passwordEncoder.encode("12"))
//                .enabled(true).role(Role.ADMIN).build() ;
//
//        userRepository.save(admin) ;
//    }

    // Configurer RestTemplate en tant que bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
