package com.pi.farmease.services;


import com.pi.farmease.dao.CouponRepository;
import com.pi.farmease.entities.Coupon;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class CouponService {
    CouponRepository couponRepository;

    public Coupon findByCode(String code) {
        return couponRepository.findByCode(code);
    }

    public boolean isCouponValid(String code) {
        Coupon coupon = findByCode(code);
        return coupon != null && !coupon.isUsed() && LocalDate.now().isBefore(coupon.getExpirationDate());
    }

    public double applyCouponDiscount(String code, double totalAmount) {
        if (isCouponValid(code)) {
            Coupon coupon = findByCode(code);
            coupon.setUsed(true);
            couponRepository.save(coupon);
            return totalAmount * (1 - coupon.getDiscountPercentage() / 100.0);
        }
        return totalAmount;
    }
}

