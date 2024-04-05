package com.pi.farmease.dao;


import com.pi.farmease.entities.User;
import com.pi.farmease.entities.enumerations.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String username) ;
    List<User> findByRole(Role role);
}
