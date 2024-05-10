package com.pi.farmease.controllers;

import com.pi.farmease.dao.PostRepository;
import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.Post;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.CommentService;
import com.pi.farmease.services.Like_postService;
import com.pi.farmease.services.PostService;
import com.pi.farmease.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController

{
    private final PostService postService ;
    private final CommentService commentService ;
    private final UserService userService ;
    private final PostRepository postRepository ;
    private final Like_postService likePostService;
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
            return ResponseEntity.ok().body("{\"message\": \"Post added successfully\"}");

        }catch(Exception e) {
            //   return ResponseEntity.badRequest().body(e.getMessage()) ;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + e.getMessage() + "\"}");

        }
        // return ResponseEntity.ok().body("post added !") ;
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> incrementLike(@PathVariable Long postId, Principal principal) {
        try {
            // Appeler la méthode addLike avec l'ID du post et l'utilisateur connecté
            likePostService.addLike(postId, principal);

            // Récupérer le nombre total de likes après l'ajout
            Optional<Post> optionalPost = postRepository.findById(postId);
            if (optionalPost.isPresent()) {
                Post existingPost = optionalPost.get();
                return ResponseEntity.ok().body(existingPost.getNbr_like_post());
            } else {
                // Le post n'existe pas avec l'ID fourni
                return ResponseEntity.badRequest().body(new MessageResponse("Post not found with ID: " + postId));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to increment like: " + e.getMessage()));
        }
    }

    @PostMapping("/{postId}/signal")
    public ResponseEntity<?> incrementSignal(@PathVariable Long postId, Principal principal) {
        try {
            // Rechercher le post par son ID
            Optional<Post> optionalPost = postRepository.findById(postId);

            if (optionalPost.isPresent()) {
                // Le post existe, récupérer l'objet Post correspondant
                Post existingPost = optionalPost.get();

                // Assurer que l'ID du post est correctement défini
                existingPost.setId_Post(postId);

                // Appeler la méthode incrementSignal du service
                postService.incrementsignal( postId, principal);


                return ResponseEntity.ok().body(existingPost.getNbr_signal_post());
            } else {
                // Le post n'existe pas avec l'ID fourni
                return ResponseEntity.badRequest().body(new MessageResponse("Post not found with ID: " + postId));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/{postId}/incrementStat1")
    public ResponseEntity<?> incrementStat1(@PathVariable Long postId ) {
        try {
            // Récupérer le post par son ID
            Optional<Post> optionalPost = postRepository.findById(postId);

            if (optionalPost.isPresent()) {
                // Le post existe, récupérer l'objet Post correspondant
                Post existingPost = optionalPost.get();

                // Assurez-vous que l'ID du post est correctement défini
                existingPost.setId_Post(postId);

                // Appeler la méthode incrementStat1 avec le post existant
                postService.incrementStat1(existingPost);

                // Mettre à jour le post dans la base de données
                postRepository.save(existingPost);

                // Renvoyer le post mis à jour
                return ResponseEntity.ok().body(existingPost);
            } else {
                // Le post n'existe pas avec l'ID fourni
                return ResponseEntity.badRequest().body(new MessageResponse("Post not found with ID: " + postId));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to increment Stat1: " + e.getMessage()));
        }
    }

    @PostMapping("/{postId}/incrementStat2")
    public ResponseEntity<?> incrementStat2(@PathVariable Long postId ) {
        try {
            // Récupérer le post par son ID
            Optional<Post> optionalPost = postRepository.findById(postId);

            if (optionalPost.isPresent()) {
                // Le post existe, récupérer l'objet Post correspondant
                Post existingPost = optionalPost.get();

                // Assurez-vous que l'ID du post est correctement défini
                existingPost.setId_Post(postId);

                // Appeler la méthode incrementStat2 avec le post existant
                postService.incrementStat2(existingPost);

                // Mettre à jour le post dans la base de données
                postRepository.save(existingPost);

                // Renvoyer le post mis à jour
                return ResponseEntity.ok().body(existingPost);
            } else {
                // Le post n'existe pas avec l'ID fourni
                return ResponseEntity.badRequest().body(new MessageResponse("Post not found with ID: " + postId));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to increment Stat2: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable Long id) {
        System.out.println(id);
        System.out.println("1");
        commentService.deleteCommentsByPostId(id);
        postService.deletePost(id);
    }

}
