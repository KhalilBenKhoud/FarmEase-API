package com.pi.farmease.dao;

import com.pi.farmease.entities.Sinister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SinisterRepository extends JpaRepository<Sinister, Integer> {
    @Query("SELECT s FROM Sinister s WHERE MONTH(s.date_Sinister) = :month")
    List<Sinister> getSinistersByDate_Sinister(int month);
}