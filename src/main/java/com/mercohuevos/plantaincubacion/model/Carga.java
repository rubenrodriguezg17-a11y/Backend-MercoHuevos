package com.mercohuevos.plantaincubacion.model;

import java.time.LocalDate;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;

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
@Table(name = "carga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carga")
    private Long idCarga;

    @ManyToOne
    @JoinColumn(name = "id_fusion_lote", nullable = false)
    private FusionLote fusionLote;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;   // total: suma de todas las categorias de esta carga

    @Column(name = "fecha_carga", nullable = false)
    private LocalDate fechaCarga;

    @Column(name = "fecha_transferencia_nacedora", nullable = false)
    private LocalDate fechaTransferenciaNacedora;   // calculada: fechaCarga + 18

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;              // calculada: fechaCarga + 21

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCarga estado = EstadoCarga.EN_INCUBACION;
}