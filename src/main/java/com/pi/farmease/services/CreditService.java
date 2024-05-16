package com.pi.farmease.services;

import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.dao.creditRepository;
import com.pi.farmease.entities.Credit;
import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CreditService implements ICreditService {
    private creditRepository Creditrepository;
    private UserRepository UserRepo ;
    private final UserService userService;
    SmsServiceImpl s;

    public List<Credit> getcredits() {
        return Creditrepository.findAll();
    }

    public Credit getcreditById(long idLoan) {
        return Creditrepository.findById(idLoan)
                .orElse(null); // ou une autre valeur par défaut si nécessaire
    }


    public Credit addCredit(Credit Credit, Principal connected) {
        User connectedUser = userService.getCurrentUser(connected);
        Credit cr = Credit.builder()
                .user(connectedUser)
                .creditPeriod(Credit.getCreditPeriod())
                .differe(Credit.getDiffere())
                .dateDemande(Credit.getDateDemande())
                .DIFF_period(Credit.getDIFF_period())
                .interestRate(Credit.getInterestRate())
                .monthlyPaymentAmount(Credit.getMonthlyPaymentAmount())
                .monthlyPaymentDate(Credit.getMonthlyPaymentDate())
                .amount(Credit.getAmount())
                .obtainingDate(Credit.getObtainingDate())
                .Reason(Credit.getReason())
                .Risk(Credit.getRisk())
                .Completed(Credit.getCompleted())
                .status(Credit.getStatus())

                .build();

//        String smsNumber = "+21656171888";
//        String smsMessage = "Loan sent successfully. Awaiting confirmation of your credit by the administration.  ";
//        String status = s.sendSms(smsNumber, smsMessage);

        return Creditrepository.save(cr);

    }


    @Override
    public void updateCredit(Credit Credit, Long idLoan) {
        Creditrepository.save(Credit);
    }

    public boolean existById(Long idLoan) {
        return Creditrepository.existsById(idLoan);
    }

    public void DeleteCredit(Long idLoan) {
        Creditrepository.deleteById(idLoan);
    }

    @Override
    public List<Credit> getCreditByDueDate() {
        return Creditrepository.getAllByDemand_Date();
    }


    //Fonction qui calcule la mensualité
    public float calculeMonthlyPayment(int idcredit) {

        Credit credit = Creditrepository.findCreditsByIdCredit(idcredit);

        float mensuelIntrestRate=credit.getInterestRate()/12;

        float mensuelPayment =(float) ((credit.getAmount()*mensuelIntrestRate)/(1-(Math.pow((1+mensuelIntrestRate),-credit.getCreditPeriod()))));

        return mensuelPayment;
    }


    public List<Amortisement> amortisement(int idCredit) {
        Credit credit = this.getcreditById(idCredit);
        List<Amortisement> values = new ArrayList<>();
        float duree = credit.getCreditPeriod();
        float intrestRate = (credit.getInterestRate() / 12) / 100;
        float payment = calculeMonthlyPayment(idCredit);
        float principal = credit.getAmount();

        for (int i = 1; i <= duree; i++) {
            float interst = principal * intrestRate;
            float amortiValue = payment - interst;
            Amortisement a = new Amortisement();
            a.setIntrest(interst);
            a.setMonthlyPayment(payment);
            a.setAmortiValue(amortiValue);
            a.setAmount(principal);
            values.add(a);
            principal -= amortiValue; // Ne pas modifier principal ici
        }
        return values;
    }






    public Amortisement Simulateur(Credit credit , int idcredit)

    {   System.out.println(credit.getAmount());
        Amortisement simulator =new Amortisement();
        //mnt total
        simulator.setAmount(0);

        //mnt interet
        simulator.setIntrest(0);
        //mnt monthly
        simulator.setMonthlyPayment(calculeMonthlyPayment(idcredit));
        List<Amortisement> credittab = amortisement(idcredit);
        float s1 = 0;
        for (int i = 0; i < credittab.size(); i++) {
            s1 += credittab.get(i).getIntrest();
        }
        //mnt interet
        simulator.setIntrest(s1);
        //mnt total
        simulator.setAmortiValue(credit.getAmount()+s1);
        //mnt credit
        simulator.setAmount(credit.getAmount());





        return simulator;

    }

    public long calculateLateDays(Credit credit) {
        // Date actuelle
        LocalDate currentDate = LocalDate.now();

        // Date de paiement mensuel
        Date monthlyPaymentDate = credit.getMonthlyPaymentDate();
        LocalDate paymentDate = monthlyPaymentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Calcul du nombre de jours de retard en comparant les dates
        long lateDays = Duration.between(paymentDate.atStartOfDay(), currentDate.atStartOfDay()).toDays();

        // Retourne le nombre de jours de retard
        return lateDays;
    }

    public String calculateRiskForCredit(Credit credit, User client) {
        // Si le client est nouveau (aucune autorisation de crédit)
        if (client.getCredit_authorization() == null) {
            // Vérifier si le salaire du garant est suffisant
            if (1.5 * credit.getGarantor().getSalaryGarantor() >= credit.getAmount()) {
                // Calcul du risque
                return "Le risque est faible.";
            } else {
                // Salaire du garant insuffisant
                return "Le salaire du garant est insuffisant.";
            }
        }
        // Si le client est un ancien client avec une autorisation de crédit
        else if (client.getCredit_authorization()) {
            // Calcul du ratio de retard
            Long clientId = client.getId().longValue();
            float lateDays = calculateLateDays(Creditrepository.getLatestCompletedCreditByClientId(clientId));
            float creditPeriodInDays = Creditrepository.getLatestCompletedCreditByClientId(clientId).getCreditPeriod() * 12 * 30;
            float ratioRetard = lateDays / creditPeriodInDays;

            // 3 cas pour évaluer le risque
            if (ratioRetard < 0.1) {
                return "Le risque est faible.";
            } else if (ratioRetard >= 0.1 && ratioRetard <= 0.25) {
                return "Le risque est modéré.";
            } else {
                // Mauvais historique de crédit
                client.setCredit_authorization(false); // Blacklister le client
                UserRepo.save(client);
                return "Le client est sur liste noire.";
            }
        }
        // Si le client est sur liste noire
        else {
            return "Le client est sur liste noire.";
        }
    }


    public void exportToExcel(List<String> data, String filePath, int idCredit) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Récupérer les données d'amortissement
            List<Amortisement> amortisements = amortisement(idCredit);

            // Style pour les en-têtes
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Ajouter les en-têtes
            Row headerRow = sheet.createRow(0);
            Cell interestHeaderCell = headerRow.createCell(0);
            interestHeaderCell.setCellValue("Intérêt");
            interestHeaderCell.setCellStyle(headerStyle);

            Cell monthlyPaymentHeaderCell = headerRow.createCell(1);
            monthlyPaymentHeaderCell.setCellValue("Paiement Mensuel");
            monthlyPaymentHeaderCell.setCellStyle(headerStyle);

            Cell amortizedValueHeaderCell = headerRow.createCell(2);
            amortizedValueHeaderCell.setCellValue("Valeur Amortie");
            amortizedValueHeaderCell.setCellStyle(headerStyle);

            Cell principalAmountHeaderCell = headerRow.createCell(3);
            principalAmountHeaderCell.setCellValue("Montant Principal");
            principalAmountHeaderCell.setCellStyle(headerStyle);

            // Boucle pour écrire les données d'amortissement dans les lignes de la feuille Excel
            int rowNum = 1; // Commencer à partir de la deuxième ligne après les en-têtes
            for (Amortisement amortisement : amortisements) {
                Row row = sheet.createRow(rowNum++);

                // Insérer les données d'amortissement dans les colonnes appropriées
                Cell interestCell = row.createCell(0);
                interestCell.setCellValue(amortisement.getIntrest());

                Cell monthlyPaymentCell = row.createCell(1);
                monthlyPaymentCell.setCellValue(amortisement.getMonthlyPayment());

                Cell amortizedValueCell = row.createCell(2);
                amortizedValueCell.setCellValue(amortisement.getAmortiValue());

                Cell principalAmountCell = row.createCell(3);
                principalAmountCell.setCellValue(amortisement.getAmount());
            }

            // Calculer le montant total amorti
            float totalAmortizedAmount = 0;
            for (Amortisement amortisement : amortisements) {
                totalAmortizedAmount += amortisement.getAmortiValue();
            }

            // Afficher la date de création du fichier
            Row dateRow = sheet.createRow(rowNum);
            dateRow.createCell(0).setCellValue("Date de création :");
            dateRow.createCell(1).setCellValue(new Date().toString()); // Utilisez la date actuelle

            // Style pour la date de création (jaune)
            CellStyle yellowStyle = workbook.createCellStyle();
            yellowStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            dateRow.getCell(0).setCellStyle(yellowStyle);
            dateRow.getCell(1).setCellStyle(yellowStyle);

            // Afficher le montant total amorti
            Row totalRow = sheet.createRow(rowNum + 1);
            totalRow.createCell(0).setCellValue("Montant total amorti :");
            totalRow.createCell(1).setCellValue(totalAmortizedAmount);

            // Style pour le montant total amorti (rouge)
            CellStyle redStyle = workbook.createCellStyle();
            Font redFont = workbook.createFont();
            redFont.setColor(IndexedColors.RED.getIndex());
            redStyle.setFont(redFont);
            totalRow.getCell(0).setCellStyle(redStyle);
            totalRow.getCell(1).setCellStyle(redStyle);

            // Écrire les données dans un fichier Excel
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        }
    }






}








