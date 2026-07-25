package com.mercohuevos.granja.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.mercohuevos.granja.enums.EstadoReporte;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reporte_traslado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteTraslado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_salida", nullable = false)
    private LocalTime horaSalida;

    @Column(name = "hora_llegada")
    private LocalTime horaLlegada;   
    
    @Column(name = "chofer", nullable = false)
    private String chofer;

    @Column(name = "placa", nullable = false)
    private String placa;

    @Column(name = "numero_reporte", unique = true)
    private String numeroReporte;

    @Column(name = "encargado_granja", nullable = false)
    private String encargadoGranja;

    @Column(name = "veterinario_responsable", nullable = false)
    private String veterinarioResponsable;

    @Column(name = "observaciones")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReporte estado = EstadoReporte.PENDIENTE;

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleLoteReporte> detalles;
}