package com.pi.farmease.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    private String token ;

    private LocalDateTime expiryDateTime  ;

    @ManyToOne
    private User user ;
}
