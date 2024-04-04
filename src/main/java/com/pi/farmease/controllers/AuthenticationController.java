package com.pi.farmease.controllers;

import com.pi.farmease.dto.requests.AuthenticationRequest;
import com.pi.farmease.dto.requests.RegisterRequest;
import com.pi.farmease.dto.requests.ResetPasswordRequest;
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


    @GetMapping("/test")
    public String test() {
        return "connected" ;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {
             authenticationService.register(request) ;
        }
        catch(Exception e) {
           return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok()
                .body(new MessageResponse("check your email to verify your account !"));
    }

    @PostMapping("/resendVerification")
    public ResponseEntity<?> resendVerification(@RequestBody String email) {

        try {
            authenticationService.sendVerifyAccountEmail(email); ;
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok()
                .body(new MessageResponse("check your email to verify your account !"));
    }

    @GetMapping("/verifyAccount")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String verifyToken) {
        try {
            authenticationService.verifyAccount(verifyToken) ;
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok()
                .body("<div style='width:100% ; height : 100vh ;  display :flex ; gap : 20px ; justify-content : center ; align-items : center ; flex-direction : column ; '>" +
                          "<p style='font-weight : 1000;'>your account is verified ! </p>"
                         + "<img style='width : 150px ; height : 150px;' src='https://cdn0.iconfinder.com/data/icons/seo-web-4-1/128/Vigor_Approved-Verified-Account-User-512.png' alt='icon'/>"
                         + "</div>");

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
                .httpOnly(true)
                .build();
        responseBody.setRefreshToken("hi ! you can find me in the cookie");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(responseBody);
    }

    @GetMapping("/refresh_token")
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
                    .httpOnly(true)
                    .build();
            responseBody.setRefreshToken("hi ! you can find me in the cookie");

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(responseBody);
        }
        else throw new IOException("no cookie found, you are logged out") ;

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout()
    {
        ResponseCookie cookie ;
        try {
            authenticationService.logout();
            final String newCookieValue = "you_are_logged_out";
              cookie = ResponseCookie.from("jwtCookie", newCookieValue).path("/api/v1/auth/refresh_token").maxAge(10)
                    .httpOnly(true).build();
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));


    }



    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody String email) {
        try {
            authenticationService.forgetPassword(email);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok()
                .body(new MessageResponse("reset password code sent ! check your email!"));

    }

    @PostMapping("/verify-forget-token")
    public ResponseEntity<?> verifyForgetToken(@RequestBody String code) {
        User user ;
        try {
            user = (User) authenticationService.verifyResetPasswordToken(code) ;
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok()
                .body(user);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        try {
           authenticationService.resetPassword(request.getNewPassword(),request.getToken());
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok()
                .body(new MessageResponse("password updated successfully"));
    }




}
