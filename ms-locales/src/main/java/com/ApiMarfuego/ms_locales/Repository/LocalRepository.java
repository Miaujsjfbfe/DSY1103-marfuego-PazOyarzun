package com.ApiMarfuego.ms_locales.Repository;

import com.ApiMarfuego.ms_locales.Model.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {

    boolean existsByNombreAndCiudad(String nombre, String ciudad);


}
