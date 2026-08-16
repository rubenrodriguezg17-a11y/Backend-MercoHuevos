package com.mercohuevos.plantaincubacion.recepcion.controller;

import com.mercohuevos.auth.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.recepcion.dto.EmbandejadoGeneralRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.EmbandejadoGeneralResponseDTO;
import com.mercohuevos.plantaincubacion.recepcion.service.IEmbandejadoGeneralService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/plantaincubacion/embandejado")
@RequiredArgsConstructor
public class EmbandejadoGeneralController {

    private final IEmbandejadoGeneralService embandejadoService;

    @RequireEmbandejado
    @PostMapping
    public ResponseEntity<EmbandejadoGeneralResponseDTO> guardar(@Valid @RequestBody EmbandejadoGeneralRequestDTO request) {
        return ResponseEntity.ok(embandejadoService.guardar(request));
    }
    @RequireEmbandejado
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<EmbandejadoGeneralResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(embandejadoService.confirmar(id));
    }
    @RequireEmbandejado
    @GetMapping("/{id}")
    public ResponseEntity<EmbandejadoGeneralResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(embandejadoService.obtenerPorId(id));
    }

    @RequireLecturaEmbandejado
    @GetMapping
    public ResponseEntity<List<EmbandejadoGeneralResponseDTO>> listarTodos() {
        return ResponseEntity.ok(embandejadoService.listarTodos());
    }

    @RequireEscrituraEmbandejado
    @PutMapping("/{id}")
    public ResponseEntity<EmbandejadoGeneralResponseDTO> editar(
            @PathVariable Long id, @Valid @RequestBody EmbandejadoGeneralRequestDTO request) {
        return ResponseEntity.ok(embandejadoService.editar(id, request));
    }
}