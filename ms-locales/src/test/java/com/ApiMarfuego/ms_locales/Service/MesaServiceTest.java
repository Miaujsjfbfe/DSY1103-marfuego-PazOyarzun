package com.ApiMarfuego.ms_locales.Service;

import com.ApiMarfuego.ms_locales.Model.EstadoMesa;
import com.ApiMarfuego.ms_locales.Model.Mesa;
import com.ApiMarfuego.ms_locales.Model.Sector;
import com.ApiMarfuego.ms_locales.Repository.MesaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MesaServiceTest {

    @Mock
    private MesaRepository repository;

    //Inyectar datos falsos
    @InjectMocks
    private MesaService service;


    @Test
    @DisplayName("Debe guardar una mesa correctamente")
    void debeGuardarMesaCorrectamente() {

        Mesa mesa = new Mesa();
        mesa.setNumero(1);
        mesa.setCapacidad(4);
        mesa.setEstado(EstadoMesa.LIBRE);
        mesa.setSector(Sector.INTERIOR);
        mesa.setLocalId(1L);

        when(repository.existsByNumeroAndLocalId(1, 1L))
                .thenReturn(false);

        when(repository.save(mesa))
                .thenReturn(mesa);

        Mesa resultado = service.guardar(mesa);

        assertNotNull(resultado);
        assertEquals(1, resultado.getNumero());
        assertEquals(1L, resultado.getLocalId());

        verify(repository).save(mesa);
    }


    @Test
    @DisplayName("No debe guardar una mesa duplicada")
    void noDebeGuardarMesaDuplicada() {

        Mesa mesa = new Mesa();
        mesa.setNumero(1);
        mesa.setLocalId(1L);

        when(repository.existsByNumeroAndLocalId(1, 1L))
                .thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.guardar(mesa)
        );

        assertEquals(
                "Ya existe una mesa con ese número en el local",
                ex.getMessage()
        );

        verify(repository, never()).save(any());
    }


    @Test
    @DisplayName("Debe listar todas las mesas")
    void debeListarMesas() {

        List<Mesa> mesas = List.of(
                new Mesa(),
                new Mesa(),
                new Mesa()
        );

        when(repository.findAll())
                .thenReturn(mesas);

        List<Mesa> resultado = service.listar();

        assertEquals(3, resultado.size());
    }


    @Test
    @DisplayName("Debe buscar una mesa por ID")
    void debeBuscarMesaPorId() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(mesa));

        Mesa resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando la mesa no existe")
    void debeLanzarExcepcionCuandoMesaNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.buscarPorId(1L)
        );

        assertEquals(
                "La mesa no existe.",
                ex.getMessage()
        );
    }


    @Test
    @DisplayName("Debe ocupar una mesa libre")
    void debeOcuparMesaLibre() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);
        mesa.setEstado(EstadoMesa.LIBRE);

        when(repository.findById(1L))
                .thenReturn(Optional.of(mesa));

        when(repository.save(any(Mesa.class)))
                .thenReturn(mesa);

        Mesa resultado = service.ocuparMesa(1L);

        assertEquals(
                EstadoMesa.OCUPADA,
                resultado.getEstado()
        );
    }


    @Test
    @DisplayName("No debe ocupar una mesa ya ocupada")
    void noDebeOcuparMesaOcupada() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);
        mesa.setEstado(EstadoMesa.OCUPADA);

        when(repository.findById(1L))
                .thenReturn(Optional.of(mesa));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.ocuparMesa(1L)
        );

        assertEquals(
                "La mesa ya está ocupada.",
                ex.getMessage()
        );

        verify(repository, never()).save(any());
    }


    @Test
    @DisplayName("Debe liberar una mesa ocupada")
    void debeLiberarMesaOcupada() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);
        mesa.setEstado(EstadoMesa.OCUPADA);

        when(repository.findById(1L))
                .thenReturn(Optional.of(mesa));

        when(repository.save(any(Mesa.class)))
                .thenReturn(mesa);

        Mesa resultado = service.liberarMesa(1L);

        assertEquals(
                EstadoMesa.LIBRE,
                resultado.getEstado());
    }


    @Test
    @DisplayName("No debe liberar una mesa libre")
    void noDebeLiberarMesaLibre() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);
        mesa.setEstado(EstadoMesa.LIBRE);

        when(repository.findById(1L))
                .thenReturn(Optional.of(mesa));

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.liberarMesa(1L));

        assertEquals(
                "La mesa ya está libre.",
                ex.getMessage());

        verify(repository, never()).save(any());
    }


    @Test
    @DisplayName("Debe eliminar una mesa")
    void debeEliminarMesa() {

        Mesa mesa = new Mesa();
        mesa.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(mesa));

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }


    @Test
    @DisplayName("Debe listar mesas de un local")
    void debeListarMesasPorLocal() {

        List<Mesa> mesas = List.of(
                new Mesa(),
                new Mesa()
        );

        when(repository.findByLocalId(1L))
                .thenReturn(mesas);

        List<Mesa> resultado =
                service.buscarPorLocal(1L);

        assertEquals(2, resultado.size());
    }


    @Test
    @DisplayName("Debe actualizar una mesa correctamente")
    void debeActualizarMesaCorrectamente() {

        Mesa existente = new Mesa();
        existente.setId(1L);
        existente.setNumero(1);
        existente.setLocalId(1L);

        Mesa nueva = new Mesa();
        nueva.setNumero(2);
        nueva.setCapacidad(6);
        nueva.setEstado(EstadoMesa.LIBRE);
        nueva.setSector(Sector.TERRAZA);
        nueva.setLocalId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(repository.existsByNumeroAndLocalId(2,1L))
                .thenReturn(false);

        when(repository.save(any(Mesa.class)))
                .thenReturn(existente);

        Mesa resultado =
                service.actualizar(1L,nueva);

        assertEquals(2,
                resultado.getNumero());
    }


    @Test
    @DisplayName("No debe actualizar si ya existe una mesa con ese número")
    void noDebeActualizarMesaDuplicada() {

        Mesa existente = new Mesa();
        existente.setId(1L);
        existente.setNumero(1);
        existente.setLocalId(1L);

        Mesa nueva = new Mesa();
        nueva.setNumero(2);
        nueva.setLocalId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(repository.existsByNumeroAndLocalId(2,1L))
                .thenReturn(true);

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.actualizar(1L,nueva));

        assertEquals(
                "Ya existe una mesa con ese número en ese local.",
                ex.getMessage());
    }


}
