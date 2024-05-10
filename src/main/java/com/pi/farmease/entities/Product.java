package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)


public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    Long productId;

    String productName;

    String productDescription;

    Double productPrice;

    Integer productStock;

@Enumerated(EnumType.STRING)
    ProductCategory productCategory;

    String productImage ;

    LocalDateTime dateAdded;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "likedProduct" , fetch = FetchType.EAGER)
    private Set<User> likedByUsers;
    public enum ProductCategory{
    FARMER,FISHER
    }
}
