package com.pi.farmease.services;

import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.dto.requests.UpdateUserRequest;
import com.pi.farmease.entities.User;

import com.pi.farmease.entities.enumerations.Role;


import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.List;

import java.util.UUID;

import java.security.Principal;


@Service
@AllArgsConstructor
public class UserServiceImp implements UserService{

    private final UserRepository userRepository ;
    private final AuthenticationService authenticationService ;


    @Override
    public User getCurrentUser(Principal connectedUser) {
        return (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    }

    @Override
    public void updateCurrentUser(Principal connectedUser, UpdateUserRequest updatedUser) {
        User currentUser = getCurrentUser(connectedUser) ;

        if(updatedUser.getFirstname() != null) currentUser.setFirstname(updatedUser.getFirstname())  ;
        if(updatedUser.getLastname() != null) currentUser.setLastname(updatedUser.getLastname());
        if(updatedUser.getEmail() != null) currentUser.setEmail(updatedUser.getEmail());
        if(updatedUser.getPassword() != null) currentUser.setPassword(updatedUser.getPassword());

        userRepository.save(currentUser) ;
    }

    @Override
    public void deleteCurrentUser(Principal connectedUser) {
        User currentUser = getCurrentUser(connectedUser) ;
        authenticationService.logout();
        userRepository.delete(currentUser);
    }

    @Override
    public String currentUploadDirectory( Principal connectedUser) {
        User current = getCurrentUser(connectedUser);

        return "src/main/resources/user_images/" + current.getId() + current.getFirstname();
    }

        @Override
        public void addProfileImage (MultipartFile imageFile, Principal connectedUser) throws IOException {

            User current = getCurrentUser(connectedUser);
            String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            String uploadDirectory = currentUploadDirectory(connectedUser);
            Path uploadPath = Path.of(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            current.setImageName(uniqueFileName);
            userRepository.save(current);

        }

        @Override
        public byte[] getProfileImage (Principal connectedUser) throws IOException {
            String uploadDirectory = currentUploadDirectory(connectedUser);
            String currentUserImageName = getCurrentUser(connectedUser).getImageName();
            Path imagePath = Path.of(uploadDirectory, currentUserImageName);

            if (Files.exists(imagePath)) {
                return Files.readAllBytes(imagePath);
            } else {
                return null; // Handle missing images
            }


        }

        @Override
        public void updateProfileImage (MultipartFile imageFile, Principal connectedUser) throws IOException {
            String uploadDirectory = currentUploadDirectory(connectedUser);
            String currentUserImageName = getCurrentUser(connectedUser).getImageName();
            Path imagePath = Path.of(uploadDirectory, currentUserImageName);
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);

            }
            addProfileImage(imageFile, connectedUser);
        }


        @Override
        public User getById (Long id){
            return userRepository.findById(id).get();
        }
        public List<User> findAdminUsers () {
            return userRepository.findByRole(Role.ADMIN);
        }

        @Override
        public User add (User user){
            return userRepository.save(user);
        }
        @Override
        public User edit (User user){
            return userRepository.save(user);
        }


    }


