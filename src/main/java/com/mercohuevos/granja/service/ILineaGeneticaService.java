package com.mercohuevos.granja.service;

import java.util.List;

import com.mercohuevos.granja.dto.LineaGeneticaDTO;
import com.mercohuevos.granja.dto.LineaGeneticaRequestDTO;

public interface ILineaGeneticaService {
    LineaGeneticaDTO crear(LineaGeneticaRequestDTO request);
    LineaGeneticaDTO obtenerPorId(Long id);
    List<LineaGeneticaDTO> listarTodos();
    LineaGeneticaDTO editar(Long id, LineaGeneticaRequestDTO request);
    void desactivar(Long id);
}