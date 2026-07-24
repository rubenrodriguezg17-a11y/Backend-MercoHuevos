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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "despacho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho")
    private Long idDespacho;

    @ManyToOne
    @JoinColumn(name = "id_carga", nullable = false)
    private Carga carga;

    @ManyToOne
    @JoinColumn(name = "id_categoria_despacho", nullable = false)
    private CategoriaDespacho categoriaDespacho;

    @Column(name = "cliente", nullable = false)
    private String cliente;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "destino", nullable = false)
    private String destino;

    @Column(name = "fecha_despacho", nullable = false)
    private LocalDate fechaDespacho;
}