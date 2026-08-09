package com.mercohuevos.auth.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    @Value("${mercohuevos.jwt.secret}")
    private String secret;

    @Value("${mercohuevos.jwt.access-expiration-ms}")
    private long accessExpirationMs;

    /**
     * Genera el access token incluyendo el rol y, si corresponde, el area
     * del usuario (GRANJA / PLANTA_INCUBACION). El area puede ser null
     * (por ejemplo para ADMIN, que no esta atado a una sola area).
     */
    public String generateAccessToken(String dni, String rol, String area) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        if (area != null) {
            claims.put("area", area);
        }

        return Jwts.builder()
                .claims(claims)
                .subject(dni)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(getSignKey())
                .compact();
    }

    public String extraerDni(String token) {
        return parseClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return parseClaims(token).get("rol", String.class);
    }

    /**
     * Devuelve el area del usuario contenida en el token, o null si no
     * tiene una area asignada (ej. ADMIN).
     */
    public String extraerArea(String token) {
        return parseClaims(token).get("area", String.class);
    }

    public boolean esValido(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(getSignKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}