package com.mercohuevos.granja.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mercohuevos.granja.dto.*;
import com.mercohuevos.granja.enums.EstadoReporte;
import com.mercohuevos.granja.mapper.IDetalleLoteReporteMapper;
import com.mercohuevos.granja.mapper.IReporteTrasladoMapper;
import com.mercohuevos.granja.model.*;
import com.mercohuevos.granja.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteTrasladoImpl implements IReporteTrasladoService {

    private final IReporteTrasladoRepository reporteRepo;
    private final ILoteRepository loteRepo;
    private final ITipoHuevoRepository tipoHuevoRepo;
    private final IRegistroMortalidadRepository mortalidadRepo;
    private final IReporteTrasladoMapper reporteMapper;
    private final IDetalleLoteReporteMapper detalleMapper;

    @Override
    public ReporteTrasladoResponseDTO crear(ReporteTrasladoRequestDTO request) {

        validarLotesSinDuplicados(request.detalles());

        LocalDate fechaReporte = (request.fecha() != null) ? request.fecha() : LocalDate.now();

        validarFechaEnRango(fechaReporte);
        validarFechaNoRepetida(fechaReporte, null);

        ReporteTraslado reporte = new ReporteTraslado();
        reporte.setFecha(fechaReporte);
        reporte.setHora(request.hora());
        reporte.setChofer(request.chofer());
        reporte.setPlaca(request.placa());
        reporte.setEncargadoGranja(request.encargadoGranja());
        reporte.setVeterinarioResponsable(request.veterinarioResponsable());
        reporte.setObservaciones(request.observaciones());
        reporte.setEstado(EstadoReporte.PENDIENTE);

        reporte.setNumeroReporte(
                (request.numeroReporte() == null || request.numeroReporte().isBlank())
                        ? generarNumero()
                        : request.numeroReporte()
        );

        List<DetalleLoteReporte> detalles = request.detalles().stream()
                .map(detalleReq -> construirDetalle(detalleReq, fechaReporte, reporte))
                .toList();

        reporte.setDetalles(detalles);

        ReporteTraslado guardado = reporteRepo.save(reporte);
        return construirResponseCompleto(guardado);
    }

    @Override
    public ReporteTrasladoResponseDTO obtenerPorId(Long id) {
        return construirResponseCompleto(buscarReporte(id));
    }

    @Override
    public List<ReporteTrasladoResponseDTO> listarTodos() {
        return reporteRepo.findAll().stream()
                .map(this::construirResponseCompleto)
                .toList();
    }

    @Override
    public ReporteTrasladoResponseDTO editar(Long id, ReporteTrasladoRequestDTO request) {

        ReporteTraslado reporte = buscarReporte(id);

        if (reporte.getEstado() != EstadoReporte.PENDIENTE) {
            throw new IllegalStateException(
                    "El reporte no puede editarse porque su estado es " + reporte.getEstado() +
                    ". Solo los reportes PENDIENTE son editables.");
        }

        validarLotesSinDuplicados(request.detalles());

        LocalDate fechaReporte = (request.fecha() != null) ? request.fecha() : reporte.getFecha();

        validarFechaEnRango(fechaReporte);
        validarFechaNoRepetida(fechaReporte, id);

        reporte.setFecha(fechaReporte);
        reporte.setHora(request.hora());
        reporte.setChofer(request.chofer());
        reporte.setPlaca(request.placa());
        reporte.setEncargadoGranja(request.encargadoGranja());
        reporte.setVeterinarioResponsable(request.veterinarioResponsable());
        reporte.setObservaciones(request.observaciones());

        if (request.numeroReporte() != null && !request.numeroReporte().isBlank()) {
            reporte.setNumeroReporte(request.numeroReporte());
        }

        List<DetalleLoteReporte> nuevosDetalles = request.detalles().stream()
                .map(detalleReq -> construirDetalle(detalleReq, fechaReporte, reporte))
                .toList();

        reporte.getDetalles().clear();
        reporte.getDetalles().addAll(nuevosDetalles);

        ReporteTraslado actualizado = reporteRepo.save(reporte);
        return construirResponseCompleto(actualizado);
    }

    @Override
    public void anular(Long id) {
        ReporteTraslado reporte = buscarReporte(id);

        if (reporte.getEstado() == EstadoReporte.RECIBIDO) {
            throw new IllegalStateException(
                    "No se puede anular un reporte que ya fue recibido por planta de incubacion");
        }

        reporte.setEstado(EstadoReporte.ANULADO);
        reporteRepo.save(reporte);
    }

    // ---------------------- métodos privados de apoyo ----------------------

    private ReporteTraslado buscarReporte(Long id) {
        return reporteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado: " + id));
    }

    private DetalleLoteReporte construirDetalle(
            DetalleLoteReporteRequestDTO detalleReq, LocalDate fecha, ReporteTraslado reporte) {

        Lote lote = loteRepo.findById(detalleReq.idLote())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Lote no encontrado: " + detalleReq.idLote()));

        registrarOActualizarMortalidad(lote, fecha, detalleReq.cantidadMuertas());

        List<ConteoTipoHuevo> conteos = detalleReq.conteos().stream()
                .map(this::construirConteo)
                .toList();

        Integer muertasAcumuladas = mortalidadRepo.sumMortalidadHastaFecha(lote, fecha);
        int poblacionActual = lote.getCantidadAvesInicial() - muertasAcumuladas;

        long semanas = ChronoUnit.WEEKS.between(lote.getFechaIngreso(), fecha);

        int totalHuevos = conteos.stream()
                .mapToInt(ConteoTipoHuevo::getCantidad)
                .sum();

        BigDecimal porcentaje = poblacionActual == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(totalHuevos)
                    .divide(BigDecimal.valueOf(poblacionActual), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

        DetalleLoteReporte detalle = new DetalleLoteReporte();
        detalle.setReporte(reporte);
        detalle.setLote(lote);
        detalle.setCantidadAvesActual(poblacionActual);
        detalle.setEdadSemanas((int) semanas);
        detalle.setTotalHuevos(totalHuevos);
        detalle.setPorcentajeProduccion(porcentaje);
        detalle.setConteos(conteos);

        conteos.forEach(c -> c.setDetalleLote(detalle));

        return detalle;
    }

    private void registrarOActualizarMortalidad(Lote lote, LocalDate fecha, Integer cantidadMuertas) {
        if (cantidadMuertas == null) return;

        RegistroMortalidad registro = mortalidadRepo.findByLoteAndFecha(lote, fecha)
                .orElseGet(RegistroMortalidad::new);

        registro.setLote(lote);
        registro.setFecha(fecha);
        registro.setCantidadMuertas(cantidadMuertas);
        if (registro.getObservacion() == null) {
            registro.setObservacion("Registrado desde reporte de traslado");
        }

        mortalidadRepo.save(registro);
    }

    private ConteoTipoHuevo construirConteo(ConteoTipoHuevoRequestDTO conteoReq) {
        TipoHuevo tipoHuevo = tipoHuevoRepo.findById(conteoReq.idTipoHuevo())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tipo de huevo no encontrado: " + conteoReq.idTipoHuevo()));

        ConteoTipoHuevo conteo = new ConteoTipoHuevo();
        conteo.setTipoHuevo(tipoHuevo);
        conteo.setCantidad(conteoReq.cantidad());
        return conteo;
    }

    private void validarLotesSinDuplicados(List<DetalleLoteReporteRequestDTO> detalles) {
        Set<Long> idsVistos = new HashSet<>();
        for (DetalleLoteReporteRequestDTO detalle : detalles) {
            if (!idsVistos.add(detalle.idLote())) {
                throw new IllegalArgumentException(
                        "El lote con id " + detalle.idLote() + " esta duplicado en el reporte");
            }
        }
    }

    private void validarFechaEnRango(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        LocalDate limiteAtras = hoy.minusDays(2);

        if (fecha.isAfter(hoy) || fecha.isBefore(limiteAtras)) {
            throw new IllegalArgumentException(
                    "La fecha del reporte debe estar entre " + limiteAtras + " y " + hoy);
        }
    }

    private void validarFechaNoRepetida(LocalDate fecha, Long idReporteExcluir) {
        boolean existe = (idReporteExcluir == null)
                ? reporteRepo.existsByFecha(fecha)
                : reporteRepo.existsByFechaAndIdReporteNot(fecha, idReporteExcluir);

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un reporte registrado para la fecha " + fecha);
        }
    }

    private String generarNumero() {
        int anio = LocalDate.now().getYear();
        long correlativo = reporteRepo.countByFechaBetween(
                LocalDate.of(anio, 1, 1), LocalDate.of(anio, 12, 31)) + 1;
        return String.format("REP-%d-%03d", anio, correlativo);
    }

    private ReporteTrasladoResponseDTO construirResponseCompleto(ReporteTraslado entity) {
        ReporteTrasladoResponseDTO dto = reporteMapper.toResponseDTO(entity);

        List<DetalleLoteReporteResponseDTO> detallesEnriquecidos = entity.getDetalles().stream()
                .map(this::construirDetalleResponse)
                .toList();

        ResumenReporteDTO resumen = construirResumen(entity.getDetalles());

        return new ReporteTrasladoResponseDTO(
                dto.idReporte(), dto.numeroReporte(), dto.fecha(), dto.hora(),
                dto.chofer(), dto.placa(), dto.encargadoGranja(),
                dto.veterinarioResponsable(), dto.observaciones(), dto.estado(),
                detallesEnriquecidos, resumen
        );
    }

    private DetalleLoteReporteResponseDTO construirDetalleResponse(DetalleLoteReporte detalle) {
        DetalleLoteReporteResponseDTO base = detalleMapper.toResponseDTO(detalle);

        Integer cantidadMuertasDelDia = mortalidadRepo
                .findByLoteAndFecha(detalle.getLote(), detalle.getReporte().getFecha())
                .map(RegistroMortalidad::getCantidadMuertas)
                .orElse(0);

        return new DetalleLoteReporteResponseDTO(
                base.codigoLote(),
                base.lineaGeneticaNombre(),
                cantidadMuertasDelDia,
                base.cantidadAvesActual(),
                base.edadSemanas(),
                base.porcentajeProduccion(),
                base.totalHuevos(),
                base.conteos()
        );
    }

    private ResumenReporteDTO construirResumen(List<DetalleLoteReporte> detalles) {
        Map<String, List<DetalleLoteReporte>> agrupadoPorLinea = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getLote().getLineaGenetica().getNombreGen()));

        List<ResumenPorLineaGeneticaDTO> porLinea = agrupadoPorLinea.entrySet().stream()
                .map(entry -> new ResumenPorLineaGeneticaDTO(
                        entry.getKey(),
                        entry.getValue().size(),
                        entry.getValue().stream().mapToInt(DetalleLoteReporte::getCantidadAvesActual).sum(),
                        entry.getValue().stream().mapToInt(DetalleLoteReporte::getTotalHuevos).sum()
                ))
                .toList();

        int totalHuevos = detalles.stream().mapToInt(DetalleLoteReporte::getTotalHuevos).sum();
        int totalAves = detalles.stream().mapToInt(DetalleLoteReporte::getCantidadAvesActual).sum();

        return new ResumenReporteDTO(detalles.size(), totalAves, totalHuevos, porLinea);
    }
}