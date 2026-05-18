package com.ApiMarfuego.ms_locales.Service;

import com.ApiMarfuego.ms_locales.Model.EstadoMesa;
import com.ApiMarfuego.ms_locales.Model.Mesa;
import com.ApiMarfuego.ms_locales.Repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }



    //MOSTRAR LISTA
    public List<Mesa> listar() {
        return mesaRepository.findAll();

    }

    //BUSCAR POR ID
    public Mesa buscarPorId(Long id) {
        return mesaRepository.findById(id)
                .orElse(null);

    }

    //GUARDAR MESA
    public Mesa guardar(Mesa mesa) {

        if(mesaRepository.existsByNumeroAndLocalId(mesa.getNumero(), mesa.getLocalId())){
            throw new RuntimeException("Ya existe una mesa con ese número en el local");
        }

        return mesaRepository.save(mesa);
    }

    //ACTUALIZAR
    public Mesa actualizar(Long id, Mesa mesaActualizada){
        Mesa mesa = buscarPorId(id);

        if(mesa == null){
            throw new RuntimeException(
                    "La mesa no existe.");
        }

        boolean mismoNumero = mesa.getNumero().equals(mesaActualizada.getNumero());
        boolean mismoLocal = mesa.getLocalId().equals(mesaActualizada.getLocalId());


        if(!(mismoNumero && mismoLocal) &&
                mesaRepository.existsByNumeroAndLocalId(
                        mesaActualizada.getNumero(),
                        mesaActualizada.getLocalId())){
            throw new RuntimeException(
                    "Ya existe una mesa con ese número");
        }

        mesa.setNumero(mesaActualizada.getNumero());
        mesa.setCapacidad(mesaActualizada.getCapacidad());
        mesa.setEstado(mesaActualizada.getEstado());
        mesa.setSector(mesaActualizada.getSector());
        mesa.setLocalId(mesaActualizada.getLocalId());

        return mesaRepository.save(mesa);
    }

    //ELIMINAR
    public void eliminar(Long id) {

        Mesa mesa = buscarPorId(id);

        if(mesa == null){
            throw new RuntimeException(
                    "La mesa no existe.");
        }

        mesaRepository.deleteById(id);
    }


    //OCUPAR MESA
    public Mesa ocuparMesa(Long id){
        Mesa mesa = buscarPorId(id);

        if (mesa == null) {
            throw new RuntimeException("La mesa no existe.");
        }

        mesa.setEstado(EstadoMesa.OCUPADA);

        return mesaRepository.save(mesa);
    }

    //LIBERAR MESA
    public Mesa liberarMesa(Long id){
        Mesa mesa = buscarPorId(id);

        if (mesa == null) {
            throw new RuntimeException("La mesa no existe.");
        }

        mesa.setEstado(EstadoMesa.LIBRE);

        return mesaRepository.save(mesa);
    }

    //BUSCAR MESAS POR LOCAL
    public List<Mesa> buscarPorLocal(Long idLocal){

        List<Mesa> mesas = mesaRepository.findByLocalId(idLocal);

        if(mesas.isEmpty()){
            throw new RuntimeException("No existen mesas para este local.");
        }

        return mesas;
    }

}
