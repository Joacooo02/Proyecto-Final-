package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.dto.ComisionDTO;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.usuario.Profesor;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.mappers.ComisionMapper;
import com.sistema.sistema.repositories.ComisionRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import com.sistema.sistema.repositories.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComisionService {

    private final ComisionRepository comisionRepository;
    private final ComisionMapper comisionMapper;
    private final MateriaRepository materiaRepository;
    private final ProfesorRepository profesorRepository;

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

    public List<Comision> listarComisiones(Integer nroComision) {
        if (nroComision != null) {
            return comisionRepository.findByNroComision(nroComision);
        }
        return comisionRepository.findAll();
    }

    public Comision modificarComision(Long id, Comision comisionModificada) {
        Comision comisionExistente = buscarComisionPorId(id);

        comisionExistente.setNroComision(comisionModificada.getNroComision());
        comisionExistente.setCantAlumnos(comisionModificada.getCantAlumnos());
        comisionExistente.setAula(comisionModificada.getAula());
        comisionExistente.setCupoMaximo(comisionModificada.getCupoMaximo());

        if (comisionModificada.getMateria() != null && comisionModificada.getMateria().getIdMateria() != null) {
            Materia materiaReal = materiaRepository.findById(comisionModificada.getMateria().getIdMateria())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Materia no encontrada"));
            comisionExistente.setMateria(materiaReal);
        }

        if (comisionModificada.getProfesor() != null && comisionModificada.getProfesor().getIdPersona() != null) {
            Profesor profesorReal = profesorRepository.findById(comisionModificada.getProfesor().getIdPersona())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Profesor no encontrado"));
            comisionExistente.setProfesor(profesorReal);
        } else {
            comisionExistente.setProfesor(null); // Por si quieren quitar al profesor de la comisión
        }

        return comisionRepository.save(comisionExistente);
    }

    public List<ComisionDTO> obtenerComisionesPorProfesor(Long profesorId) {
        return comisionRepository.findByProfesor_IdPersona(profesorId)
                .stream()
                .map(comision -> new ComisionDTO(
                        comision.getIdComision(),
                        comision.getNroComision(),
                        comision.getAula(),
                        comision.getCantAlumnos(),
                        comision.getMateria().getNombre()
                ))
                .collect(Collectors.toList());
    }
}