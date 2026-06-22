package com.example.ms_reservas.Service;

import com.example.ms_reservas.DTO.MesaDTO;
import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import com.example.ms_reservas.Repository.ReservaRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.RequestBodyUriSpec bodyUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    // Repositorio falso
    @Mock
    private ReservaRepository repository;

    // Inyectar mocks
    @InjectMocks
    private ReservaService service;


    @Test
    @DisplayName("Debe listar reservas")
    void debeListarReservas() {

        List<Reserva> reservas = List.of(
                new Reserva(),
                new Reserva()
        );

        when(repository.findAll())
                .thenReturn(reservas);

        List<Reserva> resultado =
                service.listarReservas();

        assertEquals(2, resultado.size());

        verify(repository).findAll();
    }


    @Test
    @DisplayName("Debe buscar reserva por ID")
    void debeBuscarReservaPorId() {

        Reserva reserva = new Reserva();
        reserva.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(reserva));

        Reserva resultado =
                service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(repository).findById(1L);
    }


    @Test
    @DisplayName("Debe lanzar excepción si la reserva no existe")
    void debeLanzarExcepcionSiReservaNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.buscarPorId(1L));

        assertEquals(
                "La reserva no existe.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe listar reservas por local")
    void debeListarReservasPorLocal() {

        List<Reserva> reservas =
                List.of(new Reserva());

        when(repository.findByLocalId(1L))
                .thenReturn(reservas);

        List<Reserva> resultado =
                service.listarPorLocal(1L);

        assertEquals(1,
                resultado.size());

        verify(repository)
                .findByLocalId(1L);
    }


    @Test
    @DisplayName("Debe listar reservas por estado")
    void debeListarReservasPorEstado() {

        List<Reserva> reservas =
                List.of(new Reserva());

        when(repository.findByEstado(
                EstadoReserva.PENDIENTE))
                .thenReturn(reservas);

        List<Reserva> resultado =
                service.listarPorEstado(
                        EstadoReserva.PENDIENTE);

        assertEquals(1,
                resultado.size());

        verify(repository)
                .findByEstado(
                        EstadoReserva.PENDIENTE);
    }


    @Test
    @DisplayName("Debe cambiar estado de reserva")
    void debeCambiarEstadoReserva() {

        Reserva reserva = new Reserva();

        reserva.setId(1L);
        reserva.setEstado(
                EstadoReserva.PENDIENTE);

        when(repository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(repository.save(any(Reserva.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        Reserva resultado =
                service.cambiarEstado(
                        1L,
                        EstadoReserva.CONFIRMADA);

        assertEquals(
                EstadoReserva.CONFIRMADA,
                resultado.getEstado());

        verify(repository)
                .save(reserva);
    }


    @Test
    @DisplayName("Debe retornar un WebClient")
    void debeRetornarWebClient() {

        WebClient client =
                service.getWebClient();

        assertNotNull(client);
    }


    @Test
    @DisplayName("Debe crear reserva correctamente")
    void debeCrearReservaCorrectamente() {

        Reserva reserva = new Reserva();
        reserva.setNombreCliente("Juan");
        reserva.setMesaId(1L);
        reserva.setLocalId(1L);
        reserva.setCantidadPersonas(4);
        reserva.setFechaReserva(
                LocalDateTime.now().plusDays(1));

        MesaDTO mesa = new MesaDTO();
        mesa.setId(1L);
        mesa.setCapacidad(6);
        mesa.setEstado("LIBRE");
        mesa.setLocalId(1L);

        ReservaService spyService =
                spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get())
                .thenReturn(uriSpec);

        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(MesaDTO.class))
                .thenReturn(Mono.just(mesa));

        when(webClient.put())
                .thenReturn(bodyUriSpec);

        when(bodyUriSpec.uri(anyString()))
                .thenReturn(bodyUriSpec);

        when(bodyUriSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(Void.class))
                .thenReturn(Mono.empty());

        when(repository.save(any(Reserva.class)))
                .thenAnswer(i -> i.getArgument(0));

        Reserva resultado =
                spyService.crearReserva(reserva);

        assertNotNull(resultado);

        assertEquals(
                EstadoReserva.PENDIENTE,
                resultado.getEstado());

        verify(repository)
                .save(any(Reserva.class));
    }


    @Test
    @DisplayName("Debe lanzar excepción si la mesa no está disponible")
    void debeLanzarExcepcionSiMesaNoDisponible() {

        Reserva reserva = new Reserva();
        reserva.setMesaId(1L);
        reserva.setLocalId(1L);
        reserva.setCantidadPersonas(4);
        reserva.setFechaReserva(
                LocalDateTime.now().plusDays(1));

        MesaDTO mesa = new MesaDTO();
        mesa.setId(1L);
        mesa.setEstado("OCUPADA");
        mesa.setCapacidad(6);
        mesa.setLocalId(1L);

        ReservaService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(MesaDTO.class))
                .thenReturn(Mono.just(mesa));

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> spyService.crearReserva(reserva));

        assertEquals(
                "La mesa no esta disponible.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe lanzar excepción si la mesa pertenece a otro local")
    void debeLanzarExcepcionSiMesaDeOtroLocal() {

        Reserva reserva = new Reserva();
        reserva.setMesaId(1L);
        reserva.setLocalId(1L);
        reserva.setCantidadPersonas(4);
        reserva.setFechaReserva(
                LocalDateTime.now().plusDays(1));

        MesaDTO mesa = new MesaDTO();
        mesa.setId(1L);
        mesa.setEstado("LIBRE");
        mesa.setCapacidad(6);
        mesa.setLocalId(99L);

        ReservaService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(MesaDTO.class))
                .thenReturn(Mono.just(mesa));

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> spyService.crearReserva(reserva));

        assertEquals(
                "La mesa no pertenece al local.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe lanzar excepción si la cantidad de personas es inválida")
    void debeLanzarExcepcionSiCantidadInvalida() {

        Reserva reserva = new Reserva();
        reserva.setMesaId(1L);
        reserva.setLocalId(1L);
        reserva.setCantidadPersonas(0);
        reserva.setFechaReserva(
                LocalDateTime.now().plusDays(1));

        MesaDTO mesa = new MesaDTO();
        mesa.setId(1L);
        mesa.setEstado("LIBRE");
        mesa.setCapacidad(6);
        mesa.setLocalId(1L);

        ReservaService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(MesaDTO.class))
                .thenReturn(Mono.just(mesa));

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> spyService.crearReserva(reserva));

        assertEquals(
                "La cantidad de personas debe ser mayor a cero.",
                ex.getMessage());
    }



    @Test
    @DisplayName("Debe lanzar excepción si la mesa no tiene capacidad suficiente")
    void debeLanzarExcepcionSiCapacidadInsuficiente() {

        Reserva reserva = new Reserva();
        reserva.setMesaId(1L);
        reserva.setLocalId(1L);
        reserva.setCantidadPersonas(6);
        reserva.setFechaReserva(
                LocalDateTime.now().plusDays(1));

        MesaDTO mesa = new MesaDTO();
        mesa.setId(1L);
        mesa.setEstado("LIBRE");
        mesa.setCapacidad(4);
        mesa.setLocalId(1L);

        ReservaService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(MesaDTO.class))
                .thenReturn(Mono.just(mesa));

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> spyService.crearReserva(reserva));

        assertEquals(
                "La mesa no tiene la capacidad de personas necesaria.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe lanzar excepción si la fecha es inválida")
    void debeLanzarExcepcionSiFechaInvalida() {

        Reserva reserva = new Reserva();
        reserva.setMesaId(1L);
        reserva.setLocalId(1L);
        reserva.setCantidadPersonas(4);
        reserva.setFechaReserva(
                LocalDateTime.now().minusDays(1));

        MesaDTO mesa = new MesaDTO();
        mesa.setId(1L);
        mesa.setEstado("LIBRE");
        mesa.setCapacidad(6);
        mesa.setLocalId(1L);

        ReservaService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(MesaDTO.class))
                .thenReturn(Mono.just(mesa));

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> spyService.crearReserva(reserva));

        assertEquals(
                "La fecha no es válida.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe eliminar reserva correctamente")
    void debeEliminarReserva() {

        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setMesaId(10L);

        ReservaService spyService =
                spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(repository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(webClient.put())
                .thenReturn(bodyUriSpec);

        when(bodyUriSpec.uri(anyString()))
                .thenReturn(bodyUriSpec);

        when(bodyUriSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(Void.class))
                .thenReturn(Mono.empty());

        spyService.eliminar(1L);

        verify(repository)
                .deleteById(1L);
    }


    @Test
    @DisplayName("Debe actualizar reserva correctamente")
    void debeActualizarReservaCorrectamente() {

        Reserva existente = new Reserva();
        existente.setId(1L);
        existente.setMesaId(1L);
        existente.setLocalId(1L);
        existente.setEstado(EstadoReserva.PENDIENTE);

        Reserva datos = new Reserva();
        datos.setNombreCliente("Pedro");
        datos.setMesaId(1L);
        datos.setLocalId(1L);
        datos.setCantidadPersonas(4);
        datos.setFechaReserva(
                LocalDateTime.now().plusDays(1));

        MesaDTO mesa = new MesaDTO();
        mesa.setId(1L);
        mesa.setEstado("LIBRE");
        mesa.setCapacidad(6);
        mesa.setLocalId(1L);

        ReservaService spyService =
                spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(repository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(webClient.get())
                .thenReturn(uriSpec);

        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(MesaDTO.class))
                .thenReturn(Mono.just(mesa));

        when(repository.save(any(Reserva.class)))
                .thenAnswer(i -> i.getArgument(0));

        Reserva resultado =
                spyService.actualizar(
                        1L,
                        datos);

        assertEquals(
                "Pedro",
                resultado.getNombreCliente());

        verify(repository)
                .save(existente);
    }


    @Test
    @DisplayName("Debe lanzar excepción si la reserva está cancelada")
    void debeLanzarExcepcionSiReservaCancelada() {

        Reserva existente = new Reserva();
        existente.setId(1L);
        existente.setEstado(EstadoReserva.CANCELADA);

        Reserva datos = new Reserva();
        datos.setMesaId(1L);

        MesaDTO mesa = new MesaDTO();
        mesa.setId(1L);
        mesa.setEstado("LIBRE");
        mesa.setCapacidad(6);
        mesa.setLocalId(1L);

        ReservaService spyService =
                spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(repository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(webClient.get())
                .thenReturn(uriSpec);

        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(MesaDTO.class))
                .thenReturn(Mono.just(mesa));

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> spyService.actualizar(
                                1L,
                                datos));

        assertEquals(
                "No se puede modificar una reserva cancelada o finalizada.",
                ex.getMessage());
    }



}