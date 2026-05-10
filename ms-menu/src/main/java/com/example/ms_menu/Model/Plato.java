package com.example.ms_menu.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    private String descripcion;
    private Double precio;
    private Boolean disponible;


    private Long localId;

}
