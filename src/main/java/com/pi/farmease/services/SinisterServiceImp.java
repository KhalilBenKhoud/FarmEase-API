package com.pi.farmease.services;


import com.pi.farmease.dao.InsuranceRepository;
import com.pi.farmease.entities.Insurance;
import com.pi.farmease.entities.Sinister;
import com.pi.farmease.dao.SinisterRepository;
import com.pi.farmease.entities.enumerations.StatusSinister;
import jakarta.persistence.EntityNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.util.Optional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SinisterServiceImp implements SinisterService {

    private final SinisterRepository sinisterRepository;

    private final InsuranceRepository insuranceRepository;
    private static final String UPLOAD_DIR = "uploads/Sinisters/";
    private static final List<String> FORBIDDEN_WORDS = List.of("gros_mot1", "gros_mot2", "gros_mot3");


    @Override
    public Sinister saveSinister(Sinister sinister, int insuranceId) {
        // Vérifier si l'assurance associée au sinister est présente
        Insurance concerned = insuranceRepository.findById(insuranceId).orElse(null) ;
        sinister.setInsurance(concerned);
        sinister.setStatus(StatusSinister.UNDER_REVIEW);

        return sinisterRepository.save(sinister);
    }

    @Override
    public Sinister updateSinister(Sinister sinister) {

        if (containsForbiddenWords(sinister.getDescription())) {
            throw new IllegalArgumentException("La description contient des mots interdits.");
        }

        return sinisterRepository.save(sinister);
    }

    @Override
    public void deleteSinister(Integer id) {
        sinisterRepository.deleteById(id);
    }

    @Override
    public Sinister getSinisterById(Integer id) {
        return sinisterRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sinister> getAllSinisters() {
        return sinisterRepository.findAll();
    }

    @Override

    public List<Sinister> getSinistersByInsuranceId(int insuranceId) {
        return sinisterRepository.findByInsuranceId(insuranceId);
    }
    @Override

    public String savePhoto(MultipartFile file) throws IOException {
        // Create a unique file name to prevent conflicts
        String fileName = generateUniqueFileName(file.getOriginalFilename());

        // Create the directory if it doesn't exist
        createUploadDirectoryIfNotExist();

        // Get the file path to save the image
        String filePath = UPLOAD_DIR + fileName;

        // Save the file to the specified location
        Path destPath = Paths.get(filePath);
        Files.copy(file.getInputStream(), destPath);

        return fileName;
    }
    // Helper method to generate a unique file name
    private String generateUniqueFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return uuid + extension;
    }

    // Helper method to create upload directory if it doesn't exist
    private void createUploadDirectoryIfNotExist() {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }


    // Méthode pour vérifier si la description contient des gros mots
    public boolean containsForbiddenWords(String description) {
        for (String word : FORBIDDEN_WORDS) {
            if (description.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public List<Sinister> getSinistersByDate_Sinister(int month) {
        return sinisterRepository.getSinistersByDate_Sinister(month);
    }

    // Nouvelle méthode pour récupérer les statistiques des sinistres par mois
    public String getSinisterStatisticsByMonth(int month) {
        List<Sinister> sinisters = sinisterRepository.getSinistersByDate_Sinister(month);
        double totalAmount = sinisters.stream().mapToDouble(Sinister::getAmount).sum();
        return String.format("Le nombre de sinistres du mois %d est %d, et la somme des montants est %.2f",
                month, sinisters.size(), totalAmount);
    }

    public List<Object[]> findAllSinisterCoordinates(){
        return sinisterRepository.findAllSinisterCoordinates();
    }

}