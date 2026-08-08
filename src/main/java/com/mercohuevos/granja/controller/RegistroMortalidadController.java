package com.mercohuevos.granja.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireGranja;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.granja.dto.RegistroMortalidadRequestDTO;
import com.mercohuevos.granja.dto.RegistroMortalidadResponseDTO;
import com.mercohuevos.granja.service.IRegistroMortalidadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/granja/mortalidad")
@RequiredArgsConstructor
@RequireGranja
public class RegistroMortalidadController {

    private final IRegistroMortalidadService mortalidadService;

    @PostMapping
    public ResponseEntity<RegistroMortalidadResponseDTO> registrar(
            @Valid
            @RequestBody RegistroMortalidadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mortalidadService.registrar(request));
    }

    @GetMapping("/lote/{idLote}")
    public ResponseEntity<List<RegistroMortalidadResponseDTO>> listarPorLote(@PathVariable Long idLote) {
        return ResponseEntity.ok(mortalidadService.listarPorLote(idLote));
    }
}