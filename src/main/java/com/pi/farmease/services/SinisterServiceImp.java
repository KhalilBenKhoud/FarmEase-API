package com.pi.farmease.services;

import com.pi.farmease.entities.Sinister;
import com.pi.farmease.dao.SinisterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SinisterServiceImp implements SinisterService {

    private final SinisterRepository sinisterRepository;


    @Override
    public Sinister saveSinister(Sinister sinister) {
        return sinisterRepository.save(sinister);
    }

    @Override
    public Sinister updateSinister(Sinister sinister) {
        return sinisterRepository.save(sinister);
    }

    @Override
    public void deleteSinister(Integer id) {
        sinisterRepository.deleteById(id);
    }

    @Override
    public Sinister getSinisterById(Integer id) {
        return sinisterRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sinister> getAllSinisters() {
        return sinisterRepository.findAll();
    }
}