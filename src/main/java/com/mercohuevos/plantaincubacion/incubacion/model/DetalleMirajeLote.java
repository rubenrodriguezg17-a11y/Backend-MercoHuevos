// model/DetalleMirajeLote.java
package com.mercohuevos.plantaincubacion.incubacion.model;

import com.mercohuevos.plantaincubacion.shared.model.Maquina;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_miraje_lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleMirajeLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_miraje")
    private Long idDetalleMiraje;

    @ManyToOne
    @JoinColumn(name = "id_miraje", nullable = false)
    private Miraje miraje;

    @ManyToOne
    @JoinColumn(name = "id_carga_lote", nullable = false)
    private CargaLote cargaLote;

    @ManyToOne
    @JoinColumn(name = "id_maquina", nullable = false)
    private Maquina maquina;

    @Column(name = "huevos_cargados", nullable = false)
    private Integer huevosCargados;

    @Column(name = "huevos_infertiles", nullable = false)
    private Integer huevosInfertiles;
}