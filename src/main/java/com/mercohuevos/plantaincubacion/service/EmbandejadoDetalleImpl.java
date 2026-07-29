package com.mercohuevos.plantaincubacion.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.ConteoCategoriaEmbandejadoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.ConteoCategoriaEmbandejadoResponseDTO;
import com.mercohuevos.plantaincubacion.dto.EmbandejadoDetalleRequestDTO;
import com.mercohuevos.plantaincubacion.dto.EmbandejadoDetalleResponseDTO;
import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.mapper.IConteoCategoriaEmbandejadoMapper;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.model.ConteoCategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.EmbandejadoDetalle;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.FusionLoteMiembro;
import com.mercohuevos.plantaincubacion.model.LoteOrigenReporte;
import com.mercohuevos.plantaincubacion.model.RecepcionReporte;
import com.mercohuevos.plantaincubacion.model.StockIncubable;
import com.mercohuevos.plantaincubacion.repository.ICategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.repository.IConsumoHuevoRepository;
import com.mercohuevos.plantaincubacion.repository.IConteoCategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.repository.IEmbandejadoDetalleRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteMiembroRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteRepository;
import com.mercohuevos.plantaincubacion.repository.ILoteOrigenReporteRepository;
import com.mercohuevos.plantaincubacion.repository.IStockIncubableRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmbandejadoDetalleImpl implements IEmbandejadoDetalleService {

	private final IEmbandejadoDetalleRepository embandejadoRepo;
	private final IConteoCategoriaEmbandejadoRepository conteoRepo;
	private final ILoteOrigenReporteRepository loteOrigenRepo;
	private final IFusionLoteRepository fusionLoteRepo;
	private final IFusionLoteMiembroRepository fusionMiembroRepo;
	private final ICategoriaEmbandejadoRepository categoriaRepo;
	private final IStockIncubableRepository stockRepo;
	private final IConsumoHuevoRepository consumoRepo;
	private final IConteoCategoriaEmbandejadoMapper conteoMapper;

	@Override
	public EmbandejadoDetalleResponseDTO registrar(EmbandejadoDetalleRequestDTO request) {

		FusionLote fusionLote = fusionLoteRepo.findById(request.idFusionLote()).orElseThrow(
				() -> new EntityNotFoundException("Fusion de lote no encontrada: " + request.idFusionLote()));

		List<String> codigosLote = fusionMiembroRepo.findByFusionLote(fusionLote).stream()
				.map(FusionLoteMiembro::getCodigoLoteGranja).toList();

		LocalDate hoy = LocalDate.now();

		List<LoteOrigenReporte> lotesOrigen = loteOrigenRepo
				.findByCodigoLoteGranjaInAndRecepcion_FechaReporte(codigosLote, hoy);

		if (lotesOrigen.isEmpty()) {
			throw new EntityNotFoundException(
					"No se encontro un reporte de hoy (" + hoy + ") para la fusion: " + fusionLote.getNombre());
		}

		RecepcionReporte recepcion = lotesOrigen.get(0).getRecepcion();

		if (embandejadoRepo.existsByRecepcionAndFusionLote(recepcion, fusionLote)) {
			throw new IllegalStateException(
					"La fusion " + fusionLote.getNombre() + " ya fue embandejada para el reporte de hoy");
		}

		int totalIncubable = lotesOrigen.stream().mapToInt(LoteOrigenReporte::getHuevosIncubablesGuia).sum();

		int totalComercial = lotesOrigen.stream().mapToInt(LoteOrigenReporte::getHuevosComercialGuia).sum();

		EmbandejadoDetalle detalle = new EmbandejadoDetalle();
		detalle.setRecepcion(recepcion);
		detalle.setFusionLote(fusionLote);
		detalle.setCodigoLoteGranja(fusionLote.getNombre());
		detalle.setHuevosIncubablesGuia(totalIncubable);
		detalle.setHuevosComercialGuia(totalComercial);
		detalle.setRotosTransporte(request.rotosTransporte());
		detalle.setRotosEmbandejado(request.rotosEmbandejado());
		detalle.setSeleccionDescartada(request.seleccionDescartada());
		detalle.setObservaciones(request.observaciones());

		EmbandejadoDetalle guardado = embandejadoRepo.save(detalle);

		List<ConteoCategoriaEmbandejado> conteos = request.conteos().stream().map(c -> construirConteo(c, guardado))
				.toList();
		conteoRepo.saveAll(conteos);

		registrarConsumoAutomatico(fusionLote, hoy, request.seleccionDescartada(), totalComercial);
		actualizarStockIncubable(fusionLote, hoy, conteos);

		return construirResponseCompleto(guardado, conteos);

	}

	@Override
	public EmbandejadoDetalleResponseDTO obtenerPorId(Long id) {
		EmbandejadoDetalle detalle = embandejadoRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Embandejado no encontrado: " + id));

		List<ConteoCategoriaEmbandejado> conteos = conteoRepo.findByEmbandejadoDetalle(detalle);
		return construirResponseCompleto(detalle, conteos);
	}

	// ---------------------- métodos privados de apoyo ----------------------

	private ConteoCategoriaEmbandejado construirConteo(ConteoCategoriaEmbandejadoRequestDTO conteoReq,
			EmbandejadoDetalle detalle) {

		CategoriaEmbandejado categoria = categoriaRepo.findById(conteoReq.idCategoriaEmbandejado())
				.orElseThrow(() -> new EntityNotFoundException(
						"Categoria de embandejado no encontrada: " + conteoReq.idCategoriaEmbandejado()));

		ConteoCategoriaEmbandejado conteo = new ConteoCategoriaEmbandejado();
		conteo.setEmbandejadoDetalle(detalle);
		conteo.setCategoriaEmbandejado(categoria);
		conteo.setCantidad(conteoReq.cantidad());
		return conteo;
	}

	private void registrarConsumoAutomatico(FusionLote fusionLote, LocalDate fecha, Integer seleccionDescartada,
			Integer huevosComercialGuia) {

		if (seleccionDescartada != null && seleccionDescartada > 0) {
			guardarConsumo(fusionLote, fecha, OrigenConsumo.DESCARTE_SELECCION, seleccionDescartada,
					"Descarte automatico por seleccion en embandejado");
		}

		if (huevosComercialGuia != null && huevosComercialGuia > 0) {
			guardarConsumo(fusionLote, fecha, OrigenConsumo.COMERCIAL_GRANJA, huevosComercialGuia,
					"Huevo comercial recibido directo de granja");
		}
	}

	private void guardarConsumo(FusionLote fusionLote, LocalDate fecha, OrigenConsumo origen, Integer cantidad,
			String observacion) {

		ConsumoHuevo consumo = new ConsumoHuevo();
		consumo.setFusionLote(fusionLote);
		consumo.setFecha(fecha);
		consumo.setOrigen(origen);
		consumo.setCantidad(cantidad);
		consumo.setObservacion(observacion);
		consumoRepo.save(consumo);
	}

	private void actualizarStockIncubable(FusionLote fusionLote, LocalDate fecha,
			List<ConteoCategoriaEmbandejado> conteos) {

		for (ConteoCategoriaEmbandejado conteo : conteos) {
			CategoriaEmbandejado categoria = conteo.getCategoriaEmbandejado();

			int stockAnterior = stockRepo
					.findTopByFusionLoteAndCategoriaEmbandejadoAndFechaLessThanOrderByFechaDesc(fusionLote, categoria,
							fecha)
					.map(StockIncubable::getStockActual).orElse(0);

			StockIncubable stock = stockRepo
					.findByFusionLoteAndCategoriaEmbandejadoAndFecha(fusionLote, categoria, fecha)
					.orElseGet(StockIncubable::new);

			stock.setFusionLote(fusionLote);
			stock.setCategoriaEmbandejado(categoria);
			stock.setFecha(fecha);
			stock.setStockDiaAnterior(stockAnterior);
			stock.setEmbandejadoDia(conteo.getCantidad());
			stock.setPasadoACarton(stock.getPasadoACarton() != null ? stock.getPasadoACarton() : 0);
			stock.setCargaIncubadora(stock.getCargaIncubadora() != null ? stock.getCargaIncubadora() : 0);
			stock.setStockActual(
					stockAnterior + conteo.getCantidad() - stock.getPasadoACarton() - stock.getCargaIncubadora());

			stockRepo.save(stock);
		}
	}

	private EmbandejadoDetalleResponseDTO construirResponseCompleto(EmbandejadoDetalle detalle,
			List<ConteoCategoriaEmbandejado> conteos) {

		List<ConteoCategoriaEmbandejadoResponseDTO> conteosDTO = conteos.stream().map(conteoMapper::toResponseDTO)
				.toList();

		int totalEmbandejado = conteos.stream().mapToInt(ConteoCategoriaEmbandejado::getCantidad).sum();

		int rotos = (detalle.getRotosTransporte() != null ? detalle.getRotosTransporte() : 0)
				+ (detalle.getRotosEmbandejado() != null ? detalle.getRotosEmbandejado() : 0);

		int seleccion = detalle.getSeleccionDescartada() != null ? detalle.getSeleccionDescartada() : 0;

		int totalDeclarado = totalEmbandejado + rotos + seleccion;

		int diferenciaGuia = detalle.getHuevosIncubablesGuia() - totalDeclarado;

		return new EmbandejadoDetalleResponseDTO(detalle.getIdEmbandejado(), detalle.getCodigoLoteGranja(),
				detalle.getFusionLote() != null ? detalle.getFusionLote().getNombre() : null,
				detalle.getHuevosIncubablesGuia(), detalle.getHuevosComercialGuia(), detalle.getRotosTransporte(),
				detalle.getRotosEmbandejado(), detalle.getSeleccionDescartada(), totalEmbandejado,
				detalle.getObservaciones(), conteosDTO, diferenciaGuia);
	}
}