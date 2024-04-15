package com.example.securityservice.util;

import com.example.securityservice.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class JwtGenerationUtils {

    @Value("${jwt.secret.access.key}")
    private String access;
    @Value("${jwt.secret.access.expiration}")
    private Long accessExpiration;

    public String generateToken(Map<String, Object> extraClaims, User user) {
        var expirationDate = calculateDate(accessExpiration);

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setExpiration(expirationDate)
                .signWith(getSignInKey(access), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date calculateDate(Long expiration) {
        var now = LocalDateTime.now();
        var expirationInstant = now.plusSeconds(expiration)
                .atZone(ZoneId.systemDefault()).toInstant();

        return Date.from(expirationInstant);
    }

    public Key getSignInKey(String key) {
        var keyBytes = Decoders.BASE64.decode(key);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
