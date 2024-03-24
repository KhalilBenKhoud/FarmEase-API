package com.pi.farmease.dao;

import com.pi.farmease.entities.ResetPasswordToken;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.VerifyAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyAccountTokenRepository extends JpaRepository<VerifyAccountToken,Integer> {
    void removeAllByUser(User user) ;
    Optional<VerifyAccountToken> findByToken(String token) ;
}
