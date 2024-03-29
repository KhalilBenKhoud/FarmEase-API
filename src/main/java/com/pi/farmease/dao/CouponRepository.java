package com.pi.farmease.dao;


import com.pi.farmease.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository  extends JpaRepository<Coupon, Long> {
    Coupon findByCode(String code);
}
