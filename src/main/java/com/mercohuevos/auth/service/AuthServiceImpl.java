package com.mercohuevos.auth.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mercohuevos.auth.dto.*;
import com.mercohuevos.auth.model.RefreshToken;
import com.mercohuevos.auth.model.Usuario;
import com.mercohuevos.auth.repository.IUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private static final int MAX_INTENTOS_FALLIDOS = 10;

    private final IUsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public VerificarDniResponseDTO verificarDni(String dni) {
        return usuarioRepo.findByDni(dni)
                .map(u -> new VerificarDniResponseDTO(true, u.isPasswordConfigurada(), u.getNombreCompleto()))
                .orElse(new VerificarDniResponseDTO(false, false, null));
    }

    @Override
    @Transactional
    public void crearPassword(CrearPasswordRequestDTO request) {
        Usuario usuario = usuarioRepo.findByDni(request.dni())
                .orElseThrow(() -> new IllegalArgumentException("DNI no encontrado, contacte al administrador"));

        if (usuario.isPasswordConfigurada()) {
            throw new IllegalStateException("Este usuario ya tiene una contrasena configurada");
        }

        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setPasswordConfigurada(true);
        usuarioRepo.save(usuario);
    }

    @Override
    @Transactional
    public TokenResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepo.findByDni(request.dni())
                .orElseThrow(() -> new IllegalArgumentException("DNI o contrasena incorrectos"));

        if (!usuario.isPasswordConfigurada()) {
            throw new IllegalStateException("Debe crear su contrasena antes de iniciar sesion");
        }
        if (!usuario.isActivo()) {
            throw new IllegalStateException("Usuario inactivo, contacte al administrador");
        }
        if (usuario.isBloqueado()) {
            throw new IllegalStateException("Usuario bloqueado por intentos fallidos, contacte al administrador");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.dni(), request.password()));
        } catch (BadCredentialsException e) {
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
            if (usuario.getIntentosFallidos() >= MAX_INTENTOS_FALLIDOS) {
                usuario.setBloqueado(true);
            }
            usuarioRepo.save(usuario);
            throw new IllegalArgumentException("DNI o contrasena incorrectos");
        }

        usuario.setIntentosFallidos(0);
        usuarioRepo.save(usuario);

        String accessToken = jwtService.generateAccessToken(
                usuario.getDni(),
                usuario.getRol().name(),
                usuario.getArea() != null ? usuario.getArea().name() : null);
        RefreshToken refreshToken = refreshTokenService.crear(usuario);

        return new TokenResponseDTO(
                accessToken, refreshToken.getToken(),
                usuario.getNombreCompleto(),
                usuario.getRol().name(),
                usuario.getArea() != null ? usuario.getArea().name() : null);
    }

    @Override
    @Transactional
    public TokenResponseDTO refrescar(RefreshRequestDTO request) {
        RefreshToken refreshToken = refreshTokenService.validarYObtener(request.refreshToken());
        Usuario usuario = refreshToken.getUsuario();

        String nuevoAccessToken = jwtService.generateAccessToken(
                usuario.getDni(),
                usuario.getRol().name(),
                usuario.getArea() != null ? usuario.getArea().name() : null);

        return new TokenResponseDTO(
                nuevoAccessToken,
                refreshToken.getToken(),
                usuario.getNombreCompleto(),
                usuario.getRol().name(),
                usuario.getArea() != null ? usuario.getArea().name() : null);
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revocar(refreshToken);
    }
}