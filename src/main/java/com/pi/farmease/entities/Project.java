package com.pi.farmease.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pi.farmease.entities.enumerations.ProjectStatus;
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

    private String csvFilePath;

    private String imageUrl;

    private String address;

    private Double goalAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deadline;

    private Double equityOffered;

    private Double dividendPayoutRatio;


    private Double totalInvestment; // Total amount raised so far

    @Enumerated(EnumType.STRING)
    private ProjectCategory projectCategory;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    private Date createdAt;

    private Date updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User creator;

    @OneToMany(mappedBy = "project" , fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Investment> investments;


    @OneToMany(mappedBy = "project" , fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Performance> performances;


}
