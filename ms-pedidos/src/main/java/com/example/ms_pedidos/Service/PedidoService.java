package com.example.ms_pedidos.Service;

import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
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

        // Si no viene estado, queda PENDIENTE
        if(pedido.getEstado() == null){
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }

        // Si no viene total, queda en total 0
        if(pedido.getTotal() == null){
            pedido.setTotal(0.0);
        }

        return pedidoRepository.save(pedido);
    }

    //ACTUALIZAR PEDIDO
    public Pedido actualizar(Long id, Pedido pedidoActualizado){

        Pedido pedido = buscarPorId(id);

        pedido.setNombreCliente(pedidoActualizado.getNombreCliente());
        pedido.setEstado(pedidoActualizado.getEstado());
        pedido.setTotal(pedidoActualizado.getTotal());

        return pedidoRepository.save(pedido);
    }

    //ELIMINAR PEDIDO
    public void eliminar(Long id){
        pedidoRepository.deleteById(id);

    }

    //CAMBIAR ESTADO DEL PEDIDO
    public Pedido cambiarEstado(Long id, EstadoPedido estado){

        Pedido pedido = buscarPorId(id);

        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }
}