package com.pi.farmease.dao;

import com.pi.farmease.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("SELECT app FROM Application app WHERE app.mortgage.id_mortgage = :idMortgage")
    List<Application> findByMortgageIdMortgage(@Param("idMortgage") long idMortgage);
    List<Application> findByUserId(int userId);

}
