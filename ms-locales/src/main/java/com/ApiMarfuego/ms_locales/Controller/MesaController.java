package com.ApiMarfuego.ms_locales.Controller;

import com.ApiMarfuego.ms_locales.Model.Mesa;
import com.ApiMarfuego.ms_locales.Service.MesaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    // LISTAR TODAS LAS MESAS
    @GetMapping
    public ResponseEntity<?> listar(){

        List<Mesa> mesas = mesaService.listar();

        return ResponseEntity.ok(mesas);
    }

    // BUSCAR MESA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        Mesa mesa = mesaService.buscarPorId(id);

        if(mesa == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La mesa no existe");
        }

        return ResponseEntity.ok(mesa);
    }

    // CREAR MESA
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Mesa mesa){
        try{
            Mesa nuevaMesa = mesaService.guardar(mesa);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nuevaMesa);

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ACTUALIZAR MESA
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Mesa mesa){

        if(mesaService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La mesa no existe");
        }

        Mesa mesaActualizada = mesaService.actualizar(id, mesa);

        return ResponseEntity.ok(mesaActualizada);
    }

    // ELIMINAR MESA
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        if(mesaService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La mesa no existe");
        }

        mesaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // OCUPAR MESA
    @PutMapping("/{id}/ocupar")
    public ResponseEntity<?> ocuparMesa(@PathVariable Long id){

        try{

            Mesa mesa = mesaService.ocuparMesa(id);

            return ResponseEntity.ok(mesa);

        }catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // LIBERAR MESA
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

    // VERIFICAR DISPONIBILIDAD
    @GetMapping("/{id}/disponible")
    public ResponseEntity<?> estaDisponible(@PathVariable Long id){

        try{

            boolean disponible = mesaService.estaDisponible(id);

            return ResponseEntity.ok(disponible);

        }catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // LISTAR MESAS POR LOCAL
    @GetMapping("/local/{localId}")
    public ResponseEntity<?> buscarPorLocal(@PathVariable Long localId){

        List<Mesa> mesas = mesaService.buscarPorLocal(localId);

        return ResponseEntity.ok(mesas);
    }
}