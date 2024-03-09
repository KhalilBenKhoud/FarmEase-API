package com.pi.farmease.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.pi.farmease.entities.Project;
import com.pi.farmease.entities.User;
import com.pi.farmease.dao.ProjectRepository;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService ;


    @Override
    public Project createProject(Project requestBody, Principal connected) {
        User connectedUser = userService.getCurrentUser(connected) ;
        Project project = Project.builder().creator(connectedUser).createdAt(new Date())
                .title(requestBody.getTitle())
                .description(requestBody.getDescription())
                .deadline(requestBody.getDeadline())
                .equityOffered(requestBody.getEquityOffered())
                .goalAmount(requestBody.getGoalAmount())
                .projectCategory(requestBody.getProjectCategory())
                .imageUrl(requestBody.getImageUrl())
                .build();


        projectRepository.save(project) ;
        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> getProjectsByCreator(User creator) {
        return projectRepository.findByCreator(creator);
    }

    @Override
    public Page<Project> findAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Page<Project> findProjectsByCreator(Long userId, Pageable pageable) {
        return projectRepository.findByCreatorId(userId, pageable);
    }

    @Override
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return projectRepository.existsById(id);
    }
    @Override
    public String findProjectOwnerEmail(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.map(value -> value.getCreator().getEmail()).orElse(null);
    }


}
