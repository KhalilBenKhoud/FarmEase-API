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

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {


    CartService cartService ;
    ProductService productService ;
    CartRepository cartRepository ;
    ProductRepository productRepository;
    TransactionService transactionService ;
    UserService userService ;



    @PostMapping("/addProductToCart")
    public ResponseEntity<String> addToCart(@RequestParam Long productId, @RequestParam Integer quantity, @RequestParam Long userId) {
        try {
            cartService.addToCart(productId, quantity, userId);
            return ResponseEntity.ok("Produit ajouté au panier avec succès !");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/remove-product/{productId}/{userId}")
    public ResponseEntity<?> removeFromCartByProductId(@PathVariable Long productId, @PathVariable Long userId) {
        try {
            cartService.removeFromCartByProductId(productId, userId);
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

    @GetMapping("/cartitemsByUser/{userId}")
    public List<CartItems> getAllCartItemsByUserId(@PathVariable Long userId) {
        return cartService.selectAll(userId);
    }

    @GetMapping("/cartDetail/{userId}")
    public List<Map<String, Object>> retrieveCart(@PathVariable Long userId) {
        return cartService.retrieveCart(userId);
    }
    @GetMapping("/calculateMonthlyPrices")
    public ResponseEntity<List<Double>> calculateMonthlyPrices(@RequestParam Long cartId, @RequestParam Integer numberOfMonths, @RequestParam Double downPayment) {
        try {
            List<Double> monthlyPrices = cartService.calculateMonthlyPrices(cartId, numberOfMonths, downPayment);
            return ResponseEntity.ok(monthlyPrices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/{cartId}/applyCoupon")
    public ResponseEntity<String> applyCouponToCart(@PathVariable Long cartId, @RequestParam String couponCode) {
        try {
            cartService.applyCouponToCart(cartId, couponCode);
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


    @PostMapping("/confirm/{userId}")
    public ResponseEntity<String> confirmPurchase(@PathVariable Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("User with ID " + userId + " not found.");
        }
        try {
            cartService.confirmPurchase(user);
            return ResponseEntity.ok("Purchase confirmed successfully.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to confirm purchase: " + e.getMessage());
        }
    }
    /////////////////////////
    @PostMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        User user = userService.getById(userId);
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
