package com.mercohuevos.auth.controller;

import com.mercohuevos.auth.annotation.RequireAdmin;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.mercohuevos.auth.dto.CrearUsuarioRequestDTO;
import com.mercohuevos.auth.dto.UsuarioDTO;
import com.mercohuevos.auth.service.IUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService service;

    @PostMapping
    @RequireAdmin
    public UsuarioDTO crear(@Valid @RequestBody CrearUsuarioRequestDTO request) {
        return service.crear(request);
    }

    @GetMapping
    @RequireAdmin
    public List<UsuarioDTO> listarTodos() {
        return service.listarTodos();
    }

    @PatchMapping("/{id}/desbloquear")
    @RequireAdmin
    public UsuarioDTO desbloquear(@PathVariable Long id) {
        return service.desbloquear(id);
    }

    @GetMapping("/me")
    public UsuarioDTO me(Authentication authentication) {
        String dni = authentication.getName();
        return service.obtenerPorDni(dni);
    }
}