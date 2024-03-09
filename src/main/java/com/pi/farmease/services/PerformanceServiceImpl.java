package com.pi.farmease.services;

import lombok.AllArgsConstructor;
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
    public void deletePerformanceByProject(Long projectId) {
        Optional<Performance> performance = getPerformanceByProject(projectId);
        performance.ifPresent(value -> performanceRepository.delete(value));
    }
    @Override
    public Map<String, Double> calculateBasicPerformance(Project project) {
        Map<String, Double> performanceMetrics = new HashMap<>();

        // Retrieve the latest performance record for the project
        Optional<Performance> latestPerformance = getLatestPerformance(project);

        if (latestPerformance.isPresent()) {
            Performance performance = latestPerformance.get();
            double roi = calculateROI(performance);
            performanceMetrics.put("ROI", roi);
            performanceMetrics.put("Net Income", performance.getNetIncome());
            // Add other relevant metrics as needed
        }

        return performanceMetrics;
    }
    @Override
    public Optional<Performance> getLatestPerformance(Project project) {
        //TODO: filter performances by date in the repository impl
        return performanceRepository.findFirstByProjectOrderByYearDesc(project.getId(), 1);
    }
    private double calculateROI(Performance performance) {
        double totalInvestment = performance.getTotalInvestment();
        double currentMarketValue = performance.getCurrentMarketValue();

        if (totalInvestment > 0) {
            return (currentMarketValue - totalInvestment) / totalInvestment;
        } else {
            return 0.0; // Handle cases where totalInvestment is 0 or unavailable
        }
    }
}