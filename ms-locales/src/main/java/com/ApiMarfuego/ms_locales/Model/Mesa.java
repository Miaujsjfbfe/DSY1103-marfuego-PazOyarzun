package com.ApiMarfuego.ms_locales.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;
    private Integer capacidad;
    private String estado; // LIBRE, OCUPADA, EN_LIMPIEZA
    //cambiar a enum

    @ManyToOne
    @JoinColumn(name = "local_id")
    private Local local;
}