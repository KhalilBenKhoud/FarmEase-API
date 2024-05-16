package com.pi.farmease.services;


import com.pi.farmease.dao.ProductRepository;
import com.pi.farmease.entities.Product;
import com.pi.farmease.entities.User;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@EnableScheduling
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    UserService userService ;
    NotificationService notificationService ;


    @Override
    public Product edit(Long productId, Product updatedProduct) {
        // Vérifier si le produit existe dans la base de données
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
existingProduct.setDateAdded(LocalDateTime.now());
        // Mettre à jour les champs du produit existant avec les valeurs du produit mis à jour
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setProductDescription(updatedProduct.getProductDescription());
        existingProduct.setProductPrice(updatedProduct.getProductPrice());
        existingProduct.setProductStock(updatedProduct.getProductStock());
        existingProduct.setProductCategory(updatedProduct.getProductCategory());


        // Sauvegarder et retourner le produit mis à jour
        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> selectAll() {
        return productRepository.findAll();
    }

    @Override
    public Product selectById(Long idProduct) {
        return productRepository.findById(idProduct).get();
    }

    @Override
    public void deleteById(Long idProduct) {
        productRepository.deleteById(idProduct);

    }
    @Override
    public Product toggleLike(Long productId, Principal connected) {
        User u = userService.getCurrentUser(connected) ;
        Product p=productRepository.findById(productId).get();
        if (p.getLikedByUsers().contains(u)){
            p.getLikedByUsers().remove(u);
            u.getLikedProduct().remove(p);
        }else{
            p.getLikedByUsers().add(u);
            u.getLikedProduct().add(p);
        }
        productRepository.save(p);
        userService.edit(u);
        return p;
    }

    public List<Product> trierProduitsParPrix() {
        // Implémentez ici la logique de tri des produits par prix
        // Par exemple, utilisez productRepository pour récupérer les produits triés
        return productRepository.findByOrderByProductPriceAsc(); // Exemple : trier par ordre croissant de prix
    }

    @Override
    public List<Product> getMostLikedProducts() {
        // Retrieve all products from the repository
        return productRepository.findMostLikedProducts();
    }

    @Override
    public void addProduct(MultipartFile imageFile , Product product) throws IOException {
       if(imageFile != null) {
           String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

           String uploadDirectory = "src\\main\\resources\\image";
           Path uploadPath = Path.of(uploadDirectory);
           if (!Files.exists(uploadPath)) {
               Files.createDirectories(uploadPath);
           }
           Path filePath = uploadPath.resolve(uniqueFileName);
           Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
           product.setProductImage(uniqueFileName);
       }
        product.setDateAdded(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByCategory(Product.ProductCategory productCategory) {
        return productRepository.findByProductCategory(productCategory);
    }
    @Override
    public List<Product> getProductsByName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Scheduled(fixedRate = 120000) // Vérifie toutes les 2 minutes
  //@Scheduled(cron = "0 0 0 */2 * *")
    public void checkOutOfStockProducts() {
        List<Product> outOfStockProducts = productRepository.findByProductStockEquals(0);
        for (Product product : outOfStockProducts) {
            String message = "Le produit " + product.getProductName() + " est en rupture de stock.";
            notificationService.sendNotificationToAdmin(message);
        }
    }

    public List<Product> getProductOutOfStock() {
        return productRepository.findByProductStockEquals(0);
    }



    @Override
    public List<Product> findProductsInPriceRange(float minPrice, float maxPrice) {
        return productRepository.findByProductPriceBetween(minPrice, maxPrice);
    }
    public Map<Long, Integer> countLikesByProduct() {
        List<Object[]> likeCounts = productRepository.countLikesByProduct();
        Map<Long, Integer> likesByProduct = new HashMap<>();

        for (Object[] result : likeCounts) {
            Long productId = (Long) result[0];
            Integer likeCount = (Integer) result[1];
            likesByProduct.put(productId, likeCount);
        }

        return likesByProduct;
    }

}


