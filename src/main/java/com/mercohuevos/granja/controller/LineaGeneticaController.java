package com.mercohuevos.granja.controller;

import java.util.List;

import com.mercohuevos.auth.annotation.RequireGranja;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercohuevos.granja.dto.LineaGeneticaDTO;
import com.mercohuevos.granja.dto.LineaGeneticaRequestDTO;
import com.mercohuevos.granja.service.ILineaGeneticaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/granja/lineas-geneticas")
@RequiredArgsConstructor
public class LineaGeneticaController {
	
	private final ILineaGeneticaService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<LineaGeneticaDTO> obtenerPorId(@PathVariable Long id){
		return ResponseEntity.ok(service.obtenerPorId(id));
	}
	
	@GetMapping
	public ResponseEntity<List<LineaGeneticaDTO>> listarGeneticas(){
		return ResponseEntity.ok(service.listarTodos());
	}

	@RequireGranja
	@PostMapping
	public ResponseEntity<LineaGeneticaDTO> crearLineaGenetica(@Valid @RequestBody LineaGeneticaRequestDTO reques){
		return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(reques));
	}

	@RequireGranja
	@PutMapping("/{id}")
	public ResponseEntity<LineaGeneticaDTO> editar(
	        @PathVariable Long id, @Valid @RequestBody LineaGeneticaRequestDTO request) {
	    return ResponseEntity.ok(service.editar(id, request));
	}

	@RequireGranja
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> desactivar(@PathVariable Long id) {
	    service.desactivar(id);
	    return ResponseEntity.noContent().build();
	}
}
