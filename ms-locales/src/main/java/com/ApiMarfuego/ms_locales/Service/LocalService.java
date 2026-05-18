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
                .orElse(null);
    }


    //CREAR
    public Local crear(Local local){
        return localRepository.save(local);
    }


    //ACTUALIZAR
    public Local actualizar(Long id, Local datos){
        Local local = buscarPorId(id);

        if(local == null) {
            throw new RuntimeException(
                    "El local no existe");
        }

        local.setNombre(datos.getNombre());
        local.setCiudad(datos.getCiudad());

        return localRepository.save(local);
    }


    //ELIMINAR
    public void eliminar(Long id){

        Local local = buscarPorId(id);

        if(local == null) {
            throw new RuntimeException(
                    "El local no existe");
        }

        localRepository.deleteById(id);

    }


}
