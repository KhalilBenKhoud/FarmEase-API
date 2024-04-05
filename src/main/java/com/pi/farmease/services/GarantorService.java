package com.pi.farmease.services;
import com.pi.farmease.dao.creditRepository;
import com.pi.farmease.entities.Credit;
import com.pi.farmease.entities.Garantor ;
import com.pi.farmease.dao.GarantorRepository ;
import com.pi.farmease.services.CreditService ;


import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GarantorService implements IGarantorService {

    private  GarantorRepository garantroRepo ;
    private creditRepository  creditRepo ;
    private final UserService userService ;
    private CreditService crService ;
    public List<Garantor> getGarantor()
    {
        return garantroRepo.findAll() ;
    }

    public Optional<Garantor> getGarantorById(long idGarantor)
    {
        return garantroRepo.findById(idGarantor) ;
    }

    public Garantor addGarantor(Garantor garantor, Long creditId, Principal connected) {
        // Récupérer l'utilisateur connecté
        User connectedUser = userService.getCurrentUser(connected);

        // Définir l'utilisateur connecté pour le garant
        garantor.setUser(connectedUser);

        // Vérifier si l'ID du crédit est null ou non
        if (creditId == null) {
            throw new IllegalArgumentException("L'ID du crédit ne peut pas être null.");
        }

        // Construire le Garantor à partir des informations fournies
        Garantor garantor1 = Garantor.builder()
                .user(connectedUser)
                .nameGarantor(garantor.getNameGarantor())
                .secondnameGarantor(garantor.getSecondnameGarantor())
                .salaryGarantor(garantor.getSalaryGarantor())
                .workGarantor(garantor.getWorkGarantor())
                .qrString(garantor.getQrString())
                .build();

        // Récupérer le crédit associé à partir de l'ID
        Credit credit = new Credit();
        credit.setIdCredit(creditId);
        garantor1.setCredit(credit);

        // Sauvegarder le Garantor dans la base de données
        Garantor savedGarantor = garantroRepo.save(garantor1);

        return savedGarantor;
    }

    public void updateGarantor(Garantor garantor, Long idGarantor) {
        garantroRepo.save(garantor);
    }

    public boolean existById(Long idGarantor)
    {
        return garantroRepo.existsById(idGarantor);
    }

    public void DeleteGarantor(Long idGarantor) {
        garantroRepo.deleteById(idGarantor);
    }




}
