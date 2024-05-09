package com.pi.farmease.services;

import com.pi.farmease.dao.PerformanceRepository;
import com.pi.farmease.dao.ProjectRepository;
import com.pi.farmease.dao.WalletRepository;
import com.pi.farmease.entities.*;
import com.pi.farmease.entities.enumerations.ProjectStatus;
import com.pi.farmease.exceptions.BusinessException;
import com.pi.farmease.exceptions.InsufficientBalanceException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.pi.farmease.dao.InvestmentRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvestmentServiceImpl implements InvestmentService {
    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private TransactionService transactionService;

    @Override
    public Investment createInvestment(Investment requestBody, Long projectId, Principal connected) {
        User connectedUser = userService.getCurrentUser(connected);
        Wallet wallet = connectedUser.getWallet();
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new BusinessException("Project not found with id: " + projectId);
        }
        assert project != null;
        User projectOwner = project.getCreator();
        double amountInvested  = requestBody.getAmount();

        double newBalance = wallet.getBalance() - requestBody.getAmount();
        if (newBalance < 0) {
            throw new InsufficientBalanceException("Insufficient balance for investment.");
        }
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        try {
            //Project project = projectRepository.findById(projectId).orElse(null);
//            if (project == null) {
//                throw new BusinessException("Project not found with id: " + projectId);
//            }

            // Calculate total investment dynamically without updating project's total investment
            double totalInvestment = project.getTotalInvestment() == null ? requestBody.getAmount() :
                    project.getTotalInvestment() + requestBody.getAmount();

            // Update project's status if the goal amount is reached
            if (totalInvestment >= project.getGoalAmount()) {
                project.setProjectStatus(ProjectStatus.FUNDED);

            }else if(totalInvestment == project.getGoalAmount()) {
                project.setProjectStatus(ProjectStatus.PENDING);
            }

            // Calculate investor's ownership stake
            double ownershipStake = (requestBody.getAmount() / totalInvestment) * project.getEquityOffered();

            // Calculate potential payout
            double potentialPayout = 0.0;
            Performance performance = performanceRepository.findByProject(project).orElse(null);
            if (performance != null && performance.getAnnualProjectedProfit() > 0) {
                double dividendPayoutRatio = project.getDividendPayoutRatio();
                potentialPayout = requestBody.getAmount() * performance.getAnnualProjectedProfit() * dividendPayoutRatio / 100;
            }

            // Create investment object
            Investment investment = Investment.builder()
                    .investor(connectedUser)
                    .investedAt(new Date())
                    .amount(requestBody.getAmount())
                    .investorShare(ownershipStake)
                    .potentialPayout(potentialPayout)
                    .project(project)
                    .build();

            // Save updated project status
            projectRepository.save(project);

            // Send investment notification email
            String investorEmail = connectedUser.getEmail();
            String subject = "Your Investment is Successful!";
            String body = "Dear " + connectedUser.getFirstname() + ",\n\n" +
                    "We are pleased to inform you that your investment of " + requestBody.getAmount() +
                    "$ in the project has been successful!\n\n" +
                    "Thank you for investing in our platform.\n\n" +
                    "Sincerely,\n" +
                    "FARMEASE Team";
            sendInvestmentNotification(investorEmail, subject, body);
            transactionService.transferMoneyFromUsertoUser(connectedUser, projectOwner, amountInvested);

            // Save investment
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
    public Double calculateOwnershipStake(Investment investment) {
        Project project = investment.getProject();
        if (project.getTotalInvestment() == null || project.getTotalInvestment() == 0.0) {
            return 0.0; // Handle division by zero
        }
        return (investment.getAmount() / project.getTotalInvestment()) * project.getEquityOffered();
    }
}
