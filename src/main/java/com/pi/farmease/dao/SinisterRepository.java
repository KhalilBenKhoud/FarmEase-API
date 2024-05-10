package com.pi.farmease.dao;

import com.pi.farmease.entities.Sinister;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SinisterRepository extends JpaRepository<Sinister, Integer> {
    // Récupérer les sinistres pour un mois donné
    @Query("SELECT s FROM Sinister s WHERE MONTH(s.date_Sinister) = :month")
    List<Sinister> getSinistersByDate_Sinister(int month);

    // Calculer la somme des montants pour un mois donné
    @Query("SELECT SUM(s.amount) FROM Sinister s WHERE MONTH(s.date_Sinister) = :month")
    Double getTotalAmountByMonth(int month);
    List<Sinister> findByInsuranceId(int insuranceId);

    @Query("SELECT u.address.latitude, u.address.longitude FROM Sinister u")
    List<Object[]> findAllSinisterCoordinates();
}

