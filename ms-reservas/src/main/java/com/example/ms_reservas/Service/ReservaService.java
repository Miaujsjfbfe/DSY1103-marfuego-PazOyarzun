package com.example.ms_reservas.Service;

import com.example.ms_reservas.DTO.MesaDTO;
import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import com.example.ms_reservas.Repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    // LISTAR RESERVAS
    public List<Reserva> listarReservas(){
        return reservaRepository.findAll();
    }

    // BUSCAR RESERVA POR ID
    public Reserva buscarPorId(Long id){
        return reservaRepository.findById(id)
                .orElse(null);
    }


    //WEB CLIENT MS-LOCALES
     private final WebClient webClient = WebClient.create("http://localhost:8081");

    // CREAR RESERVA
    public Reserva crearReserva(Reserva reserva){

        MesaDTO mesa = webClient.get()
                .uri("/api/v1/mesas/" + reserva.getMesaId())
                .retrieve()
                .bodyToMono(MesaDTO.class)
                .block();

        //Valida que la mesa exista
        if(mesa == null){
            throw new RuntimeException("La mesa no existe.");
        }

        //Valida el estado de la mesa
        if(!mesa.getEstado().equals("LIBRE")){
            throw new RuntimeException("La mesa no esta disponible.");
        }

        //Validar local
        if(!mesa.getLocalId().equals(reserva.getLocalId())){
            throw new RuntimeException("La mesa no pertenece al local.");
        }

        //Validar capacidad
        if(reserva.getCantidadPersonas()> mesa.getCapacidad()){
            throw new RuntimeException("La mesa no tiene la capacidad de personas necesaria.");
        }


        //Settear estado inicial
        reserva.setEstado(
                EstadoReserva.PENDIENTE);


        //Cambiar estado de mesa LIBRE a OCUPADA
        webClient.put()
                .uri("/api/v1/mesas/"
                        + reserva.getMesaId()
                        + "/ocupar")
                .retrieve()
                .bodyToMono(Void.class)
                .block();


        return reservaRepository.save(reserva);


    }


    // ACTUALIZAR RESERVA
    public Reserva actualizar(Long id, Reserva datos){

        Reserva reserva = buscarPorId(id);

        if(reserva == null){
            throw new RuntimeException("La reserva no existe");
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

        if(reserva == null){
            throw new RuntimeException("La reserva no existe.");
        }

        //Cambio el estado de la mesa de OCUPADA a LIBRE
        webClient.put()
                .uri("/api/v1/mesas/"
                        + reserva.getMesaId()
                        + "/liberar")
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        reservaRepository.deleteById(id);
    }

    // CAMBIAR ESTADO RESERVA
    public Reserva cambiarEstado(Long id, EstadoReserva estado){

        Reserva reserva = buscarPorId(id);

        if(reserva == null){
            throw new RuntimeException("La reserva no existe");
        }

        reserva.setEstado(estado);

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