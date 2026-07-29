// granja/service/ReporteTrasladoImpl.java
package com.mercohuevos.granja.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.mercohuevos.common.dto.ConteoTipoHuevoEventDTO;
import com.mercohuevos.common.dto.DetalleLoteEventDTO;
import com.mercohuevos.common.dto.ReporteTrasladoEventDTO;
import com.mercohuevos.common.event.ReporteTrasladoCreadoEvent;
import com.mercohuevos.granja.dto.*;
import com.mercohuevos.granja.enums.ClasificacionHuevo;
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
	private final ApplicationEventPublisher eventPublisher;

	@Override
	public ReporteTrasladoResponseDTO crear(ReporteTrasladoRequestDTO request) {
		List<LoteReporteRequestDTO> lotesAplanados = aplanarLotes(request.lineasGeneticas());
		validarLotesSinDuplicados(lotesAplanados);
		LocalDate fechaReporte = (request.fecha() != null) ? request.fecha() : LocalDate.now();
		validarFechaEnRango(fechaReporte);
		validarFechaNoRepetida(fechaReporte, null);

		ReporteTraslado reporte = new ReporteTraslado();
		reporte.setFecha(fechaReporte);
		reporte.setHoraSalida(request.horaSalida());
		reporte.setChofer(request.chofer());
		reporte.setPlaca(request.placa());
		reporte.setEncargadoGranja(request.encargadoGranja());
		reporte.setVeterinarioResponsable(request.veterinarioResponsable());
		reporte.setObservaciones(request.observaciones());
		reporte.setEstado(EstadoReporte.PENDIENTE);
		reporte.setNumeroReporte(
				(request.numeroReporte() == null || request.numeroReporte().isBlank()) ? generarNumero()
						: request.numeroReporte());

		List<DetalleLoteReporte> detalles = lotesAplanados.stream()
				.map(detalleReq -> construirDetalle(detalleReq, fechaReporte, reporte)).toList();
		reporte.setDetalles(detalles);

		ReporteTraslado guardado = reporteRepo.save(reporte);
		publicarEvento(guardado);
		return construirResponseCompleto(guardado);
	}

	@Override
	public ReporteTrasladoResponseDTO obtenerPorId(Long id) {
		return construirResponseCompleto(buscarReporte(id));
	}

	@Override
	public List<ReporteTrasladoResponseDTO> listarTodos() {
		return reporteRepo.findAll().stream().map(this::construirResponseCompleto).toList();
	}

	@Override
	public ReporteTrasladoResponseDTO editar(Long id, ReporteTrasladoRequestDTO request) {
		ReporteTraslado reporte = buscarReporte(id);
		if (reporte.getEstado() != EstadoReporte.PENDIENTE) {
			throw new IllegalStateException("El reporte no puede editarse porque su estado es " + reporte.getEstado()
					+ ". Solo los reportes PENDIENTE son editables.");
		}
		List<LoteReporteRequestDTO> lotesAplanados = aplanarLotes(request.lineasGeneticas());
		validarLotesSinDuplicados(lotesAplanados);
		LocalDate fechaReporte = (request.fecha() != null) ? request.fecha() : reporte.getFecha();
		validarFechaEnRango(fechaReporte);
		validarFechaNoRepetida(fechaReporte, id);

		reporte.setFecha(fechaReporte);
		reporte.setHoraSalida(request.horaSalida());
		reporte.setChofer(request.chofer());
		reporte.setPlaca(request.placa());
		reporte.setEncargadoGranja(request.encargadoGranja());
		reporte.setVeterinarioResponsable(request.veterinarioResponsable());
		reporte.setObservaciones(request.observaciones());
		if (request.numeroReporte() != null && !request.numeroReporte().isBlank()) {
			reporte.setNumeroReporte(request.numeroReporte());
		}

		List<DetalleLoteReporte> nuevosDetalles = lotesAplanados.stream()
				.map(detalleReq -> construirDetalle(detalleReq, fechaReporte, reporte)).toList();
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

	// ---------------------- Métodos privados de apoyo ----------------------
	private ReporteTraslado buscarReporte(Long id) {
		return reporteRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado: " + id));
	}

	private DetalleLoteReporte construirDetalle(LoteReporteRequestDTO detalleReq, LocalDate fecha,
			ReporteTraslado reporte) {
		Lote lote = loteRepo.findById(detalleReq.idLote())
				.orElseThrow(() -> new EntityNotFoundException("Lote no encontrado: " + detalleReq.idLote()));
		registrarOActualizarMortalidad(lote, fecha, detalleReq.cantidadMuertas());
		List<ConteoTipoHuevo> conteos = detalleReq.conteos().stream().map(this::construirConteo).toList();
		Integer muertasAcumuladas = mortalidadRepo.sumMortalidadHastaFecha(lote, fecha);
		int poblacionActual = lote.getCantidadAvesInicial() - muertasAcumuladas;
		long semanas = ChronoUnit.WEEKS.between(lote.getFechaIngreso(), fecha);
		int totalHuevos = conteos.stream().mapToInt(ConteoTipoHuevo::getCantidad).sum();
		BigDecimal porcentaje = poblacionActual == 0 ? BigDecimal.ZERO
				: BigDecimal.valueOf(totalHuevos).divide(BigDecimal.valueOf(poblacionActual), 4, RoundingMode.HALF_UP)
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
		if (cantidadMuertas == null)
			return;
		RegistroMortalidad registro = mortalidadRepo.findByLoteAndFecha(lote, fecha).orElseGet(RegistroMortalidad::new);
		registro.setLote(lote);
		registro.setFecha(fecha);
		registro.setCantidadMuertas(cantidadMuertas);
		if (registro.getObservacion() == null) {
			registro.setObservacion("Registrado desde reporte de traslado");
		}
		mortalidadRepo.save(registro);
	}

	private ConteoTipoHuevo construirConteo(ConteoTipoHuevoRequestDTO conteoReq) {
		TipoHuevo tipoHuevo = tipoHuevoRepo.findById(conteoReq.idTipoHuevo()).orElseThrow(
				() -> new EntityNotFoundException("Tipo de huevo no encontrado: " + conteoReq.idTipoHuevo()));
		ConteoTipoHuevo conteo = new ConteoTipoHuevo();
		conteo.setTipoHuevo(tipoHuevo);
		conteo.setCantidad(conteoReq.cantidad());
		return conteo;
	}

	private void validarLotesSinDuplicados(List<LoteReporteRequestDTO> lotes) {
		Set<Long> idsVistos = new HashSet<>();
		for (LoteReporteRequestDTO lote : lotes) {
			if (!idsVistos.add(lote.idLote())) {
				throw new IllegalArgumentException("El lote con id " + lote.idLote() + " esta duplicado en el reporte");
			}
		}
	}

	private void validarFechaEnRango(LocalDate fecha) {
		LocalDate hoy = LocalDate.now();
		LocalDate limiteAtras = hoy.minusDays(2);
		if (fecha.isAfter(hoy) || fecha.isBefore(limiteAtras)) {
			throw new IllegalArgumentException("La fecha del reporte debe estar entre " + limiteAtras + " y " + hoy);
		}
	}

	private void validarFechaNoRepetida(LocalDate fecha, Long idReporteExcluir) {
		boolean existe = (idReporteExcluir == null) ? reporteRepo.existsByFecha(fecha)
				: reporteRepo.existsByFechaAndIdReporteNot(fecha, idReporteExcluir);
		if (existe) {
			throw new IllegalArgumentException("Ya existe un reporte registrado para la fecha " + fecha);
		}
	}

	private String generarNumero() {
		int anio = LocalDate.now().getYear();
		long correlativo = reporteRepo.countByFechaBetween(LocalDate.of(anio, 1, 1), LocalDate.of(anio, 12, 31)) + 1;
		return String.format("REP-%d-%03d", anio, correlativo);
	}

	private List<LoteReporteRequestDTO> aplanarLotes(List<LineaGeneticaReporteRequestDTO> lineasGeneticas) {
		List<LoteReporteRequestDTO> resultado = new ArrayList<>();
		for (LineaGeneticaReporteRequestDTO linea : lineasGeneticas) {
			for (LoteReporteRequestDTO loteReq : linea.lotes()) {
				Lote lote = loteRepo.findById(loteReq.idLote())
						.orElseThrow(() -> new EntityNotFoundException("Lote no encontrado: " + loteReq.idLote()));
				if (!lote.getLineaGenetica().getIdGen().equals(linea.idLineaGenetica())) {
					throw new IllegalArgumentException(
							"El lote " + loteReq.idLote() + " no pertenece a la linea genetica " + linea.idLineaGenetica());
				}
				resultado.add(loteReq);
			}
		}
		return resultado;
	}

	// ---------------------- Centralización de Totales y Mapeo DTO ----------------------
	private ReporteTrasladoResponseDTO construirResponseCompleto(ReporteTraslado entity) {
		ReporteTrasladoResponseDTO baseDto = reporteMapper.toResponseDTO(entity);

		if (entity.getDetalles() == null || entity.getDetalles().isEmpty()) {
			return crearGranTotalResponse(baseDto, 0, 0, 0, 0, List.of());
		}

		Map<LineaGenetica, List<DetalleLoteReporte>> agrupadoPorGenetica = entity.getDetalles().stream()
				.collect(Collectors.groupingBy(d -> d.getLote().getLineaGenetica()));

		List<LineaGeneticaResponseDTO> lineasGeneticasDTO = agrupadoPorGenetica.entrySet().stream()
				.map(entry -> {
					LineaGenetica genetica = entry.getKey();
					List<DetalleLoteReporte> detallesGrupo = entry.getValue();

					List<DetalleLoteReporteResponseDTO> lotesDTO = detallesGrupo.stream()
							.map(this::construirDetalleResponse)
							.toList();

					int totalAves = detallesGrupo.stream()
							.mapToInt(DetalleLoteReporte::getCantidadAvesActual)
							.sum();

					int totalMuertas = detallesGrupo.stream()
							.mapToInt(d -> mortalidadRepo.findByLoteAndFecha(d.getLote(), entity.getFecha())
									.map(RegistroMortalidad::getCantidadMuertas)
									.orElse(0))
							.sum();

					int totalHuevos = detallesGrupo.stream()
							.mapToInt(DetalleLoteReporte::getTotalHuevos)
							.sum();

					return new LineaGeneticaResponseDTO(
							genetica.getIdGen(),
							genetica.getNombreGen(),
							lotesDTO.size(),
							totalAves,
							totalMuertas,
							totalHuevos,
							lotesDTO
					);
				}).toList();

		int grandTotalLotes = entity.getDetalles().size();
		int grandTotalAves = lineasGeneticasDTO.stream().mapToInt(LineaGeneticaResponseDTO::totalAvesActual).sum();
		int grandTotalMuertas = lineasGeneticasDTO.stream().mapToInt(LineaGeneticaResponseDTO::totalMuertasDelDia).sum();
		int grandTotalHuevos = lineasGeneticasDTO.stream().mapToInt(LineaGeneticaResponseDTO::totalHuevos).sum();

		return crearGranTotalResponse(baseDto, grandTotalLotes, grandTotalAves, grandTotalMuertas, grandTotalHuevos, lineasGeneticasDTO);
	}

	private DetalleLoteReporteResponseDTO construirDetalleResponse(DetalleLoteReporte detalle) {
		DetalleLoteReporteResponseDTO base = detalleMapper.toResponseDTO(detalle);
		Integer cantidadMuertasDelDia = mortalidadRepo.findByLoteAndFecha(detalle.getLote(), detalle.getReporte().getFecha())
				.map(RegistroMortalidad::getCantidadMuertas)
				.orElse(0);

		return new DetalleLoteReporteResponseDTO(
				detalle.getLote().getIdLote(),
				base.codigoLote(),
				cantidadMuertasDelDia,
				base.cantidadAvesActual(),
				base.edadSemanas(),
				base.porcentajeProduccion(),
				base.totalHuevos(),
				base.conteos()
		);
	}

	private ReporteTrasladoResponseDTO crearGranTotalResponse(
			ReporteTrasladoResponseDTO base,
			int lotes,
			int aves,
			int muertas,
			int huevos,
			List<LineaGeneticaResponseDTO> lineas) {
		return new ReporteTrasladoResponseDTO(
				base.idReporte(),
				base.numeroReporte(),
				base.fecha(),
				base.horaSalida(),
				base.horaLlegada(),
				base.chofer(),
				base.placa(),
				base.encargadoGranja(),
				base.veterinarioResponsable(),
				base.observaciones(),
				base.estado().toString(),
				lotes,
				aves,
				muertas,
				huevos,
				lineas
		);
	}

	private void publicarEvento(ReporteTraslado reporte) {
		List<DetalleLoteEventDTO> detallesEvento = reporte.getDetalles().stream()
				.map(this::construirDetalleEvento)
				.toList();

		ReporteTrasladoEventDTO eventoDTO = new ReporteTrasladoEventDTO(
				reporte.getIdReporte(),
				reporte.getNumeroReporte(),
				reporte.getFecha(),
				reporte.getHoraSalida(),
				reporte.getChofer(),
				reporte.getPlaca(),
				reporte.getEncargadoGranja(),
				reporte.getVeterinarioResponsable(),
				reporte.getObservaciones(),
				detallesEvento
		);

		eventPublisher.publishEvent(new ReporteTrasladoCreadoEvent(eventoDTO));
	}

	private DetalleLoteEventDTO construirDetalleEvento(DetalleLoteReporte detalle) {
		List<ConteoTipoHuevoEventDTO> conteosEvento = detalle.getConteos().stream()
				.filter(c -> c.getTipoHuevo().getClasificacion() != ClasificacionHuevo.DESCARTE)
				.map(c -> new ConteoTipoHuevoEventDTO(c.getTipoHuevo().getCodigo(), c.getCantidad()))
				.toList();

		return new DetalleLoteEventDTO(
				detalle.getLote().getIdLote(),
				detalle.getLote().getCodigoLote(),
				detalle.getLote().getLineaGenetica().getIdGen(),
				detalle.getLote().getLineaGenetica().getNombreGen(),
				conteosEvento
		);
	}
}