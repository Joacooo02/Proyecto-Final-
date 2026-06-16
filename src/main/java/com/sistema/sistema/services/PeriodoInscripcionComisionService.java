package com.sistema.sistema.services;

import com.sistema.sistema.dto.PeriodoInscripcionComisionDTO;
import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcion;
import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcionComision;
import com.sistema.sistema.exceptions.ComisionInvalidaException;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.mappers.PeridoInscripcionComisionMapper;
import com.sistema.sistema.mappers.PeriodoInscripcionMapper;
import com.sistema.sistema.repositories.ComisionRepository;
import com.sistema.sistema.repositories.PeriodoInscripcionComisionRepository;
import com.sistema.sistema.repositories.PeriodoInscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodoInscripcionComisionService {

    private final PeriodoInscripcionRepository periodoRepository;
    private final ComisionRepository comisionRepository;
    private final PeriodoInscripcionComisionRepository periodoComisionRepository;
    private final PeridoInscripcionComisionMapper mapper;

    public PeriodoInscripcionComisionDTO habilitarComision(Long idPeriodo, Long idComision) {
        PeriodoInscripcion periodo = periodoRepository.findById(idPeriodo).orElseThrow(() -> new EntidadNoEncontradaException("No se encontro el periodo"));

        Comision comision = comisionRepository.findById(idComision).orElseThrow(() -> new EntidadNoEncontradaException("No se encontro la comision"));

        boolean existe = periodoComisionRepository.existsByPeriodoIdPeriodoAndComisionIdComision(idPeriodo, idComision);

        if (existe) {
            throw new ComisionInvalidaException("La comision ya fue habilitada en este periodo");
        }

        PeriodoInscripcionComision relacion = PeriodoInscripcionComision.builder()
                .periodo(periodo)
                .comision(comision)
                .build();

        return mapper.toDTO(periodoComisionRepository.save(relacion));
    }

    public List<PeriodoInscripcionComisionDTO> listarComisionesHabilitadas(Long idPeriodo)
    {
        return mapper.toDTOList(periodoComisionRepository.findByPeriodoIdPeriodo(idPeriodo));
    }


}