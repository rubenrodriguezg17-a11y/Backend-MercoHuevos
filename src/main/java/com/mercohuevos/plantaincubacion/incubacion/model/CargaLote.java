package com.mercohuevos.plantaincubacion.incubacion.model;

import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carga_lote", uniqueConstraints =
@UniqueConstraint(name = "uk_carga_fusion_lote", columnNames = {"id_carga", "id_fusion_lote"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargaLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carga_lote")
    private Long idCargaLote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carga", nullable = false)
    private Carga carga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fusion_lote", nullable = false)
    private FusionLote fusionLote;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;   // huevos de ESTE lote que entran a ESTA carga
}