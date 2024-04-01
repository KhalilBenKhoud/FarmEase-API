package com.pi.farmease.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImp implements JwtService{
    @Value("${SIGN_IN_KEY}")
    private String SECRET_KEY ;
    private long accessExpiration = 1000*60*15; // in millis : 15 minute
    private long refreshExpiration = 1000*60*60*24*7 ; // on millis : 7 days

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token) ;
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) ;
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date()) ;
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token) ;
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject) ;

    }


    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails) ;
    }

    public String generateToken(   Map<String, Object> extraClaims,
                                   UserDetails userDetails)
    {
        return buildToken(extraClaims,userDetails,accessExpiration) ;
    }

    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration ))
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact() ;
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody() ;
    }

    public Key getSignInkey() {
        System.out.println("secret key : " + SECRET_KEY);
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY) ;
        return Keys.hmacShaKeyFor(keyBytes) ;
    }

}
