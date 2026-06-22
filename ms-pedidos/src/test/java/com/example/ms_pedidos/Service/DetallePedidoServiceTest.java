package com.example.ms_pedidos.Service;

import com.example.ms_pedidos.DTO.PlatoDTO;
import com.example.ms_pedidos.Model.DetallePedido;
import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Repository.DetallePedidoRepository;
import com.example.ms_pedidos.Repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetallePedidoServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private DetallePedidoRepository detallePedidoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private DetallePedidoService service;


    @Test
    @DisplayName("Debe agregar plato al pedido correctamente")
    void debeAgregarPlatoPedidoCorrectamente() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setLocalId(1L);
        pedido.setTotal(10000.0);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        PlatoDTO plato = new PlatoDTO();
        plato.setId(2L);
        plato.setNombre("Pizza");
        plato.setPrecio(5000.0);
        plato.setDisponible(true);
        plato.setLocalId(1L);

        DetallePedidoService spyService = spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(pedidoService.buscarPorId(1L))
                .thenReturn(pedido);

        when(webClient.get())
                .thenReturn(uriSpec);

        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(PlatoDTO.class))
                .thenReturn(Mono.just(plato));

        when(detallePedidoRepository.save(any(DetallePedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DetallePedido resultado =
                spyService.agregarPlatoPedido(
                        1L,
                        2L,
                        2);

        assertNotNull(resultado);

        assertEquals(
                "Pizza",
                resultado.getNombrePlato());

        assertEquals(
                2,
                resultado.getCantidad());

        assertEquals(
                10000.0,
                resultado.getSubtotal());

        assertEquals(
                20000.0,
                pedido.getTotal());

        verify(pedidoRepository)
                .save(pedido);

        verify(detallePedidoRepository)
                .save(any(DetallePedido.class));
    }


    @Test
    @DisplayName("Debe lanzar excepción si el pedido está cancelado")
    void debeLanzarExcepcionSiPedidoCancelado() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado(EstadoPedido.CANCELADO);

        when(pedidoService.buscarPorId(1L))
                .thenReturn(pedido);

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.agregarPlatoPedido(
                                1L,
                                1L,
                                2));

        assertEquals(
                "No se pueden agregar platos a un pedido cancelado.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe lanzar excepción si el plato no existe")
    void debeLanzarExcepcionSiPlatoNoExiste() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setLocalId(1L);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        DetallePedidoService spyService =
                spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(pedidoService.buscarPorId(1L))
                .thenReturn(pedido);

        when(webClient.get())
                .thenReturn(uriSpec);

        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(PlatoDTO.class))
                .thenThrow(
                        mock(WebClientResponseException.NotFound.class));

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> spyService.agregarPlatoPedido(
                                1L,
                                99L,
                                1));

        assertEquals(
                "El plato no existe.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe lanzar excepción si el plato pertenece a otro local")
    void debeLanzarExcepcionSiPlatoDeOtroLocal() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setLocalId(1L);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        PlatoDTO plato = new PlatoDTO();
        plato.setId(1L);
        plato.setNombre("Pizza");
        plato.setPrecio(5000.0);
        plato.setDisponible(true);
        plato.setLocalId(99L);

        DetallePedidoService spyService =
                spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(pedidoService.buscarPorId(1L))
                .thenReturn(pedido);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(PlatoDTO.class))
                .thenReturn(Mono.just(plato));

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> spyService.agregarPlatoPedido(
                                1L,
                                1L,
                                1));

        assertEquals(
                "El plato no existe en el local del pedido.",
                ex.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el plato está agotado")
    void debeLanzarExcepcionSiPlatoAgotado() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setLocalId(1L);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        PlatoDTO plato = new PlatoDTO();
        plato.setId(1L);
        plato.setNombre("Pizza");
        plato.setPrecio(5000.0);
        plato.setDisponible(false);
        plato.setLocalId(1L);

        DetallePedidoService spyService =
                spy(service);

        doReturn(webClient)
                .when(spyService)
                .getWebClient();

        when(pedidoService.buscarPorId(1L))
                .thenReturn(pedido);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(PlatoDTO.class))
                .thenReturn(Mono.just(plato));

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> spyService.agregarPlatoPedido(
                                1L,
                                1L,
                                1));

        assertEquals(
                "El plato está agotado.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe listar todos los detalles")
    void debeListarDetalles() {

        List<DetallePedido> lista = List.of(
                new DetallePedido(),
                new DetallePedido()
        );

        when(detallePedidoRepository.findAll())
                .thenReturn(lista);

        List<DetallePedido> resultado =
                service.listarDetalles();

        assertEquals(2, resultado.size());

        verify(detallePedidoRepository).findAll();
    }

    @Test
    @DisplayName("Debe buscar detalle por ID")
    void debeBuscarDetallePorId() {

        DetallePedido detalle =
                new DetallePedido();

        when(detallePedidoRepository.findById(1L))
                .thenReturn(Optional.of(detalle));

        DetallePedido resultado =
                service.buscarPorId(1L);

        assertNotNull(resultado);

        verify(detallePedidoRepository)
                .findById(1L);
    }


    @Test
    @DisplayName("Debe lanzar excepción si el detalle no existe")
    void debeLanzarExcepcionSiDetalleNoExiste() {

        when(detallePedidoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> service.buscarPorId(1L));

        assertEquals(
                "El detalle pedido no existe.",
                ex.getMessage());
    }


    @Test
    @DisplayName("Debe listar detalles por pedido")
    void debeListarDetallesPorPedido() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);

        List<DetallePedido> lista =
                List.of(new DetallePedido());

        when(pedidoService.buscarPorId(1L))
                .thenReturn(pedido);

        when(detallePedidoRepository.findByPedidoId(1L))
                .thenReturn(lista);

        List<DetallePedido> resultado =
                service.listarPorPedido(1L);

        assertEquals(1,
                resultado.size());

        verify(detallePedidoRepository)
                .findByPedidoId(1L);
    }

    @Test
    @DisplayName("Debe eliminar detalle correctamente")
    void debeEliminarDetalle() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setTotal(20000.0);

        DetallePedido detalle =
                new DetallePedido();

        detalle.setId(1L);
        detalle.setPedido(pedido);
        detalle.setSubtotal(5000.0);

        when(detallePedidoRepository.findById(1L))
                .thenReturn(Optional.of(detalle));

        service.eliminar(1L);

        assertEquals(
                15000.0,
                pedido.getTotal());

        verify(pedidoRepository)
                .save(pedido);

        verify(detallePedidoRepository)
                .deleteById(1L);
    }


    @Test
    @DisplayName("Debe retornar un WebClient")
    void debeRetornarWebClient() {

        WebClient client =
                service.getWebClient();

        assertNotNull(client);
    }


}
