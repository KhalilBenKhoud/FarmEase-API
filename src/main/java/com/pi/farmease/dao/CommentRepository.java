package com.pi.farmease.dao;

import com.pi.farmease.entities.Comment;
import com.pi.farmease.entities.Conversation;
import com.pi.farmease.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
   @Query("SELECT c FROM Comment c WHERE c.post.id_Post = :postId")
    List<Comment> findByPostId(Long postId);
    //Comment findCommentByPost_Id_Post(Long postId);
}
