package com.example.ms_reservas.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(
        name = "Reserva",
        description = "Reservas de mesas.")

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservas")
public class Reserva {

    @Schema(
            title = "Identificador único",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(
            title = "Nombre del cliente",
            example = "Juan Pérez"
    )
    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    @Schema(
            title = "Fecha y hora de la reserva",
            example = "2026-06-20T20:00:00"
    )
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Schema(
            title = "Cantidad de personas",
            example = "4"
    )
    @Column(name = "cantidad_personas", nullable = false)
    private Integer cantidadPersonas;

    @Schema(
            title = "ID de la mesa",
            example = "3"
    )
    @Column(name = "mesa_id", nullable = false)
    private Long mesaId;

    @Schema(
            title = "ID del local",
            example = "1"
    )
    @Column(name = "local_id", nullable = false)
    private Long localId;

    @Schema(
            title = "Estado de la reserva",
            example = "PENDIENTE",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoReserva estado;
}


