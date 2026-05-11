package com.example.ms_pedidos.Service;

import com.example.ms_pedidos.DTO.PlatoDTO;
import com.example.ms_pedidos.Model.DetallePedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Repository.DetallePedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoService pedidoService;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository, PedidoService pedidoService) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoService = pedidoService;
    }

    // LISTAR TODOS LOS DETALLES
    public List<DetallePedido> listarDetalles(){
        return detallePedidoRepository.findAll();
    }

    // BUSCAR DETALLE POR ID
    public DetallePedido buscarPorId(Long id){
        return detallePedidoRepository.findById(id)
                .orElse(null);
    }

    // CREAR DETALLE
    public DetallePedido crearDetalle(DetallePedido detallePedido){

        // Calcular subtotal automáticamente
        detallePedido.setSubtotal(
                detallePedido.getPrecio() * detallePedido.getCantidad()
        );

        return detallePedidoRepository.save(detallePedido);
    }

    // ACTUALIZAR DETALLE
    public DetallePedido actualizar(Long id, DetallePedido detalleActualizado){

        DetallePedido detalle = buscarPorId(id);

        detalle.setPlatoId(detalleActualizado.getPlatoId());
        detalle.setNombrePlato(detalleActualizado.getNombrePlato());
        detalle.setPrecio(detalleActualizado.getPrecio());
        detalle.setCantidad(detalleActualizado.getCantidad());

        // recalcular subtotal
        detalle.setSubtotal(
                detalle.getPrecio() * detalle.getCantidad()
        );

        detalle.setPedido(detalleActualizado.getPedido());

        return detallePedidoRepository.save(detalle);
    }

    // ELIMINAR DETALLE
    public void eliminar(Long id){
        detallePedidoRepository.deleteById(id);

    }

    // LISTAR DETALLES POR PEDIDO
    public List<DetallePedido> listarPorPedido(Long pedidoId){

        Pedido pedido = pedidoService.buscarPorId(pedidoId);

        if(pedido == null){
            throw new RuntimeException("El pedido no existe");

        }

        return detallePedidoRepository.findByPedidoId(pedidoId);
    }


    //AGREGAR PLATO A DETALLEPEDIDO

    //WEBCLIENT PARA CONECTAR CON MS-MENU
    private final WebClient webClient =
            WebClient.create("http://localhost:8082");


    //AGREGAR PLATO AL PEDIDO
    public DetallePedido agregarPlatoPedido(Long pedidoId, Long platoId, Integer cantidad){

        // BUSCAR PEDIDO
        Pedido pedido = pedidoService.buscarPorId(pedidoId);

        if(pedido == null){
            throw new RuntimeException("El pedido no existe");
        }

        // CONSULTAR PLATO EN MS-MENU
        PlatoDTO plato = webClient.get()
                .uri("/api/v1/platos/" + platoId)
                .retrieve()
                .bodyToMono(PlatoDTO.class)
                .block();

        // VALIDAR SI EXISTE
        if(plato == null){
            throw new RuntimeException("El plato no existe");
        }

        // VALIDAR DISPONIBILIDAD
        if(!plato.getDisponible()){
            throw new RuntimeException("El plato está agotado");
        }

        // CREAR DETALLE
        DetallePedido detalle = new DetallePedido();

        detalle.setPedido(pedido);
        detalle.setPlatoId(plato.getId());
        detalle.setNombrePlato(plato.getNombre());
        detalle.setPrecio(plato.getPrecio());
        detalle.setCantidad(cantidad);

        // CALCULAR SUBTOTAL
        Double subtotal = plato.getPrecio() * cantidad;
        detalle.setSubtotal(subtotal);

        // ACTUALIZAR TOTAL DEL PEDIDO
        Double totalActual = pedido.getTotal();

        if(totalActual == null){
            totalActual = 0.0;
        }

        pedido.setTotal(totalActual + subtotal);

        pedidoService.crearPedido(pedido);

        // GUARDAR DETALLE
        return detallePedidoRepository.save(detalle);
    }

}