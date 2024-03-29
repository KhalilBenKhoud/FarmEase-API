package com.pi.farmease.controllers;


import com.pi.farmease.entities.Product;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.ProductService;
import com.pi.farmease.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:4200")

public class ProductController {

    private ProductService iProductService;
    UserService userService;

    //admin
    @GetMapping("/allProduct")
    public List<Product> getAllProductsAdmin() {
        return iProductService.selectAll();
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
 return ResponseEntity.ok().body("produit ajouter");
    }





    @PutMapping("editProduct")
    public Product editProduct(@RequestBody Product p) {

        return iProductService.edit(p);
    }
//////////////////////
    @PostMapping("/checkOutOfStockProductsssssssssssssssss")
    public void checkOutOfStockProducts1() {
        iProductService.checkOutOfStockProducts1();
    }

    @DeleteMapping("deleteProductById/{id}")
    public List<Product> deletebyid(@PathVariable Long id) {
        iProductService.deleteById(id);
        return iProductService.selectAll();
    }


    @GetMapping("getProductById/{id}")
    public Product getProdcutById(@PathVariable Long id) {
        return iProductService.selectById(id);
    }


    @PostMapping("addLike/{userId}")
    public Product toggleLikePack(@RequestBody Product product, @PathVariable long userId) {
        User u = userService.getById(userId);
        return iProductService.toggleLike(product, u);

    }


    @GetMapping("/most-liked")
    public List<Product> getMostLikedProducts() {
        return iProductService.getMostLikedProducts();
    }



    @GetMapping("/RetrieveProductByCategorie")
    public List<Product> afficheravectypepay(@RequestParam Product.ProductCategory type) {
        return iProductService.getProductsByCategory(type);
    }

    @GetMapping("/RetrieveProductByName")
    public List<Product> afficherproductbyname(@RequestParam String name) {
        return iProductService.getProductsByName(name);
    }
    @GetMapping("/checkOutOfStock")
    public List<Product> checkOutOfStockProducts() {

            return iProductService.getProducctOutOfStock();

    }
    @GetMapping("/priceRange")
    public List<Product> getProductsInPriceRange(
            @RequestParam(name = "minPrice") float minPrice,
            @RequestParam(name = "maxPrice") float maxPrice) {
        return iProductService.findProductsInPriceRange(minPrice, maxPrice);
    }





}
