package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.AlumnoMateriaDTO;
import com.sistema.sistema.services.AlumnoMateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alumno-materia")
@RequiredArgsConstructor
public class AlumnoMateriaController {

    private final AlumnoMateriaService alumnoMateriaService;

    @GetMapping
    public List<AlumnoMateriaDTO> listar() {
        return alumnoMateriaService.listar();
    }

    @GetMapping("/{id}")
    public AlumnoMateriaDTO obtenerPorId(@PathVariable Long id) {
        return alumnoMateriaService.obtenerPorId(id);
    }

    @GetMapping("/historial/{idAlumno}")
    public ResponseEntity<List<AlumnoMateriaDTO>> historialAcademico(@PathVariable Long idAlumno) {
        return ResponseEntity.ok(alumnoMateriaService.historialAcademico(idAlumno));
    }

    @PostMapping
    public AlumnoMateriaDTO crear(@RequestBody AlumnoMateriaDTO dto) {
        return alumnoMateriaService.crear(dto);
    }

    @PutMapping("/{id}")
    public AlumnoMateriaDTO modificar(@PathVariable Long id, @RequestBody AlumnoMateriaDTO dto) {
        return alumnoMateriaService.modificar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        alumnoMateriaService.eliminar(id);
    }
}