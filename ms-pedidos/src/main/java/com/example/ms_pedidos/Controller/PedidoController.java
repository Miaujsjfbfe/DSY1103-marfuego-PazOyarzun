package com.example.ms_pedidos.Controller;

import com.example.ms_pedidos.DTO.PedidoRequestDTO;
import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }



    // LISTAR PEDIDOS
    @GetMapping
    public ResponseEntity<?> listarPedidos(){

        List<Pedido> pedidos = pedidoService.listarPedidos();

        return ResponseEntity.ok(pedidos);
    }


    // BUSCAR PEDIDO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        Pedido pedido = pedidoService.buscarPorId(id);

        if(pedido == null){
            throw new RuntimeException("El pedido no existe.");
        }

        return ResponseEntity.ok(pedido);
    }


    // CREAR PEDIDO
    @PostMapping
    public ResponseEntity<?> crearPedido(@Valid @RequestBody PedidoRequestDTO dto){

        Pedido pedido = new Pedido();

        pedido.setNombreCliente(dto.getNombreCliente());
        pedido.setLocalId(dto.getLocalId());

        Pedido pedidoGuardar = pedidoService.crearPedido(pedido);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoGuardar);
    }


    // ACTUALIZAR PEDIDO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody PedidoRequestDTO dto){

        Pedido pedido = new Pedido();

        pedido.setNombreCliente(dto.getNombreCliente());
        pedido.setLocalId(dto.getLocalId());

        Pedido pedidoActualizar = pedidoService.actualizar(id, pedido);

        return ResponseEntity.ok(pedidoActualizar);
    }


    // ELIMINAR PEDIDO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        pedidoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }


    // CAMBIAR ESTADO DEL PEDIDO
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado){

        Pedido pedido = pedidoService.cambiarEstado(id, estado);

        return ResponseEntity.ok(pedido);
    }
}