package com.pi.farmease.services;


import com.pi.farmease.dao.*;
import com.pi.farmease.entities.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;


@AllArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    CartItemsRepository cartItemsRepository;
    CouponService couponService ;
    TransactionService transactionService ;
    CouponRepository couponRepository;
    EmailService emailService ;
    UserService userService ;

    public Double getTotalCartPrice(Principal connected) {
        User user = userService.getCurrentUser(connected) ;
        Cart cart = cartRepository.findByUserId(user.getId());
        // Implémentez la logique pour calculer le prix total du panier
        Double totalPrice = cart.getTotalPrice() /* Logique pour calculer le prix total */;
        return totalPrice;
    }

    public void addToCart(Long productId, Integer quantity, Principal connected) {
        User user = userService.getCurrentUser(connected) ;


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

    public void removeFromCartByProductId(Long productId, Principal connected) {

        // **Nouveau :** Obtient l'utilisateur à partir de son identifiant
        User user = userService.getCurrentUser(connected);

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
    public List<Map<String, Object>> retrieveCart(Principal connected) {
        User user = userService.getCurrentUser(connected);
        Integer userId = user.getId();
        Cart cart = cartRepository.findByUserId(userId);
        Set<CartItems> cartItems = cart != null ? cart.getCartItems() : new HashSet<>();

        List<Map<String, Object>> productList = new ArrayList<>();
        double cartPrice = 0.0;

        for (CartItems item : cartItems) {
            Product product = productRepository.findById(item.getProduct().getProductId()).orElse(null);

            if (product != null) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("productName", product.getProductName());
                productDetails.put("quantity", item.getCartItemsQuantity());
                productDetails.put("unitPrice", item.getItemsPrice() / item.getCartItemsQuantity());
                productDetails.put("totalPrice", item.getItemsPrice());
                productDetails.put("imageUrl", product.getProductImage()); // Ajoutez l'URL de l'image du produit
                productDetails.put("productid", product.getProductId());
                productList.add(productDetails);

                cartPrice += item.getItemsPrice();
            } else {
                // Handle missing product
                System.out.println("Error: Product not found for cart item: " + item.getCartItemsId());
            }
        }

        // Add cart total as a separate item
//        Map<String, Object> cartPriceDetails = new HashMap<>();
//        cartPriceDetails.put("cartPrice", cartPrice);
   //     productList.add(cartPriceDetails);

        return productList;
    }

    @Override
    public List<CartItems> selectAll() {
        return cartItemsRepository.findAll();
    }

    @Override
    public List<CartItems> selectAll(Principal connected) {
        User user = userService.getCurrentUser(connected) ;
        return cartItemsRepository.findAllByCartUserId(user.getId());
    }

    @Override
    public List<Cart> selectAllCart() {
        return cartRepository.findAll();
    }



    public List<Double> calculateMonthlyPrices(Principal connected, Integer numberOfMonths, Double downPayment) {
        List<Double> monthlyPrices = new ArrayList<>();
        User user = userService.getCurrentUser(connected) ;

        Cart cart = cartRepository.findById(user.getCart().getCartId()).orElse(null);
        Double v0 = cart.getTotalPrice() - downPayment; // Prix initial de la carte
        Double i = 0.05;


        // Calculer le prix pour chaque mois en utilisant la formule v = v0 * (1 + i)^n
        for (Integer month = 1; month <= numberOfMonths; month++) {
            double priceForMonth = (v0 * Math.pow(1 + i, month) / numberOfMonths);
            monthlyPrices.add(priceForMonth);
        }

        return monthlyPrices;
    }
    //////////////////////////////////////////COUPON
    public void applyCouponToCart(Principal connected, String couponCode) {
        User user = userService.getCurrentUser(connected) ;
        // Récupérer le panier
        Cart cart = cartRepository.findById(user.getCart().getCartId()).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("Panier introuvable.");
        }

        // Vérifier si le coupon est valide
        Coupon coupon = couponRepository.findByCodeAndExpirationDateAfter(couponCode, LocalDate.now());
        if (coupon == null) {
            throw new IllegalArgumentException("Coupon non valide ou expiré.");
        }

        // Appliquer la réduction au montant total du panier
        double discountAmount = cart.getTotalPrice() * (coupon.getDiscountPercentage() / 100);
        double totalPriceWithDiscount = cart.getTotalPrice() - discountAmount;

        // Mettre à jour le montant total du panier et enregistrer le code du coupon appliqué
        cart.setTotalPrice(totalPriceWithDiscount);
        cart.setCouponCode(couponCode);
        cartRepository.save(cart);
    }



    public void confirmPurchase(Principal connected) {
        User user = userService.getCurrentUser(connected) ;
        try {
            Cart cart = user.getCart(); // Récupérer le panier de l'utilisateur
            double totalAmount = cart.getTotalPrice(); // Calculer le montant total du panier
            transactionService.sendMoney(user, totalAmount); // Effectuer la transaction


            // Générer un code coupon aléatoire
            String couponCode = generateCouponCode();

            // Enregistrer le code coupon dans la base de données
            Coupon coupon = saveCoupon(couponCode);

            // Envoyer un e-mail de confirmation d'achat avec le code coupon
            sendConfirmationEmail(connected, cart, coupon);
            transactionService.saveSaleTransaction(user,totalAmount);

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private String generateCouponCode() {
        // Générer un code coupon aléatoire
        return UUID.randomUUID().toString();
    }

    private Coupon saveCoupon(String couponCode) {
        // Créer une nouvelle instance de coupon
        Coupon coupon = new Coupon();
        coupon.setCode(couponCode);
        coupon.setDiscountPercentage(10.0); // Par exemple, définir le pourcentage de réduction à 10%
        coupon.setExpirationDate(LocalDate.now().plusYears(1)); // Définir la date d'expiration dans un an
        coupon.setUsed(false); // Initialiser le coupon comme non utilisé
        // Enregistrer le coupon dans la base de données
        return couponRepository.save(coupon);
    }


    public void sendConfirmationEmail(Principal connected, Cart cart, Coupon coupon) {
        User user = userService.getCurrentUser(connected) ;
        String userEmail = user.getEmail();
        String subject = "Confirmation d'achat et code coupon";

        StringBuilder message = new StringBuilder();
        message.append("Bonjour ").append(user.getFirstname()).append(",\n\n");
        message.append("Nous sommes heureux de vous confirmer que votre achat a été effectué avec succès !\n\n");

        // Ajouter les détails du panier et le montant total de la commande
        message.append("\nDétails de la commande :\n");
        for (CartItems item : cart.getCartItems()) {
            message.append("- ").append(item.getProduct().getProductName()).append(": ").append(item.getCartItemsQuantity()).append(" x ").append(item.getProduct().getProductPrice()).append(" TND\n");
        }
        message.append("\nMontant total de la commande : ").append(cart.getTotalPrice()).append(" TND\n\n");

        // Ajouter le code coupon
        message.append("Utilisez le code coupon suivant pour bénéficier d'une réduction lors de votre prochain achat : ").append(coupon.getCode()).append("\n\n");

        // Message de remerciement
        message.append("Merci pour votre achat !\n\nCordialement,\nFARMEASE");

        // Envoi du mail à l'utilisateur
        emailService.sendEmail(userEmail, subject, message.toString());
    }


    @Override
    @Transactional
    public void clearCart(Cart cart) {
        cart.setTotalPrice(0.0);

        cartItemsRepository.deleteByCartCartId(cart.getCartId());

    }



}






