package com.pi.farmease.dao;

import com.pi.farmease.entities.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance,Integer> {
}
