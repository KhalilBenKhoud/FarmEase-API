package com.pi.farmease.services;

import com.pi.farmease.dao.ResetPasswordTokenRepository;
import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.dao.VerifyAccountTokenRepository;
import com.pi.farmease.entities.ResetPasswordToken;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.VerifyAccountToken;
import com.pi.farmease.entities.enumerations.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Locale.filter;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImp implements AdminUserService{

     private final UserRepository userRepository ;
    @Override
    public List<User> getAll()  {
        return  userRepository.findAll() ;
    }
    public List<User> getSortedByMoney() {
        List<User> list =  userRepository.findAll() ;
        Comparator<User> comparator = Comparator.comparing(user -> user.getWallet().getBalance());

        return list.stream().
                filter(user -> user.getRole() != Role.ADMIN)
                .sorted(comparator)
                .collect(Collectors.toList()) ;

    }

    @Override
    public void banUser(Integer id) {
        User concernedUser = userRepository.findById(id.longValue()).orElse(null) ;

        if(concernedUser == null)  throw new RuntimeException("user doesn't exist") ;
        if(!concernedUser.isEnabled()) throw new RuntimeException("user already banned") ;
        concernedUser.setEnabled(false);
        userRepository.save(concernedUser) ;

    }
    @Override
    public void permitUser(Integer id) {
        User concernedUser = userRepository.findById(id.longValue()).orElse(null) ;
        if(concernedUser == null)  throw new RuntimeException("user doesn't exist") ;
        if(concernedUser.isEnabled()) throw new RuntimeException("user already enabled") ;
        concernedUser.setEnabled(true);
        userRepository.save(concernedUser) ;
    }


    @Override
    public User userWithMostMoney() {
        return userRepository.findAll()
                .stream().filter(user -> user.getRole() != Role.ADMIN).max(Comparator.comparing(user ->  user.getWallet().getBalance()))
                .orElse(null) ;

    }
    public List<User> usersSortedByRegistrationDate()  {
        return userRepository.findAll().stream().filter(user -> user.getRole() != Role.ADMIN)
                .sorted(Comparator.comparing(User::getRegistrationDate).reversed())
                .collect(Collectors.toList()) ;

    }

}
