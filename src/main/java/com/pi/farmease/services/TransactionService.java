package com.pi.farmease.services;

import com.pi.farmease.entities.User;

public interface TransactionService {

    public void receiveFund(User concernedUser, double amount) ;

    public void transferMoneyFromUsertoUser(User sender, User recipient, double amount) ;



}
