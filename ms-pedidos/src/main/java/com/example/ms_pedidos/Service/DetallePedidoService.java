package com.example.ms_pedidos.Service;

import com.example.ms_pedidos.DTO.PlatoDTO;
import com.example.ms_pedidos.Model.DetallePedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Repository.DetallePedidoRepository;
import com.example.ms_pedidos.Repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoService pedidoService;
    private final PedidoRepository pedidoRepository;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository,
                                PedidoService pedidoService,
                                PedidoRepository pedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
    }

    //WEBCLIENT PARA CONECTAR CON MS-MENU
    private final WebClient webClient =
            WebClient.create("http://localhost:8082");


    // LISTAR TODOS LOS DETALLES
    public List<DetallePedido> listarDetalles(){
        return detallePedidoRepository.findAll();
    }


    // BUSCAR DETALLE POR ID
    public DetallePedido buscarPorId(Long id){
        return detallePedidoRepository.findById(id)
                .orElse(null);
    }

    //AGREGAR PLATO AL PEDIDO - Aqui se CREA el detalle
    public DetallePedido agregarPlatoPedido(Long pedidoId,
                                            Long platoId,
                                            Integer cantidad){

        // BUSCAR PEDIDO
        Pedido pedido = pedidoService.buscarPorId(pedidoId);

        if(pedido == null){
            throw new RuntimeException("El pedido no existe.");
        }

        // CONSULTAR PLATO EN MS-MENU
        PlatoDTO plato = webClient.get()
                .uri("/api/v1/platos/" + platoId)
                .retrieve()
                .bodyToMono(PlatoDTO.class)
                .block();

        // VALIDAR SI EXISTE
        if(plato == null){
            throw new RuntimeException("El plato no existe.");
        }

        // VALIDAR QUE EL PLATO PERTENEZCA AL LOCAL
        if(!plato.getLocalId().equals(pedido.getLocalId())){
            throw new RuntimeException("El plato no existe en el local del pedido.");
        }

        // VALIDAR DISPONIBILIDAD
        if(!plato.getDisponible()){
            throw new RuntimeException("El plato está agotado.");
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

        pedido.setTotal(totalActual + subtotal);

        pedidoRepository.save(pedido);

        // GUARDAR DETALLE
        return detallePedidoRepository.save(detalle);
    }


    // ELIMINAR DETALLE
    public void eliminar(Long id){

        DetallePedido detalle = buscarPorId(id);

        if(detalle == null){
            throw new RuntimeException("El detalle no existe.");
        }

        //Elimino el precio del detalle al total del pedido
        Pedido pedido = detalle.getPedido();
        pedido.setTotal(pedido.getTotal() - detalle.getSubtotal());

        pedidoRepository.save(pedido);

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

}