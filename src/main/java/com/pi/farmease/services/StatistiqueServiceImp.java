package com.pi.farmease.services;

import com.pi.farmease.dao.SinisterRepository;
import com.pi.farmease.entities.Sinister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatistiqueServiceImp implements StatistiqueService {

    @Autowired
    private SinisterRepository sinisterRepository;

    @Override
    public List<Sinister> getSinisterStatisticsByMonth() {
        int month = 2;
        return sinisterRepository.getSinistersByDate_Sinister(month);
    }
}