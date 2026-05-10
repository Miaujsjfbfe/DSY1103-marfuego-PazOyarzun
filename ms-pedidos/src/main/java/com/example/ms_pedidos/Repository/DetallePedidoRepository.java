package com.example.ms_pedidos.Repository;

import com.example.ms_pedidos.Model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    // LISTAR DETALLES POR PEDIDO
    List<DetallePedido> findByPedidoId(Long pedidoId);
}