package com.pi.farmease.services;

import com.pi.farmease.dto.requests.AuthenticationRequest;
import com.pi.farmease.dto.requests.RegisterRequest;
import com.pi.farmease.dto.responses.AuthenticationResponse;
import com.pi.farmease.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface AuthenticationService {

    public void register(RegisterRequest request) throws MessagingException;
    public AuthenticationResponse authenticate(AuthenticationRequest request) ;
    public AuthenticationResponse refreshToken(HttpServletRequest request, String refreshToken) throws IOException ;

    public void logout() ;

    public void forgetPassword(String email) throws MessagingException;

    public String createResetPasswordToken(User concernedUser ) ;

    public String createVerifyAccountToken(User concernedUser ) ;

    public Object verifyResetPasswordToken(String token) ;

    public void resetPassword(String newPassword, String token) ;

    public void sendVerifyAccountEmail(String email) throws  MessagingException;

    public void verifyAccount(String token) ;
}
