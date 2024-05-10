package com.pi.farmease.services;


import com.pi.farmease.dao.LikesRepository;
import com.pi.farmease.dao.ProductRepository;
import com.pi.farmease.entities.Likes;
import com.pi.farmease.entities.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class LikeServiceImpl implements LikeService {
private LikesRepository likeRepository;
ProductRepository productRepository ;
    @Override
    public Likes add(Likes likes) {
        return likeRepository.save(likes);
    }

    public Map<Long, Integer> calculateLikesStats() {
        List<Likes> allLikes = likeRepository.findAll();
        Map<Long, Integer> likesStats = new HashMap<>();

        System.out.println("Likes récupérés depuis la base de données : " + allLikes);

        for (Likes like : allLikes) {
            Long productId = like.getProductId();
            likesStats.put(productId, likesStats.getOrDefault(productId, 0) + 1);
        }

        System.out.println("Statistiques de likes calculées : " + likesStats);

        return likesStats;
    }
    public List<Product> getProductsRankedByLikes() {
        Map<Long, Integer> likesStats = calculateLikesStats();

        // Convertir la Map en List pour pouvoir trier
        List<Map.Entry<Long, Integer>> sortedLikes = new ArrayList<>(likesStats.entrySet());
        sortedLikes.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Récupérer les produits correspondants aux identifiants triés
        List<Long> productIds = sortedLikes.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        return productRepository.findAllById(productIds);
    }
}
