package com.pi.farmease.services;


import com.pi.farmease.entities.Cart;
import com.pi.farmease.entities.CartItems;
import com.pi.farmease.entities.User;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface CartService {

    public Double getTotalCartPrice(Principal connected) ;
    public void addToCart(Long productId, Integer quantity , Principal connected) ;

    public void removeFromCartByProductId(Long productId,Principal connected) ;

    public void updateQuantity(Long cartItemId, Integer newQuantity) ;
    //public List<CartItems> retrieveCart(Long userId) ;

    List<CartItems> selectAll();

    public List<CartItems> selectAll(Principal connected);

    public List<Map<String, Object>> retrieveCart(Principal connected) ;

    public List<Cart> selectAllCart() ;
    public List<Double> calculateMonthlyPrices(Principal connected, Integer numberOfMonths, Double downPayment) ;
    public void applyCouponToCart(Principal connected, String couponCode) ;

    public void confirmPurchase(Principal connected) ;
    public void clearCart(Cart cart) ;
}
