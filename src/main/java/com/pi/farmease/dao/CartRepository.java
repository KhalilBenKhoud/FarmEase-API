package com.pi.farmease.dao;

import com.pi.farmease.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Integer userId);

}
