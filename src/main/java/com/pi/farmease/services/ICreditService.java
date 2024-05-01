package com.pi.farmease.services;


import com.pi.farmease.entities.Credit;
import com.pi.farmease.entities.User;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ICreditService {
    List<Credit> getcredits();
    Credit getcreditById(long idLoan);
     boolean existById(Long idLoan);


    Credit addCredit(Credit Credit , Principal connected  );
    void updateCredit(Credit Credit, Long idLoan);

    void DeleteCredit(Long idLoan);


    List<Credit> getCreditByDueDate();

    public Amortisement Simulateur(Credit credit , int idcredit) ;
    float calculeMonthlyPayment(int credit);
    List<Amortisement> amortisement(int idCredit);


    public String calculateRiskForCredit(Credit credit, User client) ;
    public void exportToExcel(List<String> data, String filePath , int idCredit) throws IOException ;
    public double calculateTotalAmount() ;


}



