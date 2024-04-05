package com.pi.farmease.dao;


import com.pi.farmease.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByType(Transaction.TransactionType type);
}
