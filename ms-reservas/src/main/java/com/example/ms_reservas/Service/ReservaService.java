package com.example.ms_reservas.Service;

import com.example.ms_reservas.DTO.MesaDTO;
import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import com.example.ms_reservas.Repository.ReservaRepository;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
                .orElseThrow(() -> {

                    return new RuntimeException(
                            "La reserva no existe.");
                });
    }


    //WEB CLIENT MS-LOCALES
     private final WebClient webClient = WebClient.create("http://localhost:8081");


    //METODO PARA VERIFICAR MESA
    private MesaDTO validarMesa(Long mesaId){

        MesaDTO mesa;
        try{
            mesa = webClient.get()
                    .uri("/api/v1/mesas/" + mesaId)
                    .retrieve()
                    .bodyToMono(MesaDTO.class)
                    .block();
            return mesa;

        }catch(WebClientResponseException.NotFound e){

            throw new RuntimeException("La mesa no existe.");

        }
    }



    // CREAR RESERVA
    public Reserva crearReserva(Reserva reserva){

        MesaDTO mesa = validarMesa(reserva.getMesaId());

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

        MesaDTO mesa = validarMesa(datos.getMesaId());

        //VALIDAR DATOS DE MESA

        //Valida el estado de la mesa
        if(!mesa.getEstado().equals("LIBRE")
                && !mesa.getId().equals(reserva.getMesaId())){
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

        // Validar un cambio de mesa
        if(!reserva.getMesaId().equals(datos.getMesaId())){

            // Liberar mesa antigua
            webClient.put()
                    .uri("/api/v1/mesas/"
                            + reserva.getMesaId()
                            + "/liberar")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            // Ocupar nueva mesa
            webClient.put()
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