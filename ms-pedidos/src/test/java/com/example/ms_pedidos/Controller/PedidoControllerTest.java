package com.example.ms_pedidos.Controller;

import com.example.ms_pedidos.DTO.PedidoRequestDTO;
import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Service.PedidoService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.ms_pedidos.Model.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class PedidoControllerTest {

    @InjectMocks
    private PedidoController controller;

    @Mock
    private PedidoService service;

    @Test
    @DisplayName("Debe listar pedidos")
    void debeListarPedidos() {

        List<Pedido> pedidos = List.of(
                new Pedido(),
                new Pedido()
        );

        when(service.listarPedidos())
                .thenReturn(pedidos);

        ResponseEntity<?> response =
                controller.listarPedidos();

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                pedidos,
                response.getBody());
    }

    @Test
    @DisplayName("Debe buscar pedido por ID")
    void debeBuscarPedidoPorId() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(service.buscarPorId(1L))
                .thenReturn(pedido);

        ResponseEntity<?> response =
                controller.buscarPorId(1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                pedido,
                response.getBody());
    }


    @Test
    @DisplayName("Debe eliminar pedido")
    void debeEliminarPedido() {

        ResponseEntity<?> response =
                controller.eliminar(1L);

        assertEquals(
                204,
                response.getStatusCode().value());

        verify(service)
                .eliminar(1L);
    }


    @Test
    @DisplayName("Debe crear un pedido")
    void debeCrearPedido() {

        PedidoRequestDTO dto =
                new PedidoRequestDTO();

        dto.setNombreCliente("Juan Perez");
        dto.setLocalId(1L);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNombreCliente("Juan Perez");
        pedido.setLocalId(1L);

        when(service.crearPedido(any(Pedido.class)))
                .thenReturn(pedido);

        ResponseEntity<?> response =
                controller.crearPedido(dto);

        assertEquals(
                201,
                response.getStatusCode().value());

        assertEquals(
                pedido,
                response.getBody());
    }


    @Test
    @DisplayName("Debe actualizar pedido")
    void debeActualizarPedido() {

        PedidoRequestDTO dto =
                new PedidoRequestDTO();

        dto.setNombreCliente("Pedro");
        dto.setLocalId(1L);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNombreCliente("Pedro");

        when(service.actualizar(
                eq(1L),
                any(Pedido.class)))
                .thenReturn(pedido);

        ResponseEntity<?> response =
                controller.actualizar(
                        1L,
                        dto);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                pedido,
                response.getBody());
    }


    @Test
    @DisplayName("Debe cambiar estado del pedido")
    void debeCambiarEstadoPedido() {

        Pedido pedido = new Pedido();

        pedido.setId(1L);
        pedido.setEstado(
                EstadoPedido.ENTREGADO);

        when(service.cambiarEstado(
                1L,
                EstadoPedido.ENTREGADO))
                .thenReturn(pedido);

        ResponseEntity<?> response =
                controller.cambiarEstado(
                        1L,
                        EstadoPedido.ENTREGADO);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                pedido,
                response.getBody());
    }

}