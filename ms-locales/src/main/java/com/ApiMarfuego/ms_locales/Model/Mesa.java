package com.ApiMarfuego.ms_locales.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "Mesa",
        description = "Mesas del local")

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="mesas")
public class Mesa {

    @Schema(
            title = "Identificador único",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(
            title = "Número de mesa",
            example = "5")
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Schema(
            title = "Capacidad máxima de personas",
            example = "4")
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Schema(
            title = "Estado actual de la mesa",
            example = "LIBRE")
    @Column(name = "estado", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoMesa estado;

    @Schema(
            title = "Sector de ubicación de la mesa",
            example = "TERRAZA")
    @Enumerated(EnumType.STRING)
    @Column(name = "sector", nullable = false, length = 20)
    private Sector sector;

    @Schema(
            title = "ID del local asociado",
            example = "1")
    @Column(name = "local_id", nullable = false)
    private Long localId;


}