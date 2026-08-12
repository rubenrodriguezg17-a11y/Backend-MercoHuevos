package com.mercohuevos.granja.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.granja.dto.LoteDTO;
import com.mercohuevos.granja.dto.LoteRequestDTO;
import com.mercohuevos.granja.enums.EstadoLote;
import com.mercohuevos.granja.mapper.ILoteMapper;
import com.mercohuevos.granja.model.LineaGenetica;
import com.mercohuevos.granja.model.Lote;
import com.mercohuevos.granja.repository.ILineaGeneticaRepository;
import com.mercohuevos.granja.repository.ILoteRepository;
import com.mercohuevos.granja.repository.IRegistroMortalidadRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoteImpl implements ILoteService {

    private final ILoteRepository loteRepo;
    private final ILineaGeneticaRepository lineaGeneticaRepo;
    private final IRegistroMortalidadRepository mortalidadRepo;
    private final ILoteMapper loteMapper;

    @Override
    public LoteDTO crear(LoteRequestDTO request) {
        LineaGenetica lineaGenetica = buscarLineaGenetica(request.idLineaGenetica());

        Lote lote = new Lote();
        lote.setCodigoLote(request.codigoLote());
        lote.setLineaGenetica(lineaGenetica);
        lote.setFechaIngreso(request.fechaIngreso());
        lote.setCantidadAvesInicial(request.cantidadAvesInicial());
        lote.setGalpon(request.galpon());
        lote.setEstado(EstadoLote.valueOf(request.estado()));

        return construirDTOCompleto(loteRepo.save(lote));
    }

    @Override
    public LoteDTO obtenerPorId(Long id) {
        return construirDTOCompleto(buscarLote(id));
    }

    @Override
    public List<LoteDTO> listarTodos() {
        return loteRepo.findAll().stream()
                .map(this::construirDTOCompleto)
                .toList();
    }

    @Override
    public LoteDTO editar(Long id, LoteRequestDTO request) {
        Lote lote = buscarLote(id);
        LineaGenetica lineaGenetica = buscarLineaGenetica(request.idLineaGenetica());

        lote.setCodigoLote(request.codigoLote());
        lote.setLineaGenetica(lineaGenetica);
        lote.setFechaIngreso(request.fechaIngreso());
        lote.setCantidadAvesInicial(request.cantidadAvesInicial());
        lote.setGalpon(request.galpon());
        lote.setEstado(EstadoLote.valueOf(request.estado()));

        return construirDTOCompleto(loteRepo.save(lote));
    }

    @Override
    public void darDeBaja(Long id) {
        Lote lote = buscarLote(id);
        lote.setEstado(EstadoLote.DE_BAJA);
        loteRepo.save(lote);
    }

    @Override
    public Integer calcularPoblacionActual(Long idLote) {
        Lote lote = buscarLote(idLote);
        return lote.getCantidadAvesInicial() - obtenerMortalidadAcumulada(lote);
    }

    private LoteDTO construirDTOCompleto(Lote lote) {
        LoteDTO base = loteMapper.toDTO(lote);
        int totalMortalidad = obtenerMortalidadAcumulada(lote);
        int poblacionActual = lote.getCantidadAvesInicial() - totalMortalidad;

        return new LoteDTO(
                base.idLote(),
                base.codigoLote(),
                base.idLineaGenetica(),
                base.lineaGeneticaNombre(),
                base.fechaIngreso(), base.cantidadAvesInicial(), base.galpon(), base.estado(),
                totalMortalidad, poblacionActual
        );
    }

    private int obtenerMortalidadAcumulada(Lote lote) {
        return mortalidadRepo.sumMortalidadHastaFecha(lote, LocalDate.now());
    }

    private Lote buscarLote(Long id) {
        return loteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lote no encontrado: " + id));
    }

    private LineaGenetica buscarLineaGenetica(Long id) {
        return lineaGeneticaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Linea genetica no encontrada: " + id));
    }
}