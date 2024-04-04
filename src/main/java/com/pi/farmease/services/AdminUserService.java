package com.pi.farmease.services;

import com.pi.farmease.entities.User;

public interface AdminUserService {

    public void banUser(Integer id) ;

    public User userWithMostMoney() ;
    public void permitUser(Integer id) ;
}
