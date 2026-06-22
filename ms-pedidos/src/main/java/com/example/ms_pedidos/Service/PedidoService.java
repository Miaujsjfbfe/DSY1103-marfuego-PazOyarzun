package com.example.ms_pedidos.Service;

import com.example.ms_pedidos.DTO.LocalDTO;
import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    //Obtiene URL desde application properties
    @Value("${ms.locales.url}")
    private String localesUrl;

    // Metodo separado para facilitar Tests
    protected WebClient getWebClient() {
        return WebClient.create(localesUrl);
    }



    // VALIDAR LOCAL
    private void validarLocal(Long localId){

        LocalDTO local;

        try{
            local = getWebClient()
                    .get()
                    .uri("/api/v1/locales/" + localId)
                    .retrieve()
                    .bodyToMono(LocalDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e){

            log.error("El local {} no existe.", localId);

            throw new RuntimeException("El local no existe.");
        }
    }


    //LISTAR PEDIDOS
    public List<Pedido> listarPedidos(){
        return pedidoRepository.findAll();
    }


    //BUSCAR PEDIDO POR ID
    public Pedido buscarPorId(Long id){
        return pedidoRepository.findById(id)
                .orElseThrow(()->{
                    log.error("El pedido {} no existe.", id);
                    return new RuntimeException(
                            "El pedido no existe.");
                });
    }


    //CREAR PEDIDO
    public Pedido crearPedido(Pedido pedido){

        log.info("Creando pedido para cliente: {}", pedido.getNombreCliente());

        //Validar que el local exista
        validarLocal(pedido.getLocalId());

        //Queda PENDIENTE automaticamente
        pedido.setEstado(EstadoPedido.PENDIENTE);

        //Queda en total 0 hasta que se agreguen platos
        pedido.setTotal(0.0);

        //Pongo la fecha del sistema
        pedido.setFechaPedido(LocalDateTime.now());

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        log.info("Pedido creado correctamente con ID: {}", pedidoGuardado.getId());

        return pedidoGuardado;
    }


    //ACTUALIZAR PEDIDO
    public Pedido actualizar(Long id, Pedido pedidoActualizado){

        Pedido pedido = buscarPorId(id);

        pedido.setNombreCliente(pedidoActualizado.getNombreCliente());

        log.info("Pedido {} actualizado correctamente.", pedido.getId());

        return pedidoRepository.save(pedido);
    }


    //ELIMINAR PEDIDO
    public void eliminar(Long id){

        buscarPorId(id);

        log.info("Eliminando pedido {}", id);

        pedidoRepository.deleteById(id);
    }


    //CAMBIAR ESTADO DEL PEDIDO
    public Pedido cambiarEstado(Long id, EstadoPedido estado){

        Pedido pedido = buscarPorId(id);

        log.info("Cambiando estado del pedido {} a {}", id, estado);

        pedido.setEstado(estado);

        return pedidoRepository.save(pedido);
    }
}