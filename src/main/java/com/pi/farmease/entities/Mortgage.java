package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Mortgage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mortage ;
    private String description_mortage ;
    private double prize_mortage ;
    private String category_mortage ;
    private String type_mortage ;
    @JsonIgnore
    @ToString.Exclude

    @OneToMany(mappedBy = "mortgage")
    private Set<Application> applications;


}
