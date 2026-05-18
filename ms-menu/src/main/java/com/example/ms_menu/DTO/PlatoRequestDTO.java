package com.example.ms_menu.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PlatoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria.")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio.")
    @Min(value = 1, message = "El precio debe ser mayor a 0.")
    private Double precio;

    private Boolean disponible;

    @NotNull(message = "El ID del local es obligatorio.")
    private Long localId;

}
