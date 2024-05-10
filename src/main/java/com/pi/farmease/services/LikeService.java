package com.pi.farmease.services;

import com.pi.farmease.entities.Likes;
import com.pi.farmease.entities.Product;

import java.util.List;
import java.util.Map;

public interface LikeService {
    Likes add(Likes likes);
    public Map<Long, Integer> calculateLikesStats();
    public List<Product> getProductsRankedByLikes();
}
