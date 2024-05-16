package com.pi.farmease.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Like_post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_Like_post;


    private long post_id_like;


    private Integer user_id_like;
}
