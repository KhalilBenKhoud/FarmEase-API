package com.pi.farmease.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.pi.farmease.entities.Project;
import com.pi.farmease.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project createProject(Project project, MultipartFile imageUrl, Principal connected ) throws IOException;

    Optional<Project> getProjectById(Long id);

    List<Project> getProjectsByCreator(User creator);

    Page<Project> findAllProjects(Pageable pageable);

    Page<Project> findProjectsByCreator(Long userId, Pageable pageable);

    Project updateProject(Project project);

    void deleteProjectById(Long id);

    boolean existsById(Long id);
    String findProjectOwnerEmail(Long projectId);
}
