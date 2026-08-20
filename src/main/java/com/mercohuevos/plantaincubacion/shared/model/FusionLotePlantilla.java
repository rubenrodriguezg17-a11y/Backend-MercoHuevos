package com.mercohuevos.plantaincubacion.shared.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "fusion_lote_plantilla", uniqueConstraints =
@UniqueConstraint(name = "uk_plantilla_linea_codigo",
        columnNames = {"id_linea_genetica", "codigo_fusion"}))
@Getter
@Setter
public class FusionLotePlantilla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlantilla;

    @Column(name = "id_linea_genetica", nullable = false)
    private Long idLineaGenetica;

    @Column(name = "linea_genetica_nombre", nullable = false)
    private String lineaGeneticaNombre;

    @Column(name = "codigo_fusion", nullable = false)
    private String codigoFusion;

    @Column(nullable = false)
    private Boolean activa = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
}