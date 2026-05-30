package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Carrera;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.repositories.CarreraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository carreraRepository;

    public Carrera buscarCarreraPorId(Long id) {
        return carreraRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Carrera con id: " + id + " no encontrada"));
    }

    public Carrera agregarCarrera(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public void eliminarCarrera(Long id) {
        buscarCarreraPorId(id);
        carreraRepository.deleteById(id);
    }

    public List<Carrera> listarCarreras() {
        return carreraRepository.findAll();
    }

    public Carrera modificarCarrera(Long id, Carrera carreraModificada) {
        Carrera carreraExistente = buscarCarreraPorId(id);

        carreraExistente.setNombre(carreraModificada.getNombre());
        carreraExistente.setDuracion(carreraModificada.getDuracion());
        carreraExistente.setTituloOtorgado(carreraModificada.getTituloOtorgado());
        carreraExistente.setModalidadCarrera(carreraModificada.getModalidadCarrera());
        carreraExistente.setPlanDeEstudio(carreraModificada.getPlanDeEstudio());

        return carreraRepository.save(carreraExistente);
    }
}
