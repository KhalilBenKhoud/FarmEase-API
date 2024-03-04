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


    @GetMapping("/user")
    public ResponseEntity<?> getCurrent(Principal connectedUser) {
        final User responseBody ;
        try {
            responseBody = userService.getCurrentUser(connectedUser);
        }catch(Exception e) {
            e.printStackTrace();
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

}
