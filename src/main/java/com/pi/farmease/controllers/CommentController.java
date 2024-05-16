package com.pi.farmease.controllers;

import com.pi.farmease.entities.Comment;
import com.pi.farmease.entities.MessageResponse;
import com.pi.farmease.services.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RestController
@AllArgsConstructor

@RequestMapping("/api/v1")
public class CommentController {
    private CommentService commentservice;
    @GetMapping("/Comment")
    public List<Comment> getComment()
    {
        return commentservice.getComment();
    }
    @GetMapping("/Comment/{id_comment}")
    public Comment getComment(@PathVariable Long id_comment )
    {
        return commentservice.getCommentById(id_comment).orElseThrow(
                ()->new EntityNotFoundException("Requested Comment not found")
        );
    }
    @PostMapping("/Comment/add/{id_post}")
    public ResponseEntity<?> addComment(Principal connected, @RequestBody Comment comment, @PathVariable Long id_post)
    {

        Comment responseBody ;
        try {
            commentservice.addComment( comment,id_post,connected) ;
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
        return ResponseEntity.ok().body(new MessageResponse("Comment added !")) ;
    }
    @GetMapping("/Comment/post/{postId}")
    public List<Comment> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentservice.getCommentsByPostId(postId);
        return comments;
    }
    }

