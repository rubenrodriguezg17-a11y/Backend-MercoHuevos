package com.mercohuevos.plantaincubacion.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fusion_lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FusionLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fusion_lote")
    private Long idFusionLote;

    @Column(name = "nombre", nullable = false)
    private String nombre;   

    @Column(name = "linea_genetica_nombre", nullable = false)
    private String lineaGeneticaNombre;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}