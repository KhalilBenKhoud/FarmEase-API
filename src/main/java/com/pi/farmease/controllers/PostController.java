package com.pi.farmease.controllers;

import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.Post;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.PostService;
import com.pi.farmease.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService ;
    private final UserService userService ;
    @GetMapping("/get")
    public List<Post> getPost()
    {
        return postService.getPost() ;
    }
    @GetMapping("/like")
    public List<Post> getPostsSortedByLikes() {
        return postService.getPostsSortedByLikes();
    }
    @GetMapping("/test")
    public String test()
    {
        return "test from post" ;
    }


    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(Principal connectedUser) {
         User responseBody ;
        try {
            responseBody = userService.getCurrentUser(connectedUser);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;

        }
        return ResponseEntity.ok().body(responseBody) ;

    }
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
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> incrementLike(@PathVariable Long postId, @RequestBody Post post) {
        try {
            post.setId_Post(postId); // Assurez-vous que l'ID du post est correctement défini
            postService.incrementLike(post);
            return ResponseEntity.ok().body("Like incremented successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }



}
