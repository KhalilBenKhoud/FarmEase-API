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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@EnableScheduling
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    UserService userService ;
    NotificationService notificationService ;


    @Override
    public Product edit(Product p) {
        return productRepository.save(p);
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
    public Product toggleLike(Product a, User u) {
        Product p=productRepository.findById(a.getProductId()).get();
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

    @Override
    public List<Product> getMostLikedProducts() {
        // Retrieve all products from the repository
        List<Product> products = productRepository.findAll();

        // Sort the products in descending order of likes using a comparator
        return products.stream()
                .sorted(Comparator.comparingLong(p -> p.getLikedByUsers().size())) // Get size directly
                .collect(Collectors.toList());

    }

    @Override
    public void addProduct(MultipartFile imageFile ,Product product) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        String uploadDirectory = "src\\main\\resources\\image" ;
        Path uploadPath = Path.of(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        product.setProductImage(uniqueFileName);
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
//    @Scheduled(cron = "0 */2 * * * *") // Exécuter toutes les deux minutes
//@Scheduled(cron = "0 0 0 1 */6 ?") // Exécuter tous les 6 mois (le 1er de chaque 6ème mois)
//public void updateProductPrice() {
//    List<Product> products = productRepository.findAll();
//
//    LocalDateTime now = LocalDateTime.now();
//
//    for (Product product : products) {
//        // Calculer la différence entre la date actuelle et la date d'ajout du produit
//        Duration duration = Duration.between(product.getDateAdded(), now);
//
//        // Vérifier si la différence est égale à 6 mois (en secondes)
//        long sixMonthsInSeconds = 6L * 30L * 24L * 60L * 60L; // 6 mois en secondes
//        if (duration.getSeconds() >= sixMonthsInSeconds) {
//            // Effectuer la mise à jour du prix du produit
//            Double newPrice = product.getProductPrice() * 0.8; // 20% de réduction
//            product.setProductPrice(newPrice);
//        }
//    }
//
//    productRepository.saveAll(products);
//}
  //  @Scheduled(fixedRate = 120000) // Vérifie toutes les 2 minutes
  @Scheduled(cron = "0 0 0 */2 * *")
    public void checkOutOfStockProducts() {
        List<Product> outOfStockProducts = productRepository.findByProductStockEquals(0);
        for (Product product : outOfStockProducts) {
            String message = "Le produit " + product.getProductName() + " est en rupture de stock.";
            notificationService.sendNotificationToAdmin(message);
        }
    }

    public List<Product> getProducctOutOfStock() {
        return productRepository.findByProductStockEquals(0);
    }


    EmailNotificationService emailNotificationService; // Injectez le service de notification par courriel

    public void checkOutOfStockProducts1() {
        List<Product> outOfStockProducts = productRepository.findByProductStockEquals(0);
        for (Product product : outOfStockProducts) {
            try {
                List<User> adminUsers = userService.findAdminUsers();
                for (User adminUser : adminUsers) {
                    emailNotificationService.sendStockNotification(adminUser.getEmail(), product.getProductName());
                }
            } catch (MessagingException e) {
                e.printStackTrace(); // Gérer l'erreur d'envoi de courriel
            }
        }
    }
    @Override
    public List<Product> findProductsInPriceRange(float minPrice, float maxPrice) {
        return productRepository.findByProductPriceBetween(minPrice, maxPrice);
    }

}


