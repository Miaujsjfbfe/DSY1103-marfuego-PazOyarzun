package com.ApiMarfuego.ms_locales.Controller;

import com.ApiMarfuego.ms_locales.DTO.LocalRequestDTO;
import com.ApiMarfuego.ms_locales.Model.Local;
import com.ApiMarfuego.ms_locales.Service.LocalService;
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
public class LocalControllerTest {

    @Mock
    private LocalService service;

    @InjectMocks
    private LocalController controller;

    @Test
    @DisplayName("Debe listar todos los locales")
    void debeListarLocales() {

        List<Local> locales = List.of(
                new Local(),
                new Local()
        );

        when(service.listarLocales())
                .thenReturn(locales);

        ResponseEntity<?> response =
                controller.listarLocales();

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(locales,
                response.getBody());
    }


    @Test
    @DisplayName("Debe buscar un local por ID")
    void debeBuscarLocalPorId() {

        Local local = new Local();
        local.setId(1L);
        local.setNombre("Marfuego");

        when(service.buscarPorId(1L))
                .thenReturn(local);

        ResponseEntity<?> response =
                controller.buscarPorId(1L);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(local,
                response.getBody());
    }


    @Test
    @DisplayName("Debe crear un local")
    void debeCrearLocal() {

        LocalRequestDTO dto =
                new LocalRequestDTO();

        dto.setNombre("Marfuego");
        dto.setCiudad("Puerto Montt");

        Local local = new Local();
        local.setId(1L);
        local.setNombre("Marfuego");
        local.setCiudad("Puerto Montt");

        when(service.crear(any(Local.class)))
                .thenReturn(local);

        ResponseEntity<?> response =
                controller.crear(dto);

        assertEquals(201,
                response.getStatusCode().value());

        assertEquals(local,
                response.getBody());
    }


    @Test
    @DisplayName("Debe actualizar un local")
    void debeActualizarLocal() {

        LocalRequestDTO dto =
                new LocalRequestDTO();

        dto.setNombre("Nuevo");
        dto.setCiudad("Puerto Varas");

        Local actualizado = new Local();

        actualizado.setId(1L);
        actualizado.setNombre("Nuevo");
        actualizado.setCiudad("Puerto Varas");

        when(service.actualizar(
                eq(1L),
                any(Local.class)))
                .thenReturn(actualizado);

        ResponseEntity<?> response =
                controller.actualizar(1L, dto);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(actualizado,
                response.getBody());
    }


    @Test
    @DisplayName("Debe eliminar un local")
    void debeEliminarLocal() {

        ResponseEntity<?> response =
                controller.eliminar(1L);

        assertEquals(204,
                response.getStatusCode().value());

        verify(service)
                .eliminar(1L);
    }









}
