package com.pi.farmease.dao;


import com.pi.farmease.entities.Transaction;
import com.pi.farmease.entities.enumerations.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByType(TransactionType type);
}
