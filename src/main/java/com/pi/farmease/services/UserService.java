package com.pi.farmease.services;

import com.pi.farmease.dto.requests.UpdateUserRequest;
import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;



public interface UserService {

    public User getCurrentUser(Principal connectedUser) ;

    public void updateCurrentUser(Principal connectedUser, UpdateUserRequest updatedUser) ;

    public void deleteCurrentUser(Principal connectedUser) ;

    public String currentUploadDirectory( Principal connectedUser) ;
    public void addProfileImage( MultipartFile imageFile , Principal connectedUser) throws IOException;

    public byte[] getProfileImage( Principal connectedUser) throws IOException;

    public void updateProfileImage( MultipartFile imageFile , Principal connectedUser) throws IOException;

}
