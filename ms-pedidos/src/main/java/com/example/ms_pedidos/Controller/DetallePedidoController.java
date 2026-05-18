package com.example.ms_pedidos.Controller;

import com.example.ms_pedidos.DTO.DetallePedidoRequestDTO;
import com.example.ms_pedidos.Model.DetallePedido;
import com.example.ms_pedidos.Service.DetallePedidoService;
import jakarta.validation.Valid;
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


    //AGREGAR PLATOS AL PEDIDO
    @PostMapping
    public ResponseEntity<?> agregarPlatoPedido(@Valid @RequestBody
                                                    DetallePedidoRequestDTO dto){

        DetallePedido detalle = detallePedidoService.agregarPlatoPedido(
                dto.getPedidoId(), dto.getPlatoId(), dto.getCantidad());


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detalle);
    }


    // ELIMINAR DETALLE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        detallePedidoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }


    // LISTAR DETALLES POR PEDIDO
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> listarPorPedido(@PathVariable Long pedidoId){

        List<DetallePedido> detalles = detallePedidoService.listarPorPedido(pedidoId);

        return ResponseEntity.ok(detalles);
    }


}