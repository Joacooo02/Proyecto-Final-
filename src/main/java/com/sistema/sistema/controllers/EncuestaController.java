package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.EncuestaRequest;
import com.sistema.sistema.entities.encuestas.PreguntaEncuesta;
import com.sistema.sistema.entities.encuestas.RespuestaEncuesta;
import com.sistema.sistema.services.EncuestaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/encuestas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EncuestaController {

    private final EncuestaService encuestaService;

    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'ADMIN')")
    @GetMapping("/preguntas")
    public List<PreguntaEncuesta> listarPreguntas() {
        return encuestaService.listarPreguntas();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @GetMapping("/{idComision}/ya-respondio/{idAlumno}")
    public boolean yaRespondio(@PathVariable Long idComision, @PathVariable Long idAlumno) {
        return encuestaService.yaRespondio(idComision, idAlumno);
    }

    @PreAuthorize("hasAnyRole('ALUMNO')")
    @PostMapping("/{idComision}/responder/{idAlumno}")
    @ResponseStatus(HttpStatus.CREATED)
    public RespuestaEncuesta responderEncuesta(
            @PathVariable Long idComision,
            @PathVariable Long idAlumno,
            @RequestBody EncuestaRequest request) {
        return encuestaService.responderEncuesta(
                idComision, idAlumno,
                request.getRespuestas(),
                request.getComentarioFinal());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{idComision}/resultados")
    public Map<String, Double> obtenerResultados(@PathVariable Long idComision) {
        return encuestaService.obtenerResultados(idComision);
    }
}