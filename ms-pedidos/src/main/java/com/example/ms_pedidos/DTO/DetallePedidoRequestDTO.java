package com.example.ms_pedidos.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DetallePedidoRequestDTO {

    @NotNull(message = "El pedido es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El plato es obligatorio")
    private Long platoId;

    @NotNull(message = "La cantidad es obligatorio")
    private Integer cantidad;

}
