package com.pi.farmease.controllers;

import com.pi.farmease.entities.Conversation;
import com.pi.farmease.entities.ConversationMessageRequest;
import com.pi.farmease.entities.MessageRequest;
import com.pi.farmease.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable int id) {
        Optional<Conversation> conversation = conversationService.findById(id);
        return conversation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<Conversation> getAllConversations() {
        return conversationService.getall();
    }

    @GetMapping("/user/{userid}")
    public List<Conversation> getConversationsByUserId(@PathVariable int userid) {
        return conversationService.getallbyuserid(userid);
    }

    @PostMapping("/add-conversation")
    public void addConversation(@RequestBody ConversationMessageRequest conversationMessageRequest) {
        conversationService.ajoutconvertation(conversationMessageRequest);
    }

    @PostMapping("/add-message")
    public void addMessage(@RequestBody MessageRequest messageRequest) {
        conversationService.ajoutmessage(messageRequest);
    }

    @DeleteMapping("/delete-all")
    public void deleteAllConversations() {
        conversationService.deleteAll();
    }
}
