package com.example.ms_reservas.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCliente;
    private LocalDateTime fechaReserva;
    private Integer cantidadPersonas;
    private Long mesaId;
    private Long localId;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;
}


