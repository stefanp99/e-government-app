package com.stefan.egovernmentapp.utils;

import com.stefan.egovernmentapp.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = Jwts.SIG.HS256.key().toString();
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String emailAddress, Role role) {
        return Jwts.builder()
                .subject(emailAddress)
                .claim("role", role.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmailAddress(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}