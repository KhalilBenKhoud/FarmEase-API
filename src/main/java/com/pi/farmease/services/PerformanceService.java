package com.pi.farmease.services;

import com.pi.farmease.entities.Performance;
import com.pi.farmease.entities.Project;

import java.util.Map;
import java.util.Optional;

public interface PerformanceService {


    Optional<Performance> getPerformanceByProject(Long projectId);

    Performance updatePerformance(Performance performance);

    void deletePerformanceByProject(Long projectId);
    //**********************
    Map<String, Double> calculateBasicPerformance(Project project);
    Optional<Performance> getLatestPerformance(Project project);

    //todo:Calculate specific performance metrics (optional)
}
