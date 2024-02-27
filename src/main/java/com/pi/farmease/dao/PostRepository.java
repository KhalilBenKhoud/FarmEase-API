package com.pi.farmease.dao;

import com.pi.farmease.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select * from Forum order by nbr_like desc",nativeQuery = true)
    public List<Post> getAllBynbr_like() ;
}

