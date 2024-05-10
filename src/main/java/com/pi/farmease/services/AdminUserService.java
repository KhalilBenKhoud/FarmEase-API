package com.pi.farmease.services;

import com.pi.farmease.entities.User;

import java.util.List;

public interface AdminUserService {

    public void banUser(Integer id) ;

    public User userWithMostMoney() ;
    public void permitUser(Integer id) ;

    public List<User> usersSortedByRegistrationDate() ;

    public List<User> getAll() ;

    public List<User> getSortedByMoney() ;
}
