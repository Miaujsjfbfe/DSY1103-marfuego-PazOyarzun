package com.example.ms_menu.Service;

import com.example.ms_menu.Model.Plato;
import com.example.ms_menu.Repository.PlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class PlatoService {

    private final PlatoRepository platoRepository;

    public PlatoService(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }


    //LISTAR
    public List<Plato> listarPlatos(){
        return platoRepository.findAll();
    }

    //BUSCAR PLATO POR ID
    public Plato buscarPorId( Long id){
        return platoRepository.findById(id)
                .orElse(null);
    }

    //CREAR
    public Plato crearPlato(Plato plato){
        if(plato.getDisponible() == null){
            plato.setDisponible(true);
        }

        return platoRepository.save(plato);
    }

    //ACTUALIZAR
    public Plato actualizar(Long id, Plato plato){
        Plato plato1 = buscarPorId(id);

        plato1.setNombre(plato.getNombre());
        plato1.setDescripcion(plato.getDescripcion());
        plato1.setPrecio(plato.getPrecio());
        plato1.setDisponible(plato.getDisponible());
        plato1.setLocalId(plato.getLocalId());

        return platoRepository.save(plato1);

    }

    //ELIMINAR
    public void eliminar(Long id){
        platoRepository.deleteById(id);
    }

    //CAMBIAR DISPONIBILIDAD
    public Plato cambiarDisponibilidad(Long id, Boolean disponible){

        Plato plato = buscarPorId(id);
        plato.setDisponible(disponible);

        return platoRepository.save(plato);
    }

    //LISTAR PLATOS DISPONIBLES
    public List<Plato> listarDisponibles(){
        return platoRepository.findByDisponibleTrue();
    }

    //LISTAR PLATOS POR LOCAL
    public List<Plato> listarPorLocal(Long localId){
        return platoRepository.findByLocalId(localId);
    }

    //LISTAR PLATOS DISPONIBLES POR LOCAL
    public List<Plato> listarDisponiblesPorLocal(Long localId){
        return platoRepository.findByLocalIdAndDisponibleTrue(localId);
    }
}
