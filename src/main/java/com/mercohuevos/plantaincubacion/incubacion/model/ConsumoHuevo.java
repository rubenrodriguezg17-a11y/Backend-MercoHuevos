package com.mercohuevos.plantaincubacion.incubacion.model;

import java.time.LocalDate;

import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;

import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
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
@Table(name = "consumo_huevo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumoHuevo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consumo")
    private Long idConsumo;

    @ManyToOne
    @JoinColumn(name = "id_fusion_lote", nullable = false)
    private FusionLote fusionLote;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen", nullable = false)
    private OrigenConsumo origen;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "cantidad_descontada", nullable = false)
    private Integer cantidadDescontada = 0;

    @Column(name = "observacion")
    private String observacion;
}