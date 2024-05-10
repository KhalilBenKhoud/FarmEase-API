package com.pi.farmease.services;

import com.pi.farmease.dao.LikePostRepository;
import com.pi.farmease.dao.PostRepository;
import com.pi.farmease.entities.Like_post;
import com.pi.farmease.entities.Post;
import com.pi.farmease.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@AllArgsConstructor
public class LikePostServiceImpl implements Like_postService {
    private final LikePostRepository likePostRepository;
    private final UserService userService;
    private final PostRepository postRepository;


    @Override

    public void addLike(Long postId, Principal principal) {
  User user = userService.getCurrentUser(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        // Vérifier si l'utilisateur a déjà aimé ce post
        Like_post existingLikePost = likePostRepository.findByPostIdAndUserId(post.getId_Post(), user.getId());
        if (existingLikePost != null) {
            // Supprimer le like existant
            likePostRepository.delete(existingLikePost);

            // Mettre à jour le nombre de likes sur le post
            post.setNbr_like_post(post.getNbr_like_post() - 1);
        } else {
            // Créer un nouveau like
            Like_post likePost = Like_post.builder()
                    .post_id_like(post.getId_Post())
                    .user_id_like(user.getId())
                    .build();
            likePostRepository.save(likePost);

            // Mettre à jour le nombre de likes sur le post
            post.setNbr_like_post(post.getNbr_like_post() + 1);
        }

        // Enregistrer les modifications apportées au post
        postRepository.save(post);
    }
}


