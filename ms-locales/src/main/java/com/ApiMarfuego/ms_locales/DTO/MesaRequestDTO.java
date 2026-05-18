package com.ApiMarfuego.ms_locales.DTO;

import com.ApiMarfuego.ms_locales.Model.EstadoMesa;
import com.ApiMarfuego.ms_locales.Model.Sector;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MesaRequestDTO {


    @NotNull(message = "El número de mesa es obligatorio.")
    @Min(value = 1, message = "El número debe ser mayor a 0.")
    private Integer numero;

    @NotNull(message = "La capacidad es obligatoria.")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0.")
    private Integer capacidad;

    @NotNull(message = "El estado es obligatorio.")
    private EstadoMesa estado;

    @NotNull(message = "El sector es obligatorio.")
    private Sector sector;

    @NotNull(message = "El local es obligatorio")
    private Long localId;


}
