package com.example.demoservice.util;

import com.example.demoservice.model.enumeration.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Slf4j
@Component
public class JwtDecodingUtils {

    @Value("${jwt.secret.access.key}")
    private String access;

    public Key getSignInKey(String key) {
        var keyBytes = Decoders.BASE64.decode(key);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, access, Claims::getSubject);
    }

    public Role extractRole(String token) {
        var claims = extractAllClaims(token, access);

        return Role.valueOf(claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, String key, Function<Claims, T> claimsResolver) {
        var claims = extractAllClaims(token, key);

        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token, String key) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        return isTokenValid(token, access);
    }

    private boolean isTokenValid(String token, String key) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey(key))
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }

        return false;
    }

}
