package com.example.ms_reservas.Controller;

import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import com.example.ms_reservas.Service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


    // LISTAR LAS RESERVAS
    @GetMapping
    public ResponseEntity<?> listarReservas(){

        List<Reserva> reservas = reservaService.listarReservas();

        return ResponseEntity.ok(reservas);

    }

    @GetMapping("/{id}")
    // BUSCAR RESERVAS POR ID
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){

        Reserva reserva = reservaService.buscarPorId(id);

        if(reserva == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La reserva no existe");
        }
        return ResponseEntity.ok(reserva);
    }


    // CREAR RESERVA
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva){
        try {
            Reserva nuevaReserva = reservaService.crearReserva(reserva);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nuevaReserva);

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ACTUALIZAR RESERVA
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Reserva reserva){

        if(reservaService.buscarPorId(id) == null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La reserva no existe");
        }

        try{

            Reserva reservaActualizada =
                    reservaService.actualizar(id, reserva);

            return ResponseEntity.ok(reservaActualizada);

        }catch (IllegalArgumentException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ELIMINAR RESERVA
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        if(reservaService.buscarPorId(id) == null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La reserva no existe");
        }

        reservaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // CAMBIAR ESTADO RESERVA
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam EstadoReserva estado){

        if(reservaService.buscarPorId(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La reserva no existe");
        }

        Reserva reserva = reservaService.cambiarEstado(id, estado);

        return ResponseEntity.ok(reserva);
    }

    // LISTAR RESERVAS POR LOCAL
    @GetMapping("/local/{localId}")
    public ResponseEntity<?> listarPorLocal(@PathVariable Long localId){

        List<Reserva> reservas = reservaService.listarPorLocal(localId);

        return ResponseEntity.ok(reservas);
    }

    // LISTAR RESERVAS POR ESTADO
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> listarPorEstado(@PathVariable EstadoReserva estado){

        List<Reserva> reservas = reservaService.listarPorEstado(estado);

        return ResponseEntity.ok(reservas);
    }
}