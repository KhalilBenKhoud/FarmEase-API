package com.pi.farmease.entities;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Message implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idM;

    private String message ;
    private Date date;

    @OneToOne
    private User sender;
    @OneToOne
    private User receiver;


    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;




}