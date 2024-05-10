package com.pi.farmease.dao;

import com.pi.farmease.entities.Mortgage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MortgageRepository extends JpaRepository<Mortgage, Long> {

}
