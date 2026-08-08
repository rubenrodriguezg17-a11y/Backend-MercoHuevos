package com.mercohuevos.auth.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercohuevos.auth.model.RefreshToken;
import com.mercohuevos.auth.model.Usuario;
import com.mercohuevos.auth.repository.IRefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final IRefreshTokenRepository repository;

    @Value("${mercohuevos.jwt.refresh-expiration-hours}")
    private long refreshExpirationHours;

    public RefreshToken crear(Usuario usuario) {
        RefreshToken token = new RefreshToken();
        token.setUsuario(usuario);
        token.setToken(UUID.randomUUID().toString());
        token.setFechaExpiracion(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS));
        return repository.save(token);
    }

    public RefreshToken validarYObtener(String tokenStr) {
        RefreshToken token = repository.findByTokenAndRevocadoFalse(tokenStr)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token invalido o revocado"));

        if (token.getFechaExpiracion().isBefore(Instant.now())) {
            throw new IllegalStateException("El refresh token expiro, vuelva a iniciar sesion");
        }
        return token;
    }

    public void revocar(String tokenStr) {
        repository.findByTokenAndRevocadoFalse(tokenStr).ifPresent(t -> {
            t.setRevocado(true);
            repository.save(t);
        });
    }
}