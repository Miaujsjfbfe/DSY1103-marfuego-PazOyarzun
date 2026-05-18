package com.example.ms_pedidos.Service;

import com.example.ms_pedidos.DTO.LocalDTO;
import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }


    //Web client para LOCALES
    private final WebClient webClient = WebClient.create("http://localhost:8081");

    // VALIDAR LOCAL
    private void validarLocal(Long localId){

        LocalDTO local =
                webClient.get()
                        .uri("/api/v1/locales/"
                                + localId)
                        .retrieve()
                        .bodyToMono(LocalDTO.class)
                        .block();

        if(local == null){
            throw new RuntimeException(
                    "El local no existe.");
        }
    }



    //LISTAR PEDIDOS
    public List<Pedido> listarPedidos(){
        return pedidoRepository.findAll();
    }


    //BUSCAR PEDIDO POR ID
    public Pedido buscarPorId(Long id){
        return pedidoRepository.findById(id)
                .orElse(null);
    }


    //CREAR PEDIDO
    public Pedido crearPedido(Pedido pedido){

        //Validar que el local exista
        validarLocal(pedido.getLocalId());

        //Queda PENDIENTE automaticamente
        pedido.setEstado(EstadoPedido.PENDIENTE);

        // Queda en total 0 hasta que se agreguen platos
        pedido.setTotal(0.0);

        //Pongo la fecha del sistema
        pedido.setFechaPedido(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }


    //ACTUALIZAR PEDIDO
    public Pedido actualizar(Long id, Pedido pedidoActualizado){

        Pedido pedido = buscarPorId(id);

        if (pedido == null){
            throw new RuntimeException("El pedido no existe.");
        }

        //valido que exista el local nuevo
        validarLocal(pedidoActualizado.getLocalId());

        pedido.setNombreCliente(pedidoActualizado.getNombreCliente());

        return pedidoRepository.save(pedido);
    }


    //ELIMINAR PEDIDO
    public void eliminar(Long id){

        Pedido pedido = buscarPorId(id);

        if (pedido == null){
            throw new RuntimeException("El pedido no existe.");
        }

        pedidoRepository.deleteById(id);

    }

    //CAMBIAR ESTADO DEL PEDIDO
    public Pedido cambiarEstado(Long id, EstadoPedido estado){

        Pedido pedido = buscarPorId(id);

        if (pedido == null){
            throw new RuntimeException("El pedido no existe.");
        }

        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }
}