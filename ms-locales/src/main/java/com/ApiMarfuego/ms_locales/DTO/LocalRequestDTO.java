package com.ApiMarfuego.ms_locales.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LocalRequestDTO {


    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria.")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String ciudad;

}
