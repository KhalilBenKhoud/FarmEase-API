package com.pi.farmease.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi.farmease.entities.Loan_Type;
import com.pi.farmease.services.ILoanTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RestController
@AllArgsConstructor
@CrossOrigin("a")
@RequestMapping("/api/v1")

public class LoanTypeController {
    private ILoanTypeService LoanTypeService ;

    @GetMapping("/LoanType")
    public List<Loan_Type> getLoanType()
    {
        return LoanTypeService.getLoanType();
    }


    @GetMapping("/LoanType/{loanType_id}")
    public Loan_Type getLoanType(@PathVariable Long loanType_id )
    {
        return LoanTypeService.getLoanTypeById(loanType_id).orElseThrow(
                ()-> new EntityNotFoundException("requested LoanType not found")
        );
    }



    @PostMapping("/LoanType")
    public ResponseEntity<Loan_Type> addLoanType(@RequestPart("loanType") String loanTypeJson,
                                                 @RequestPart("image") MultipartFile image) {
        ObjectMapper objectMapper = new ObjectMapper();
        Loan_Type loanType;
        try {
            loanType = objectMapper.readValue(loanTypeJson, Loan_Type.class);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Loan_Type savedLoanType = LoanTypeService.addLoanType(loanType, image);
            return ResponseEntity.ok(savedLoanType);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/LoanType/{loanType_id}")
    public ResponseEntity<?> UpdateLoanType(@RequestBody Loan_Type LoanType , @PathVariable Long loanType_id) {
        if (LoanTypeService.existById(loanType_id)) {
            com.pi.farmease.entities.Loan_Type Loan_Type1 = LoanTypeService.getLoanTypeById(loanType_id).orElseThrow(
                    ()->new EntityNotFoundException(("Requested LoanType not found"))
            );
            Loan_Type1.setName(LoanType.getName());

            Loan_Type1.setValue(LoanType.getValue());
            if (LoanType.getImage() != null) {
                Loan_Type1.setImage(LoanType.getImage());
            }
            // Sauvegardez les modifications apportées à credit1, pas à Credit
            LoanTypeService.updateLoanType(Loan_Type1,loanType_id);

            // Retournez la réponse avec l'objet credit1 modifié
            return ResponseEntity.ok().body(Loan_Type1);
        } else {
            HashMap<String , String> message= new HashMap<>();
            message.put("message", loanType_id +  "LoanType not found or matched") ;
            // Retournez une réponse avec un code d'erreur 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @DeleteMapping("/LoanType/{loanType_id}")
    @ResponseBody
    public void removeLoanType(@PathVariable("loanType_id") long loanType_id) {
        LoanTypeService.DeleteLoanType(loanType_id);
    }



}
