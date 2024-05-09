package com.pi.farmease.dao;

import com.pi.farmease.entities.Materiel;
import com.pi.farmease.entities.Mortgage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterielRepository extends JpaRepository<Materiel, Long> {
}
