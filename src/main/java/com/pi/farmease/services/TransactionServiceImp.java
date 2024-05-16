package com.pi.farmease.services;


import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;



import com.pi.farmease.dao.TransactionRepository;
import com.pi.farmease.entities.Transaction;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.Wallet;
import com.pi.farmease.entities.enumerations.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImp implements  TransactionService {


    private final UserService userService;
    private final TransactionRepository transactionRepository ;

    @Override
    public void receiveFund(User concernedUser, double amount) {
        if (concernedUser == null) throw new RuntimeException("the user you're trying to fund in not found");
        concernedUser.getWallet().setBalance(concernedUser.getWallet().getBalance() + amount);
    }

    public void transferMoneyFromUsertoUser(User sender, User recipient, double amount) {
        if (sender == null) throw new RuntimeException("the sender is not found");
        if (recipient == null) throw new RuntimeException("the recipient is not found");
        double senderBalence = sender.getWallet().getBalance();
        if (senderBalence < amount) throw new RuntimeException("sender does not have enough funds");
        sender.getWallet().setBalance(senderBalence - amount);
        recipient.getWallet().setBalance(recipient.getWallet().getBalance() + amount);
    }

    @Override
    public void sendMoney(User concernedUser, double amount) {

        if (concernedUser == null) {
            throw new IllegalArgumentException("The user you're trying to fund is not found.");
        }

        Wallet userWallet = concernedUser.getWallet();
        if (userWallet.getBalance() >= amount) {
            userWallet.setBalance(userWallet.getBalance() - amount);
        } else {
            throw new IllegalArgumentException("Insufficient balance to complete the transaction.");
        }

    }

    @Override
    public Transaction add(Transaction transaction) {

       return transactionRepository.save(transaction);
    }
    @Override
    public void saveSaleTransaction(User user, double amount) {
        Transaction saleTransaction = Transaction.builder()
                .description("Achat de produits")
                .issuedAt(new Date())
                .type(TransactionType.SALE)
                .recipient(user)
                .amount(amount)
                .build();
        add(saleTransaction);
    }
    public List<Transaction> getAllSaleTransactions() {
        return transactionRepository.findByType(TransactionType.SALE);
    }



}

