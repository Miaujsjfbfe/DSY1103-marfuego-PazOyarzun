package com.example.ms_pedidos.Controller;

import com.example.ms_pedidos.Model.DetallePedido;
import com.example.ms_pedidos.Service.DetallePedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detalles")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    // LISTAR TODOS LOS DETALLES
    @GetMapping
    public ResponseEntity<?> listarDetalles(){

        List<DetallePedido> detalles = detallePedidoService.listarDetalles();

        return ResponseEntity.ok(detalles);
    }

    // BUSCAR DETALLE POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        DetallePedido detalle = detallePedidoService.buscarPorId(id);

        if(detalle == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El detalle no existe.");
        }

        return ResponseEntity.ok(detalle);
    }

    // CREAR DETALLE
    @PostMapping
    public ResponseEntity<?> crearDetalle(@RequestBody DetallePedido detallePedido){
        try{

            DetallePedido nuevoDetalle = detallePedidoService.crearDetalle(detallePedido);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nuevoDetalle);

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ACTUALIZAR DETALLE
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody DetallePedido detallePedido){

        if(detallePedidoService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El detalle no existe.");
        }

        try{
            DetallePedido detalleActualizado = detallePedidoService.actualizar(id, detallePedido);

            return ResponseEntity.ok(detalleActualizado);

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ELIMINAR DETALLE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        if(detallePedidoService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El detalle no existe.");
        }
        detallePedidoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // LISTAR DETALLES POR PEDIDO
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> listarPorPedido(@PathVariable Long pedidoId){

        try{
            List<DetallePedido> detalles = detallePedidoService.listarPorPedido(pedidoId);

            return ResponseEntity.ok(detalles);

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}