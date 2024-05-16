package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pi.farmease.entities.enumerations.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Boolean Credit_authorization;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date registrationDate = new Date() ;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Wallet wallet ;
    @JsonManagedReference
    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Comment> comments  = new ArrayList<>();
    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Application> applications  = new ArrayList<>();

    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Insurance> insurances  = new ArrayList<>();

    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Credit> credit  = new ArrayList<>();

    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Garantor> garantor  = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Project> createdProjects  = new ArrayList<>();

    @OneToMany(mappedBy = "investor", fetch = FetchType.EAGER)

    private List<Investment> investments  = new ArrayList<>();



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
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Product> Products;

}
