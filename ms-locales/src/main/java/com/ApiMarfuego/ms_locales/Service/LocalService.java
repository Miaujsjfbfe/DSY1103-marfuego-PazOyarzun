package com.ApiMarfuego.ms_locales.Service;

import com.ApiMarfuego.ms_locales.Model.Local;
import com.ApiMarfuego.ms_locales.Repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.slf4j.*;
import java.util.List;

@Service
public class LocalService {

    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(LocalService.class);


    //LISTAR
    public List<Local> listarLocales(){
        return localRepository.findAll();
    }


    //BUSCAR POR ID
    public Local buscarPorId(Long id){

        return localRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("El local {} no existe.", id);

                    return new RuntimeException("El local no existe.");
                });
    }


    //CREAR
    public Local crear(Local local){

        log.info("Creando local {}", local.getNombre());

        //Validacion para duplicados
        if(localRepository.existsByNombreAndCiudad(local.getNombre(), local.getCiudad())){

            log.warn("Intento de crear local duplicado: {} - {}",
                    local.getNombre(),
                    local.getCiudad());

            throw new RuntimeException(
                    "El local ya existe.");
        }

        return localRepository.save(local);
    }


    //ACTUALIZAR
    public Local actualizar(Long id, Local datos){
        Local local = buscarPorId(id);

        //Validacion que impide duplicados en datos
        if(localRepository.existsByNombreAndCiudad(datos.getNombre(), datos.getCiudad())
                && !(local.getNombre().equals(datos.getNombre())
                        && local.getCiudad().equals(datos.getCiudad())))
        {
            throw new RuntimeException("El local ya existe.");
        }

        local.setNombre(datos.getNombre());
        local.setCiudad(datos.getCiudad());

        return localRepository.save(local);
    }


    //ELIMINAR
    public void eliminar(Long id){

        buscarPorId(id);

        log.info("Eliminando local {}", id);

        localRepository.deleteById(id);
    }


}
