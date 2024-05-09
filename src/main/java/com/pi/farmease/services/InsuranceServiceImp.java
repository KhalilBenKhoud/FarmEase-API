package com.pi.farmease.services;

import com.pi.farmease.entities.Insurance;
import com.pi.farmease.dao.InsuranceRepository;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.enumerations.StatusInsurance;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InsuranceServiceImp implements InsuranceService{

    private final InsuranceRepository insuranceRepository;
    private final IEmailService emailService;
    private final UserService userService ;

    // Méthode pour récupérer toutes les assurances
    @Override
    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    // Méthode pour récupérer les assurances de l'utilisateur connecté
    @Override
    public List<Insurance> getInsurancesByCurrentUser(Principal connectedUser) {
        // Vérifier si un utilisateur est connecté
        if (connectedUser == null) {
            throw new IllegalArgumentException("No authenticated user found.");
        }

        // Récupérer l'utilisateur connecté
        User currentUser = userService.getCurrentUser(connectedUser);

        // Récupérer toutes les assurances de l'utilisateur connecté
        return insuranceRepository.findByUser(currentUser);
    }

    // Méthode pour récupérer une assurance par son ID
    @Override
    public Insurance getInsuranceById(Integer id) {

        return insuranceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));
    }

    // Méthode pour enregistrer une assurance

    @Override
    public Insurance saveInsurance(Insurance insurance, Principal connected, int contractDuration) {
        /////////////////MAILING//////////////////////////
       // emailService.sendSimpleMailMessage(insurance,insurance.getUser().getEmail());
        //////////////////////////////////////////////////
        Insurance newInsurance = Insurance.builder()
                .coverage_amount(insurance.getCoverage_amount())
                .start_date(LocalDate.now())
                .end_date(LocalDate.now().plusMonths(contractDuration))
                .user(userService.getCurrentUser(connected))
                .sinisters(new ArrayList<>())
                .premium(insurance.getPremium())
                .type(insurance.getType())
                .status(StatusInsurance.ACTIVE)
                .build();


        return insuranceRepository.save(newInsurance);
    }


    // Méthode pour mettre à jour une assurance en utilisant l'utilisateur connecté
    @Override
    public Insurance updateInsurance(Insurance insurance, Principal connected) {
        // Vérifier si l'utilisateur est connecté
        if (connected == null) {
            throw new IllegalArgumentException("No authenticated user found.");
        }

        User currentUser = userService.getCurrentUser(connected);

        // Vérifier si l'utilisateur connecté est autorisé à modifier cette assurance
        if (!insurance.getUser().equals(currentUser)) {
            throw new IllegalArgumentException("You are not authorized to update this insurance.");
        }

        // Mettre à jour les détails de l'assurance
        insurance.setCoverage_amount(insurance.getCoverage_amount());
        insurance.setEnd_date(insurance.getEnd_date());
        // Mettre à jour d'autres propriétés selon vos besoins

        // Enregistrer les modifications dans la base de données
        return insuranceRepository.save(insurance);
    }

    // Méthode pour supprimer une assurance
    @Override
    public void deleteInsurance(Integer id) {
        insuranceRepository.deleteById(id);
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void checkInsuranceExpiration() {
        LocalDate currentDate = LocalDate.now();
        List<Insurance> activeInsurances = insuranceRepository.findInsurancesByStatus(StatusInsurance.ACTIVE);

        for (Insurance insurance : activeInsurances) {
            LocalDate endDate = insurance.getEnd_date();
            // Vérifie si la date de fin est passée
            if (endDate.isBefore(currentDate)) {
                // Met à jour le statut de l'assurance à "EXPIRED"
                insurance.setStatus(StatusInsurance.EXPIRED);
                insuranceRepository.save(insurance);
            }
        }
    }
}


/////////////////MAILING//////////////////////////
//emailService.sendSimpleMailMessage(c.getSurname()+ " "+ c.getName(),c.getEmail(),confirmation.getToken());
