package com.stefan.egovernmentapp.utils;

import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.models.Role;
import com.stefan.egovernmentapp.models.User;
import com.stefan.egovernmentapp.repositories.ResidentRepository;
import com.stefan.egovernmentapp.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor

@Component
public class JwtUtil {
    private final UserRepository userRepository;
    private final ResidentRepository residentRepository;

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

    public Optional<Resident> findResidentByToken(String token) {
        Optional<User> optionalUser = userRepository.findByEmailAddress(extractEmailAddress(token.substring(7)));
        return optionalUser.flatMap(user -> residentRepository.findByUser_Id((user.getId())));
    }

    public Optional<User> findUserByToken(String token) {
        return userRepository.findByEmailAddress(extractEmailAddress(token.substring(7)));
    }
}