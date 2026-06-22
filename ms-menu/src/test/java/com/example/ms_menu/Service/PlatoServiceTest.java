package com.example.ms_menu.Service;

import com.example.ms_menu.Model.Plato;
import com.example.ms_menu.Repository.PlatoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.ms_menu.DTO.LocalDTO;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class PlatoServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;



    //Repositorio falso
    @Mock
    private PlatoRepository repository;

    //Inyectar datos falsos
    @InjectMocks
    private PlatoService service;


    @Test
    @DisplayName("Debe buscar plato por ID")
    void debeBuscarPlatoPorId() {

        Plato plato = new Plato();
        plato.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(plato));

        Plato resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(repository).findById(1L);
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando el plato no existe")
    void debeLanzarExcepcionSiNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.buscarPorId(1L)
        );

        assertEquals(
                "El plato no existe.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe listar todos los platos")
    void debeListarPlatos() {

        List<Plato> lista = List.of(
                new Plato(),
                new Plato()
        );

        when(repository.findAll())
                .thenReturn(lista);

        List<Plato> resultado =
                service.listarPlatos();

        assertEquals(2, resultado.size());

        verify(repository).findAll();
    }


    @Test
    @DisplayName("Debe cambiar disponibilidad")
    void debeCambiarDisponibilidad() {

        Plato plato = new Plato();

        plato.setId(1L);
        plato.setDisponible(true);

        when(repository.findById(1L))
                .thenReturn(Optional.of(plato));

        when(repository.save(any(Plato.class)))
                .thenReturn(plato);

        Plato resultado =
                service.cambiarDisponibilidad(
                        1L,
                        false);

        assertFalse(resultado.getDisponible());

        verify(repository).save(plato);
    }


    @Test
    @DisplayName("Debe eliminar un plato")
    void debeEliminarPlato() {

        Plato plato = new Plato();

        when(repository.findById(1L))
                .thenReturn(Optional.of(plato));

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }


    @Test
    @DisplayName("Debe listar platos disponibles")
    void debeListarDisponibles() {

        List<Plato> lista = List.of(
                new Plato(),
                new Plato()
        );

        when(repository.findByDisponibleTrue())
                .thenReturn(lista);

        List<Plato> resultado =
                service.listarDisponibles();

        assertEquals(2,
                resultado.size());
    }

    //WEB CLIENT TESTS
    @Test
    @DisplayName("Debe crear un plato correctamente")
    void debeCrearPlatoCorrectamente() {

        // Datos de prueba
        Plato plato = new Plato();
        plato.setNombre("Pizza");
        plato.setDescripcion("Pizza napolitana");
        plato.setPrecio(12000.0);
        plato.setLocalId(1L);

        // Crear spy del service
        PlatoService spyService = spy(service);

        // Mock WebClient
        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get())
                .thenReturn(uriSpec);

        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(LocalDTO.class))
                .thenReturn(Mono.just(new LocalDTO()));

        // Mock repository
        when(repository.save(any(Plato.class)))
                .thenReturn(plato);

        // Ejecutar
        Plato resultado = spyService.crearPlato(plato);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals("Pizza", resultado.getNombre());
        assertEquals(12000.0, resultado.getPrecio());

        verify(repository).save(any(Plato.class));
    }


    @Test
    @DisplayName("Debe asignar disponible true por defecto")
    void debeAsignarDisponibleTruePorDefecto() {

        Plato plato = new Plato();
        plato.setNombre("Pizza");
        plato.setPrecio(12000.0);
        plato.setLocalId(1L);
        plato.setDisponible(null);

        PlatoService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get())
                .thenReturn(uriSpec);

        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(LocalDTO.class))
                .thenReturn(Mono.just(new LocalDTO()));

        when(repository.save(any(Plato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Plato resultado = spyService.crearPlato(plato);

        assertTrue(resultado.getDisponible());
    }


    @Test
    @DisplayName("Debe actualizar un plato correctamente")
    void debeActualizarPlatoCorrectamente() {

        Plato existente = new Plato();
        existente.setId(1L);
        existente.setNombre("Pizza Antigua");
        existente.setPrecio(10000.0);
        existente.setLocalId(1L);

        Plato actualizado = new Plato();
        actualizado.setNombre("Pizza Nueva");
        actualizado.setDescripcion("Nueva receta");
        actualizado.setPrecio(15000.0);
        actualizado.setDisponible(true);
        actualizado.setLocalId(1L);

        PlatoService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(LocalDTO.class))
                .thenReturn(Mono.just(new LocalDTO()));

        when(repository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(repository.save(any(Plato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Plato resultado = spyService.actualizar(1L, actualizado);

        assertEquals("Pizza Nueva", resultado.getNombre());
        assertEquals(15000.0, resultado.getPrecio());

        verify(repository).save(any(Plato.class));
    }


    @Test
    @DisplayName("Debe listar platos por local")
    void debeListarPlatosPorLocal() {

        Plato plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Pizza");
        plato.setLocalId(1L);

        List<Plato> lista = List.of(plato);

        PlatoService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(LocalDTO.class))
                .thenReturn(Mono.just(new LocalDTO()));

        when(repository.findByLocalId(1L))
                .thenReturn(lista);

        List<Plato> resultado = spyService.listarPorLocal(1L);

        assertEquals(1, resultado.size());

        verify(repository).findByLocalId(1L);
    }


    @Test
    @DisplayName("Debe listar platos disponibles por local")
    void debeListarDisponiblesPorLocal() {

        Plato plato = new Plato();
        plato.setId(1L);
        plato.setDisponible(true);
        plato.setLocalId(1L);

        List<Plato> lista = List.of(plato);

        PlatoService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(LocalDTO.class))
                .thenReturn(Mono.just(new LocalDTO()));

        when(repository.findByLocalIdAndDisponibleTrue(1L))
                .thenReturn(lista);

        List<Plato> resultado =
                spyService.listarDisponiblesPorLocal(1L);

        assertEquals(1, resultado.size());

        verify(repository)
                .findByLocalIdAndDisponibleTrue(1L);
    }


    @Test
    @DisplayName("Debe lanzar excepción si el plato no existe")
    void debeLanzarExcepcionSiPlatoNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> service.buscarPorId(1L));

        assertEquals(
                "El plato no existe.",
                exception.getMessage());
    }


    @Test
    @DisplayName("Debe lanzar excepción si el local no existe")
    void debeLanzarExcepcionSiLocalNoExiste() {

        Plato plato = new Plato();
        plato.setNombre("Pizza");
        plato.setPrecio(12000.0);
        plato.setLocalId(99L);

        PlatoService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(webClient.get())
                .thenReturn(uriSpec);

        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(LocalDTO.class))
                .thenThrow(mock(WebClientResponseException.NotFound.class));

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> spyService.crearPlato(plato));

        assertEquals(
                "El local no existe.",
                ex.getMessage());
    }


}
