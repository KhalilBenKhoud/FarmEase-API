package com.pi.farmease.services;


import com.pi.farmease.dao.LikesRepository;
import com.pi.farmease.entities.Likes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class LikeServiceImpl implements LikeService {
private LikesRepository likeRepository;
    @Override
    public Likes add(Likes likes) {
        return likeRepository.save(likes);
    }
}
