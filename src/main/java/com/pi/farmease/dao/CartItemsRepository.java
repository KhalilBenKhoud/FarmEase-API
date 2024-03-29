package com.pi.farmease.dao;


import com.pi.farmease.entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItems,Long> {
    List<CartItems> findAllByCartUserUserId(Long userId);
}
