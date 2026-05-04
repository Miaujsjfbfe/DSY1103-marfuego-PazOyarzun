package com.ApiMarfuego.ms_locales.Repository;

import com.ApiMarfuego.ms_locales.Model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long > {

    List<Mesa> findByLocalId(Long localId);




}
