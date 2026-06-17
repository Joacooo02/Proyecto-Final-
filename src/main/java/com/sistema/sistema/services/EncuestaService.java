package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Comision;
import com.sistema.sistema.entities.encuestas.PreguntaEncuesta;
import com.sistema.sistema.entities.encuestas.RespuestaEncuesta;
import com.sistema.sistema.entities.encuestas.RespuestaPregunta;
import com.sistema.sistema.exceptions.EncuestaYaRespondidaException;
import com.sistema.sistema.repositories.PreguntaEncuestaRepository;
import com.sistema.sistema.repositories.RespuestaEncuestaRepository;
import com.sistema.sistema.repositories.RespuestaPreguntaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EncuestaService {

    private final PreguntaEncuestaRepository preguntaRepo;
    private final RespuestaEncuestaRepository respuestaRepo;
    private final RespuestaPreguntaRepository respuestaPreguntaRepo;
    private final ComisionService comisionService;

    public List<PreguntaEncuesta> listarPreguntas() {
        return preguntaRepo.findAllByOrderByOrdenAsc();
    }

    public boolean yaRespondio(Long idComision, Long idAlumno) {
        return respuestaRepo.existsByComision_IdComisionAndHashAlumno(idComision, hashAlumno(idAlumno));
    }

    @Transactional
    public RespuestaEncuesta responderEncuesta(Long idComision, Long idAlumno, List<RespuestaPregunta> respuestas,String comentarioFinal)
    {
        String hash = hashAlumno(idAlumno);

        if (respuestaRepo.existsByComision_IdComisionAndHashAlumno(idComision, hash)) {
            throw new EncuestaYaRespondidaException(
                    "El alumno ya respondió la encuesta para la comisión con id: " + idComision);
        }

        for (RespuestaPregunta rp : respuestas) {
            if (rp.getCalificacion() < 1 || rp.getCalificacion() > 5) {
                throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
            }
        }

        Comision comision = comisionService.buscarComisionPorId(idComision);

        RespuestaEncuesta encuesta = RespuestaEncuesta.builder()
                .comision(comision)
                .hashAlumno(hash)
                .comentarioFinal(comentarioFinal)
                .build();

        for (RespuestaPregunta rp : respuestas)
        {
           Long idPregunta = rp.getPreguntaEncuesta().getIdPreguntaEncuesta();

           PreguntaEncuesta preguntaReal = preguntaRepo.findById(idPregunta).orElseThrow(() -> new IllegalArgumentException("La pregunta no existe"));

           rp.setPreguntaEncuesta(preguntaReal);
           rp.setRespuestaEncuesta(encuesta);
        }
        encuesta.setRespuestas(respuestas);

        return respuestaRepo.save(encuesta);
    }

    public Map<String, Double> obtenerResultados(Long idComision) {
        List<Object[]> rows = respuestaPreguntaRepo.promediosPorPregunta(idComision);
        Map<String, Double> resultados = new LinkedHashMap<>();
        for (Object[] row : rows) {
            String enunciado = (String) row[0];
            Double promedio = ((Number) row[1]).doubleValue();
            resultados.put(enunciado, promedio);
        }
        return resultados;
    }

    private String hashAlumno(Long idAlumno) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(idAlumno.toString().getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash del alumno", e);
        }
    }
}