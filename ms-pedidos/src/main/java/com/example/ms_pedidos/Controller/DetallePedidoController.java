package com.example.ms_pedidos.Controller;

import com.example.ms_pedidos.DTO.DetallePedidoRequestDTO;
import com.example.ms_pedidos.Model.DetallePedido;
import com.example.ms_pedidos.Service.DetallePedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detalles")

@Tag(
        name = "DetallePedido",
        description = "Operaciones relacionadas con la gestión de detalles de pedidos."
)
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    // LISTAR TODOS LOS DETALLES
    @Operation(
            summary = "Obtiene todos los detalles de pedido",
            description = "Retorna la lista completa de detalles de pedidos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping
    public ResponseEntity<?> listarDetalles(){

        List<DetallePedido> detalles = detallePedidoService.listarDetalles();

        return ResponseEntity.ok(detalles);
    }


    // BUSCAR DETALLE POR ID
    @Operation(
            summary = "Buscar detalle por ID",
            description = "Retorna un detalle de pedido según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Detalle encontrado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Detalle no encontrado."
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        DetallePedido detalle = detallePedidoService.buscarPorId(id);

        return ResponseEntity.ok(detalle);
    }


    //AGREGAR PLATOS AL PEDIDO
    @Operation(
            summary = "Agregar plato a un pedido",
            description = "Crea un detalle de pedido asociando un plato a un pedido existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Detalle creado correctamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido o plato no encontrado."
            )
    })
    @PostMapping
    public ResponseEntity<?> agregarPlatoPedido(@Valid @RequestBody
                                                    DetallePedidoRequestDTO dto){

        DetallePedido detalle = detallePedidoService.agregarPlatoPedido(
                dto.getPedidoId(), dto.getPlatoId(), dto.getCantidad());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detalle);
    }


    // ELIMINAR DETALLE
    @Operation(
            summary = "Eliminar detalle de pedido",
            description = "Elimina un detalle de pedido según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Detalle eliminado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Detalle no encontrado."
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        detallePedidoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }


    // LISTAR DETALLES POR PEDIDO
    @Operation(
            summary = "Listar detalles por pedido",
            description = "Retorna todos los detalles asociados a un pedido."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado."
            )
    })
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> listarPorPedido(@PathVariable Long pedidoId){

        List<DetallePedido> detalles = detallePedidoService.listarPorPedido(pedidoId);

        return ResponseEntity.ok(detalles);
    }


}