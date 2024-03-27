package com.pi.farmease.services;

import com.pi.farmease.entities.Comment;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getComment();

    public Optional<Comment> getCommentById(long id_comment);
   // public boolean existByIdcomment(Long id_comment);


    public void addComment(Comment requestBody, Long postId, Principal connected);
    //Comment updateComment(Comment comment);

    //void DeleteCommentaire(Long id_commentaire);
}
