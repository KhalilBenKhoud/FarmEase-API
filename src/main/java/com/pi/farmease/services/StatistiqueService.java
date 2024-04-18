package com.pi.farmease.services;

import com.pi.farmease.entities.Sinister;

import java.util.List;

public interface StatistiqueService {
    List<Sinister> getSinisterStatisticsByMonth();
}
