package com.sistema.sistema.services;

import com.sistema.sistema.dto.CorrelatividadDTO;
import com.sistema.sistema.entities.areaAcademica.Correlatividad;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.exceptions.MateriaInexistente;
import com.sistema.sistema.mappers.CorrelatividadMapper;
import com.sistema.sistema.repositories.CorrelatividadRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CorrelatividadService {

    private final CorrelatividadRepository correlatividadRepository;
    private final MateriaRepository materiaRepository;
    private final CorrelatividadMapper correlatividadMapper;

    public CorrelatividadDTO crear(CorrelatividadDTO dto)
    {
        Materia materia = materiaRepository.findById(dto.getIdMateria())
                .orElseThrow(() -> new MateriaInexistente("Materia no encontrada"));

        Materia correlativa = materiaRepository.findById(dto.getIdMateriaCorrelativa())
                .orElseThrow(() -> new MateriaInexistente("Materia correlativa no encontrada"));

        Correlatividad correlatividad = Correlatividad.builder()
                .materia(materia)
                .materiaCorrelativa(correlativa)
                .estadoParaCursar(dto.getEstadoParaCursar())
                .estadoParaRendir(dto.getEstadoParaRendir())
                .build();

        return correlatividadMapper.toDTO(correlatividadRepository.save(correlatividad));
    }

    public List<CorrelatividadDTO> listar()
    {
        return correlatividadMapper.toDTOList(
                correlatividadRepository.findAll());
    }

    public List<CorrelatividadDTO> buscarPorMateria(Long idMateria)
    {
        return correlatividadMapper.toDTOList(correlatividadRepository.findByMateriaIdMateria(idMateria));
    }

    public void eliminar(Long id)
    {
        correlatividadRepository.deleteById(id);
    }

}
