package com.pi.farmease.dao;

import com.pi.farmease.entities.Garantor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository


public interface GarantorRepository extends JpaRepository<Garantor, Long> {
    @Query(value = "SELECT MAX(id_credit) FROM credit", nativeQuery = true)
    Long getLastCreditId();

    Optional<Garantor> findFirstByOrderByIdGarantorDesc();
    List<Garantor> findByQrStringIsNotNull();
}
