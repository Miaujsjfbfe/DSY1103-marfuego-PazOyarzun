package com.example.ms_menu.Repository;

import com.example.ms_menu.Model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    List<Plato> findByDisponibleTrue();
    List<Plato> findByLocalId(Long id);
    List<Plato> findByLocalIdAndDisponibleTrue(Long id);

}
