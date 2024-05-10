package com.pi.farmease.controllers;

import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.AdminUserService;
import com.pi.farmease.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/user")
public class AdminUserController {
    private final AdminUserService adminUserService ;
    private final WalletService walletService ;

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

    @GetMapping("/sortedByDate")
    public List<User> usersSortedByRegistrationDate() {
        return adminUserService.usersSortedByRegistrationDate();
    }

    @GetMapping("/all")
    public List<User> getAll() {
        return adminUserService.getAll();
    }
    @GetMapping("/sortedByMoney")
    public List<User> getSortedByMoney() {
        return adminUserService.getSortedByMoney();
    }

    @GetMapping("/wealthDistributionIndex")
    public ResponseEntity<?> getWealthIndex() {
        double index ;
        try {
           index = walletService.CalculateWealthDistributionIndex() ;
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body(index);
    }
}
