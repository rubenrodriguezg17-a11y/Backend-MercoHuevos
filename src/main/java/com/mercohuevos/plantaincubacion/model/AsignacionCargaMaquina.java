package com.mercohuevos.plantaincubacion.model;

import com.mercohuevos.plantaincubacion.enums.FaseAsignacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asignacion_carga_maquina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionCargaMaquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Long idAsignacion;

    @ManyToOne
    @JoinColumn(name = "id_carga", nullable = false)
    private Carga carga;

    @ManyToOne
    @JoinColumn(name = "id_maquina", nullable = false)
    private Maquina maquina;

    @Enumerated(EnumType.STRING)
    @Column(name = "fase", nullable = false)
    private FaseAsignacion fase;   // INCUBACION o NACEDORA

    @Column(name = "cantidad_asignada", nullable = false)
    private Integer cantidadAsignada;
}