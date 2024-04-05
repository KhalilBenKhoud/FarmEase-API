package com.pi.farmease.services;


import com.pi.farmease.entities.Garantor ;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface IGarantorService {

    List<Garantor> getGarantor();
    Optional<Garantor> getGarantorById(long idGarantor);
    boolean existById(Long idGarantor);


    public Garantor addGarantor(Garantor garantor, Long creditId, Principal connected) ;
    void updateGarantor(Garantor garantor, Long idGarantor);

    void DeleteGarantor(Long idGarantor);


}
