package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.repositories.ComisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComisionService {

    private final ComisionRepository comisionRepository;

    public Comision buscarComisionPorId(Long id) {
        return comisionRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Comisión con id: " + id + " no encontrada"));
    }

    public Comision agregarComision(Comision comision) {
        return comisionRepository.save(comision);
    }

    public void eliminarComision(Long id) {
        buscarComisionPorId(id);
        comisionRepository.deleteById(id);
    }

    public List<Comision> listarComisiones() {
        return comisionRepository.findAll();
    }

    public Comision modificarComision(Long id, Comision comisionModificada) {
        Comision comisionExistente = buscarComisionPorId(id);

        comisionExistente.setNroComision(comisionModificada.getNroComision());
        comisionExistente.setCantAlumnos(comisionModificada.getCantAlumnos());
        comisionExistente.setAula(comisionModificada.getAula());

        comisionExistente.setMateria(comisionModificada.getMateria());
        comisionExistente.setProfesor(comisionModificada.getProfesor());

        return comisionRepository.save(comisionExistente);
    }
}