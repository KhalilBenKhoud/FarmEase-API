package com.pi.farmease.services;

import com.pi.farmease.entities.User;

import java.security.Principal;
import java.util.Date;

public interface TransactionService {

    public void receiveFund(User concernedUser, double amount) ;
    void transferMoneyFromUser(User sender, double amount, Date date_payment) ;
    public void transferMoneyFromUsertoUser(User sender, User recipient, double amount) ;

}
