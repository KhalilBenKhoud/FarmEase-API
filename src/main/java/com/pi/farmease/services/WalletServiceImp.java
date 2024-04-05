package com.pi.farmease.services;

import com.pi.farmease.dao.WalletRepository;
import com.pi.farmease.entities.Wallet;
import com.pi.farmease.entities.enumerations.Role;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImp implements WalletService{

    private final UserService userService ;
    private final WalletRepository walletRepository ;
    @Scheduled(fixedDelay = 1000*5)
    @Override
    public void addInterest() {

        List<Wallet> listOfWallets = walletRepository.findAll() ;

        for(Wallet wallet : listOfWallets) {
            // newBalance = current balance * (1 + (interestRate / compoundingFrequency));
            double newBalance = wallet.getBalance() * (1 + 0.005 / 12) ;
            wallet.setBalance(newBalance);
        }

    }

    public double CalculateWealthDistributionIndex() {
        // Wealth Distribution Index = (Wealthiest Segment Balance - Poorest Segment Balance) / Total Wallet

        double poorest = walletRepository.findAll().stream()
                .map(Wallet::getBalance)
                .min(Double::compare).orElse(0.0) ;
        double wealthiest =  walletRepository.findAll().stream()
                .map(Wallet::getBalance)
                .max(Double::compare).orElse(0.0) ;

        double total = walletRepository.findAll().stream()
                .map(Wallet::getBalance).reduce(0.0, Double::sum) ;

       double index = (wealthiest - poorest ) / total ;
       return index ;
    }
}
