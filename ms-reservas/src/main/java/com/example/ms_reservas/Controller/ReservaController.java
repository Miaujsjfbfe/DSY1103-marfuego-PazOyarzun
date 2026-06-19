package com.example.ms_reservas.Controller;

import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import com.example.ms_reservas.Service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/v1/reservas")

@Tag(
        name = "Reservas",
        description = "Operaciones relacionadas con la gestión de reservas."
)
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


    // LISTAR LAS RESERVAS
    @Operation(
            summary = "Obtiene todas las reservas",
            description = "Retorna la lista completa de reservas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping
    public ResponseEntity<?> listarReservas(){

        List<Reserva> reservas = reservaService.listarReservas();

        return ResponseEntity.ok(reservas);

    }

    @GetMapping("/{id}")
    // BUSCAR RESERVAS POR ID
    @Operation(
            summary = "Buscar reserva por ID",
            description = "Retorna una reserva según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada."
            )
    })
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        Reserva reserva = reservaService.buscarPorId(id);

        return ResponseEntity.ok(reserva);
    }


    // CREAR RESERVA
    @Operation(
            summary = "Crear una nueva reserva",
            description = "Crea una nueva reserva asociada a una mesa y un local."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva creada correctamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mesa no encontrada."
            )
    })
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva){

        Reserva nuevaReserva = reservaService.crearReserva(reserva);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nuevaReserva);
    }

    // ACTUALIZAR RESERVA
    @Operation(
            summary = "Actualizar una reserva",
            description = "Actualiza los datos de una reserva existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva actualizada correctamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva o mesa no encontrada."
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Reserva reserva){

        Reserva reservaActualizada = reservaService.actualizar(id, reserva);

        return ResponseEntity.ok(reservaActualizada);
    }

    // ELIMINAR RESERVA
    @Operation(
            summary = "Eliminar una reserva",
            description = "Elimina una reserva según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Reserva eliminada correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada."
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        reservaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // CAMBIAR ESTADO RESERVA
    @Operation(
            summary = "Cambiar estado de una reserva",
            description = "Modifica el estado actual de una reserva."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado correctamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Estado inválido."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada."
            )
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam EstadoReserva estado){

        Reserva reserva = reservaService.cambiarEstado(id, estado);

        return ResponseEntity.ok(reserva);
    }

    // LISTAR RESERVAS POR LOCAL
    @Operation(
            summary = "Listar reservas por local",
            description = "Retorna todas las reservas asociadas a un local."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping("/local/{localId}")
    public ResponseEntity<?> listarPorLocal(@PathVariable Long localId){

        List<Reserva> reservas = reservaService.listarPorLocal(localId);

        return ResponseEntity.ok(reservas);
    }

    // LISTAR RESERVAS POR ESTADO
    @Operation(
            summary = "Listar reservas por estado",
            description = "Retorna todas las reservas con el estado indicado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> listarPorEstado(@PathVariable EstadoReserva estado){

        List<Reserva> reservas = reservaService.listarPorEstado(estado);

        return ResponseEntity.ok(reservas);
    }
}