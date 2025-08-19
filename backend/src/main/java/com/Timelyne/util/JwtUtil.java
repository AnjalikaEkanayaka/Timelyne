package com.Timelyne.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.security.Key;

@Component
public class JwtUtil {

    private String secret = "mysecretkeymysecretkeymysecretkey"; // must be at least 32 chars for HS256
    private long expiration = 86400000; // 1 day in ms

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) 
                .setIssuedAt(new Date()) 
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) 
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) 
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true; 
        } catch (Exception e) {
            return false; 
        }
    }

    public String extractEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); 
    }
}
