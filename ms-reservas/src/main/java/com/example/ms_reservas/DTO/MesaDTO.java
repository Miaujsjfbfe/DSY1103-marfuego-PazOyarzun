package com.example.ms_reservas.DTO;

public class MesaDTO {

    private Long id;
    private Integer numero;
    private Integer capacidad;
    private String estado;

    private String sector;
    private Long localId;

    public MesaDTO() {
    }

    public MesaDTO(Long id, Integer numero, Integer capacidad, String estado, String sector, Long localId) {

        this.id = id;
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado;
        this.sector = sector;
        this.localId = localId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }
}