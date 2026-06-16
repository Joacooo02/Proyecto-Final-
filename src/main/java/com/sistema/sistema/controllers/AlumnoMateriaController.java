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


    @PutMapping("/{idAlumno}/{idMateria}/cursando")
    public AlumnoMateriaDTO pasarACursando(@PathVariable Long idAlumno,@PathVariable Long idMateria)
    {
        return alumnoMateriaService.pasarAcursando(idAlumno, idMateria);
    }

    @PutMapping("/{idAlumno}/{idMateria}/parciales")
    public AlumnoMateriaDTO registrarParciales(@PathVariable Long idAlumno, @PathVariable Long idMateria, @RequestParam double n1,@RequestParam double n2)
    {
        return alumnoMateriaService.registrarParciales(idAlumno, idMateria, n1, n2);
    }

    @PutMapping("/{idAlumno}/{idMateria}/final")
    public AlumnoMateriaDTO aprobarFinal(@PathVariable Long idAlumno, @PathVariable Long idMateria, @RequestParam double notaFinal)
    {
        return alumnoMateriaService.aprobarFinal(idAlumno, idMateria, notaFinal);
    }
}