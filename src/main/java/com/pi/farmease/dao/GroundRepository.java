package com.pi.farmease.dao;

import com.pi.farmease.entities.Ground;
import com.pi.farmease.entities.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroundRepository extends JpaRepository<Ground, Long> {
}
