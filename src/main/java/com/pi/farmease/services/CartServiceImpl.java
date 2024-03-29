package com.pi.farmease.services;


import com.pi.farmease.dao.CartItemsRepository;
import com.pi.farmease.dao.CartRepository;
import com.pi.farmease.dao.ProductRepository;
import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.entities.Cart;
import com.pi.farmease.entities.CartItems;
import com.pi.farmease.entities.Product;
import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@AllArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    CartItemsRepository cartItemsRepository;
    CouponService couponService ;


    public void addToCart(Long productId, Integer quantity, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));



        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à zéro.");
        } else if (quantity > product.getProductStock()) {
            throw new IllegalArgumentException("La quantité demandée dépasse le stock disponible.");
        }

        // Obtient le panier de l'utilisateur ou crée un nouveau panier s'il n'existe pas
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        // Crée un nouvel élément de panier
        CartItems cartItem = new CartItems();
        cartItem.setProduct(product);
        cartItem.setCartItemsQuantity(quantity);
        cartItem.setCart(cart);

        cartItem.setItemsPrice(product.getProductPrice() * quantity);
        cart.getCartItems().add(cartItem);
        Double newTotalPrice = cart.getTotalPrice() + cartItem.getItemsPrice();
        cart.setTotalPrice(newTotalPrice);

        Integer newProductStock = 0;
        newProductStock = product.getProductStock() - quantity;
        product.setProductStock(newProductStock);


        cartItemsRepository.save(cartItem);
        cartRepository.save(cart);
        productRepository.save(product);
    }

    public void removeFromCartByProductId(Long productId, Long userId) {

        // **Nouveau :** Obtient l'utilisateur à partir de son identifiant
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // **Nouveau :** Obtient le panier de l'utilisateur
        Cart cart = user.getCart();

        // **Nouveau :** Filtre les éléments du panier pour trouver celui avec le produit spécifié
        CartItems cartItemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé dans le panier"));

        // **Nouveau :** Calcule le prix total à soustraire
        Double priceToRemove = cartItemToRemove.getItemsPrice();

        // Supprime l'élément de panier du panier
        cart.getCartItems().remove(cartItemToRemove);

        // **Nouveau :** Met à jour le prix total du panier
        cart.setTotalPrice(cart.getTotalPrice() - priceToRemove);

        // Met à jour le panier dans la base de données
        cartRepository.save(cart);

        // **Nouveau :** Supprime l'élément de panier de la base de données
        cartItemsRepository.deleteById(cartItemToRemove.getCartItemsId());
    }

    public void updateQuantity(Long cartItemId, Integer newQuantity) {
        // Obtient l'élément de panier à partir de son identifiant
        CartItems cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Elément de panier non trouvé"));

        // Obtient le panier auquel appartient l'élément de panier
        Cart cart = cartItem.getCart();

        // Calcule la différence de prix
        Double priceDiff = (newQuantity - cartItem.getCartItemsQuantity()) * cartItem.getProduct().getProductPrice();

        // Met à jour la quantité de l'élément de panier
        cartItem.setCartItemsQuantity(newQuantity);
        cartItem.setItemsPrice(cartItem.getProduct().getProductPrice() * newQuantity);


        // Met à jour le prix total du panier
        cart.setTotalPrice(cart.getTotalPrice() + priceDiff);

        // Met à jour le panier dans la base de données
        cartRepository.save(cart);

        // Met à jour l'élément de panier dans la base de données
        cartItemsRepository.save(cartItem);
    }


    //    public List<CartItems> retrieveCart(Long userId) {
//        // Obtient le panier de l'utilisateur
//        Cart cart = cartRepository.findByUserUserId(userId);
//
//        // Récupère les éléments du panier et les convertit en liste
//        List<CartItems> cartItems = new ArrayList<>(cart.getCartItems());
//
//        // Parcourt les éléments du panier et affiche les informations
//        for (CartItems item : cartItems) {
//            System.out.println("Produit : " + item.getProduct().getProductName());
//            System.out.println("Quantité : " + item.getCartItemsQuantity());
//            System.out.println("Prix unitaire : " + item.getProduct().getProductPrice());
//            System.out.println("Prix total : " + item.getCartItemsQuantity() * item.getProduct().getProductPrice());
//            System.out.println();
//        }
//
//        // Retourne la liste des éléments du panier
//        return cartItems;
//    }
    @Override
    public List<Map<String, Object>> retrieveCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        Set<CartItems> cartItems = cart.getCartItems();

        List<Map<String, Object>> productList = new ArrayList<>();
        double cartPrice = 0.0;


        for (CartItems item : cartItems) {
            Product product = productRepository.findById(item.getProduct().getProductId()).orElse(null);

            if (product != null) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("productName", product.getProductName());
                productDetails.put("quantity", item.getCartItemsQuantity());
                productDetails.put("unitPrice", item.getItemsPrice() / item.getCartItemsQuantity()); // Calculate unit price
                productDetails.put("totalPrice", item.getItemsPrice());
                productList.add(productDetails);

                cartPrice += item.getItemsPrice();
            } else {
                // Handle missing product (log error, inform user, etc.)
                System.out.println("Error: Product not found for cart item: " + item.getCartItemsId());
            }
        }

        // Add cart total as a separate item
        Map<String, Object> cartPriceDetails = new HashMap<>();
        cartPriceDetails.put("totalPrice", cartPrice);
        productList.add(cartPriceDetails);

        return productList;
    }

    @Override
    public List<CartItems> selectAll() {
        return cartItemsRepository.findAll();
    }

    @Override
    public List<CartItems> selectAll(Long userId) {
        return cartItemsRepository.findAllByCartUserId(userId);
    }

    @Override
    public List<Cart> selectAllCart() {
        return cartRepository.findAll();
    }



    public List<Double> calculateMonthlyPrices(Long cartId, Integer numberOfMonths, Double downPayment) {
        List<Double> monthlyPrices = new ArrayList<>();
        Cart cart = cartRepository.findById(cartId).orElse(null);
        Double v0 = cart.getTotalPrice() - downPayment; // Prix initial de la carte
        Double i = 0.05;


        // Calculer le prix pour chaque mois en utilisant la formule v = v0 * (1 + i)^n
        for (Integer month = 1; month <= numberOfMonths; month++) {
            double priceForMonth = (v0 * Math.pow(1 + i, month) / numberOfMonths);
            monthlyPrices.add(priceForMonth);
        }

        return monthlyPrices;
    }

    public void applyCouponToCart(Long cartId, String couponCode) {
        // Récupérer le panier
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (!couponService.isCouponValid(couponCode)) {
            // Coupon non trouvé ou expiré
            throw new IllegalArgumentException("Coupon non valide ou expiré.");
        }

        // Appliquer la remise du coupon au montant total du panier
        double totalPriceWithDiscount = couponService.applyCouponDiscount(couponCode, cart.getTotalPrice());

        // Mettre à jour le montant total du panier et enregistrer le code du coupon appliqué
        cart.setTotalPrice(totalPriceWithDiscount);
        cart.setCouponCode(couponCode);
        cartRepository.save(cart);
    }
}






