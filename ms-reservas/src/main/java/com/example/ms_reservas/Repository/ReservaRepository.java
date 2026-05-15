package com.example.ms_reservas.Repository;

import com.example.ms_reservas.Model.EstadoReserva;
import com.example.ms_reservas.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByLocalId(Long IdLocal);

    List<Reserva> findByEstado(EstadoReserva estado);
}
