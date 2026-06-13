package com.sistema.sistema.services;

import com.sistema.sistema.dto.CorrelativaDTO;
import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoCursaCarrera;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.exceptions.MateriaInexistente;
import com.sistema.sistema.mappers.MateriaMapper;
import com.sistema.sistema.repositories.AlumnoCursaCarreraRepository;
import com.sistema.sistema.repositories.CorrelativaRepo;
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
    private final CorrelativaRepo correlativaRepo;

    public Materia buscarMateriaPorId(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Materia con id: " + id + " no encontrada"));
    }

    public Materia agregarMateria(MateriaDTO materiaDTO) {
        Materia materia = new Materia();
        materia.setNombre(materiaDTO.getNombre());
        materia.setCuatrimestre(materiaDTO.getCuatrimestre());
        materia.setAnioCursado(materiaDTO.getAnioCursado());
        materia.setCargaHoraria(materiaDTO.getCargaHoraria());

        materiaRepository.save(materia);
        return materia;
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

    public CorrelativaDTO materiaToCorrel (Materia materia){
        return CorrelativaDTO.builder()
                .id(materia.getIdMateria())
                .nombre(materia.getNombre())
                .build();
    }

    public Materia correlToMateria (CorrelativaDTO dto){
        Materia materia = materiaRepository.findById(dto.getId())
                .orElseThrow(() -> new MateriaInexistente("No se encontro la materia a la cual hace referencia. \n"));
        return materia;
    }

    public Materia agregarCorrel (Long idMateria, Long idCorrelativa){
        Materia materia = materiaRepository.findById(idMateria)
                .orElseThrow(() -> new MateriaInexistente("No se encontro la materia a la cual hace referencia.\n"));

        CorrelativaDTO correlativa = correlativaRepo.findById(idCorrelativa)
                .orElseThrow(() -> new MateriaInexistente("No se encontro la correlativa a la cual hace referencia.\n"));

        materia.getCorrelativas().add(correlativa);
        return materiaRepository.save(materia);
    }
}
