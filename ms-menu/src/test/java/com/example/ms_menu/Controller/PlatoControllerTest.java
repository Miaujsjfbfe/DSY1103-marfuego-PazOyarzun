package com.example.ms_menu.Controller;

import com.example.ms_menu.DTO.PlatoRequestDTO;
import com.example.ms_menu.Model.Plato;
import com.example.ms_menu.Service.PlatoService;
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

public class PlatoControllerTest {
    @Mock
    private PlatoService service;

    @InjectMocks
    private PlatoController controller;


    @Test
    @DisplayName("Debe listar todos los platos")
    void debeListarPlatos() {

        List<Plato> platos = List.of(
                new Plato(),
                new Plato()
        );

        when(service.listarPlatos())
                .thenReturn(platos);

        ResponseEntity<?> response =
                controller.listarPlatos();

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(platos,
                response.getBody());
    }


    @Test
    @DisplayName("Debe buscar plato por ID")
    void debeBuscarPlatoPorId() {

        Plato plato = new Plato();
        plato.setId(1L);

        when(service.buscarPorId(1L))
                .thenReturn(plato);

        ResponseEntity<?> response =
                controller.buscarPorId(1L);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(plato,
                response.getBody());
    }


    @Test
    @DisplayName("Debe crear un plato")
    void debeCrearPlato() {

        PlatoRequestDTO dto =
                new PlatoRequestDTO();

        dto.setNombre("Pizza");
        dto.setDescripcion("Napolitana");
        dto.setPrecio(10000.0);
        dto.setDisponible(true);
        dto.setLocalId(1L);

        Plato plato = new Plato();
        plato.setId(1L);

        when(service.crearPlato(any(Plato.class)))
                .thenReturn(plato);

        ResponseEntity<?> response =
                controller.guardar(dto);

        assertEquals(201,
                response.getStatusCode().value());

        assertEquals(plato,
                response.getBody());
    }


    @Test
    @DisplayName("Debe actualizar un plato")
    void debeActualizarPlato() {

        PlatoRequestDTO dto =
                new PlatoRequestDTO();

        dto.setNombre("Pizza");
        dto.setDescripcion("Nueva");
        dto.setPrecio(12000.0);
        dto.setDisponible(true);
        dto.setLocalId(1L);

        Plato plato = new Plato();
        plato.setId(1L);

        when(service.actualizar(
                eq(1L),
                any(Plato.class)))
                .thenReturn(plato);

        ResponseEntity<?> response =
                controller.actualizar(1L, dto);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(plato,
                response.getBody());
    }


    @Test
    @DisplayName("Debe eliminar un plato")
    void debeEliminarPlato() {

        ResponseEntity<?> response =
                controller.eliminar(1L);

        assertEquals(204,
                response.getStatusCode().value());

        verify(service)
                .eliminar(1L);
    }


    @Test
    @DisplayName("Debe cambiar disponibilidad")
    void debeCambiarDisponibilidad() {

        Plato plato = new Plato();

        plato.setId(1L);
        plato.setDisponible(false);

        when(service.cambiarDisponibilidad(
                1L,
                false))
                .thenReturn(plato);

        ResponseEntity<?> response =
                controller.cambiarDisponibilidad(
                        1L,
                        false);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(plato,
                response.getBody());
    }


    @Test
    @DisplayName("Debe listar platos disponibles")
    void debeListarDisponibles() {

        List<Plato> platos = List.of(
                new Plato(),
                new Plato()
        );

        when(service.listarDisponibles())
                .thenReturn(platos);

        ResponseEntity<?> response =
                controller.listarDisponibles();

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(platos,
                response.getBody());
    }


    @Test
    @DisplayName("Debe listar platos por local")
    void debeListarPorLocal() {

        List<Plato> platos = List.of(
                new Plato(),
                new Plato()
        );

        when(service.listarPorLocal(1L))
                .thenReturn(platos);

        ResponseEntity<?> response =
                controller.listarPorLocal(1L);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(platos,
                response.getBody());
    }


    @Test
    @DisplayName("Debe listar platos disponibles por local")
    void debeListarDisponiblesPorLocal() {

        List<Plato> platos = List.of(
                new Plato(),
                new Plato()
        );

        when(service.listarDisponiblesPorLocal(1L))
                .thenReturn(platos);

        ResponseEntity<?> response =
                controller.listarDisponiblesPorLocal(1L);

        assertEquals(200,
                response.getStatusCode().value());

        assertEquals(platos,
                response.getBody());
    }



}
