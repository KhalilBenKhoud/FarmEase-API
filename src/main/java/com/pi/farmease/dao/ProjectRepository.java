package com.pi.farmease.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.pi.farmease.entities.Project;
import com.pi.farmease.entities.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface ProjectRepository extends JpaRepository<Project, Long> {


    @Query("SELECT p FROM Project p WHERE p.creator.id = :userId")
    Page<Project> findByCreatorId(Long userId, Pageable pageable);

    List<Project> findByCreator(User creator);

    boolean existsById(Long id);
}
