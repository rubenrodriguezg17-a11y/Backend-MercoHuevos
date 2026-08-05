package com.mercohuevos.plantaincubacion.incubacion.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;

import jakarta.persistence.*;
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

    @Column(name = "id_linea_genetica", nullable = false)
    private Long idLineaGenetica;

    @Column(name = "linea_genetica_nombre", nullable = false)
    private String lineaGeneticaNombre;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;

    @Column(name = "fecha_carga", nullable = false)
    private LocalDate fechaCarga;

    @Column(name = "fecha_transferencia_nacedora", nullable = false)
    private LocalDate fechaTransferenciaNacedora;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCarga estado = EstadoCarga.EN_INCUBACION;

    @OneToMany(mappedBy = "carga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CargaLote> lotes = new ArrayList<>();
}