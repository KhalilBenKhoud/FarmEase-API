package com.pi.farmease.services;

import com.pi.farmease.entities.Sinister;

import java.util.List;

public interface StatistiqueService {
    // Nouvelle méthode pour récupérer les statistiques des sinistres par mois
    String getSinisterStatisticsByMonth(int month);
}
