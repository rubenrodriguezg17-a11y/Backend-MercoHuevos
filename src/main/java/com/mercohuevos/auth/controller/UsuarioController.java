package com.mercohuevos.auth.controller;

import com.mercohuevos.auth.annotation.RequireAdmin;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequireAdmin
public class UsuarioController {

    private final IUsuarioService service;

    @PostMapping
    public UsuarioDTO crear(@Valid @RequestBody CrearUsuarioRequestDTO request) {
        return service.crear(request);
    }

    @GetMapping
    public List<UsuarioDTO> listarTodos() {
        return service.listarTodos();
    }

    @PatchMapping("/{id}/desbloquear")
    public UsuarioDTO desbloquear(@PathVariable Long id) {
        return service.desbloquear(id);
    }
}