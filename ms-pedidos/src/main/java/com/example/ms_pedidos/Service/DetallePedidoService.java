package com.example.ms_pedidos.Service;

import com.example.ms_pedidos.DTO.PlatoDTO;
import com.example.ms_pedidos.Model.DetallePedido;
import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Repository.DetallePedidoRepository;
import com.example.ms_pedidos.Repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Value;

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

    private static final Logger log = LoggerFactory.getLogger(DetallePedidoService.class);

    //Obtiene URL desde application properties
    @Value("${ms.menu.url}")
    private String menuUrl;

    // Metodo separado para facilitar Tests
    protected WebClient getWebClient() {
        return WebClient.create(menuUrl);
    }




    // LISTAR TODOS LOS DETALLES
    public List<DetallePedido> listarDetalles(){
        return detallePedidoRepository.findAll();
    }


    // BUSCAR DETALLE POR ID
    public DetallePedido buscarPorId(Long id){
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("El detalle pedido {} no existe.", id);

                    return new RuntimeException(
                            "El detalle pedido no existe.");
                });
    }


    //AGREGAR PLATO AL PEDIDO - Aqui se CREA el detalle
    public DetallePedido agregarPlatoPedido(Long pedidoId, Long platoId, Integer cantidad){

        log.info("Agregando plato {} al pedido {}", platoId, pedidoId);

        // BUSCAR PEDIDO
        Pedido pedido = pedidoService.buscarPorId(pedidoId);

        // VALIDAR QUE EL PEDIDO ESTÉ ABIERTO
        if(pedido.getEstado() == EstadoPedido.CANCELADO){
            throw new RuntimeException(
                    "No se pueden agregar platos a un pedido cancelado.");
        }

        PlatoDTO plato; // CONSULTAR PLATO EN MS-MENU

        try {
            plato = getWebClient()
                    .get()
                    .uri("/api/v1/platos/" + platoId)
                    .retrieve()
                    .bodyToMono(PlatoDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e){
            log.error("El plato {} no existe.", platoId);
            throw new RuntimeException(
                    "El plato no existe.");
        }


        // VALIDAR QUE EL PLATO PERTENEZCA AL LOCAL
        if(!plato.getLocalId().equals(pedido.getLocalId())){
            log.error("El plato {} no pertenece al local del pedido {}", platoId, pedidoId);
            throw new RuntimeException("El plato no existe en el local del pedido.");
        }

        // VALIDAR DISPONIBILIDAD
        if(!plato.getDisponible()){
            log.error("El plato {} no está disponible.", plato.getNombre());
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

        log.info("Total del pedido {} actualizado a {}", pedido.getId(), pedido.getTotal());

        pedidoRepository.save(pedido);

        log.info("Detalle agregado correctamente al pedido {}", pedidoId);

        // GUARDAR DETALLE
        return detallePedidoRepository.save(detalle);
    }


    // ELIMINAR DETALLE
    public void eliminar(Long id){

        DetallePedido detalle = buscarPorId(id);

        //Elimino el precio del detalle al total del pedido
        Pedido pedido = detalle.getPedido();
        pedido.setTotal(pedido.getTotal() - detalle.getSubtotal());

        pedidoRepository.save(pedido);

        log.info("Eliminando detalle {} del pedido {}", detalle.getId(), detalle.getPedido().getId());

        detallePedidoRepository.deleteById(id);

    }


    // LISTAR DETALLES POR PEDIDO
    public List<DetallePedido> listarPorPedido(Long pedidoId) {

        pedidoService.buscarPorId(pedidoId); //Validar existencia de pedido

        return detallePedidoRepository.findByPedidoId(pedidoId);

    }

}