package com.pi.farmease.dao;

import com.pi.farmease.entities.ResetPasswordToken;
import com.pi.farmease.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken,Integer> {

    void removeAllByUser(User user) ;
    Optional<ResetPasswordToken> findByToken(String token) ;

}
