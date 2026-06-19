package com.sistema.sistema.services;

import com.sistema.sistema.dto.CarreraDTO;
import com.sistema.sistema.entities.areaAcademica.Carrera;
import com.sistema.sistema.exceptions.EntidadDuplicadaException;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.mappers.CarreraMapper;
import com.sistema.sistema.repositories.CarreraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository carreraRepository;
    private final CarreraMapper carreraMapper;

    public CarreraDTO buscarCarreraPorId(Long id) {
        return carreraMapper.toDTO(obtenerCarreraPorId(id));
    }

    public CarreraDTO agregarCarrera(CarreraDTO carreraDTO) {
        if (carreraRepository.existsByNombreIgnoreCase(carreraDTO.getNombre())) {
            throw new EntidadDuplicadaException("Ya existe una carrera con el nombre: " + carreraDTO.getNombre());
        }
        Carrera carrera = carreraMapper.toEntity(carreraDTO);
        return carreraMapper.toDTO(carreraRepository.save(carrera));
    }

    public void eliminarCarrera(Long id) {
        obtenerCarreraPorId(id);
        carreraRepository.deleteById(id);
    }

    public List<CarreraDTO> listarCarreras() {
        return carreraMapper.toDTOList(carreraRepository.findAll());
    }

    public CarreraDTO modificarCarrera(Long id, CarreraDTO carreraModificada) {
        Carrera carreraExistente = obtenerCarreraPorId(id);
        carreraModificada.setIdCarrera(id);
        carreraMapper.actualizarEntity(carreraModificada, carreraExistente);
        return carreraMapper.toDTO(carreraRepository.save(carreraExistente));
    }

    private Carrera obtenerCarreraPorId(Long id) {
        return carreraRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Carrera con id: " + id + " no encontrada"));
    }
}