package com.example.ms_pedidos.Controller;

import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Service.PedidoService;
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El pedido no existe.");
        }

        return ResponseEntity.ok(pedido);
    }

    // CREAR PEDIDO
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido){

        try{

            Pedido nuevoPedido = pedidoService.crearPedido(pedido);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nuevoPedido);

        }catch (IllegalArgumentException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ACTUALIZAR PEDIDO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Pedido pedido){
        try{

            if(pedidoService.buscarPorId(id) == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El pedido no existe.");
            }

            Pedido pedidoActualizado = pedidoService.actualizar(id, pedido);

            return ResponseEntity.ok(pedidoActualizado);

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ELIMINAR PEDIDO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        if(pedidoService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El pedido no existe.");
        }

        pedidoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // CAMBIAR ESTADO DEL PEDIDO
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,
                                           @RequestParam EstadoPedido estado){

        if(pedidoService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El pedido no existe.");
        }

        Pedido pedido = pedidoService.cambiarEstado(id, estado);

        return ResponseEntity.ok(pedido);
    }
}