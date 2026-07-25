package com.mercohuevos.plantaincubacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.EmbandejadoDetalle;

public interface IEmbandejadoDetalleRepository extends JpaRepository<EmbandejadoDetalle, Long> {
    Optional<EmbandejadoDetalle> findByCodigoLoteGranja(String codigoLoteGranja);
}