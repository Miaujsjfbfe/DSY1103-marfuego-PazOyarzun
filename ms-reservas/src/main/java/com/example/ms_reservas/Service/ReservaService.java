package com.example.ms_reservas.Service;

import com.example.ms_reservas.DTO.MesaDTO;
import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import com.example.ms_reservas.Repository.ReservaRepository;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    //WEB CLIENT MS-LOCALES
    @Value("${ms.locales.url}")
    private String localesUrl;

    // Metodo separado para facilitar Tests
    protected WebClient getWebClient() {
        return WebClient.create(localesUrl);
    }



    // LISTAR RESERVAS
    public List<Reserva> listarReservas(){
        return reservaRepository.findAll();
    }


    // BUSCAR RESERVA POR ID
    public Reserva buscarPorId(Long id){
        return reservaRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("La reserva {} no existe.", id);

                    return new RuntimeException(
                            "La reserva no existe.");
                });
    }


    //METODO PARA VERIFICAR MESA
    private MesaDTO validarMesa(Long mesaId){

        MesaDTO mesa;
        try{
            mesa = getWebClient()
                    .get()
                    .uri("/api/v1/mesas/" + mesaId)
                    .retrieve()
                    .bodyToMono(MesaDTO.class)
                    .block();
            return mesa;
        }catch(WebClientResponseException.NotFound e){

            log.error("La mesa {} no existe.", mesaId);

            throw new RuntimeException("La mesa no existe.");
        }
    }


    // CREAR RESERVA
    public Reserva crearReserva(Reserva reserva){

        log.info("Creando reserva para cliente {}", reserva.getNombreCliente());

        MesaDTO mesa = validarMesa(reserva.getMesaId());

        //Valida el estado de la mesa
        if(!mesa.getEstado().equals("LIBRE")){
            throw new RuntimeException("La mesa no esta disponible.");
        }

        //Validar local
        if(!mesa.getLocalId().equals(reserva.getLocalId())){
            throw new RuntimeException("La mesa no pertenece al local.");
        }

        //Validar la cantidad de personas requridas
        if(reserva.getCantidadPersonas() <= 0){
            throw new RuntimeException(
                    "La cantidad de personas debe ser mayor a cero.");
        }

        //Validar capacidad necesaria
        if(reserva.getCantidadPersonas()> mesa.getCapacidad()){ 
            throw new RuntimeException("La mesa no tiene la capacidad de personas necesaria.");
        }

        if(reserva.getFechaReserva().isBefore(LocalDateTime.now().minusMinutes(1))){
            throw new RuntimeException("La fecha no es válida.");
        }

        //Settear estado inicial
        reserva.setEstado(EstadoReserva.PENDIENTE);


        //Cambiar estado de mesa LIBRE a OCUPADA
        getWebClient()
                .put()
                .uri("/api/v1/mesas/"
                        + reserva.getMesaId()
                        + "/ocupar")
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        Reserva reservaGuardada = reservaRepository.save(reserva);

        log.info("Reserva creada correctamente con ID {}", reservaGuardada.getId());

        return reservaGuardada;
    }


    // ACTUALIZAR RESERVA
    public Reserva actualizar(Long id, Reserva datos){

        Reserva reserva = buscarPorId(id);

        MesaDTO mesa = validarMesa(datos.getMesaId());

        //VALIDAR DATOS

        log.info("Actualizando reserva {}", id);

        //Validar el estado de la reserva
        if(reserva.getEstado() == EstadoReserva.CANCELADA ||
                reserva.getEstado() == EstadoReserva.FINALIZADA){
            throw new RuntimeException(
                    "No se puede modificar una reserva cancelada o finalizada.");
        }

        //Valida el estado de la mesa
        if(!mesa.getEstado().equals("LIBRE")
                && !mesa.getId().equals(reserva.getMesaId())){
            throw new RuntimeException("La mesa no esta disponible.");
        }

        //Validar local
        if(!mesa.getLocalId().equals(datos.getLocalId())){
            throw new RuntimeException("La mesa no pertenece al local.");
        }

        //Validar la cantidad de personas requridas
        if(datos.getCantidadPersonas() <= 0){
            throw new RuntimeException(
                    "La cantidad de personas debe ser mayor a cero.");
        }

        //Validar capacidad necesaria
        if(datos.getCantidadPersonas()> mesa.getCapacidad()){
            throw new RuntimeException("La mesa no tiene la capacidad de personas necesaria.");
        }

        if(datos.getFechaReserva().isBefore(LocalDateTime.now().minusMinutes(1))){
            throw new RuntimeException(
                    "La fecha de reserva no puede ser pasada.");
        }

        // Validar un cambio de mesa
        if(!reserva.getMesaId().equals(datos.getMesaId())){

            // Liberar mesa antigua
            getWebClient()
                    .put()
                    .uri("/api/v1/mesas/"
                            + reserva.getMesaId()
                            + "/liberar")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            // Ocupar nueva mesa
            getWebClient()
                    .put()
                    .uri("/api/v1/mesas/"
                            + datos.getMesaId()
                            + "/ocupar")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }

        reserva.setNombreCliente(datos.getNombreCliente());
        reserva.setFechaReserva(datos.getFechaReserva());
        reserva.setCantidadPersonas(datos.getCantidadPersonas());
        reserva.setMesaId(datos.getMesaId());
        reserva.setLocalId(datos.getLocalId());

        return reservaRepository.save(reserva);
    }


    // ELIMINAR RESERVA
    public void eliminar(Long id){

        Reserva reserva = buscarPorId(id);

        //Cambio el estado de la mesa de OCUPADA a LIBRE
        getWebClient()
                .put()
                .uri("/api/v1/mesas/"
                        + reserva.getMesaId()
                        + "/liberar")
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        log.info("Reserva {} eliminada.", id);

        reservaRepository.deleteById(id);
    }


    // CAMBIAR ESTADO RESERVA
    public Reserva cambiarEstado(Long id, EstadoReserva estado){

        Reserva reserva = buscarPorId(id);

        reserva.setEstado(estado);

        log.info("Cambiando estado de reserva {} a {}", id, estado);

        return reservaRepository.save(reserva);
    }


    // LISTAR RESERVAS POR LOCAL
    public List<Reserva> listarPorLocal(Long localId){

        return reservaRepository.findByLocalId(localId);

    }


    // LISTAR RESERVAS POR ESTADO
    public List<Reserva> listarPorEstado(EstadoReserva estado){
        return reservaRepository.findByEstado(estado);

    }

}