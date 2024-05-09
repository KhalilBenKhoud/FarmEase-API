package com.pi.farmease.services;

import com.pi.farmease.dao.GroundRepository;
import com.pi.farmease.entities.Ground;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class GroundServiceImpl implements GroundService{
    private final GroundRepository groundRepository;


    public GroundServiceImpl(GroundRepository groundRepository) {
        this.groundRepository = groundRepository;
    }

    @Override
    public void addGround(Ground requestBody) {
        Ground ground = new Ground();
        ground.setPlace_ground(requestBody.getPlace_ground());
        ground.setPrice_ground(requestBody.getPrice_ground());

        // Enregistrer le ground dans la base de données
        groundRepository.save(ground);
        // Enregistrer le terrain dans la base de données

    }

    @Override
    public List<Ground> getAllGrounds() {
        return groundRepository.findAll();
    }

    @Override
    public Ground getGroundById(Long id) {
        return groundRepository.findById(id).orElse(null);
    }

    @Override
    public void updateGround(Ground updatedGround) {
        Ground existingGround = groundRepository.findById(updatedGround.getId_ground())
                .orElse(null);
        if (existingGround != null) {
            existingGround.setPlace_ground(updatedGround.getPlace_ground());
            existingGround.setPrice_ground(updatedGround.getPrice_ground());

            // Enregistrer les modifications dans la base de données
            groundRepository.save(existingGround);
        }
    }

    @Override
    public void deleteGroundById(Long id) {
        groundRepository.deleteById(id);
    }
}
