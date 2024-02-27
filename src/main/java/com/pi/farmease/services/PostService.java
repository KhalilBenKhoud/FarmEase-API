package com.pi.farmease.services;

import com.pi.farmease.entities.Post;

import java.security.Principal;

public interface PostService {

    public void addPost(Post post , Principal connected) ;

    public void updatePost(Post post, Long id) ;

    public void deletePost(Long id) ;



}
