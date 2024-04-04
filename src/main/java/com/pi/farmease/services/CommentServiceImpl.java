package com.pi.farmease.services;

import com.pi.farmease.dao.CommentRepository;
import com.pi.farmease.dao.PostRepository;
import com.pi.farmease.entities.Comment;
import com.pi.farmease.entities.Post;
import com.pi.farmease.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentrepository;
    private final UserService userService;

    private final PostRepository postRepository;

    @Override
    public List<Comment> getComment() {
        return commentrepository.findAll();
    }

    @Override
    public Optional<Comment> getCommentById(long id_comment) {
        return commentrepository.findById(id_comment);
    }

    @Override
    public void addComment(Comment requestBody, Long postId, Principal connected) {
        // Obtenez l'utilisateur connecté à partir de Principal
        User connectedUser = userService.getCurrentUser(connected);

        // Assurez-vous que l'ID de l'utilisateur est correctement défini dans le commentaire
        requestBody.setUser(connectedUser);

        // Obtenez le post auquel le commentaire est associé
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post associatedPost = postOptional.get();
            requestBody.setPost(associatedPost);

            // Enregistrez ensuite le commentaire
            commentrepository.save(requestBody);
        } else {
            throw new EntityNotFoundException("Post not found with ID: " + postId);
        }

}
   /* @Override
    public boolean existByIdcomment(Long id_comment) {
        return commentrepository.existsById(id_comment);
    }*/



/*
    @Override
    public Comment updateComment(Comment comment) {
        return commentrepository.save(comment);
    }

    @Override
    public void DeleteCommentaire(Long id_comment) {
        commentrepository.deleteById(id_comment);
    }*/
}
