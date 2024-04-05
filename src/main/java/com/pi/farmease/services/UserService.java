package com.pi.farmease.services;




import com.pi.farmease.dto.requests.UpdateUserRequest;
import com.pi.farmease.entities.User;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

import java.util.List;


public interface UserService {
    public List<User> findAdminUsers();

    User add(User user);
    User edit(User user);

    User getById(Long id);

    public User getCurrentUser(Principal connectedUser) ;

    public void updateCurrentUser(Principal connectedUser, UpdateUserRequest updatedUser) ;

    public void deleteCurrentUser(Principal connectedUser) ;

    public String currentUploadDirectory( Principal connectedUser) ;

    public byte[] getProfileImage( Principal connectedUser) throws IOException;

    public void updateProfileImage(MultipartFile imageFile, Principal connectedUser) throws IOException;
    public void addProfileImage(MultipartFile imageFile, Principal connectedUser) throws IOException ;


}
