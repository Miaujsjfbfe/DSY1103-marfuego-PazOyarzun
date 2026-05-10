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
    private final MesaRepository mesaRepository;

    public LocalService(LocalRepository localRepository, MesaRepository mesaRepository) {
        this.localRepository = localRepository;
        this.mesaRepository = mesaRepository;
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

        local.setNombre(datos.getNombre());
        local.setCiudad(datos.getCiudad());

        return localRepository.save(local);
    }

    //ELIMINAR
    public void eliminar(Long id){
        localRepository.deleteById(id);

    }

    //LISTAR MESAS POR LOCAL
    public List<Mesa> listarMesaLocal(Long id){

        if (!localRepository.existsById(id)) {
            throw new RuntimeException("El local no existe");
        }

        return mesaRepository.findByLocalId(id);
    }

}
