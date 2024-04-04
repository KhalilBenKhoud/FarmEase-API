package com.pi.farmease.dao;

import com.pi.farmease.entities.ResetPasswordToken;
import com.pi.farmease.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransationRepository extends JpaRepository<Transaction,Integer> {
}
