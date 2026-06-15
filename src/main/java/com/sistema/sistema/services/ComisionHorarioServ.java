package com.sistema.sistema.services;

import com.sistema.sistema.dto.ComisionHorarioDTO;
import com.sistema.sistema.entities.ComisionHorario;
import com.sistema.sistema.mappers.ComisionHorarioMapper;
import com.sistema.sistema.repositories.ComisionHorarioRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComisionHorarioServ {
    private final ComisionHorarioRepo repo;
    private final ComisionHorarioMapper mapper;

    public ComisionHorarioServ(ComisionHorarioRepo repo, ComisionHorarioMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public ComisionHorarioDTO agregar (ComisionHorario comHor){
        return mapper.toDTO(repo.save(comHor));
    }
    public void eliminar (Long id){
        repo.deleteById(id);
    }

    public Optional<ComisionHorarioDTO> modificar(Long id, ComisionHorarioDTO dto){
        return repo.findById(id)
                .map(comisionHorario ->{
                    comisionHorario.setIdComision(dto.getIdComision());
                    comisionHorario.setHoraInicio(dto.getHoraInicio());
                    comisionHorario.setHoraFin(dto.getHoraFin());
                    comisionHorario.setDiaSemana(dto.getDiaSemana());
                    return mapper.toDTO(repo.save(comisionHorario));
                });
    }

    public List<ComisionHorarioDTO> mostrar(){
        return repo.findAll().stream()
                .map(mapper :: toDTO)
                .toList();
    }

    public Optional<ComisionHorarioDTO> buscarPorId(Long id){
        return repo.findById(id)
                .map(mapper :: toDTO);
    }


}
