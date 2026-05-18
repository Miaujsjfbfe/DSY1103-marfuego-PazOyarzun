package com.example.ms_pedidos.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PedidoRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio.")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    private String nombreCliente;

    @NotNull(message = "El local es obligatorio.")
    private Long localId;

    //Los otros atributos se calculan solo en los metodos de crear
}
