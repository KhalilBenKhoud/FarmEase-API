package com.pi.farmease.services;


import com.pi.farmease.dao.ResetPasswordTokenRepository;
import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.dao.VerifyAccountTokenRepository;
import com.pi.farmease.dao.WalletRepository;
import com.pi.farmease.dto.requests.AuthenticationRequest;
import com.pi.farmease.dto.requests.RegisterRequest;
import com.pi.farmease.dto.responses.AuthenticationResponse;
import com.pi.farmease.entities.ResetPasswordToken;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.VerifyAccountToken;
import com.pi.farmease.entities.Wallet;
import com.pi.farmease.entities.enumerations.WalletStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImp implements AuthenticationService {

    private final UserRepository userRepository ;
    private final WalletRepository walletRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtService jwtService ;
    private final AuthenticationManager authenticationManager ;
    private final  JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository ;
    private final VerifyAccountTokenRepository verifyAccountTokenRepository ;

    @Value("${BASE_API_URL}")
    private String baseApiUrl ;


    public void register(RegisterRequest request) throws MessagingException {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .registrationDate(new Date())
                .enabled(false)
                .build() ;
        var wallet = Wallet.builder().user(user).ownerName(user.getFirstname()+" "+user.getLastname()).balance(1000)
                .status(WalletStatus.ACTIVE).build() ;
        user.setWallet(wallet);
        userRepository.save(user) ;
        walletRepository.save(wallet) ;
        sendVerifyAccountEmail(user.getEmail());
    }

    public String createVerifyAccountToken( User concernedUser ) {
        verifyAccountTokenRepository.removeAllByUser(concernedUser) ;
        UUID uuid = UUID.randomUUID();
        String tokenValue = uuid.toString() ;
        VerifyAccountToken token = VerifyAccountToken.builder()
                .token(tokenValue)
                .expiryDateTime(LocalDateTime.now().plusDays(1))
                .user(concernedUser)
                .build() ;
        verifyAccountTokenRepository.save(token) ;
        return token.getToken() ;

    }

    public void sendVerifyAccountEmail(String email) throws  MessagingException {


        InternetAddress recipientAddress = new InternetAddress();
        email = email.trim() ;
        recipientAddress.setAddress(email);
        User concernedUser = userRepository.findByEmail(email).orElse(null) ;
        if(concernedUser == null) throw new RuntimeException("problem in registration !") ;
        if(concernedUser.isEnabled()) throw new RuntimeException("user is already verified, proceed to login !") ;
        String fullName = concernedUser.getFirstname() + " " + concernedUser.getLastname() ;
        String verifyToken = createVerifyAccountToken(concernedUser) ;
        String link = baseApiUrl + "/api/v1/auth/verifyAccount?token=" + verifyToken ;
        Context context = new Context();
        context.setVariable("fullName",fullName);
        context.setVariable("link",link);
        String processedHTMLTemplate = templateEngine.process("verifyAccountEmail",context) ;


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(new InternetAddress("noreply@farmease.com"));
        mimeMessageHelper.setTo(recipientAddress);
        mimeMessageHelper.setSubject("verify account");
        mimeMessageHelper.setText(processedHTMLTemplate, true);

        javaMailSender.send(mimeMessage);
    }


    public void verifyAccount(String token) {
        VerifyAccountToken givenToken = verifyAccountTokenRepository.findByToken(token).orElse(null) ;
        if(givenToken != null) {
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(givenToken.getExpiryDateTime())) throw new RuntimeException("it has been more than a day, request another verification email !")  ;
            User concernedUser = givenToken.getUser() ;
            concernedUser.setEnabled(true);


        }  else {
            throw new RuntimeException("there was a problem verifying the user, request another verification email !") ;
        }
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

    @Override
    public void forgetPassword(String email) throws MessagingException {




        InternetAddress recipientAddress = new InternetAddress();
        email = email.trim() ;
        recipientAddress.setAddress(email);
        User concernedUser = userRepository.findByEmail(email).orElse(null) ;
        if(concernedUser == null) throw new RuntimeException("no user associated with the provided email") ;
        String fullName = concernedUser.getFirstname() + " " + concernedUser.getLastname() ;
        String resetToken = createResetPasswordToken(concernedUser) ;
        Context context = new Context();
        context.setVariable("fullName",fullName);
        context.setVariable("resetToken",resetToken);
        String processedHTMLTemplate = templateEngine.process("forgetPasswordEmail",context) ;


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(new InternetAddress("noreply@farmease.com"));
        mimeMessageHelper.setTo(recipientAddress);
        mimeMessageHelper.setSubject("forgot password ?");
        mimeMessageHelper.setText(processedHTMLTemplate, true);

        javaMailSender.send(mimeMessage);
    }


    public String createResetPasswordToken( User concernedUser ) {
        resetPasswordTokenRepository.removeAllByUser(concernedUser) ;
        UUID uuid = UUID.randomUUID();
        String tokenValue = uuid.toString().substring(0,6) ;
        ResetPasswordToken token = ResetPasswordToken.builder()
                .token(tokenValue)
                .expiryDateTime(LocalDateTime.now().plusMinutes(2))
                .user(concernedUser)
                .build() ;
       resetPasswordTokenRepository.save(token) ;
       return token.getToken() ;
    }

    public User verifyResetPasswordToken(String token) {
        ResetPasswordToken givenToken = resetPasswordTokenRepository.findByToken(token).orElse(null) ;
        if(givenToken != null) {
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(givenToken.getExpiryDateTime())) throw new RuntimeException("code expired ! try again !")  ;
            else  return givenToken.getUser() ;
        }
        else {
            throw new RuntimeException("the code you provided is wrong or overridden") ;
        }
    }

    public void resetPassword(String newPassword, String token) {
        User concernedUser =  verifyResetPasswordToken(token) ;
        concernedUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(concernedUser) ;
    }

}
