package com.mercohuevos.plantaincubacion.incubacion.repository;

import java.util.List;
import java.util.Optional;

import com.mercohuevos.plantaincubacion.shared.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.incubacion.model.Carga;
import com.mercohuevos.plantaincubacion.incubacion.model.CategoriaCarga;

public interface ICategoriaCargaRepository extends JpaRepository<CategoriaCarga, Long> {
    List<CategoriaCarga> findByCarga(Carga carga);

    Optional<CategoriaCarga> findByCargaAndFusionLoteAndCategoriaEmbandejado(
            Carga carga, FusionLote fusionLote, CategoriaEmbandejado categoria);
}