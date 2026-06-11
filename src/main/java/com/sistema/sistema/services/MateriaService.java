package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoCursaCarrera;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.mappers.MateriaMapper;
import com.sistema.sistema.repositories.AlumnoCursaCarreraRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final AlumnoCursaCarreraRepository alumnoCursaCarreraRepository;
    private final MateriaMapper materiaMapper;

    public Materia buscarMateriaPorId(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Materia con id: " + id + " no encontrada"));
    }

    public Materia agregarMateria(Materia materia) {
        return materiaRepository.save(materia);
    }

    public void eliminarMateria(Long id) {
        buscarMateriaPorId(id);
        materiaRepository.deleteById(id);
    }

    public List<Materia> listarMaterias(String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return materiaRepository.findByNombreContainingIgnoreCase(nombre);
        }
        return materiaRepository.findAll();
    }

    public Materia modificarMateria(Long id, Materia materiaModificada) {
        Materia materiaExistente = buscarMateriaPorId(id);

        materiaExistente.setNombre(materiaModificada.getNombre());
        materiaExistente.setCargaHoraria(materiaModificada.getCargaHoraria());
        materiaExistente.setCuatrimestre(materiaModificada.getCuatrimestre());
        materiaExistente.setAnioCursado(materiaModificada.getAnioCursado());

        materiaExistente.setCarrera(materiaModificada.getCarrera());

        return materiaRepository.save(materiaExistente);
    }


    public List<Materia> verPlanAcademicoAlumno(Long idAlumno)
    {
        AlumnoCursaCarrera inscripcion = alumnoCursaCarreraRepository.findByAlumnoIdPersona(idAlumno).orElseThrow(() -> new EntidadNoEncontradaException("El alumno no esta inscripto en ninguna carrera"));
        Long idCarrera = inscripcion.getCarrera().getIdCarrera();

        return materiaRepository.findByCarreraIdCarrera(idCarrera);
    }

}
