package com.pi.farmease.services;
import com.pi.farmease.entities.Sinister;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
public interface SinisterService {
    Sinister saveSinister(Sinister sinister);

    Sinister updateSinister(Sinister sinister);

    void deleteSinister(Integer id);

    Sinister getSinisterById(Integer id);

    List<Sinister> getAllSinisters();

    public String savePhoto(MultipartFile file)throws IOException;

    // Méthode pour vérifier si la description contient des gros mots
    boolean containsForbiddenWords(String description);
    // Méthode pour récupérer les sinistres par mois
    List<Sinister> getSinistersByDate_Sinister(int month);

    // Nouvelle méthode pour récupérer les statistiques des sinistres par mois
    String getSinisterStatisticsByMonth(int month);

}
