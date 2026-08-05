package com.mercohuevos.plantaincubacion.incubacion.model;

import java.time.LocalDateTime;

import com.mercohuevos.plantaincubacion.shared.model.Maquina;
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
@Table(name = "monitoreo_ambiental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitoreoAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_monitoreo")
    private Long idMonitoreo;

    @ManyToOne
    @JoinColumn(name = "id_maquina", nullable = false)
    private Maquina maquina;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "temperatura1")
    private Double temperatura1;   // solo incubadora

    @Column(name = "temperatura2")
    private Double temperatura2;   // solo incubadora

    @Column(name = "humedad", nullable = false)
    private Double humedad;        // ambas

    @Column(name = "volteo")
    private Boolean volteo;        // solo incubadora
}