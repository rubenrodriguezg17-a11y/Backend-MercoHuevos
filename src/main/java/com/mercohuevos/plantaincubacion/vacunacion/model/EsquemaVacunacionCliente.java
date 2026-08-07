package com.mercohuevos.plantaincubacion.vacunacion.model;

import com.mercohuevos.plantaincubacion.shared.model.Cliente;
import com.mercohuevos.plantaincubacion.shared.model.TipoVacuna;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "esquema_vacunacion_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EsquemaVacunacionCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_esquema")
    private Long idEsquema;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_tipo_vacuna", nullable = false)
    private TipoVacuna tipoVacuna;

    @Column(name = "instrucciones_aplicacion", nullable = false)
    private String instruccionesAplicacion;
}