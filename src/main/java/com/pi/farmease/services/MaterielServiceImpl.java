package com.pi.farmease.services;

import com.pi.farmease.dao.MaterielRepository;


import com.pi.farmease.entities.Materiel;
import com.pi.farmease.entities.Mortgage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class MaterielServiceImpl implements MaterielService {

    private final MaterielRepository materielRepository;

    @Autowired
    public MaterielServiceImpl(MaterielRepository materielRepository) {
        this.materielRepository = materielRepository;
    }

    @Override
    public void addMateriel(Materiel requestBody) {
        // Construire l'objet Materiel
        Materiel materiel = Materiel.builder()
                .name_materiel(requestBody.getName_materiel())
                .price_materiel(requestBody.getPrice_materiel())
                .build();

        // Enregistrer le materiel dans la base de données
        materielRepository.save(materiel);
    }
    @Override
    public List<Materiel> getAllMateriels() {
        return materielRepository.findAll();
    }
    @Override
    public Materiel getMaterielById(Long id) {
        return materielRepository.findById(id).orElse(null);
    }

    @Override
    public void updateMateriel(Materiel updatedMateriel) {
        Materiel existingMateriel = materielRepository.findById(updatedMateriel.getId_materiel())
                .orElse(null);
        if (existingMateriel != null) {
            existingMateriel.setName_materiel(updatedMateriel.getName_materiel());
            existingMateriel.setPrice_materiel(updatedMateriel.getPrice_materiel());

            // Enregistrer les modifications dans la base de données
            materielRepository.save(existingMateriel);
        }
    }

    @Override
    public void deleteMaterielById(Long id) {
        materielRepository.deleteById(id);
    }
}
