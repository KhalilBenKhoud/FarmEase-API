package com.pi.farmease.dao;


import com.pi.farmease.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByProductCategory(Product.ProductCategory productCategory);
    List<Product> findByProductName(String productName);
    List<Product> findByProductStockEquals(int productStock);
    List<Product> findByProductPriceBetween(float minPrice, float maxPrice);
    @Query("SELECT p FROM Product p ORDER BY SIZE(p.likedByUsers) DESC")
    List<Product> findMostLikedProducts();
    List<Product> findByOrderByProductPriceAsc();

    @Query("SELECT p.productId, SIZE(p.likedByUsers) FROM Product p")
    List<Object[]> countLikesByProduct();


}
