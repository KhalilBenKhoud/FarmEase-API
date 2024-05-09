package com.pi.farmease.services;

import com.pi.farmease.entities.Materiel;
import com.pi.farmease.entities.Mortgage;

import java.util.List;

public interface MaterielService {

    void addMateriel(Materiel materiel);
    List<Materiel> getAllMateriels();

    Materiel getMaterielById(Long id);
    void updateMateriel(Materiel materiel);
    void deleteMaterielById(Long id);
}
