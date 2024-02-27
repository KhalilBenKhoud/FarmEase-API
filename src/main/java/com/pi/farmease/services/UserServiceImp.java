package com.pi.farmease.services;

import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
@AllArgsConstructor
public class UserServiceImp implements UserService{

    @Override
    public User getCurrentUser(Principal connectedUser) {
        return (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    }
}
