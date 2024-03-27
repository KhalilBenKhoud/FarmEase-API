package com.pi.farmease.services;

import com.pi.farmease.dao.ApplicationRepository;
import com.pi.farmease.dao.MortgageRepository;
import com.pi.farmease.entities.Application;
import com.pi.farmease.entities.Mortgage;
import com.pi.farmease.entities.Post;
import com.pi.farmease.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserService userService ;
    private final MortgageRepository mortgageRepository;

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Optional<Application> getApplicationById(long id) {
        return applicationRepository.findById(id);
    }

    @Override
    public void addApplication(Application requestBody, Principal connected,long mortid) {
        User connectedUser = userService.getCurrentUser(connected);

        // Vérifie si l'ID du mortgage est présent dans la demande
        if (requestBody.getMortgage() == null || requestBody.getMortgage().getId_mortgage() == null) {
            throw new IllegalArgumentException("Mortgage ID is required in the application request.");
        }

        // Récupère le mortgage associé à l'ID fourni dans la demande
        Optional<Mortgage> mortgageOptional = mortgageRepository.findById(mortid);
        if (mortgageOptional.isPresent()) {
            Mortgage associatedMortgage = mortgageOptional.get();
            // Associe le mortgage à l'application
            requestBody.setMortgage(associatedMortgage);

            // Crée une nouvelle application en utilisant les informations fournies dans requestBody
            Application application = Application.builder()
                    .user(connectedUser) // Utilise l'utilisateur connecté comme propriétaire de l'application
                    .nom_application(requestBody.getNom_application())
                    .prenom_application(requestBody.getPrenom_application())
                    .interesting_rate_application(requestBody.getInteresting_rate_application())
                    .description_application(requestBody.getDescription_application())
                    .etat_application(requestBody.getEtat_application())
                    .build();

            // Sauvegarde la nouvelle application
            applicationRepository.save(application);
        } else {
            throw new EntityNotFoundException("Mortgage not found with ID: " + requestBody.getMortgage().getId_mortgage());
        }
    }

    @Override
    public Application updateApplication(long id, Application application) {
        application.setId_application(id);
        return applicationRepository.save(application);
    }

    @Override
    public void deleteApplication(long id) {
        applicationRepository.deleteById(id);
    }
}