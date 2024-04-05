package com.pi.farmease.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)


public class Likes  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likesId ;


    private Long userId;


    private Long ProductId;
}
