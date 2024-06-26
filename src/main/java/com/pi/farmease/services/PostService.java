package com.pi.farmease.services;

import com.pi.farmease.entities.Post;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface PostService {

    public void addPost(Post post , Principal connected) ;

    List<Post> getPost();
    void incrementStat2(Post post);
    void incrementStat1(Post post);
     void updatePost(Post post, Long id) ;
    void incrementsignal(Long postId, Principal principal);
    void banUser(Integer id);
 void deletePost(Long id) ;
    List<Post> getPostsSortedByLikes();

    public boolean containsBadWords(Post post);
    public void calculateStatPercentage(Post post);
    void incrementLike(Post post);



}
