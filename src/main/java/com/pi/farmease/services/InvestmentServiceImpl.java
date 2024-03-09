package com.pi.farmease.services;

import com.pi.farmease.dao.WalletRepository;
import com.pi.farmease.entities.Wallet;
import com.pi.farmease.exceptions.BusinessException;
import com.pi.farmease.exceptions.InsufficientBalanceException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.pi.farmease.entities.Investment;
import com.pi.farmease.entities.Project;
import com.pi.farmease.entities.User;
import com.pi.farmease.dao.InvestmentRepository;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {
    @Autowired

    private InvestmentRepository investmentRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
     UserService userService;

    @Override
    public Investment createInvestment(Investment requestBody, Principal connected) {
        User connectedUser = userService.getCurrentUser(connected);
        Wallet wallet = connectedUser.getWallet();

        double newBalance = wallet.getBalance() - requestBody.getAmount();
        if (newBalance < 0) {
            throw new InsufficientBalanceException("Insufficient balance for investment.");
        }
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        try {
            Investment investment = Investment.builder()
                    .investor(connectedUser)
                    .investedAt(new Date())
                    .amount(requestBody.getAmount())
                    .build();


            String investorEmail = connectedUser.getEmail();
            String subject = "Your Investment is Successful!";
            String body = "Dear " + connectedUser.getFirstname() + ",\n\n" +
                    "We are pleased to inform you that your investment of " + requestBody.getAmount() +
                    "$ in the project has been successful!\n\n" +
                    "Thank you for investing in our platform.\n\n" +
                    "Sincerely,\n" +
                    "FARMEASE Team";
            sendInvestmentNotification( investorEmail, subject, body);

            return investmentRepository.save(investment);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Failed to create investment due to data integrity issues.", e);
        }
    }



    @Override
    public Optional<Investment> getInvestmentById(Long id) {
        return investmentRepository.findById(id);
    }

    @Override
    public List<Investment> getAllInvestments() {
        return investmentRepository.findAll();
    }

    @Override
    public List<Investment> getInvestmentsByProject(Long projectId) {
        Project project = new Project();
        project.setId(projectId);
        return investmentRepository.findByProject(project);
    }

    @Override
    public List<Investment> getInvestmentsByInvestor(Integer investorId) {
        User investor = new User();
        investor.setId(investorId);
        return investmentRepository.findByInvestor(investor);
    }

    @Override
    public Investment updateInvestment(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Override
    public void deleteInvestmentById(Long id) {
        investmentRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return investmentRepository.existsById(id);
    }

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public InvestmentServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendInvestmentNotification(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            //mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);
//            for (MultipartFile multipartFile : file) {
//                mimeMessageHelper.addAttachment(
//                        Objects.requireNonNull(multipartFile.getOriginalFilename()),
//                        new ByteArrayResource(multipartFile.getBytes())
//                );
//
//            }
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
