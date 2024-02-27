package com.pi.farmease.controllers;

import com.pi.farmease.entities.Post;
import com.pi.farmease.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService ;

    @PostMapping("/add")
    public ResponseEntity<?> addPost(Principal connected, @RequestBody Post post) {
        Post responseBody ;
        try {
            postService.addPost(post,connected);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
        return ResponseEntity.ok().body("post added !") ;
    }
}
