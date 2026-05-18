package com.ApiMarfuego.ms_locales.Controller;

import com.ApiMarfuego.ms_locales.DTO.LocalRequestDTO;
import com.ApiMarfuego.ms_locales.Model.Local;
import com.ApiMarfuego.ms_locales.Service.LocalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locales")
public class LocalController {

    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    //LISTAR LOCALES
    @GetMapping
    public ResponseEntity<?> listarLocales(){

        List<Local> locales = localService.listarLocales();

        return ResponseEntity.ok(locales);
    }

    //BUSCAR LOCAL POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        Local local = localService.buscarPorId(id);

        return ResponseEntity.ok(local);
    }


    //CREAR LOCAL
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
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody LocalRequestDTO dto){

        Local local = new Local();

        local.setNombre(dto.getNombre());
        local.setCiudad(dto.getCiudad());

        Local actualizado = localService.actualizar(id, local);

        return ResponseEntity.ok(actualizado);
    }


    // ELIMINAR LOCAL
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        localService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

}