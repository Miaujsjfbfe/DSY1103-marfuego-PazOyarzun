package com.ApiMarfuego.ms_locales.Controller;

import com.ApiMarfuego.ms_locales.DTO.LocalRequestDTO;
import com.ApiMarfuego.ms_locales.Model.Local;
import com.ApiMarfuego.ms_locales.Service.LocalService;
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
@RequestMapping("/api/v1/locales")

@Tag(
        name = "Locales",
        description = "Operaciones relacionadas con la gestion de locales."
)
public class LocalController {

    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }


    //LISTAR LOCALES
    @Operation(
            summary = "Obtiene todos los locales existentes.",
            description = "Retorna la lista completa de Locales."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa."
            )
    })
    @GetMapping
    public ResponseEntity<?> listarLocales(){

        List<Local> locales = localService.listarLocales();

        return ResponseEntity.ok(locales);
    }


    //BUSCAR LOCAL POR ID
    @Operation(
            summary = "Busca un Local por ID",
            description = "Retorna el Local según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Local encontrado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Local no encontrado." //CAMBIAR RESPUESTA A 404
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        Local local = localService.buscarPorId(id);

        return ResponseEntity.ok(local);
    }


    //CREAR LOCAL
    @Operation(
            summary = "Crear un nuevo local",
            description = "Crea un nuevo local en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Local creado correctamente."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos."
            )
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody LocalRequestDTO dto){
        Local local = new Local();

        local.setNombre(dto.getNombre());
        local.setCiudad(dto.getCiudad());

        Local nuevo = localService.crear(local);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nuevo);
    }


    // ACTUALIZAR LOCAL
    @Operation(
            summary = "Actualizar un local",
            description = "Actualiza los datos de un local existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Local actualizado correctamente."
            ),
            @ApiResponse(
                    responseCode = "400", //ARREGLAR CODIGO
                    description = "Datos inválidos."
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody LocalRequestDTO dto){

        Local local = new Local();

        local.setNombre(dto.getNombre());
        local.setCiudad(dto.getCiudad());

        Local actualizado = localService.actualizar(id, local);

        return ResponseEntity.ok(actualizado);
    }


    // ELIMINAR LOCAL
    @Operation(
            summary = "Eliminar un local",
            description = "Elimina un local según el ID ingresado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Local eliminado correctamente."
            ),
            @ApiResponse(
                    responseCode = "404", //CAMbiar codigo
                    description = "Local no encontrado."
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        localService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

}