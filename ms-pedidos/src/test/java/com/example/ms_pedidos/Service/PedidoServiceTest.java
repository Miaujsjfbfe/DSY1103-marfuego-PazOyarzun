package com.example.ms_pedidos.Service;

import com.example.ms_pedidos.DTO.LocalDTO;
import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

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
    private PedidoRepository repository;

    //Inyectar datos falsos
    @InjectMocks
    private PedidoService service;


    @Test
    @DisplayName("Debe buscar pedido por ID")
    void debeBuscarPedidoPorId() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(pedido));

        Pedido resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(repository).findById(1L);
    }


    @Test
    @DisplayName("Debe lanzar excepción si el pedido no existe")
    void debeLanzarExcepcionSiPedidoNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.buscarPorId(1L));

        assertEquals(
                "El pedido no existe.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe listar pedidos")
    void debeListarPedidos() {

        List<Pedido> lista = List.of(
                new Pedido(),
                new Pedido()
        );

        when(repository.findAll())
                .thenReturn(lista);

        List<Pedido> resultado =
                service.listarPedidos();

        assertEquals(2, resultado.size());

        verify(repository).findAll();
    }


    @Test
    @DisplayName("Debe crear pedido correctamente")
    void debeCrearPedidoCorrectamente() {

        Pedido pedido = new Pedido();
        pedido.setNombreCliente("Juan Perez");
        pedido.setLocalId(1L);

        PedidoService spyService = spy(service);

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
                .thenReturn(reactor.core.publisher.Mono.just(new LocalDTO()));

        when(repository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado =
                spyService.crearPedido(pedido);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombreCliente());

        assertEquals(
                EstadoPedido.PENDIENTE,
                resultado.getEstado());

        assertEquals(
                0.0,
                resultado.getTotal());

        assertNotNull(
                resultado.getFechaPedido());

        verify(repository)
                .save(any(Pedido.class));
    }


    @Test
    @DisplayName("Debe lanzar excepción si el local no existe")
    void debeLanzarExcepcionSiLocalNoExiste() {

        Pedido pedido = new Pedido();
        pedido.setNombreCliente("Juan Perez");
        pedido.setLocalId(99L);

        PedidoService spyService = spy(service);

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
                assertThrows(
                        RuntimeException.class,
                        () -> spyService.crearPedido(pedido));

        assertEquals(
                "El local no existe.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe actualizar pedido")
    void debeActualizarPedido() {

        Pedido existente = new Pedido();
        existente.setId(1L);
        existente.setNombreCliente("Juan");

        Pedido actualizado = new Pedido();
        actualizado.setNombreCliente("Pedro");

        when(repository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(repository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado =
                service.actualizar(1L, actualizado);

        assertEquals(
                "Pedro",
                resultado.getNombreCliente());

        verify(repository)
                .save(existente);
    }


    @Test
    @DisplayName("Debe eliminar pedido")
    void debeEliminarPedido() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(pedido));

        service.eliminar(1L);

        verify(repository)
                .deleteById(1L);
    }


    @Test
    @DisplayName("Debe cambiar estado del pedido")
    void debeCambiarEstadoPedido() {

        Pedido pedido = new Pedido();

        pedido.setId(1L);
        pedido.setEstado(
                EstadoPedido.PENDIENTE);

        when(repository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(repository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado =
                service.cambiarEstado(
                        1L,
                        EstadoPedido.ENTREGADO);

        assertEquals(
                EstadoPedido.ENTREGADO,
                resultado.getEstado());

        verify(repository)
                .save(pedido);
    }


    @Test
    @DisplayName("Debe retornar un WebClient")
    void debeRetornarWebClient() {

        WebClient cliente =
                service.getWebClient();

        assertNotNull(cliente);
    }


}
