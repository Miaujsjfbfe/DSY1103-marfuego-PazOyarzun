package com.ApiMarfuego.ms_locales.Service;

import com.ApiMarfuego.ms_locales.Model.Local;
import com.ApiMarfuego.ms_locales.Repository.LocalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocalServiceTest {

    @Mock
    private LocalRepository repository;

    //Inyectar datos falsos
    @InjectMocks
    private LocalService service;

    //Test = Nombre-Que-voy-a-probar-y-cuale-es-el-resultado-esperado
    @Test
    @DisplayName("Debe crear un local correctamente")
    void debeCrearLocalCorrectamente() {

        //creo los datos
        Local local = new Local();
        local.setNombre("Marfuego");
        local.setCiudad("Puerto Montt");

        //simulo que no esta repetido
        when(repository.existsByNombreAndCiudad(
                local.getNombre(),
                local.getCiudad()))
                .thenReturn(false);

        //simulo que lo guardo correctamente
        when(repository.save(local))
                .thenReturn(local);

        //ejecuto el metodo
        Local resultado = service.crear(local);

        //prueba final
        assertNotNull(resultado);
        assertEquals("Marfuego", resultado.getNombre());
        assertEquals("Puerto Montt", resultado.getCiudad());

        //verifico que llamo al repositorio
        verify(repository).save(local);
    }


    @Test
    @DisplayName("No debe crear un local duplicado")
    void noDebeCrearLocalDuplicado() {

        //creo el local
        Local local = new Local();
        local.setNombre("Marfuego");
        local.setCiudad("Puerto Montt");

        //simulo que ya existe
        when(repository.existsByNombreAndCiudad(
                local.getNombre(),
                local.getCiudad()))
                .thenReturn(true);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.crear(local)
                );

        //mensaje de exception
        assertEquals("El local ya existe.", exception.getMessage());

        verify(repository, never()).save(local);
    }


    @Test
    @DisplayName("Debe buscar un local por id correctamente")
    void debeBuscarLocalPorId() {

        Local local = new Local();
        local.setId(1L);
        local.setNombre("Marfuego");
        local.setCiudad("Puerto Montt");

        when(repository.findById(1L))
                .thenReturn(Optional.of(local));

        Local resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(repository).findById(1L);
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando el local no existe")
    void debeLanzarExcepcionCuandoLocalNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El local no existe.");

        verify(repository).findById(1L);
    }


    @Test
    @DisplayName("Debe actualizar un local correctamente")
    void debeActualizarLocalCorrectamente() {

        Local local = new Local();
        local.setId(1L);
        local.setNombre("Antiguo");
        local.setCiudad("Osorno");

        Local datos = new Local();
        datos.setNombre("Marfuego");
        datos.setCiudad("Puerto Montt");

        when(repository.findById(1L))
                .thenReturn(Optional.of(local));

        when(repository.save(any(Local.class)))
                .thenReturn(local);

        Local resultado = service.actualizar(1L, datos);

        assertEquals("Marfuego", resultado.getNombre());
        assertEquals("Puerto Montt", resultado.getCiudad());

        verify(repository).save(local);
    }


    @Test
    @DisplayName("Debe eliminar un local correctamente")
    void debeEliminarLocalCorrectamente() {

        Local local = new Local();
        local.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(local));

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }


    @Test
    @DisplayName("Debe lanzar excepción al eliminar un local inexistente")
    void debeLanzarExcepcionAlEliminarLocalInexistente() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminar(1L)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El local no existe.");

        verify(repository, never()).deleteById(anyLong());
    }



}
