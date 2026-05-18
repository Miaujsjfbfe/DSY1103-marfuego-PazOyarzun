package com.ApiMarfuego.ms_locales.Controller;

import com.ApiMarfuego.ms_locales.DTO.MesaRequestDTO;
import com.ApiMarfuego.ms_locales.Model.Mesa;
import com.ApiMarfuego.ms_locales.Service.MesaService;
import jakarta.validation.Valid;
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

        return ResponseEntity.ok(mesa);
    }


    // CREAR MESA
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

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


    // LISTAR MESAS POR LOCAL
    @GetMapping("/local/{localId}")
    public ResponseEntity<?> buscarPorLocal(@PathVariable Long localId){

        List<Mesa> mesas = mesaService.buscarPorLocal(localId);

        return ResponseEntity.ok(mesas);
    }



}