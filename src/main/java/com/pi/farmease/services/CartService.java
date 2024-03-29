package com.pi.farmease.services;


import com.pi.farmease.entities.Cart;
import com.pi.farmease.entities.CartItems;

import java.util.List;
import java.util.Map;

public interface CartService {


    public void addToCart(Long productId, Integer quantity , Long userId) ;

    public void removeFromCartByProductId(Long productId, Long userId) ;

    public void updateQuantity(Long cartItemId, Integer newQuantity) ;
    //public List<CartItems> retrieveCart(Long userId) ;

    List<CartItems> selectAll();

    public List<CartItems> selectAll(Long userId);

    public List<Map<String, Object>> retrieveCart(Long userId) ;

    public List<Cart> selectAllCart() ;
    public List<Double> calculateMonthlyPrices(Long cartId, Integer numberOfMonths, Double downPayment) ;
    public void applyCouponToCart(Long cartId, String couponCode) ;
}
