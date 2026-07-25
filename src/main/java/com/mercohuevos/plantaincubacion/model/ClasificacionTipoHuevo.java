package com.mercohuevos.plantaincubacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clasificacion_tipo_huevo",
       uniqueConstraints = @UniqueConstraint(columnNames = {"codigo_tipo_huevo"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionTipoHuevo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clasificacion")
    private Long idClasificacion;

    @Column(name = "codigo_tipo_huevo", nullable = false)
    private String codigoTipoHuevo;

    @Column(name = "es_incubable", nullable = false)
    private boolean esIncubable;
}