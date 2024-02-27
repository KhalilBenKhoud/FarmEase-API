package com.pi.farmease.services;

import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;



public interface UserService {

    public User getCurrentUser(Principal connectedUser) ;

}
