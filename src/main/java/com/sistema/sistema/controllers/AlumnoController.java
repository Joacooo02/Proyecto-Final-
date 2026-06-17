package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.AltaAlumnoDTO;
import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.dto.HistorialAcademicoDTO;
import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.services.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    //@PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/{legajo}")
    public AlumnoDTO buscarAlumnoPorLegajo(@PathVariable Long legajo){
        return alumnoService.buscarAlumnoPorLegajo(legajo);
    }

    //@PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @PostMapping
    public AlumnoDTO agregarAlumno(@RequestBody AltaAlumnoDTO altaAlumno){
        return alumnoService.agregarAlumno(altaAlumno);
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{legajo}")
    public void eliminarAlumno(@PathVariable Long legajo){
        alumnoService.eliminarAlumno(legajo);
    }


    //@PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping
    public List<AlumnoDTO> listarAlumnos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long legajo) {

        return alumnoService.listarAlumnos(nombre, apellido, dni, email, legajo);
    }

    //@PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @PutMapping("/{legajo}")
    public AlumnoDTO modificarAlumno(@PathVariable Long legajo,@RequestBody AlumnoDTO alumnoModificado){
        return alumnoService.modificarAlumno(legajo,alumnoModificado);
    }

    //@PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping("/{legajo}/historial-academico")
    public List<HistorialAcademicoDTO> verHistorialAcademicoAlumno(@PathVariable Long legajo)
    {
        return alumnoService.verHistorialAcademicoAlumno(legajo);
    }

    //@PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @GetMapping("/{legajo}/materias")
    public List<MateriaDTO> obtenerMaterias(@PathVariable Long legajo)
    {
        return alumnoService.obtenerMaterias(legajo);
    }

    //@PreAuthorize("hasAnyRole('ALUMNO', 'ADMIN')")
    @PostMapping("/boleto/{id}")
    public String registrarBoleto(@PathVariable Long id) {
         alumnoService.registrarBoleto(id);
         return "Boleto registrado correctamente";
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/boleto/{id}")
    public boolean tieneBoletoActivo(@PathVariable Long id)
    {
        return alumnoService.tieneBoletoActivo(id);
    }

}