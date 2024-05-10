package com.pi.farmease.controllers;


import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.dao.creditRepository;
import com.pi.farmease.entities.Credit;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.Amortisement;
import com.pi.farmease.services.ICreditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RestController
@AllArgsConstructor
@CrossOrigin("a")
@RequestMapping("/api/v1")
public class CreditController {

    private final creditRepository CreditRepository;
    private ICreditService creditservice;

    private UserRepository userRep ;

    @GetMapping("/credit")
    public List<Credit> getcredits() {
        return creditservice.getcredits();
    }

    @GetMapping("/credit/{idLoan}")
    public Credit getredits(@PathVariable Long idLoan) {
        return creditservice.getcreditById(idLoan);

    }

    @PostMapping("/credit")
    public Credit addCredit(@RequestBody Credit Credit , Principal connected) {
        return creditservice.addCredit(Credit , connected);
    }

    @PutMapping("/credit/{idLoan}")
    public ResponseEntity<?> UpdateCredit(@RequestBody Credit Credit, @PathVariable Long idLoan) {
        if (creditservice.existById(idLoan)) {
            com.pi.farmease.entities.Credit credit1 = creditservice.getcreditById(idLoan);


            credit1.setCreditPeriod(Credit.getCreditPeriod());
            credit1.setAmount(Credit.getAmount());
            credit1.setStatus(Credit.getStatus());


            credit1.setCompleted(Credit.getCompleted());
            credit1.setDiffere(Credit.getDiffere());

            credit1.setDIFF_period(Credit.getDIFF_period());
            credit1.setInterestRate(Credit.getInterestRate());
            credit1.setRisk(Credit.getRisk());

            credit1.setReason(Credit.getReason());
            credit1.setMonthlyPaymentDate(Credit.getMonthlyPaymentDate());
            credit1.setMonthlyPaymentAmount(Credit.getMonthlyPaymentAmount());

            credit1.setDiffere(Credit.getDiffere());


            // Sauvegardez les modifications apportées à credit1, pas à Credit
            creditservice.updateCredit(credit1, idLoan);

            // Retournez la réponse avec l'objet credit1 modifié
            return ResponseEntity.ok().body(credit1);
        } else {
            HashMap<String, String> message = new HashMap<>();
            message.put("message", idLoan + "Credit not found or matched");
            // Retournez une réponse avec un code d'erreur 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }


    @DeleteMapping("/credit/{idCredit}")
    @ResponseBody
    public void removecredit(@PathVariable("idCredit") long idCredit) {
        creditservice.DeleteCredit(idCredit);
    }


    @GetMapping("/credit/duedate")
    public List<Credit> getCreditByDueDate() {
        return creditservice.getCreditByDueDate();
    }

    @GetMapping("/admin/mensuelPayment/{id}")
    public float calculateMonthlyPayment(@PathVariable int id) {
        return creditservice.calculeMonthlyPayment(id);
    }


    @GetMapping("/getAmortisementTable/{idCredit}")
    public List<Amortisement> TABLE(@PathVariable int idCredit) {
        return creditservice.amortisement(idCredit);
    }



    @GetMapping("/simulate/{idCredit}/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public Amortisement simulate(@PathVariable("idCredit") int idCredit,@PathVariable("mnttotl") float mnttotl,@PathVariable("period") float period
            ,@PathVariable("interst") float interst) {
        Credit cr=new Credit(mnttotl,period,interst);

        return creditservice.Simulateur(cr,idCredit);

    }

    @GetMapping("/calculateRisk/{creditId}/{clientId}")
    public String calculateRiskForCredit(@PathVariable Long creditId, @PathVariable Long clientId) {
        Credit credit = creditservice.getcreditById(creditId);
        User client = userRep.getById(Math.toIntExact(clientId));
        return creditservice.calculateRiskForCredit(credit, client);
    }





    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam int idCredit) {
        try {
            // Récupérer les données d'amortissement pour l'identifiant de crédit donné
            List<Amortisement> amortisements = creditservice.amortisement(idCredit);

            // Convertir les données d'amortissement en une liste de chaînes pour la fonction exportToExcel
            List<String> data = convertAmortisementsToStringList(amortisements);

            String filePath = "example.xlsx"; // Spécifier le chemin du fichier

            // Appeler la fonction exportToExcel avec les données d'amortissement et le chemin du fichier
            creditservice.exportToExcel(data, "C:\\Users\\pc\\Desktop\\infini\\" + filePath, idCredit);

            // Lire le fichier Excel généré
            Path path = Paths.get("C:\\Users\\pc\\Desktop\\infini\\" + filePath);
            byte[] fileContent = Files.readAllBytes(path);

            // Configurer les en-têtes de la réponse
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filePath);
            headers.setContentLength(fileContent.length);

            // Retourner la réponse avec le contenu du fichier Excel
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Méthode pour convertir les données d'amortissement en une liste de chaînes
    private List<String> convertAmortisementsToStringList(List<Amortisement> amortisements) {
        List<String> dataList = new ArrayList<>();
        for (Amortisement amortisement : amortisements) {
            // Ajouter les valeurs pertinentes à la liste de chaînes
            dataList.add(amortisement.getIntrest() + "," +
                    amortisement.getMonthlyPayment() + "," +
                    amortisement.getAmortiValue() + "," +
                    amortisement.getAmount());
        }
        return dataList;




    }


    @GetMapping("/credits/average")
    public double getAverageCreditAmount() {
        return creditservice.calculateTotalAmount() ; // Appel de la méthode du service
    }
}















