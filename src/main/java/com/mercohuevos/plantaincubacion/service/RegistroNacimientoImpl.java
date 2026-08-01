package com.mercohuevos.plantaincubacion.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.RegistroNacimientoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.RegistroNacimientoResponseDTO;
import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.RegistroNacimiento;
import com.mercohuevos.plantaincubacion.repository.ICargaRepository;
import com.mercohuevos.plantaincubacion.repository.IRegistroNacimientoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistroNacimientoImpl implements IRegistroNacimientoService {

    private final IRegistroNacimientoRepository nacimientoRepo;
    private final ICargaRepository cargaRepo;

    @Override
    public RegistroNacimientoResponseDTO registrar(Long idCarga, RegistroNacimientoRequestDTO request) {
        Carga carga = buscarCarga(idCarga);

        if (nacimientoRepo.findByCarga(carga).isPresent()) {
            throw new IllegalStateException("Esta carga ya tiene un registro de nacimiento");
        }

        RegistroNacimiento nacimiento = new RegistroNacimiento();
        nacimiento.setCarga(carga);
        nacimiento.setCantidadMachos(request.cantidadMachos());
        nacimiento.setCantidadHembras(request.cantidadHembras());
        nacimiento.setCantidadPrimera(request.cantidadPrimera());
        nacimiento.setCantidadSegunda(request.cantidadSegunda());
        nacimiento.setCantidadDescarte(request.cantidadDescarte());

        RegistroNacimiento guardado = nacimientoRepo.save(nacimiento);

        carga.setEstado(EstadoCarga.FINALIZADA);
        cargaRepo.save(carga);

        return construirResponseDTO(guardado);
    }

    @Override
    public RegistroNacimientoResponseDTO obtenerPorCarga(Long idCarga) {
        Carga carga = buscarCarga(idCarga);
        RegistroNacimiento nacimiento = nacimientoRepo.findByCarga(carga)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Esta carga aun no tiene registro de nacimiento"));

        return construirResponseDTO(nacimiento);
    }

    private RegistroNacimientoResponseDTO construirResponseDTO(RegistroNacimiento nacimiento) {
        int total = nacimiento.getCantidadMachos() + nacimiento.getCantidadHembras()
                + nacimiento.getCantidadPrimera() + nacimiento.getCantidadSegunda()
                + nacimiento.getCantidadDescarte();

        BigDecimal porcentajePrimera = calcularPorcentaje(nacimiento.getCantidadPrimera(), total);
        BigDecimal porcentajeSegunda = calcularPorcentaje(nacimiento.getCantidadSegunda(), total);

        return new RegistroNacimientoResponseDTO(
                nacimiento.getIdNacimiento(),
                nacimiento.getCarga().getIdCarga(),
                nacimiento.getCarga().getFusionLote().getCodigoFusion(),
                nacimiento.getCantidadMachos(),
                nacimiento.getCantidadHembras(),
                nacimiento.getCantidadPrimera(),
                nacimiento.getCantidadSegunda(),
                nacimiento.getCantidadDescarte(),
                total,
                porcentajePrimera,
                porcentajeSegunda
        );
    }

    private BigDecimal calcularPorcentaje(Integer cantidad, int total) {
        if (total == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(cantidad)
                .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private Carga buscarCarga(Long id) {
        return cargaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carga no encontrada: " + id));
    }
}