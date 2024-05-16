package com.pi.farmease.services;

import com.pi.farmease.entities.Insurance;


import java.security.Principal;

import java.util.List;
import java.util.Optional;

public interface InsuranceService {
    List<Insurance> getAllInsurances();

    List<Insurance> getInsurancesByCurrentUser(Principal connectedUser);
    Insurance getInsuranceById(Integer id);
    Insurance saveInsurance(Insurance insurance, Principal connected, int duration);
    Insurance updateInsurance(Insurance insurance , Principal connected);
    void deleteInsurance(Integer id);
    public void checkInsuranceExpiration();

}
