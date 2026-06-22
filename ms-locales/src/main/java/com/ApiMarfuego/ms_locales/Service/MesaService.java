package com.ApiMarfuego.ms_locales.Service;

import com.ApiMarfuego.ms_locales.Model.EstadoMesa;
import com.ApiMarfuego.ms_locales.Model.Mesa;
import com.ApiMarfuego.ms_locales.Repository.MesaRepository;
import org.springframework.stereotype.Service;
import org.slf4j.*;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(MesaService.class);


    //MOSTRAR LISTA
    public List<Mesa> listar() {
        return mesaRepository.findAll();
    }


    //BUSCAR POR ID
    public Mesa buscarPorId(Long id) {

        return mesaRepository.findById(id)
                .orElseThrow(()-> {

                    log.error("La mesa {} no existe.", id);

                    return new RuntimeException("La mesa no existe.");
                });
    }


    //GUARDAR MESA
    public Mesa guardar(Mesa mesa) {

        //Validacion que impide dublicados
        if(mesaRepository.existsByNumeroAndLocalId(mesa.getNumero(), mesa.getLocalId())){
            log.warn("Intento de crear mesa duplicada {} en local {}", mesa.getNumero(), mesa.getLocalId());

            throw new RuntimeException("Ya existe una mesa con ese número en el local");
        }

        log.info("Creando mesa {} para local {}", mesa.getNumero(), mesa.getLocalId());

        return mesaRepository.save(mesa);
    }


    //ACTUALIZAR
    public Mesa actualizar(Long id, Mesa mesaActualizada){

        Mesa mesa = buscarPorId(id);

        boolean mismoNumero = mesa.getNumero().equals(mesaActualizada.getNumero());
        boolean mismoLocal = mesa.getLocalId().equals(mesaActualizada.getLocalId());

        //Validacion evitar actualizar y crear duplicado
        if(!(mismoNumero && mismoLocal) &&
                mesaRepository.existsByNumeroAndLocalId(
                        mesaActualizada.getNumero(),
                        mesaActualizada.getLocalId())){

            log.warn("Intento de actualizar mesa {} duplicada {} en local {}",
                    id, mesaActualizada.getNumero(), mesaActualizada.getLocalId());

            throw new RuntimeException(
                    "Ya existe una mesa con ese número en ese local.");
        }

        mesa.setNumero(mesaActualizada.getNumero());
        mesa.setCapacidad(mesaActualizada.getCapacidad());
        mesa.setEstado(mesaActualizada.getEstado());
        mesa.setSector(mesaActualizada.getSector());
        mesa.setLocalId(mesaActualizada.getLocalId());

        log.info("Actualizando mesa {}", id);

        return mesaRepository.save(mesa);
    }


    //ELIMINAR
    public void eliminar(Long id) {

        buscarPorId(id);

        log.info("Eliminando mesa {}", id);

        mesaRepository.deleteById(id);
    }


    //OCUPAR MESA
    public Mesa ocuparMesa(Long id){

        Mesa mesa = buscarPorId(id);

        //Verifica el estado
        if(mesa.getEstado() == EstadoMesa.OCUPADA){

            log.warn("Intento de ocupar mesa {} ya ocupada.", id);

            throw new RuntimeException(
                    "La mesa ya está ocupada.");
        }

        log.info("Ocupando mesa {}", id);

        mesa.setEstado(EstadoMesa.OCUPADA);

        return mesaRepository.save(mesa);
    }


    //LIBERAR MESA
    public Mesa liberarMesa(Long id){

        Mesa mesa = buscarPorId(id);

        //Verifica el estado
        if(mesa.getEstado() == EstadoMesa.LIBRE){

            log.warn("Intento de liberar mesa {} ya libre.", id);

            throw new RuntimeException(
                    "La mesa ya está libre.");
        }

        log.info("Liberando mesa {}", id);

        mesa.setEstado(EstadoMesa.LIBRE);

        return mesaRepository.save(mesa);
    }


    //BUSCAR MESAS POR LOCAL
    public List<Mesa> buscarPorLocal(Long idLocal){

        List<Mesa> mesas = mesaRepository.findByLocalId(idLocal);


        return mesas;
    }

}
