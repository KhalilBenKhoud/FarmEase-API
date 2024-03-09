package com.pi.farmease.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.pi.farmease.entities.Performance;
import com.pi.farmease.entities.Project;

import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance,Long> {
    @Query("SELECT p FROM Performance p WHERE p.project.id = ?1 AND p.year = ?2 ORDER BY p.year DESC")
    Optional<Performance> findFirstByProjectOrderByYearDesc(Long projectId,int year);
    Optional<Performance> findByProject(Project project);
}
