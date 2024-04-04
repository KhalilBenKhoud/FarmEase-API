package com.pi.farmease.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.pi.farmease.entities.Performance;
import com.pi.farmease.entities.Project;
import com.pi.farmease.dao.PerformanceRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {

    private PerformanceRepository performanceRepository;

    @Override
    public Optional<Performance> getPerformanceByProject(Long projectId) {
        Project project = new Project();
        project.setId(projectId);
        return performanceRepository.findByProject(project);
    }

    @Override
    public Performance updatePerformance(Performance performance) {
        return performanceRepository.save(performance);
    }
    @Override
    public void updatePerformanceForYear(Project project, int year, double additionalMetrics) {
        Optional<Performance> latestPerformance = getLatestPerformance(project);

        if (latestPerformance.isPresent()) {
            Performance performance = latestPerformance.get();
            // Update performance metrics for the specified year
            performance.setYear(year);
            // Add additional metrics here as needed
            // For example:
            // performance.setSomeOtherMetric(additionalMetrics);
            performanceRepository.save(performance);
        }
    }


    @Override
    public Optional<Performance> getLatestPerformance(Project project) {
        return performanceRepository.findFirstByProjectOrderByYearAsc(project.getId(), 1);
    }


    @Override
    public void deletePerformanceByProject(Long projectId) {
        Optional<Performance> performance = getPerformanceByProject(projectId);
        performance.ifPresent(value -> performanceRepository.delete(value));
    }
}