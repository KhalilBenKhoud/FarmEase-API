package com.pi.farmease.dao;

import com.pi.farmease.entities.Garantor ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GarantorRepository extends JpaRepository<Garantor, Long> {

}
