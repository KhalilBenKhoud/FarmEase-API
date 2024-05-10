package com.pi.farmease.dao;

import com.pi.farmease.entities.Signal_post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SignalPostRepository extends JpaRepository<Signal_post, Long> {
    @Query(value = "SELECT * FROM Signal_post lp WHERE lp.post_id_signal = ?1 AND lp.user_id_signal = ?2", nativeQuery = true)
    Signal_post findByPostIdAndUserId(long postId, int userId);
}