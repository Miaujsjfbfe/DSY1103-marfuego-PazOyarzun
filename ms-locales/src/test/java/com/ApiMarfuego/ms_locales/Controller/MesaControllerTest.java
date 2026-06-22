package com.ApiMarfuego.ms_locales.Controller;

import com.ApiMarfuego.ms_locales.DTO.MesaRequestDTO;
import com.ApiMarfuego.ms_locales.Model.EstadoMesa;
import com.ApiMarfuego.ms_locales.Model.Mesa;
import com.ApiMarfuego.ms_locales.Model.Sector;
import com.ApiMarfuego.ms_locales.Service.MesaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class MesaControllerTest {

    @Mock
    private MesaService service;

    @InjectMocks
    private MesaController controller;


    @Test
    @DisplayName("Debe listar todas las mesas")
    void debeListarMesas() {

        List<Mesa> mesas = List.of(
                new Mesa(),
                new Mesa()
        );

        when(service.listar())
                .thenReturn(mesas);

        ResponseEntity<?> response =
                controller.listar();

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(mesas,
                response.getBody());
    }


    @Test
    @DisplayName("Debe buscar una mesa por ID")
    void debeBuscarMesaPorId() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);

        when(service.buscarPorId(1L))
                .thenReturn(mesa);

        ResponseEntity<?> response =
                controller.buscarPorId(1L);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(mesa,
                response.getBody());
    }


    @Test
    @DisplayName("Debe crear una mesa")
    void debeCrearMesa() {

        MesaRequestDTO dto = new MesaRequestDTO();

        dto.setNumero(1);
        dto.setCapacidad(4);
        dto.setEstado(EstadoMesa.LIBRE);
        dto.setSector(Sector.INTERIOR);
        dto.setLocalId(1L);

        Mesa mesa = new Mesa();
        mesa.setId(1L);

        when(service.guardar(any(Mesa.class)))
                .thenReturn(mesa);

        ResponseEntity<?> response =
                controller.guardar(dto);

        assertEquals(201,
                response.getStatusCode().value());

        assertEquals(mesa,
                response.getBody());
    }


    @Test
    @DisplayName("Debe actualizar una mesa")
    void debeActualizarMesa() {

        MesaRequestDTO dto = new MesaRequestDTO();

        dto.setNumero(2);
        dto.setCapacidad(6);
        dto.setEstado(EstadoMesa.LIBRE);
        dto.setSector(Sector.TERRAZA);
        dto.setLocalId(1L);

        Mesa actualizada = new Mesa();
        actualizada.setId(1L);

        when(service.actualizar(
                eq(1L),
                any(Mesa.class)))
                .thenReturn(actualizada);

        ResponseEntity<?> response =
                controller.actualizar(1L, dto);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(actualizada,
                response.getBody());
    }


    @Test
    @DisplayName("Debe eliminar una mesa")
    void debeEliminarMesa() {

        ResponseEntity<?> response =
                controller.eliminar(1L);

        assertEquals(204,
                response.getStatusCode().value());

        verify(service)
                .eliminar(1L);
    }


    @Test
    @DisplayName("Debe ocupar una mesa")
    void debeOcuparMesa() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);

        when(service.ocuparMesa(1L))
                .thenReturn(mesa);

        ResponseEntity<?> response =
                controller.ocuparMesa(1L);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(mesa,
                response.getBody());
    }


    @Test
    @DisplayName("Debe retornar 404 al ocupar una mesa inexistente")
    void debeRetornar404AlOcuparMesaInexistente() {

        when(service.ocuparMesa(1L))
                .thenThrow(
                        new RuntimeException(
                                "La mesa no existe."
                        ));

        ResponseEntity<?> response =
                controller.ocuparMesa(1L);

        assertEquals(404,
                response.getStatusCode().value());

        assertEquals(
                "La mesa no existe.",
                response.getBody());
    }


    @Test
    @DisplayName("Debe liberar una mesa")
    void debeLiberarMesa() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);

        when(service.liberarMesa(1L))
                .thenReturn(mesa);

        ResponseEntity<?> response =
                controller.liberarMesa(1L);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(mesa,
                response.getBody());
    }

    @Test
    @DisplayName("Debe retornar 404 al liberar una mesa inexistente")
    void debeRetornar404AlLiberarMesaInexistente() {

        when(service.liberarMesa(1L))
                .thenThrow(
                        new RuntimeException(
                                "La mesa no existe."
                        ));

        ResponseEntity<?> response =
                controller.liberarMesa(1L);

        assertEquals(404,
                response.getStatusCode().value());

        assertEquals(
                "La mesa no existe.",
                response.getBody());
    }


    @Test
    @DisplayName("Debe listar mesas por local")
    void debeListarMesasPorLocal() {

        List<Mesa> mesas = List.of(
                new Mesa(),
                new Mesa()
        );

        when(service.buscarPorLocal(1L))
                .thenReturn(mesas);

        ResponseEntity<?> response =
                controller.buscarPorLocal(1L);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(mesas,
                response.getBody());
    }



}
