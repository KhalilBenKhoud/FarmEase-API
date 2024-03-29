package com.pi.farmease.services;


import com.pi.farmease.entities.User;

import java.util.List;


public interface UserService {
    public List<User> findAdminUsers();

    User add(User user);
    User edit(User user);

    User getById(Long userId);
}
