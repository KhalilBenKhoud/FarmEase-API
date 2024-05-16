package com.pi.farmease.services;

import com.pi.farmease.dao.ApplicationRepository;
import com.pi.farmease.dao.MortgageRepository;
import com.pi.farmease.entities.Application;
import com.pi.farmease.entities.Mortgage;
import com.pi.farmease.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserService userService;
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
    public List<Application> getApplicationsByConnectedUser(Principal connectedUser) {
        // Récupérer l'utilisateur connecté à partir de Principal
        User user = userService.getCurrentUser(connectedUser);

        // Récupérer les applications de l'utilisateur connecté
        return applicationRepository.findByUserId(user.getId());
    }
    @Override
    public void addApplication(Application requestBody, Principal connected, long mortgageid) {
        User connectedUser = userService.getCurrentUser(connected);

        Optional<Mortgage> mortgageOptional = mortgageRepository.findById(mortgageid);
        if (mortgageOptional.isPresent()) {
            Mortgage associatedMortgage = mortgageOptional.get();

            // Crée une nouvelle application en utilisant les informations fournies dans requestBody
            Application application = Application.builder()
                    .user(connectedUser) // Utilise l'utilisateur connecté comme propriétaire de l'application
                    .nom_application(connectedUser.getFirstname()) // Ajoute le nom de l'utilisateur à l'application
                    .prenom_application(connectedUser.getLastname()) // Ajoute le prénom de l'utilisateur à l'application
                    .interesting_rate_application(requestBody.getInteresting_rate_application()) // Ajoute le taux d'intérêt à l'application
                    .description_application(requestBody.getDescription_application())
                    .etat_application("pending")
                    .message_application(requestBody.getMessage_application())
                    .mortgage(associatedMortgage) // Associe l'hypothèque à l'application
                    .build();

            // Sauvegarde la nouvelle application
            applicationRepository.save(application);
        } else {
            throw new EntityNotFoundException("Mortgage not found with ID: " + mortgageid);
        }
    }


    @Override
    public void updateApplication(Application application, long id) {
        Application existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));

        // Update attributes with new values
        existingApplication.setNom_application(application.getNom_application());
        existingApplication.setPrenom_application(application.getPrenom_application());
        existingApplication.setInteresting_rate_application(application.getInteresting_rate_application());
        existingApplication.setDescription_application(application.getDescription_application());
        existingApplication.setEtat_application(application.getEtat_application());
        existingApplication.setMessage_application(application.getMessage_application());

        // Save the changes to the database
        applicationRepository.save(existingApplication);
    }
    @Override
    public void accept_application( long id) {
        Application existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));

        // Update attributes with new values

        existingApplication.setEtat_application("accepted");


        // Save the changes to the database
        applicationRepository.save(existingApplication);
    }
    @Override
    public void refuse_application( long id) {
        Application existingApplication = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));

        // Update attributes with new values

        existingApplication.setEtat_application("refused");


        // Save the changes to the database
        applicationRepository.save(existingApplication);
    }
    @Override
    public List<Application> getApplicationsByIdMortgage(long idMortgage) {
        return applicationRepository.findByMortgageIdMortgage(idMortgage);
    }
    @Override
    public void exportApplicationToPdf(Application application, String filePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setLeading(15f); // Set line spacing
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();

                // Initial Y offset
                float yPosition = 720;

                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Application Details");
                yPosition += 20; // Adjust Y position for the next line

                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("ID: " + application.getId_application());
                yPosition += 20; // Adjust Y position for the next line

                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Nom: " + application.getNom_application());
                yPosition += 60; // Adjust Y position for the next line

                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Etat: " + application.getEtat_application());
                yPosition += 80; // Adjust Y position for the next line

                // Add more attributes as needed
                contentStream.endText();
            }

            document.save(new File(filePath));
        }
    }

    @Override
    public void deleteApplication(long id) {
        applicationRepository.deleteById(id);
    }
    @Override
    public void deleteApplicationBymortgage(long idMortgage) {
        List<Application> applicationsToDelete = applicationRepository.findByMortgageIdMortgage(idMortgage);
        applicationRepository.deleteAll(applicationsToDelete);
    }



}


