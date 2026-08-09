package com.mercohuevos.auth.controller;

import org.springframework.web.bind.annotation.*;
import com.mercohuevos.auth.dto.*;
import com.mercohuevos.auth.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService service;

    @GetMapping("/verificar-dni/{dni}")
    public VerificarDniResponseDTO verificarDni(@PathVariable String dni) {
        return service.verificarDni(dni);
    }

    @PostMapping("/crear-password")
    public void crearPassword(@Valid @RequestBody CrearPasswordRequestDTO request) {
        service.crearPassword(request);
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return service.login(request);
    }

    @PostMapping("/refresh")
    public TokenResponseDTO refrescar(@Valid @RequestBody RefreshRequestDTO request) {
        return service.refrescar(request);
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody RefreshRequestDTO request) {
        service.logout(request.refreshToken());
    }
}