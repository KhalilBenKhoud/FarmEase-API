package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.pi.farmease.entities.enumerations.ProjectCategory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Double netIncomeLastYear;

    private Double marketValue;

    private String imageUrl;

    private Double goalAmount;

    private Date deadline;

    private Double equityOffered;

    private Double dividendPayoutRatio;


    @Enumerated(EnumType.STRING)
    private ProjectCategory projectCategory;

    private Date createdAt;

    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonBackReference
    @JsonIgnore
    private User creator;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Investment> investments;


    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Performance> performances;


}
