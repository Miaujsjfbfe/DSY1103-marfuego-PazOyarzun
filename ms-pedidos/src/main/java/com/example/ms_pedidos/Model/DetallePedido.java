package com.example.ms_pedidos.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(
        name = "Detalle Pedido",
        description = "Platos dentro del pedido.")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalles_pedidos")
public class DetallePedido {

    @Schema(
            title = "Identificador único",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(
            title = "ID del plato asociado",
            example = "5"
    )
    @Column(name = "plato_id", nullable = false)
    private Long platoId;

    @Schema(
            title = "Nombre del plato",
            example = "Salmón a la plancha",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Column(name = "nombre_plato", nullable = false, length = 100)
    private String nombrePlato;

    @Schema(
            title = "Precio unitario del plato",
            example = "12990",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Column(name = "precio", nullable = false)
    private Double precio;

    @Schema(
            title = "Cantidad solicitada",
            example = "2"
    )
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Schema(
            title = "Subtotal calculado",
            example = "25980",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;


    @Schema(
            title = "Pedido asociado",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}