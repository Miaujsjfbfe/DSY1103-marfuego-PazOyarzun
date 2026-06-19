package com.ApiMarfuego.ms_locales.Controller;

import com.ApiMarfuego.ms_locales.DTO.MesaRequestDTO;
import com.ApiMarfuego.ms_locales.Model.Mesa;
import com.ApiMarfuego.ms_locales.Service.MesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mesas")

@Tag(
        name = "Mesas",
        description = "Operaciones relacionadas con la gestión de mesas."
)
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }


    // LISTAR TODAS LAS MESAS
    @Operation(
            summary = "Obtiene todas las mesas existentes.",
            description = "Retorna la lista completa de mesas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping
    public ResponseEntity<?> listar(){

        List<Mesa> mesas = mesaService.listar();

        return ResponseEntity.ok(mesas);
    }


    // BUSCAR MESA POR ID
    @Operation(
            summary = "Busca una mesa por ID",
            description = "Retorna la mesa según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mesa encontrada correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mesa no encontrada." //CAMBIAR RESPUESTA A 404
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        Mesa mesa = mesaService.buscarPorId(id);

        return ResponseEntity.ok(mesa);
    }


    // CREAR MESA
    @Operation(
            summary = "Crear una nueva mesa",
            description = "Crea una nueva mesa en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Mesa creada correctamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            )
    })
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody MesaRequestDTO dto){

        Mesa mesa = new Mesa();

        mesa.setNumero(dto.getNumero());
        mesa.setCapacidad(dto.getCapacidad());
        mesa.setEstado(dto.getEstado());
        mesa.setSector(dto.getSector());
        mesa.setLocalId(dto.getLocalId());

        Mesa nueva = mesaService.guardar(mesa);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nueva);
    }


    // ACTUALIZAR MESA
    @Operation(
            summary = "Actualizar una mesa",
            description = "Actualiza los datos de una mesa existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mesa actualizada correctamente."
            ),
            @ApiResponse(
                    responseCode = "400", //ARREGLAR CODIGO
                    description = "Datos inválidos."
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody MesaRequestDTO dto){

        Mesa mesa = new Mesa();

        mesa.setNumero(dto.getNumero());
        mesa.setCapacidad(dto.getCapacidad());
        mesa.setEstado(dto.getEstado());
        mesa.setSector(dto.getSector());
        mesa.setLocalId(dto.getLocalId());

        Mesa actualizada = mesaService.actualizar(id, mesa);

        return ResponseEntity.ok(actualizada);
    }


    // ELIMINAR MESA
    @Operation(
            summary = "Eliminar una mesa",
            description = "Elimina una mesa según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Mesa eliminada correctamente."
            ),
            @ApiResponse(
                    responseCode = "404", //CAMbiar codigo
                    description = "Mesa no encontrada."
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        mesaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }


    // OCUPAR MESA
    @Operation(
            summary = "Ocupar una mesa",
            description = "Cambia el estado de una mesa de LIBRE a OCUPADA."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mesa ocupada correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mesa no encontrada."
            )
    })
    @PutMapping("/{id}/ocupar")
    public ResponseEntity<?> ocuparMesa(@PathVariable Long id){

        try{
            Mesa mesa = mesaService.ocuparMesa(id); //DEBERIA CAMBIAR ESTE TRY???

            return ResponseEntity.ok(mesa);

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


    // LIBERAR MESA
    @Operation(
            summary = "Liberar una mesa",
            description = "Cambia el estado de una mesa de OCUPADA a LIBRE."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mesa liberada correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mesa no encontrada."
            )
    })
    @PutMapping("/{id}/liberar")
    public ResponseEntity<?> liberarMesa(@PathVariable Long id){
        try{
            Mesa mesa = mesaService.liberarMesa(id);

            return ResponseEntity.ok(mesa);

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


    // LISTAR MESAS POR LOCAL
    @Operation(
            summary = "Listar mesas por local",
            description = "Retorna todas las mesas asociadas a un local."
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
    @GetMapping("/local/{localId}")
    public ResponseEntity<?> buscarPorLocal(@PathVariable Long localId){

        List<Mesa> mesas = mesaService.buscarPorLocal(localId);

        return ResponseEntity.ok(mesas);
    }



}