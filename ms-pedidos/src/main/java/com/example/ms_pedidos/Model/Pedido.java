package com.example.ms_pedidos.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(
        name = "Pedido",
        description = "Pedidos realizados por clientes.")

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
public class Pedido {

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
            example = "Juan Pérez")
    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    @Schema(
            title = "Total del pedido",
            example = "15990",
            accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "total", nullable = false)
    private Double total;

    @Schema(
            title = "Estado actual del pedido",
            example = "PENDIENTE",
            accessMode = Schema.AccessMode.READ_ONLY)
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoPedido estado;

    @Schema(
            title = "Fecha de creación del pedido",
            example = "2026-06-17T14:30:00",
            accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    @Schema(
            title = "ID del local asociado",
            example = "1"
    )
    @Column(name = "local_id", nullable = false)
    private Long localId;
}