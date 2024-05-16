package com.pi.farmease.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Signal_post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_signal_post;


    private long post_id_signal;


    private Integer user_id_signal;
}
