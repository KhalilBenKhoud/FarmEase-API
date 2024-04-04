package com.pi.farmease.controllers;

import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/user")
public class AdminUserController {
    private final AdminUserService adminUserService ;

    @PostMapping("/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable Integer id) {
        try {
            adminUserService.banUser(id);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body(new MessageResponse("user banned successfully"));
    }
    @PostMapping("/permit/{id}")
    public ResponseEntity<?> permitUser(@PathVariable Integer id) {
        try {
            adminUserService.permitUser(id);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body(new MessageResponse("user enabled successfully"));
    }

    @GetMapping("/withMostMoney")
    public ResponseEntity<?> userWithMostMoney() {
        User user ;
        try {
            user = adminUserService.userWithMostMoney();
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body(user);
    }
}
