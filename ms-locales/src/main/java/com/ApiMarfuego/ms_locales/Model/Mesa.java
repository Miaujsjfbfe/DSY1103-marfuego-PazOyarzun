package com.ApiMarfuego.ms_locales.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    private EstadoMesa estado;

    @Enumerated(EnumType.STRING)
    private Sector sector;

    private Long localId;


}