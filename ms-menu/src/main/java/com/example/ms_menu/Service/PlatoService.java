package com.example.ms_menu.Service;

import com.example.ms_menu.Model.Plato;
import com.example.ms_menu.Repository.PlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.ms_menu.DTO.LocalDTO;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class PlatoService {

    private final PlatoRepository platoRepository;

    public PlatoService(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    @Value("${ms.locales.url}")
    private String localesUrl;

    //METODO VALIDAR LOCAL
    private void validarLocal(Long localId){

        LocalDTO local;
        try {
            local = WebClient.create(localesUrl)
                    .get()
                    .uri("/api/v1/locales/" + localId)
                    .retrieve()
                    .bodyToMono(LocalDTO.class)
                    .block();
        }catch(WebClientResponseException.NotFound e){

            throw new RuntimeException("El local no existe.");
        }
    }


    //LISTAR
    public List<Plato> listarPlatos(){
        return platoRepository.findAll();
    }


    //BUSCAR PLATO POR ID
    public Plato buscarPorId( Long id){
        return platoRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException(
                                "El plato no existe."
                        ));
    }


    //CREAR
    public Plato crearPlato(Plato plato){

        //Consulto el local
        validarLocal(plato.getLocalId());

        //Disponibilidad por defecto
        if(plato.getDisponible() == null){
            plato.setDisponible(true);
        }

        return platoRepository.save(plato);
    }


    //ACTUALIZAR
    public Plato actualizar(Long id, Plato plato) {

        Plato plato1 = buscarPorId(id);

        //Consulto el local
        validarLocal(plato.getLocalId());

        plato1.setNombre(plato.getNombre());
        plato1.setDescripcion(plato.getDescripcion());
        plato1.setPrecio(plato.getPrecio());
        plato1.setDisponible(plato.getDisponible());
        plato1.setLocalId(plato.getLocalId());

        return platoRepository.save(plato1);
    }


    //ELIMINAR
    public void eliminar(Long id){

        buscarPorId(id);

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

        validarLocal(localId);

        return platoRepository.findByLocalId(localId);
    }

    //LISTAR PLATOS DISPONIBLES POR LOCAL
    public List<Plato> listarDisponiblesPorLocal(Long localId){

        validarLocal(localId);

        return platoRepository.findByLocalIdAndDisponibleTrue(localId);
    }
}
