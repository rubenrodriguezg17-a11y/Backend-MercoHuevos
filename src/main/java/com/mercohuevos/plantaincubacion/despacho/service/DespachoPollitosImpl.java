package com.mercohuevos.plantaincubacion.despacho.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.despacho.dto.*;
import com.mercohuevos.plantaincubacion.despacho.model.DespachoPollitos;
import com.mercohuevos.plantaincubacion.despacho.model.DetalleDespachoLote;
import com.mercohuevos.plantaincubacion.despacho.repository.IDespachoPollitosRepository;
import com.mercohuevos.plantaincubacion.despacho.repository.IDetalleDespachoLoteRepository;
import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.incubacion.dto.CargaDetalleResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.service.ICargaService;
import com.mercohuevos.plantaincubacion.shared.model.Cliente;
import com.mercohuevos.plantaincubacion.shared.repository.IClienteRepository;
import com.mercohuevos.plantaincubacion.vacunacion.dto.DetalleVacunacionInfoDTO;
import com.mercohuevos.plantaincubacion.vacunacion.service.IOrdenVacunacionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DespachoPollitosImpl implements IDespachoPollitosService {

    private final IDespachoPollitosRepository despachoRepo;
    private final IDetalleDespachoLoteRepository detalleRepo;
    private final IClienteRepository clienteRepo;
    private final ICargaService cargaService;
    private final IOrdenVacunacionService ordenVacunacionService;

    @Override
    @Transactional
    public DespachoResponseDTO registrar(Long idCarga, RegistrarDespachoRequestDTO request) {
        CargaDetalleResponseDTO carga = cargaService.obtenerPorId(idCarga);

        if (!"EN_VACUNACION".equals(carga.estado())) {
            throw new IllegalStateException(
                    "Solo se puede despachar una carga que este EN_VACUNACION. Estado actual: " + carga.estado());
        }

        Cliente cliente = clienteRepo.findById(request.idCliente())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + request.idCliente()));
        if (!cliente.getActivo()) {
            throw new IllegalArgumentException("El cliente " + cliente.getRazonSocial() + " esta desactivado");
        }
        DespachoPollitos despacho = new DespachoPollitos();
        despacho.setIdCarga(idCarga);
        despacho.setCliente(cliente);
        despacho.setFechaDespacho(request.fechaDespacho());
        despacho.setHoraDespacho(request.horaDespacho());
        despacho.setPlacaVehiculo(request.placaVehiculo());
        despacho.setNombreConductor(request.nombreConductor());
        despacho.setDestino(request.destino());

        for (DetalleDespachoLoteRequestDTO detalleReq : request.detalleDespacho()) {
            DetalleVacunacionInfoDTO vacInfo = ordenVacunacionService.obtenerDetallePorId(detalleReq.idDetalleVacunacion());

            if (!vacInfo.idCarga().equals(idCarga)) {
                throw new IllegalArgumentException(
                        "El detalle de vacunacion " + detalleReq.idDetalleVacunacion() + " no pertenece a la carga " + idCarga);
            }
            if (!vacInfo.idCliente().equals(request.idCliente())) {
                throw new IllegalArgumentException(
                        "El detalle de vacunacion " + detalleReq.idDetalleVacunacion() + " fue vacunado para otro cliente");
            }

            TotalDespachadoDTO yaDespachado = detalleRepo.sumarDespachadoPorDetalle(detalleReq.idDetalleVacunacion());

            validarNoSupera("machos 1ra", yaDespachado.machos1ra() + detalleReq.machos1raDespachados(), vacInfo.machos1raVacunados());
            validarNoSupera("machos 2da", yaDespachado.machos2da() + detalleReq.machos2daDespachados(), vacInfo.machos2daVacunados());
            validarNoSupera("hembras 1ra", yaDespachado.hembras1ra() + detalleReq.hembras1raDespachadas(), vacInfo.hembras1raVacunadas());
            validarNoSupera("hembras 2da", yaDespachado.hembras2da() + detalleReq.hembras2daDespachadas(), vacInfo.hembras2daVacunadas());

            DetalleDespachoLote detalle = new DetalleDespachoLote();
            detalle.setDespacho(despacho);
            detalle.setIdDetalleVacunacion(detalleReq.idDetalleVacunacion());
            detalle.setMachos1raDespachados(detalleReq.machos1raDespachados());
            detalle.setMachos2daDespachados(detalleReq.machos2daDespachados());
            detalle.setHembras1raDespachadas(detalleReq.hembras1raDespachadas());
            detalle.setHembras2daDespachadas(detalleReq.hembras2daDespachadas());

            despacho.getDetalles().add(detalle);
        }

        despachoRepo.save(despacho);

        if (cargaCompletamenteDespachada(idCarga)) {
            cargaService.cambiarEstado(idCarga, EstadoCarga.FINALIZADA);
        }

        return construirResponse(despacho);
    }

    @Override
    public DespachoResponseDTO obtenerPorId(Long idCarga, Long idDespacho) {
        DespachoPollitos despacho = despachoRepo.findByIdDespachoAndIdCarga(idDespacho, idCarga)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Despacho no encontrado: " + idDespacho + " para la carga " + idCarga));
        return construirResponse(despacho);
    }

    @Override
    public List<DespachoResponseDTO> getAllDespachosPollitos() {
        return despachoRepo.findAll().stream()
                .sorted(Comparator.comparing(DespachoPollitos::getFechaDespacho).reversed())
                .map(this::construirResponse)
                .toList();
    }

    @Override
    public List<DespachoResponseDTO> listarPorCarga(Long idCarga) {
        return despachoRepo.findByIdCargaOrderByFechaDespachoAsc(idCarga).stream()
                .map(this::construirResponse)
                .toList();
    }


    private void validarNoSupera(String etiqueta, int totalConEsteDespacho, int vacunado) {
        if (totalConEsteDespacho > vacunado) {
            throw new IllegalArgumentException(
                    "Se supera lo vacunado en " + etiqueta + ". Vacunado: " + vacunado + ", intentando despachar: " + totalConEsteDespacho);
        }
    }

    private boolean cargaCompletamenteDespachada(Long idCarga) {
        List<DetalleVacunacionInfoDTO> vacunados = ordenVacunacionService.listarDetallesPorCarga(idCarga);
        for (DetalleVacunacionInfoDTO v : vacunados) {
            TotalDespachadoDTO despachado = detalleRepo.sumarDespachadoPorDetalle(v.idDetalleVacunacion());
            int totalVacunado = v.machos1raVacunados() + v.machos2daVacunados() + v.hembras1raVacunadas() + v.hembras2daVacunadas();
            if (despachado.total() < totalVacunado) return false;
        }
        return true;
    }


    private DespachoResponseDTO construirResponse(DespachoPollitos despacho) {
        List<DetalleDespachoLoteResponseDTO> detallesDTO = despacho.getDetalles().stream()
                .map(d -> new DetalleDespachoLoteResponseDTO(
                        d.getIdDetalleDespacho(), d.getIdDetalleVacunacion(),
                        d.getMachos1raDespachados(), d.getMachos2daDespachados(),
                        d.getHembras1raDespachadas(), d.getHembras2daDespachadas(),
                        d.getMachos1raDespachados() + d.getMachos2daDespachados()
                                + d.getHembras1raDespachadas() + d.getHembras2daDespachadas()
                ))
                .toList();

        return new DespachoResponseDTO(
                despacho.getIdDespacho(), despacho.getIdCarga(),
                despacho.getCliente().getIdCliente(), despacho.getCliente().getRazonSocial(),
                despacho.getFechaDespacho(), despacho.getHoraDespacho(),
                despacho.getPlacaVehiculo(), despacho.getNombreConductor(), despacho.getDestino(),
                detallesDTO
        );
    }
}