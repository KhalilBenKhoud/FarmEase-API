package com.pi.farmease.services;

import java.security.Principal;

public interface Like_postService {
    void addLike(Long postId, Principal principal);

}
