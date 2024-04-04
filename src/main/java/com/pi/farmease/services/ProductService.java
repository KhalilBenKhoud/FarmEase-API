package com.pi.farmease.services;


import com.pi.farmease.entities.Product;
import com.pi.farmease.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ProductService {

    public void addProduct(MultipartFile file , Product p) throws IOException;
    Product edit(Product a);
    List<Product> selectAll();
    Product selectById(Long idPack);
    void deleteById(Long idPack);
    Product toggleLike(Product a, User u);

    public List<Product> getProductsByCategory(Product.ProductCategory productCategory) ;
    public List<Product> getProductsByName(String productName) ;
    public List<Product> getMostLikedProducts() ;
    public void checkOutOfStockProducts() ;
    public List<Product> getProductOutOfStock();

    public List<Product> findProductsInPriceRange(float minPrice, float maxPrice) ;




}
