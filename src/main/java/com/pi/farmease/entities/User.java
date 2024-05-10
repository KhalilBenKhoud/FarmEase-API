package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.pi.farmease.entities.enumerations.Role;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @NonNull
    private String firstname ;
    @NonNull
    private String lastname ;
    @NonNull
    private String password ;

    @NonNull
    @Column(unique = true)
    private String email ;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Role role ;

    private String imageName ;

    private boolean enabled  ;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date registrationDate = new Date() ;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Wallet wallet ;
    @JsonManagedReference
    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Post> posts ;
    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Comment> comments ;
    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Application> applications ;


    @OneToMany(mappedBy = "creator")
    @JsonManagedReference
    private List<Project> createdProjects;

    @OneToMany(mappedBy = "investor")
    @JsonManagedReference
    private List<Investment> investments;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())) ;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled ;
    }
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private Set<Product> likedProduct;
    @JsonIgnore
    @OneToOne
    private Cart cart;
    @OneToMany
    private Set<Product> Products;

}
