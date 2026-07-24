package com.mercohuevos.plantaincubacion.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock_incubable",
uniqueConstraints = @UniqueConstraint(columnNames = {"id_fusion_lote", "id_categoria_embandejado", "fecha"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockIncubable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long idStock;

    @ManyToOne
    @JoinColumn(name = "id_fusion_lote", nullable = false)
    private FusionLote fusionLote;

    @ManyToOne
    @JoinColumn(name = "id_categoria_embandejado", nullable = false)
    private CategoriaEmbandejado categoriaEmbandejado;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "stock_dia_anterior", nullable = false)
    private Integer stockDiaAnterior;

    @Column(name = "embandejado_dia", nullable = false)
    private Integer embandejadoDia;

    @Column(name = "pasado_a_carton", nullable = false)
    private Integer pasadoACarton;

    @Column(name = "carga_incubadora", nullable = false)
    private Integer cargaIncubadora;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;   // = stockDiaAnterior + embandejadoDia - pasadoACarton - cargaIncubadora
}