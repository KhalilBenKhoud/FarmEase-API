package com.pi.farmease.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pi.farmease.entities.Investment;
import com.pi.farmease.entities.Project;
import com.pi.farmease.entities.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByProject(Project project);

    List<Investment> findByInvestor(User investor);

    Optional<Investment> findByProjectAndInvestor(Project project, User investor);
}
