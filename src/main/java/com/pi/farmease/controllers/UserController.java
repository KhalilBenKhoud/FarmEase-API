package com.pi.farmease.controllers;


import com.pi.farmease.dto.requests.UpdateUserRequest;
import com.pi.farmease.dto.responses.ImageResponse;
import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.UserService;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService ;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrent(Principal connectedUser) {
        final User responseBody ;
        try {
            responseBody = userService.getCurrentUser(connectedUser);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body(responseBody) ;
    }

    @PutMapping("/current")
    public ResponseEntity<?> updateCurrent(Principal connectedUser, @RequestBody UpdateUserRequest updatedUser) {
        try {
            userService.updateCurrentUser(connectedUser,updatedUser);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body("User updated successfully !") ;
    }

    @DeleteMapping("/current")
    public ResponseEntity<?> deleteCurrent(Principal connectedUser) {
        try {
            userService.deleteCurrentUser(connectedUser);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body("User deleted successfully !") ;
    }

    @PostMapping("/current/image")
    public ResponseEntity<?> addProfileImage(@RequestBody  MultipartFile imageFile, Principal connectedUser)   {
        try {
            userService.addProfileImage(imageFile,connectedUser);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body("profile image added successfully !") ;
    }

    @GetMapping("/current/image")
    public ResponseEntity<?> getProfileImage( Principal connectedUser) {
        byte[] responseBody ;
        try {
           responseBody = userService.getProfileImage(connectedUser) ;
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }


        return ResponseEntity.ok().body(new ImageResponse(responseBody)) ;
    }

    @PutMapping("/current/image")
    public ResponseEntity<?> updateProfileImage(@RequestBody  MultipartFile imageFile, Principal connectedUser)   {
        try {
            userService.updateProfileImage(imageFile,connectedUser);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body("profile image updated successfully !") ;
    }


}
