package com.pi.farmease.services;
import com.pi.farmease.entities.Sinister;

import java.util.List;
import java.util.Optional;
public interface SinisterService {
    Sinister saveSinister(Sinister sinister);

    Sinister updateSinister(Sinister sinister);

    void deleteSinister(Integer id);

    Sinister getSinisterById(Integer id);

    List<Sinister> getAllSinisters();
}
