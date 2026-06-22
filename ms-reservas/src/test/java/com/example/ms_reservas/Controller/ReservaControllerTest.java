package com.example.ms_reservas.Controller;

import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import com.example.ms_reservas.Service.ReservaService;
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
class ReservaControllerTest {

    @Mock
    private ReservaService service;

    @InjectMocks
    private ReservaController controller;


    @Test
    @DisplayName("Debe listar reservas")
    void debeListarReservas() {

        List<Reserva> reservas = List.of(
                new Reserva(),
                new Reserva()
        );

        when(service.listarReservas())
                .thenReturn(reservas);

        ResponseEntity<?> response =
                controller.listarReservas();

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                reservas,
                response.getBody());
    }


    @Test
    @DisplayName("Debe buscar reserva por ID")
    void debeBuscarReservaPorId() {

        Reserva reserva = new Reserva();
        reserva.setId(1L);

        when(service.buscarPorId(1L))
                .thenReturn(reserva);

        ResponseEntity<?> response =
                controller.buscarPorId(1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                reserva,
                response.getBody());
    }


    @Test
    @DisplayName("Debe crear reserva")
    void debeCrearReserva() {

        Reserva reserva = new Reserva();
        reserva.setId(1L);

        when(service.crearReserva(any(Reserva.class)))
                .thenReturn(reserva);

        ResponseEntity<?> response =
                controller.crearReserva(
                        new Reserva());

        assertEquals(
                201,
                response.getStatusCode().value());

        assertEquals(
                reserva,
                response.getBody());
    }


    @Test
    @DisplayName("Debe actualizar reserva")
    void debeActualizarReserva() {

        Reserva reserva = new Reserva();
        reserva.setId(1L);

        when(service.actualizar(
                eq(1L),
                any(Reserva.class)))
                .thenReturn(reserva);

        ResponseEntity<?> response =
                controller.actualizar(
                        1L,
                        new Reserva());

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                reserva,
                response.getBody());
    }


    @Test
    @DisplayName("Debe eliminar reserva")
    void debeEliminarReserva() {

        ResponseEntity<?> response =
                controller.eliminar(1L);

        assertEquals(
                204,
                response.getStatusCode().value());

        verify(service)
                .eliminar(1L);
    }


    @Test
    @DisplayName("Debe cambiar estado de reserva")
    void debeCambiarEstadoReserva() {

        Reserva reserva = new Reserva();

        reserva.setId(1L);
        reserva.setEstado(
                EstadoReserva.CONFIRMADA);

        when(service.cambiarEstado(
                1L,
                EstadoReserva.CONFIRMADA))
                .thenReturn(reserva);

        ResponseEntity<?> response =
                controller.cambiarEstado(
                        1L,
                        EstadoReserva.CONFIRMADA);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                reserva,
                response.getBody());
    }


    @Test
    @DisplayName("Debe listar reservas por local")
    void debeListarReservasPorLocal() {

        List<Reserva> reservas = List.of(
                new Reserva(),
                new Reserva()
        );

        when(service.listarPorLocal(1L))
                .thenReturn(reservas);

        ResponseEntity<?> response =
                controller.listarPorLocal(1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                reservas,
                response.getBody());
    }


    @Test
    @DisplayName("Debe listar reservas por estado")
    void debeListarReservasPorEstado() {

        List<Reserva> reservas = List.of(
                new Reserva(),
                new Reserva()
        );

        when(service.listarPorEstado(
                EstadoReserva.PENDIENTE))
                .thenReturn(reservas);

        ResponseEntity<?> response =
                controller.listarPorEstado(
                        EstadoReserva.PENDIENTE);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                reservas,
                response.getBody());
    }


}