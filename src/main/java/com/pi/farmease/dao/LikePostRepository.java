package com.pi.farmease.dao;

import com.pi.farmease.entities.Like_post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<Like_post, Long> {
    @Query(value = "SELECT * FROM Like_post lp WHERE lp.post_id_like = ?1 AND lp.user_id_like = ?2", nativeQuery = true)
    Like_post findByPostIdAndUserId(long postId, int userId);
 //   @Query(value = "SELECT FROM Like_post  WHERE user_id_like = idpost AND user_id_like = iduser")
}
