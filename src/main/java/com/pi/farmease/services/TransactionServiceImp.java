package com.pi.farmease.services;

import com.pi.farmease.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;

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
    public void transferMoneyFromUser(User sender, double amount, Date date_payment) {

        if(sender == null) throw new RuntimeException("the sender is not found") ;
        if (!date_payment.equals(LocalDate.now())) {
            throw new RuntimeException("Transfer can only be executed on the current date for security reasons.");
        }

        double senderBalence = sender.getWallet().getBalance();
        if(senderBalence < amount) throw new RuntimeException("sender does not have enough funds") ;
        sender.getWallet().setBalance(senderBalence - amount);

    }

}
