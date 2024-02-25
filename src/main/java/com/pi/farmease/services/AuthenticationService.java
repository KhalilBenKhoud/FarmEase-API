package com.pi.farmease.services;

import com.pi.farmease.dto.requests.AuthenticationRequest;
import com.pi.farmease.dto.requests.RegisterRequest;
import com.pi.farmease.dto.responses.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request) ;
    public AuthenticationResponse authenticate(AuthenticationRequest request) ;
    public AuthenticationResponse refreshToken(HttpServletRequest request, String refreshToken) throws IOException ;

    public void logout() ;
}
