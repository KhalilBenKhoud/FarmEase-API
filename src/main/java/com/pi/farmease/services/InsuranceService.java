package com.pi.farmease.services;

import com.pi.farmease.entities.Insurance;

import java.util.List;
import java.util.Optional;

public interface InsuranceService {
    List<Insurance> getAllInsurances();
    Insurance getInsuranceById(Integer id);
    Insurance saveInsurance(Insurance insurance);
    Insurance updateInsurance(Insurance insurance);
    void deleteInsurance(Integer id);
}
