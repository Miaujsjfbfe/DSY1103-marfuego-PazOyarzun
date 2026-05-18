package com.ApiMarfuego.ms_locales.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="mesas")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "estado", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoMesa estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "sector", nullable = false, length = 20)
    private Sector sector;

    @Column(name = "local_id", nullable = false)
    private Long localId;


}