package com.pi.farmease.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long cartItemsId;

    Double itemsPrice;
    Integer cartItemsQuantity;
    @JsonIgnore
    @ManyToOne
    private Product product;
    @JsonIgnore
    @ManyToOne
    private Cart cart;
    @Override
    public int hashCode() {
        return Objects.hash(cartItemsId, itemsPrice, cartItemsQuantity);
    }

}
