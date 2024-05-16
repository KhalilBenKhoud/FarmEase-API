package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long cartId;
    String cartName;

 Double totalPrice;



    String couponCode;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE, fetch =  FetchType.EAGER)
    private Set<CartItems> cartItems = new HashSet<>();

    @OneToOne(mappedBy = "cart")
    private User user;


    @Override
    public int hashCode() {
        return Objects.hash(cartId, cartName, totalPrice, couponCode);
    }


}
