package com.pi.farmease.services;

import com.pi.farmease.entities.Comment;
import com.pi.farmease.entities.Conversation;
import com.pi.farmease.entities.ConversationMessageRequest;
import com.pi.farmease.entities.MessageRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ConversationService {
    Optional<Conversation> findById(int id);
    List<Conversation> getall();

    List<Conversation> getallbyuserid(int userid);




    void ajoutconvertation(ConversationMessageRequest n);
    void ajoutmessage(MessageRequest n);





    List<Conversation> findByIdUser(int id);

    void deleteAll();

    public Conversation getConv(int id);


}
