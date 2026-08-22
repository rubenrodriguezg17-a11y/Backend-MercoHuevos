package com.mercohuevos.plantaincubacion.vacunacion.controller;

import java.util.List;
import com.mercohuevos.auth.annotation.RequireLogistica;
import com.mercohuevos.plantaincubacion.vacunacion.dto.DetalleVacunacionInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercohuevos.plantaincubacion.vacunacion.dto.OrdenVacunacionResponseDTO;
import com.mercohuevos.plantaincubacion.vacunacion.dto.RegistrarOrdenVacunacionRequestDTO;
import com.mercohuevos.plantaincubacion.vacunacion.service.IOrdenVacunacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantaincubacion/cargas/{idCarga}/vacunacion")
@RequiredArgsConstructor
@RequireLogistica
public class OrdenVacunacionController {

    private final IOrdenVacunacionService service;

    @PostMapping
    public ResponseEntity<OrdenVacunacionResponseDTO> registrar(
            @PathVariable Long idCarga, @Valid @RequestBody RegistrarOrdenVacunacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(idCarga, request));
    }

    @GetMapping
    public ResponseEntity<List<OrdenVacunacionResponseDTO>> listarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarPorCarga(idCarga));
    }

    @GetMapping("/detalle/{idDetalleVacunacion}")
    public ResponseEntity<DetalleVacunacionInfoDTO> obtenerDetallePorId(
            @PathVariable Long idCarga, @PathVariable Long idDetalleVacunacion) {
        return ResponseEntity.ok(service.obtenerDetallePorId(idDetalleVacunacion));
    }

    @GetMapping("/detalles")
    public ResponseEntity<List<DetalleVacunacionInfoDTO>> listarDetallesPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.listarDetallesPorCarga(idCarga));
    }
}