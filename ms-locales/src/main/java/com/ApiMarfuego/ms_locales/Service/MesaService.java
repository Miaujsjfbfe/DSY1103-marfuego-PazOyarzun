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
        return mesaRepository.save(mesa);

    }

    //ACTUALIZAR
    public Mesa actualizar(Long id, Mesa mesaActualizada){
        Mesa mesa = buscarPorId(id);

        mesa.setNumero(mesaActualizada.getNumero());
        mesa.setCapacidad(mesaActualizada.getCapacidad());
        mesa.setEstado(mesaActualizada.getEstado());
        mesa.setSector(mesaActualizada.getSector());
        mesa.setLocalId(mesaActualizada.getLocalId());

        return mesaRepository.save(mesa);
    }

    //ELIMINAR
    public void eliminar(Long id) {
        if(!mesaRepository.existsById(id)){
            throw new RuntimeException("La mesa no existe");
        }

        mesaRepository.deleteById(id);
    }

    //OCUPAR MESA
    public Mesa ocuparMesa(Long id){
        Mesa mesa = buscarPorId(id);

        if (mesa == null) {
            throw new RuntimeException("Mesa no encontrada");
        }

        mesa.setEstado(EstadoMesa.OCUPADA);

        return mesaRepository.save(mesa);
    }

    //LIBERAR MESA
    public Mesa liberarMesa(Long id){
        Mesa mesa = buscarPorId(id);

        if (mesa == null) {
            throw new RuntimeException("Mesa no encontrada");
        }

        mesa.setEstado(EstadoMesa.LIBRE);

        return mesaRepository.save(mesa);
    }

    //PREGUNTAR ESTADO MESA
    public boolean estaDisponible(Long id){
        Mesa mesa = buscarPorId(id);

        if (mesa == null) {
            throw new RuntimeException("Mesa no encontrada");
        }

        return mesa.getEstado() == EstadoMesa.LIBRE;
    }

    //BUSCAR MESAS POR LOCAL
    public List<Mesa> buscarPorLocal(Long idLocal){
        return mesaRepository.findByLocalId(idLocal);
    }
}
