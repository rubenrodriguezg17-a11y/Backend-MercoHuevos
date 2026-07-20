package com.mercohuevos.granja.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalle_lote_reporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleLoteReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_reporte", nullable = false)
    private ReporteTraslado reporte;

    @ManyToOne
    @JoinColumn(name = "id_lote", nullable = false)
    private Lote lote;

    @Column(name = "cantidad_aves_actual", nullable = false)
    private Integer cantidadAvesActual;

    @Column(name = "edad_semanas", nullable = false)
    private Integer edadSemanas;

    @Column(name = "porcentaje_produccion", nullable = false)
    private BigDecimal porcentajeProduccion;

    @Column(name = "total_huevos", nullable = false)
    private Integer totalHuevos;

    @OneToMany(mappedBy = "detalleLote", cascade = CascadeType.ALL)
    private List<ConteoTipoHuevo> conteos;
}