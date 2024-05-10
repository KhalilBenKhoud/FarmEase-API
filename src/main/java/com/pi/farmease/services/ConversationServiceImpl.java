package com.pi.farmease.services;

import com.pi.farmease.dao.ConversationRepository;
import com.pi.farmease.dao.MessageRepository;
import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service

public class ConversationServiceImpl implements ConversationService {

    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;










    @Override
    public Optional<Conversation> findById(int id) {
        return conversationRepository.findById(id);
    }

    @Override
    public List<Conversation> getall() {
        return conversationRepository.findAll();
    }

    @Override
    public List<Conversation> getallbyuserid(int userid) {
        User user = userRepository.findById(userid).get();
        List<Conversation> conversations = conversationRepository.findAll();
        List<Conversation> convertationbyuser = conversations.stream()
                .filter(c -> c.getUser1().getId() == (userid) || c.getUser2().getId() == (userid))
                .collect(Collectors.toList());

        return convertationbyuser;
    }

    @Override
    public Conversation getConv(int id) {
        return conversationRepository.findById(id).get();
    }





    @Override
    public void ajoutconvertation(ConversationMessageRequest n) {
        // Recherche de l'utilisateur envoyant le message
        User sender = userRepository.findById(n.getSenderid()).orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        // Recherche de l'utilisateur recevant le message
        User receiver = userRepository.findById(n.getReceiverid()).orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // Vérifie si une conversation existe déjà entre les deux utilisateurs
        Optional<Conversation> existingConversation = conversationRepository.findByUser1AndUser2(sender, receiver);

        if (existingConversation.isPresent()) {
            // Si une conversation existe déjà, utilisez-la
            return;
        }

        // Sinon, créez une nouvelle conversation
        Conversation conversation = new Conversation();
        conversation.setUser1(sender);
        conversation.setUser2(receiver);
        conversationRepository.save(conversation);
    }




    @Override
    public void ajoutmessage(MessageRequest n) {
        Optional<Conversation> conversationOptional = conversationRepository.findById(n.getIdconversation());
        if (conversationOptional.isPresent()) {
            Conversation conversation = conversationOptional.get();
            User sender = userRepository.findById(n.getSenderid()).orElseThrow(() -> new IllegalArgumentException("Sender not found"));
            User receiver = userRepository.findById(n.getReceiverid()).orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
            Date date = new Date();

            // Création du message
            Message message = new Message();
            message.setDate(date);

            message.setConversation(conversation);

            message.setMessage(n.getMessage());
            message.setSender(sender);
            message.setReceiver(receiver);
            messageRepository.save(message);

            // Ajout du message à la liste des messages de la conversation
            List<Message> messages = conversation.getMessages();
            messages.add(message);
            conversation.setMessages(messages);
            conversationRepository.save(conversation);

            // Création de la notification associée au message

        } else {
            throw new IllegalArgumentException("Conversation not found");
        }
    }

    @Override
    public List<Conversation> findByIdUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Récupérer toutes les conversations où l'utilisateur est impliqué
        List<Conversation> userConversations = conversationRepository.findByUser1OrUser2(user);

        return userConversations;
    }

    @Override
    public void deleteAll() {
        conversationRepository.deleteAll();
    }




}
