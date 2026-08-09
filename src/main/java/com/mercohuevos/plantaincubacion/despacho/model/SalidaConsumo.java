package com.mercohuevos.plantaincubacion.despacho.model;

import java.time.LocalDate;
import com.mercohuevos.plantaincubacion.enums.TipoSalidaConsumo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "salida_consumo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalidaConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salida")
    private Long idSalida;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_salida", nullable = false)
    private TipoSalidaConsumo tipoSalida;

    @Column(name = "destino")
    private String destino;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "saldo_restante", nullable = false)
    private Integer saldoRestante;

    @Column(name = "anulado", nullable = false)
    private Boolean anulado = false;
}