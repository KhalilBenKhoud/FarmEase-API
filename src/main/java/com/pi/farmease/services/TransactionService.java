package com.pi.farmease.services;

import com.pi.farmease.entities.User;

import java.security.Principal;
import java.util.Date;
import com.pi.farmease.entities.Transaction;
import com.pi.farmease.entities.User;

import java.util.List;







public interface TransactionService {
    public void receiveFund(User concernedUser, double amount) ;

    public void transferMoneyFromUsertoUser(User sender, User recipient, double amount) ;

    public void sendMoney(User concernedUser, double amount) ;
    Transaction add(Transaction transaction);
    public void saveSaleTransaction(User user, double amount) ;
    public List<Transaction> getAllSaleTransactions();



  
}
