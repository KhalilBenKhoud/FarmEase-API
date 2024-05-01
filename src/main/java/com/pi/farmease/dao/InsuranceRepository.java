package com.pi.farmease.dao;

import com.pi.farmease.entities.Insurance;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.enumerations.StatusInsurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceRepository extends JpaRepository<Insurance,Integer> {
    List<Insurance> findInsurancesByStatus(StatusInsurance statusInsurance);

    List<Insurance> findByUser(User currentUser);
}
