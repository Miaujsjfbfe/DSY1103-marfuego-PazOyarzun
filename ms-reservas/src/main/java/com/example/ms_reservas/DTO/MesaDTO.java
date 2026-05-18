package com.example.ms_reservas.DTO;

import lombok.*;

@Data
public class MesaDTO {

    private Long id;
    private Integer numero;
    private Integer capacidad;
    private String estado;

    private String sector;
    private Long localId;

}