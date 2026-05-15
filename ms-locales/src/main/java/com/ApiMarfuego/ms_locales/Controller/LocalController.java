package com.ApiMarfuego.ms_locales.Controller;

import com.ApiMarfuego.ms_locales.Model.Local;
import com.ApiMarfuego.ms_locales.Service.LocalService;
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

        if(local == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El local no existe.");
        }

        return ResponseEntity.ok(local);
    }

    //CREAR LOCAL
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Local local){
        try{
            Local nuevoLocal = localService.crear(local);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nuevoLocal);

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ACTUALIZAR LOCAL
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Local local){

        if(localService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El local no existe.");
        }

        try{
            Local localActualizado = localService.actualizar(id, local);

            return ResponseEntity.ok(localActualizado);

        }catch (IllegalArgumentException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ELIMINAR LOCAL
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        if(localService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El local no existe.");
        }

        localService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

}