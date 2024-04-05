package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.ConnectionBuilder;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Mortgage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mortgage ;
    private String description_mortgage ;
    private long duration_mortgage;
    private double prize_mortgage ;
    private double month_payment;
    private String category_mortgage ;
    private String type_mortgage ;
    private long price_mortgage;
    private String land_description;
    @JsonIgnore
    @ToString.Exclude

    @OneToMany(mappedBy = "mortgage")
    private Set<Application> applications;



}
