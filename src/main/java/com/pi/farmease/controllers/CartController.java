package com.pi.farmease.controllers;

import com.pi.farmease.dao.CartRepository;
import com.pi.farmease.dao.ProductRepository;
import com.pi.farmease.entities.Cart;
import com.pi.farmease.entities.CartItems;
import com.pi.farmease.entities.Transaction;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.CartService;
import com.pi.farmease.services.ProductService;
import com.pi.farmease.services.TransactionService;
import com.pi.farmease.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {


    CartService cartService ;
    ProductService productService ;
    CartRepository cartRepository ;
    ProductRepository productRepository;
    TransactionService transactionService ;
    UserService userService ;


    @GetMapping("/totalPrice")
    public ResponseEntity<Double> getTotalCartPrice(Principal connected) {
        Double totalPrice = cartService.getTotalCartPrice(connected);
        return ResponseEntity.ok(totalPrice);
    }
    @PostMapping("/addProductToCart/{productId}/{quantity}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId, @PathVariable  Integer quantity, Principal connected) {
        try {
            cartService.addToCart(productId, quantity, connected);
            return ResponseEntity.ok("Produit ajouté au panier avec succès !");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/remove-product/{productId}")
    public ResponseEntity<?> removeFromCartByProductId(@PathVariable Long productId,Principal connected) {
        try {
            cartService.removeFromCartByProductId(productId, connected);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update-quantity/{cartItemId}/{newQuantity}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long cartItemId, @PathVariable Integer newQuantity) {
        try {
            cartService.updateQuantity(cartItemId, newQuantity);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cartitems")
    public List<CartItems> getAllCartItems() {
        return cartService.selectAll();
    }
    @GetMapping("/cart")
    public List<Cart> getAllCart() {
        return cartService.selectAllCart();
    }

    @GetMapping("/cartitemsByUser")
    public List<CartItems> getAllCartItemsByUserId( Principal connected) {
        return cartService.selectAll(connected);
    }

    @GetMapping("/cartDetail")
    public List<Map<String, Object>> retrieveCart(Principal connected) {
        return cartService.retrieveCart(connected);
    }
    @GetMapping("/calculateMonthlyPrices/{numberOfMonths}/{downPayment}")
    public ResponseEntity<List<Double>> calculateMonthlyPrices( Principal connected, @PathVariable Integer numberOfMonths, @PathVariable Double downPayment) {
        try {
            List<Double> monthlyPrices = cartService.calculateMonthlyPrices(connected, numberOfMonths, downPayment);
            return ResponseEntity.ok(monthlyPrices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/applyCoupon/{couponCode}")
    public ResponseEntity<String> applyCouponToCart(Principal connected, @PathVariable String couponCode) {
        try {
            cartService.applyCouponToCart(connected, couponCode);
            return ResponseEntity.ok("Coupon applied successfully to the cart.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to apply coupon to the cart.");
        }
    }



    //////////////////////TRANSACTION

    @GetMapping("/transactions/sale")
    public List<Transaction> getAllSaleTransactions() {
        return transactionService.getAllSaleTransactions();
    }


    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPurchase(Principal connected) {
        User user = userService.getCurrentUser(connected) ;
      //  User user = userService.getById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("User with ID " + user.getId() + " not found.");
        }
        try {
            cartService.confirmPurchase(connected);
            return ResponseEntity.ok("Purchase confirmed successfully.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to confirm purchase: " + e.getMessage());
        }
    }
    /////////////////////////
    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(Principal connected) {
        User user = userService.getCurrentUser(connected) ;
        //User user = userService.getById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Cart cart = user.getCart();
        if (cart == null) {
            return new ResponseEntity<>("Cart not found for the user", HttpStatus.NOT_FOUND);
        }

        cartService.clearCart(cart);

        return new ResponseEntity<>("Cart cleared successfully", HttpStatus.OK);
    }

}
