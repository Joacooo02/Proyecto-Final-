package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionExamenFinal;
import com.sistema.sistema.services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscripcion")
public class InscripcionController {

    @Autowired
    private InscripcionService service;

    @PreAuthorize("hasAnyRole('ALUMNO')")
    @PostMapping("/comision/{idAlumno}/{idComision}/{idPeriodo}")
    public AlumnoInscripcionComision inscribirComision(@PathVariable Long idAlumno, @PathVariable Long idComision, @PathVariable Long idPeriodo)
    {
        return service.inscribirComision(idAlumno, idComision,idPeriodo);
    }

    @PreAuthorize("hasAnyRole('ALUMNO')")
    @PostMapping("/examenFinal/{idAlumno}/{idExamen}")
    public AlumnoInscripcionExamenFinal inscribirExamen(@PathVariable Long idAlumno, @PathVariable Long idExamen)
    {
        return service.inscribirExamen(idAlumno, idExamen);
    }

    @PreAuthorize("hasAnyRole('ALUMNO')")
    @GetMapping("/materia/{idMateria}/comisiones-disponibles")
    public List<Comision> obtenerComisionesDisponibles(@PathVariable Long idMateria)
    {
        return service.obtenerComisionesDisponibles(idMateria);
    }

    @PreAuthorize("hasAnyRole('ALUMNO')")
    @PostMapping("/materia/{idAlumno}/{idMateria}")
    public AlumnoMateria inscribirMateria(@PathVariable Long idAlumno, @PathVariable Long idMateria)
    {
        return service.inscribirMateria(idAlumno,idMateria);
    }
}
