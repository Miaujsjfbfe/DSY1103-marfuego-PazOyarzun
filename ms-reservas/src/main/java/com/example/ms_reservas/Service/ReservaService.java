package com.example.ms_reservas.Service;

import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import com.example.ms_reservas.Repository.ReservaRepository;
import org.springframework.stereotype.Service;

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

    // CREAR RESERVA
    public Reserva crearReserva(Reserva reserva){

        // Estado inicial automático puesto como pendiente
        reserva.setEstado(EstadoReserva.PENDIENTE);

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

        if(!reservaRepository.existsById(id)){
            throw new RuntimeException("La reserva no existe");
        }

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