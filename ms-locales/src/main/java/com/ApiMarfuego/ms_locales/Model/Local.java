package com.ApiMarfuego.ms_locales.Model;

import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "Local",
        description = "Locales de restaurante")

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="locales")
public class Local {

    @Schema(
            title = "Identificador único",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Schema(
            title = "Nombre del local",
            example = "MarFuego Centro")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;


    @Schema(
            title = "Ciudad del local",
            example = "Puerto Montt")
    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad;


}