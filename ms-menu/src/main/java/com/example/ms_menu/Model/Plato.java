package com.example.ms_menu.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(
        name = "Plato",
        description = "Platos del menú.")

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="platos")
public class Plato {

    @Schema(
            title = "Identificador único",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(
            title = "Nombre del plato",
            example = "Fideos con salsa")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;


    @Schema(
            title = "Descripción del plato.",
            example = "Fideos con salsa roja de carne.")
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Schema(
            title = "Precio del plato.",
            example = "8000")
    @Column(name = "precio", nullable = false)
    private Double precio;

    @Schema(
            title = "Estado disponibilidad del plato.",
            example = "true")
    @Column(name = "disponible", nullable = false)
    private Boolean disponible;

    @Schema(
            title = "id de Local asociado.",
            example = "2")
    @Column(name = "local_id", nullable = false)
    private Long localId;

}
