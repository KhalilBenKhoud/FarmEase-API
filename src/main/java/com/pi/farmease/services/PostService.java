package com.pi.farmease.services;

import com.pi.farmease.entities.Post;

import java.security.Principal;
import java.util.List;

public interface PostService {

    public void addPost(Post post , Principal connected) ;
    List<Post> getPost();

    public void updatePost(Post post, Long id) ;

    public void deletePost(Long id) ;


}
