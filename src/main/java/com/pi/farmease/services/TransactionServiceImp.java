package com.pi.farmease.services;

import com.pi.farmease.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImp implements  TransactionService {
  private final UserService userService ;
    @Override
    public void receiveFund(User concernedUser, double amount) {
        if(concernedUser == null) throw new RuntimeException("the user you're trying to fund in not found") ;
        concernedUser.getWallet().setBalance(concernedUser.getWallet().getBalance() + amount);
    }
    public void transferMoneyFromUsertoUser(User sender, User recipient, double amount) {
        if(sender == null) throw new RuntimeException("the sender is not found") ;
        if(recipient == null) throw new RuntimeException("the recipient is not found") ;
        double senderBalence = sender.getWallet().getBalance();
       if(senderBalence < amount) throw new RuntimeException("sender does not have enough funds") ;
       sender.getWallet().setBalance(senderBalence - amount);
       recipient.getWallet().setBalance(recipient.getWallet().getBalance() + amount);
    }


}


