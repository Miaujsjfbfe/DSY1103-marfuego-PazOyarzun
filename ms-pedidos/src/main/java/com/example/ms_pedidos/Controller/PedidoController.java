package com.example.ms_pedidos.Controller;

import com.example.ms_pedidos.DTO.PedidoRequestDTO;
import com.example.ms_pedidos.Model.EstadoPedido;
import com.example.ms_pedidos.Model.Pedido;
import com.example.ms_pedidos.Service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")

@Tag(
        name = "Pedidos",
        description = "Operaciones relacionadas con la gestión de los pedidos."
)

public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }



    // LISTAR PEDIDOS
    @Operation(
            summary = "Obtiene todos los pedidos",
            description = "Retorna la lista completa de pedidos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping
    public ResponseEntity<?> listarPedidos(){

        List<Pedido> pedidos = pedidoService.listarPedidos();

        return ResponseEntity.ok(pedidos);
    }


    // BUSCAR PEDIDO POR ID
    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna un pedido según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado."
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        Pedido pedido = pedidoService.buscarPorId(id);

        return ResponseEntity.ok(pedido);
    }


    // CREAR PEDIDO
    @Operation(
            summary = "Crear un nuevo pedido",
            description = "Crea un nuevo pedido asociado a un local."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido creado correctamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Local no encontrado."
            )
    })
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
    @Operation(
            summary = "Actualizar un pedido",
            description = "Actualiza los datos de un pedido existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido actualizado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido o local no encontrado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            )
    })
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
    @Operation(
            summary = "Eliminar un pedido",
            description = "Elimina un pedido según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Pedido eliminado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado."
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        pedidoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }


    // CAMBIAR ESTADO DEL PEDIDO
    @Operation(
            summary = "Cambiar estado de un pedido",
            description = "Modifica el estado actual de un pedido."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Estado inválido."
            )
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado){

        Pedido pedido = pedidoService.cambiarEstado(id, estado);

        return ResponseEntity.ok(pedido);
    }
}