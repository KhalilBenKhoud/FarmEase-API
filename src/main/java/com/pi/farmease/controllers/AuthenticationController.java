package com.pi.farmease.controllers;

import com.pi.farmease.dto.requests.AuthenticationRequest;
import com.pi.farmease.dto.requests.RegisterRequest;
import com.pi.farmease.dto.responses.AuthenticationResponse;
import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.User;
import com.pi.farmease.services.AuthenticationService;
import com.pi.farmease.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService ;
    private final UserService userService ;

    @GetMapping("/test")
    public String test() {
        return "connected" ;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
       final AuthenticationResponse responseBody ;
       final ResponseCookie jwtCookie ;
        try {
            responseBody = authenticationService.register(request) ;

           jwtCookie = ResponseCookie
                    .from("jwtCookie", responseBody.getRefreshToken())
                    .path(  "/api/v1/auth/refresh_token").maxAge(7 * 24 * 60 * 60)
                    .httpOnly(true).build();

            responseBody.setRefreshToken("hi ! you can find me in the cookie");

        }
        catch(Exception e) {
           return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(responseBody);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
         AuthenticationResponse responseBody ;
        try {
             responseBody = authenticationService.authenticate(request);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        ResponseCookie jwtCookie = ResponseCookie
                .from("jwtCookie", responseBody.getRefreshToken())
                .path("/api/v1/auth/refresh_token").maxAge(7 * 24 * 60 * 60)
                .httpOnly(true).build();
        responseBody.setRefreshToken("hi ! you can find me in the cookie");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(responseBody);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponse> refresh_token(HttpServletRequest request) throws IOException {
        Cookie cookie = WebUtils.getCookie(request, "jwtCookie");
        final String refreshToken ;
        if(cookie != null)
        {
            refreshToken = cookie.getValue() ;
            System.out.println(refreshToken);
            AuthenticationResponse responseBody = authenticationService.refreshToken(request,refreshToken) ;
            ResponseCookie jwtCookie = ResponseCookie
                    .from("jwtCookie", responseBody.getRefreshToken())
                    .path("/api/v1/auth/refresh_token").maxAge(7 * 24 * 60 * 60)
                    .httpOnly(true).build();
            responseBody.setRefreshToken("hi ! you can find me in the cookie");

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(responseBody);
        }
        else throw new IOException("no cookie found, you are logged out") ;

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout()
    {
        authenticationService.logout();
        final String newCookieValue = "you_are_logged_out" ;
        ResponseCookie cookie = ResponseCookie.from("jwtCookie", newCookieValue).path("/api/v1/auth/refresh_token").maxAge(10)
                .httpOnly(true).build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));


    }



}
