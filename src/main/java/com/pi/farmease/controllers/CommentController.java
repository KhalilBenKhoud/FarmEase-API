package com.pi.farmease.controllers;

import com.pi.farmease.entities.Comment;
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
    @PostMapping("/Comment/add")
    public ResponseEntity<String> addComment(Principal connected, @RequestBody Comment comment)
    {long postid=1;

        Comment responseBody ;
        try {
            commentservice.addComment( comment,postid,connected) ;
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
        return ResponseEntity.ok().body("Comment added !") ;
    }
    }

