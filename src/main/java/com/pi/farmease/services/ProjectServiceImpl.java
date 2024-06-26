package com.pi.farmease.services;

import com.pi.farmease.dao.PerformanceRepository;
import com.pi.farmease.entities.Performance;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.pi.farmease.entities.Project;
import com.pi.farmease.entities.User;
import com.pi.farmease.dao.ProjectRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService ;
    private final PerformanceRepository performanceRepository;


    @Override
    public Project createProject(Project requestBody, Principal connected) throws IOException {
        User connectedUser = userService.getCurrentUser(connected);

        // Upload project image
//        String uniqueImageFileName = UUID.randomUUID().toString() + "_" + imageUrl.getOriginalFilename();
//        String imageUploadDirectory = "src\\main\\resources\\image";
//        Path imageUploadPath = Path.of(imageUploadDirectory);
//        if (!Files.exists(imageUploadPath)) {
//            Files.createDirectories(imageUploadPath);
//        }
//        Path imageFilePath = imageUploadPath.resolve(uniqueImageFileName);
//        Files.copy(imageUrl.getInputStream(), imageFilePath, StandardCopyOption.REPLACE_EXISTING);

//        // Upload CSV file
//        String uniqueCsvFileName = UUID.randomUUID().toString() + "_" + csvFile.getOriginalFilename();
//        String csvUploadDirectory = "src\\main\\resources\\csv";
//        Path csvUploadPath = Path.of(csvUploadDirectory);
//        if (!Files.exists(csvUploadPath)) {
//            Files.createDirectories(csvUploadPath);
//        }
//        Path csvFilePath = csvUploadPath.resolve(uniqueCsvFileName);
//        Files.copy(csvFile.getInputStream(), csvFilePath, StandardCopyOption.REPLACE_EXISTING);
        Project project = null ;
        try {
            // Create project entity
             project = Project.builder()
                    .creator(connectedUser)
                    .createdAt(new Date())
                    .title(requestBody.getTitle())
                    .description(requestBody.getDescription())
                    .deadline(requestBody.getDeadline())
                    .netIncomeLastYear(requestBody.getNetIncomeLastYear())
                    .equityOffered(requestBody.getEquityOffered())
                    .goalAmount(requestBody.getGoalAmount())
                    .projectCategory(requestBody.getProjectCategory())
                    .build();

            // Save project entity

        }catch (Exception e) {
            e.printStackTrace();
        }

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
