package com.pi.farmease.services;

import com.pi.farmease.entities.Ground;

import java.util.List;

public interface GroundService {
    void addGround(Ground requestBody);
    List<Ground> getAllGrounds();
    Ground getGroundById(Long id);
    void updateGround(Ground updatedGround);
    void deleteGroundById(Long id);
}
