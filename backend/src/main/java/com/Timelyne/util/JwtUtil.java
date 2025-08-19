package com.Timelyne.util;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "mySuperSecretKey123";  // ❗ Don't leave this empty
    private final long expiration = 86400000; // 1 day in milliseconds

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)  // sign using the secret
                .compact();
    }
}
