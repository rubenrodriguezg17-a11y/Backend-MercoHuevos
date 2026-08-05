// plantaincubacion/controller/EmbandejadoGeneralController.java
package com.mercohuevos.plantaincubacion.recepcion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.recepcion.dto.EmbandejadoGeneralRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.EmbandejadoGeneralResponseDTO;
import com.mercohuevos.plantaincubacion.recepcion.service.IEmbandejadoGeneralService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/embandejado")
@RequiredArgsConstructor
public class EmbandejadoGeneralController {

    private final IEmbandejadoGeneralService embandejadoService;

    @PostMapping
    public ResponseEntity<EmbandejadoGeneralResponseDTO> guardar(@Valid @RequestBody EmbandejadoGeneralRequestDTO request) {
        return ResponseEntity.ok(embandejadoService.guardar(request));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<EmbandejadoGeneralResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(embandejadoService.confirmar(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmbandejadoGeneralResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(embandejadoService.obtenerPorId(id));
    }
}