package com.pi.farmease.controllers;



import com.pi.farmease.entities.Loan_Type ;
import com.pi.farmease.services.ILoanTypeService ;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public Loan_Type addLoanType(@RequestBody Loan_Type Loan_Type)
    {
        return LoanTypeService.addLoanType(Loan_Type);
    }

    @PutMapping("/LoanType/{loanType_id}")
    public ResponseEntity<?> UpdateLoanType(@RequestBody Loan_Type LoanType , @PathVariable Long loanType_id) {
        if (LoanTypeService.existById(loanType_id)) {
            com.pi.farmease.entities.Loan_Type Loan_Type1 = LoanTypeService.getLoanTypeById(loanType_id).orElseThrow(
                    ()->new EntityNotFoundException(("Requested LoanType not found"))
            );
            Loan_Type1.setName(LoanType.getName());

            Loan_Type1.setValue(LoanType.getValue());

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
