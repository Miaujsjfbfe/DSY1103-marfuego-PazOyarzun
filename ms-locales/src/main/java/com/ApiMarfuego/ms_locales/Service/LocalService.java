package com.ApiMarfuego.ms_locales.Service;

import com.ApiMarfuego.ms_locales.Model.Local;
import com.ApiMarfuego.ms_locales.Model.Mesa;
import com.ApiMarfuego.ms_locales.Repository.LocalRepository;
import com.ApiMarfuego.ms_locales.Repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalService {

    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }


    //LISTAR
    public List<Local> listarLocales(){
        return localRepository.findAll();
    }


    //BUSCAR POR ID
    public Local buscarPorId(Long id){

        return localRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException(
                                "El local no existe."
                        ));
    }


    //CREAR
    public Local crear(Local local){

        if(localRepository.existsByNombreAndCiudad(
                local.getNombre(),
                local.getCiudad())){
            throw new RuntimeException(
                    "El local ya existe.");
        }

        return localRepository.save(local);
    }


    //ACTUALIZAR
    public Local actualizar(Long id, Local datos){
        Local local = buscarPorId(id);


        local.setNombre(datos.getNombre());
        local.setCiudad(datos.getCiudad());

        return localRepository.save(local);
    }


    //ELIMINAR
    public void eliminar(Long id){

        buscarPorId(id);

        localRepository.deleteById(id);

    }


}
