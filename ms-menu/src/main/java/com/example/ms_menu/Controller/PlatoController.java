package com.example.ms_menu.Controller;

import com.example.ms_menu.DTO.PlatoRequestDTO;
import com.example.ms_menu.Model.Plato;
import com.example.ms_menu.Service.PlatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/platos")

@Tag(
        name = "Platos",
        description = "Operaciones relacionadas con la gestión de platos."
)

public class PlatoController {

    private final PlatoService platoService;

    public PlatoController(PlatoService platoService) {
        this.platoService = platoService;
    }


    // LISTAR TODOS LOS PLATOS
    @Operation(
            summary = "Obtiene todos los platos",
            description = "Retorna la lista completa de platos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping
    public ResponseEntity<?> listarPlatos(){
        List<Plato> platos = platoService.listarPlatos();

        return ResponseEntity.ok(platos);
    }


    // BUSCAR PLATO POR ID
    @Operation(
            summary = "Buscar plato por ID",
            description = "Retorna un plato según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Plato encontrado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Plato no encontrado."
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?>  buscarPorId(@PathVariable Long id){

        Plato plato = platoService.buscarPorId(id);

        return ResponseEntity.ok(plato);
    }

    // CREAR PLATO
    @Operation(
            summary = "Crear un nuevo plato",
            description = "Crea un nuevo plato en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Plato creado correctamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            )
    })
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody PlatoRequestDTO dto) {
        Plato plato = new Plato();

        plato.setNombre(dto.getNombre());
        plato.setDescripcion(dto.getDescripcion());
        plato.setPrecio(dto.getPrecio());
        plato.setDisponible(dto.getDisponible());
        plato.setLocalId(dto.getLocalId());

        Plato platoGuardar = platoService.crearPlato(plato);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(platoGuardar);
    }

    // ACTUALIZAR PLATO
    @Operation(
            summary = "Actualizar un plato",
            description = "Actualiza los datos de un plato existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Plato actualizado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Plato no encontrado."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,@Valid @RequestBody PlatoRequestDTO dto){

        Plato plato = new Plato();

        plato.setNombre(dto.getNombre());
        plato.setDescripcion(dto.getDescripcion());
        plato.setPrecio(dto.getPrecio());
        plato.setDisponible(dto.getDisponible());
        plato.setLocalId(dto.getLocalId());

        Plato platoActualizado = platoService.actualizar(id, plato);

        return ResponseEntity.ok(platoActualizado);

    }


    // ELIMINAR PLATO
    @Operation(
            summary = "Eliminar un plato",
            description = "Elimina un plato según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Plato eliminado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Plato no encontrado."
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        platoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }


    // CAMBIAR DISPONIBILIDAD
    @Operation(
            summary = "Cambiar disponibilidad de un plato",
            description = "Modifica la disponibilidad de un plato."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Disponibilidad actualizada correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Plato no encontrado."
            )
    })
    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<?> cambiarDisponibilidad(@PathVariable Long id,
                                                   @RequestParam Boolean disponible) {

        Plato plato = platoService.cambiarDisponibilidad(id, disponible);

        return ResponseEntity.ok(plato);

    }

    // LISTAR PLATOS DISPONIBLES
    @Operation(
            summary = "Listar platos disponibles",
            description = "Retorna todos los platos disponibles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping("/disponibles")
    public ResponseEntity<?> listarDisponibles(){

        List<Plato> platos = platoService.listarDisponibles();

        return ResponseEntity.ok(platos);
    }

    // LISTAR PLATOS POR LOCAL
    @Operation(
            summary = "Listar platos por local",
            description = "Retorna todos los platos asociados a un local."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            ),
            @ApiResponse(
                    responseCode = "404", //AGREGAR BUSCAR POR ID
                    description = "Local no encontrado."
            )
    })
    @GetMapping("/local/{localId}")
    public ResponseEntity<?> listarPorLocal(@PathVariable Long localId){

        List<Plato> platos = platoService.listarPorLocal(localId);

        return ResponseEntity.ok(platos);
    }

    // LISTAR PLATOS DISPONIBLES POR LOCAL
    @Operation(
            summary = "Listar platos disponibles por local",
            description = "Retorna los platos disponibles asociados a un local."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Local no encontrado."
            )
    })
    @GetMapping("/local/{localId}/disponibles")
    public ResponseEntity<?> listarDisponiblesPorLocal(@PathVariable Long localId){

        List<Plato> platos = platoService.listarDisponiblesPorLocal(localId);

        return ResponseEntity.ok(platos);
    }
}