package com.pi.farmease.services;


import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.enumerations.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
@NoArgsConstructor

public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    public List<User> findAdminUsers() {
        return userRepository.findByRole(Role.ADMIN);
    }

    @Override
    public User add(User user) {
        return userRepository.save(user);
    }
    @Override
    public User edit(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).get();
    }


}





