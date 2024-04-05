package com.pi.farmease.controllers;


import  com.pi.farmease.entities.Garantor ;
import com.pi.farmease.dao.GarantorRepository ;
import com.pi.farmease.services.GarantorService ;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RestController
@AllArgsConstructor
@RequestMapping("/Garantor Loan")
public class GarantorController {
    private final GarantorRepository garantorRepo ;
    private GarantorService garantorService ;

    @GetMapping("/Garantor")
    public List<Garantor> getcredits()
    {
        return garantorService.getGarantor();
    }

    @GetMapping("/Garantor/{idGarantor}")
    public Garantor getGarantor(@PathVariable Long idGarantor )
    {
        return garantorService.getGarantorById(idGarantor).orElseThrow(
                ()->new EntityNotFoundException("Requested Credit not found")
        );
    }


    @PostMapping("/addGarantor")
    public ResponseEntity<Garantor> addGarantor(
            @RequestBody Garantor garantor,
            @RequestParam(value = "creditId") Long creditId,
            Principal connected) {
        Garantor savedGarantor = garantorService.addGarantor(garantor, creditId, connected);
        return new ResponseEntity<>(savedGarantor, HttpStatus.CREATED);
    }

    @PutMapping("/Garantor/{idGarantor}")
    public ResponseEntity<?> UpdateGarantor(@RequestBody Garantor garantor, @PathVariable Long idGarantor) {
        if (garantorService.existById(idGarantor)) {
            com.pi.farmease.entities.Garantor garantor1 = garantorService.getGarantorById(idGarantor).orElseThrow(
                    ()->new EntityNotFoundException(("Requested credit not found"))
            );
            garantor1.setIdGarantor(garantor.getIdGarantor());
            garantor1.setNameGarantor(garantor.getNameGarantor());
            garantor1.setSalaryGarantor(garantor.getSalaryGarantor());
            garantor1.setSecondnameGarantor(garantor.getSecondnameGarantor());
            garantor1.setWorkGarantor(garantor.getWorkGarantor());
            garantor1.setQrString(garantor.getQrString());

            // Sauvegardez les modifications apportées à credit1, pas à Credit
            garantorService.updateGarantor(garantor1,idGarantor);

            // Retournez la réponse avec l'objet credit1 modifié
            return ResponseEntity.ok().body(garantor1);
        } else {
            HashMap<String , String> message= new HashMap<>();
            message.put("message", idGarantor +  "Credit not found or matched") ;
            // Retournez une réponse avec un code d'erreur 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }



    }


    @DeleteMapping("/Garantor/{idGarantor}")
    @ResponseBody
    public void removeGarantor(@PathVariable("idGarantor") long idGarantor) {
        garantorService.DeleteGarantor(idGarantor);
    }



}
