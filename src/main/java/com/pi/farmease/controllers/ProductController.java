package com.pi.farmease.controllers;


import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.Notification;
import com.pi.farmease.entities.Product;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.LikeService;
import com.pi.farmease.services.NotificationSer;
import com.pi.farmease.services.ProductService;
import com.pi.farmease.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/product")
@CrossOrigin(origins = "http://localhost:4200")

public class ProductController {

    private ProductService iProductService;
    UserService userService;
    NotificationSer notificationSer ;
LikeService likeService ;
    //admin
    @GetMapping("/allProduct")
    public List<Product> getAllProductsAdmin() {
        return iProductService.selectAll();
    }
    @GetMapping("/allnotification")
    public List<Notification> getAllNotification() {
        return notificationSer.selectAll();
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestParam String productName,
                                        @RequestParam String productDescription,
                                        @RequestParam Double productPrice,
                                        @RequestParam Integer productStock,
                                        @RequestParam Product.ProductCategory productCategory,

                                        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Product product = new Product();
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductPrice(productPrice);
        product.setProductStock(productStock);
        product.setProductCategory(productCategory);

 try{
     iProductService.addProduct(imageFile,product);
    }
 catch (Exception e){
     return ResponseEntity.badRequest().body(e.getMessage());
 }
 return ResponseEntity.ok().body(new MessageResponse("prduit ajouté"));
    }





    @PutMapping("/edit/{productId}")
    public ResponseEntity<Product> editProduct(@PathVariable Long productId, @RequestBody Product updatedProduct){

        Product editedProduct = iProductService.edit(productId, updatedProduct);
        // Retourner une réponse avec le produit modifié et le statut OK
        return ResponseEntity.ok(editedProduct);
    }
//////////////////////



    @DeleteMapping("deleteProductById/{productId}")
    public List<Product> deletebyid(@PathVariable Long productId) {
        iProductService.deleteById(productId);
        return iProductService.selectAll();
    }


    @GetMapping("getProductById/{id}")
    public Product getProdcutById(@PathVariable Long id) {
        return iProductService.selectById(id);
    }


    @PostMapping("addLike/{productId}")
    public Product toggleLikePack(@PathVariable Long productId, Principal connected) {

        return iProductService.toggleLike(productId, connected );

    }


    @GetMapping("/most-liked")
    public List<Product> getMostLikedProducts() {
        return iProductService.getMostLikedProducts();
    }



    @GetMapping("/products/{category}")
    public List<Product> getProductsByCategory(@PathVariable Product.ProductCategory category) {
        return iProductService.getProductsByCategory(category);
    }

    @GetMapping("{name}")
    public List<Product> getProductsByName(@PathVariable String name) {
        return iProductService.getProductsByName(name);
    }
    @GetMapping("/checkOutOfStock")
    public List<Product> checkOutOfStockProducts() {

            return iProductService.getProductOutOfStock();

    }
    @GetMapping("/products/price/{minPrice}/{maxPrice}")
    public List<Product> getProductsInPriceRange(@PathVariable float minPrice, @PathVariable float maxPrice) {
        return iProductService.findProductsInPriceRange(minPrice, maxPrice);
    }
    @GetMapping("/produits/trier/prix")
    public List<Product> trierProduitsParPrix() {
        return iProductService.trierProduitsParPrix();
    }


    @GetMapping("/productLikes")
    public ResponseEntity<Map<Long, Integer>> getProductLikes() {
        Map<Long, Integer> likesByProduct = iProductService.countLikesByProduct();
        return ResponseEntity.ok(likesByProduct);
    }
    @GetMapping("/products/likes")
    public List<Product> getProductsRankedByLikes() {
        return likeService.getProductsRankedByLikes();
    }
}
