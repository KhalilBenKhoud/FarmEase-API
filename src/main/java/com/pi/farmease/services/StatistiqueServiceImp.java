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
    public String getSinisterStatisticsByMonth(int month) {
        List<Sinister> sinisters = sinisterRepository.getSinistersByDate_Sinister(month);
        double totalAmount = sinisters.stream().mapToDouble(Sinister::getAmount).sum();
        return String.format("Le nombre de sinistres du mois %d est %d, et la somme des montants est %.2f",
                month, sinisters.size(), totalAmount);
    }
}