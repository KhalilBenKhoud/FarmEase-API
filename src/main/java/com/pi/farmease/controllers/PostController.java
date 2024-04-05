package com.pi.farmease.controllers;

import com.pi.farmease.dao.PostRepository;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService ;
    private final UserService userService ;
    private final PostRepository postRepository ;
    @GetMapping("/get")
    public List<Post> getPost()
    {
        return postService.getPost() ;
    }
    @GetMapping("/like")
    public List<Post> getPostsSortedByLikes() {
        return postService.getPostsSortedByLikes();
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
    public ResponseEntity<?> incrementLike(@PathVariable Long postId ) {
        try {
            // Récupérer le post par son ID
            Optional<Post> optionalPost = postRepository.findById(postId);

            if (optionalPost.isPresent()) {
                // Le post existe, récupérer l'objet Post correspondant
                Post existingPost = optionalPost.get();

                // Assurez-vous que l'ID du post est correctement défini
                existingPost.setId_Post(postId);

                // Appeler la méthode incrementLike avec le post existant
                postService.incrementLike(existingPost);

                return ResponseEntity.ok().body("Like incremented successfully");
            } else {
                // Le post n'existe pas avec l'ID fourni
                return ResponseEntity.badRequest().body(new MessageResponse("Post not found with ID: " + postId));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/{postId}/signal")
    public ResponseEntity<?> incrementSignal(@PathVariable Long postId) {
        try {
            // Rechercher le post par son ID
            Optional<Post> optionalPost = postRepository.findById(postId);

            if (optionalPost.isPresent()) {
                // Le post existe, récupérer l'objet Post correspondant
                Post existingPost = optionalPost.get();

                // Assurer que l'ID du post est correctement défini
                existingPost.setId_Post(postId);

                // Appeler la méthode incrementSignal du service
                postService.incrementSignal(existingPost);

                return ResponseEntity.ok().body("Signal incremented successfully");
            } else {
                // Le post n'existe pas avec l'ID fourni
                return ResponseEntity.badRequest().body(new MessageResponse("Post not found with ID: " + postId));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }




}
