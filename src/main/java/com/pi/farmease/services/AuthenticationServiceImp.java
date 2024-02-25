package com.pi.farmease.services;


import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.dao.WalletRepository;
import com.pi.farmease.dto.requests.AuthenticationRequest;
import com.pi.farmease.dto.requests.RegisterRequest;
import com.pi.farmease.dto.responses.AuthenticationResponse;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.Wallet;
import com.pi.farmease.entities.enumerations.WalletStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@AllArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

    private final UserRepository userRepository ;
    private final WalletRepository walletRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtService jwtService ;
    private final AuthenticationManager authenticationManager ;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .registrationDate(new Date())
                .enabled(true)
                .build() ;
        var wallet = Wallet.builder().user(user).ownerName(user.getFirstname()+" "+user.getLastname())
                .status(WalletStatus.ACTIVE).build() ;
        user.setWallet(wallet);
        userRepository.save(user) ;
        walletRepository.save(wallet) ;
        var jwtToken = jwtService.generateToken(user) ;
        var refreshToken = jwtService.generateRefreshToken(user) ;

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build() ;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        ) ;
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow() ;
        var jwtToken = jwtService.generateToken(user) ;
        var refreshToken = jwtService.generateRefreshToken(user) ;
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build() ;
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, String refreshToken) throws IOException {

        final String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var newAccessToken = jwtService.generateToken(user);
                var newRefreshToken = jwtService.generateRefreshToken(user);
                return  AuthenticationResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
            }
        }
        else throw new IOException("token not valid") ;
        return null;
    }

    public void logout() {
        SecurityContextHolder.clearContext();

    }

}
