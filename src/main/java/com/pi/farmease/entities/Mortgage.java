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
@Getter
@Setter
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
    private double rating_mortgage;

    private float interest;

    @OneToMany(mappedBy = "mortgage" , fetch = FetchType.EAGER)
    private Set<Application> applications;



    @ToString.Exclude

    @ManyToMany()
    private Set<Materiel> materiels;




}
