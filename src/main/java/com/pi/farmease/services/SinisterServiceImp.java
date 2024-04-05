package com.pi.farmease.services;

import com.pi.farmease.entities.Sinister;
import com.pi.farmease.dao.SinisterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SinisterServiceImp implements SinisterService {

    private final SinisterRepository sinisterRepository;
    private static final String UPLOAD_DIR = "uploads/Sinisters/";


    @Override
    public Sinister saveSinister(Sinister sinister) {
        return sinisterRepository.save(sinister);
    }

    @Override
    public Sinister updateSinister(Sinister sinister) {
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
}