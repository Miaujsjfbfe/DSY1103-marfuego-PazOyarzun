package com.example.ms_menu.Controller;

import com.example.ms_menu.DTO.PlatoRequestDTO;
import com.example.ms_menu.Model.Plato;
import com.example.ms_menu.Service.PlatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/platos")
public class PlatoController {

    private final PlatoService platoService;

    public PlatoController(PlatoService platoService) {
        this.platoService = platoService;
    }

    // LISTAR TODOS LOS PLATOS
    @GetMapping
    public ResponseEntity<?> listarPlatos(){
        List<Plato> platos = platoService.listarPlatos();

        return ResponseEntity.ok(platos);
    }


    // BUSCAR PLATO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?>  buscarPorId(@PathVariable Long id){

        Plato plato = platoService.buscarPorId(id);

        if(plato == null){
            throw new RuntimeException("El plato no existe.");
        }

        return ResponseEntity.ok(plato);
    }

    // CREAR PLATO
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        platoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // CAMBIAR DISPONIBILIDAD
    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<?> cambiarDisponibilidad(@PathVariable Long id,
                                                   @RequestParam Boolean disponible) {

        Plato plato = platoService.cambiarDisponibilidad(id, disponible);

        return ResponseEntity.ok(plato);

    }

    // LISTAR PLATOS DISPONIBLES
    @GetMapping("/disponibles")
    public ResponseEntity<?> listarDisponibles(){

        List<Plato> platos = platoService.listarDisponibles();

        return ResponseEntity.ok(platos);
    }

    // LISTAR PLATOS POR LOCAL
    @GetMapping("/local/{localId}")
    public ResponseEntity<?> listarPorLocal(@PathVariable Long localId){

        List<Plato> platos = platoService.listarPorLocal(localId);

        return ResponseEntity.ok(platos);
    }

    // LISTAR PLATOS DISPONIBLES POR LOCAL
    @GetMapping("/local/{localId}/disponibles")
    public ResponseEntity<?> listarDisponiblesPorLocal(@PathVariable Long localId){

        List<Plato> platos = platoService.listarDisponiblesPorLocal(localId);

        return ResponseEntity.ok(platos);
    }
}