package com.pi.farmease.controllers;


import com.google.zxing.WriterException;
import com.pi.farmease.dao.GarantorRepository;
import com.pi.farmease.dao.creditRepository;
import com.pi.farmease.entities.Credit;
import com.pi.farmease.entities.Garantor;
import com.pi.farmease.services.GarantorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class GarantorController {
    private final GarantorRepository garantorRepo;
    private GarantorService garantorService;
    private final GarantorRepository garantorRepository;
    private final creditRepository CreditRepository;


    @GetMapping("/Garantor")
    public List<Garantor> getcredits() {
        return garantorService.getGarantor();
    }

    @GetMapping("/Garantor/{idGarantor}")
    public Garantor getGarantor(@PathVariable Long idGarantor) {
        return garantorService.getGarantorById(idGarantor).orElseThrow(
                () -> new EntityNotFoundException("Requested Credit not found")
        );
    }


    @PostMapping("/addGarantor")
    public ResponseEntity<Garantor> addGarantor(
            @RequestBody Garantor garantor,
            Principal connected) {
        // Récupérer le dernier ID de crédit disponible dans la base de données
        Long lastCreditId = garantorRepository.getLastCreditId();

        // Créer le garant en utilisant les données fournies
        Garantor savedGarantor = new Garantor();
        savedGarantor.setNameGarantor(garantor.getNameGarantor());
        savedGarantor.setSecondnameGarantor(garantor.getSecondnameGarantor());
        savedGarantor.setSalaryGarantor(garantor.getSalaryGarantor());
        savedGarantor.setWorkGarantor(garantor.getWorkGarantor());
        savedGarantor.setQrString(garantor.getQrString());

        // Assurez-vous que le dernier ID de crédit est non null
        if (lastCreditId != null) {
            // Associer le crédit au garant
            Optional<Credit> optionalCredit = CreditRepository.findById(lastCreditId);
            optionalCredit.ifPresent(savedGarantor::setCredit);
        } else {
            // Gérer le cas où le dernier ID de crédit est null
            // Cela peut être dû à une erreur de récupération du dernier ID de crédit
            // Vous pouvez choisir de renvoyer une erreur ou de gérer autrement ce cas
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Sauvegarder le garant dans la base de données
        Garantor addedGarantor = garantorRepository.save(savedGarantor);

        // Retourner la réponse avec le garant ajouté
        return new ResponseEntity<>(addedGarantor, HttpStatus.CREATED);
    }


    @PutMapping("/Garantor/{idGarantor}")
    public ResponseEntity<?> UpdateGarantor(@RequestBody Garantor garantor, @PathVariable Long idGarantor) {
        if (garantorService.existById(idGarantor)) {
            com.pi.farmease.entities.Garantor garantor1 = garantorService.getGarantorById(idGarantor).orElseThrow(
                    () -> new EntityNotFoundException(("Requested credit not found"))
            );
            garantor1.setIdGarantor(garantor.getIdGarantor());
            garantor1.setNameGarantor(garantor.getNameGarantor());
            garantor1.setSalaryGarantor(garantor.getSalaryGarantor());
            garantor1.setSecondnameGarantor(garantor.getSecondnameGarantor());
            garantor1.setWorkGarantor(garantor.getWorkGarantor());
            garantor1.setQrString(garantor.getQrString());

            // Sauvegardez les modifications apportées à credit1, pas à Credit
            garantorService.updateGarantor(garantor1, idGarantor);

            // Retournez la réponse avec l'objet credit1 modifié
            return ResponseEntity.ok().body(garantor1);
        } else {
            HashMap<String, String> message = new HashMap<>();
            message.put("message", idGarantor + "Credit not found or matched");
            // Retournez une réponse avec un code d'erreur 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }


    }


    @DeleteMapping("/Garantor/{idGarantor}")
    @ResponseBody
    public void removeGarantor(@PathVariable("idGarantor") long idGarantor) {
        garantorService.DeleteGarantor(idGarantor);
    }

    @PostMapping("/pdf/upload")
    public ResponseEntity<?> uploadPdfDocumentForLastGarantor(@RequestParam("file") MultipartFile file) {
        try {
            // Sauvegarder le document PDF associé au dernier garant
            garantorService.savePdfDocumentForLastGarantor(file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload PDF document: " + e.getMessage());
        }
    }


    @GetMapping(value = "/qrcode/all", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCodeForAllPDFs() {
        try {
            byte[] qrCodeBytes =  garantorService.generateQRCodeForAllPDFs();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeBytes);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{id}/pdfDocument")
    public ResponseEntity<byte[]> getPdfDocument(@PathVariable Long id) {
        Optional<Garantor> optionalGarantor = garantorRepository.findById(id);
        if (optionalGarantor.isPresent()) {
            Garantor garantor = optionalGarantor.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(garantor.getPdfDocument());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

