// ============================================================
// Encuestas (alumno) - responder de forma automatizada
// ============================================================

SesionAlumno.requerir();

const idAlumno = SesionAlumno.idAlumno();
const legajoAlumno = SesionAlumno.legajo(); // Usamos el legajo para buscar sus materias/comisiones

const tbodyComisiones = document.getElementById("tbody-comisiones");
const dialogoEncuesta = document.getElementById("dialogo-encuesta");
const formEncuesta = document.getElementById("form-encuesta");
const contenedorPreguntas = document.getElementById("preguntas");
const btnCancelarEncuesta = document.getElementById("btn-cancelar-encuesta");
const tituloEncuesta = document.getElementById("titulo-encuesta");

let idComisionActual = null;

// ============================================================
// Cargar Comisiones del Alumno
// ============================================================
async function cargarComisionesInscriptas() {
    try {
        // Consultamos las materias/comisiones del alumno usando su legajo
        const respuesta = await fetch(API_BASE + "/alumnos/" + legajoAlumno + "/materias");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const materias = await respuesta.json();
        mostrarComisiones(materias);
    } catch (error) {
        mostrarMensaje("No se pudieron cargar tus comisiones: " + error.message);
    }
}

function mostrarComisiones(listaMaterias) {
    if (!listaMaterias || listaMaterias.length === 0) {
        tbodyComisiones.innerHTML = `<tr><td colspan="4" class="celda-vacia">No estás cursando ninguna comisión actualmente.</td></tr>`;
        return;
    }

    // CORREGIDO: Cambiamos 'materia.idComision' por 'materia.id' que es lo que viene de tu MateriaDTO
    tbodyComisiones.innerHTML = listaMaterias
        .map(
            (materia) => `
            <tr>
                <td>${materia.id ?? "N/A"}</td>
                <td>${materia.nombre ?? "Materia sin nombre"}</td>
                <td>${materia.profesor ?? "No asignado"}</td>
                <td>
                    <button class="btn btn-primario btn-sm" 
                            onclick="prepararEncuesta(${materia.id}, '${materia.nombre}')">
                        Responder Encuesta
                    </button>
                </td>
            </tr>`
        )
        .join("");
}

// ============================================================
// Preparar y Abrir Modal de Encuesta de forma Segura
// ============================================================
async function prepararEncuesta(idComision, nombreMateria) {
    if (!idComision) {
        mostrarMensaje("Error: La comisión seleccionada no posee un ID válido.");
        return;
    }

    try {
        // 1) Consultamos al backend si ya respondió (Ahora con los permisos corregidos)
        const respYa = await fetch(API_BASE + "/encuestas/" + idComision + "/ya-respondio/" + idAlumno);
        if (!respYa.ok) {
            throw new Error(await extraerMensajeError(respYa));
        }
        const yaRespondio = await respYa.json();
        if (yaRespondio === true) {
            mostrarMensaje(`Ya respondiste la encuesta para la materia: ${nombreMateria}`, "ok");
            return;
        }

        // 2) Traer las preguntas del sistema
        const respPreg = await fetch(API_BASE + "/encuestas/preguntas");
        if (!respPreg.ok) {
            throw new Error(await extraerMensajeError(respPreg));
        }
        const preguntas = await respPreg.json();
        if (!preguntas || preguntas.length === 0) {
            mostrarMensaje("No hay preguntas configuradas en el sistema actualmente.");
            return;
        }

        // Fijamos el ID de forma 100% segura y abrimos el Modal
        idComisionActual = idComision;
        tituloEncuesta.textContent = `Encuesta: ${nombreMateria}`;
        mostrarPreguntas(preguntas);
        ocultarMensaje();
        dialogoEncuesta.showModal();

    } catch (error) {
        mostrarMensaje("No se pudo abrir la encuesta: " + error.message);
    }
}

function mostrarPreguntas(preguntas) {
    contenedorPreguntas.innerHTML = "";
    for (const pregunta of preguntas) {
        const label = document.createElement("label");
        label.className = "campo-formulario";
        label.innerHTML = `
            <span>${pregunta.enunciado}</span>
            <select data-id="${pregunta.idPreguntaEncuesta}" required>
                <option value="1">1 (Muy Malo)</option>
                <option value="2">2 (Malo)</option>
                <option value="3" selected>3 (Regular)</option>
                <option value="4">4 (Bueno)</option>
                <option value="5">5 (Excelente)</option>
            </select>
        `;
        contenedorPreguntas.appendChild(label);
    }
}

btnCancelarEncuesta.addEventListener("click", () => dialogoEncuesta.close());

// ============================================================
// Enviar Respuestas
// ============================================================
formEncuesta.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    // Verificación de resguardo para blindar que jamás viaje un null
    if (!idComisionActual) {
        mostrarMensaje("Error fatal: No se detectó ninguna comisión vinculada.");
        dialogoEncuesta.close();
        return;
    }

    const respuestas = [];
    for (const select of contenedorPreguntas.querySelectorAll("select")) {
        respuestas.push({
            preguntaEncuesta: { idPreguntaEncuesta: Number(select.dataset.id) },
            calificacion: Number(select.value),
        });
    }

    const body = {
        respuestas: respuestas,
        comentarioFinal: formEncuesta.elements.comentarioFinal.value.trim() || null,
    };

    try {
        const respuesta = await fetch(
            API_BASE + "/encuestas/" + idComisionActual + "/responder/" + idAlumno,
            {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body),
            }
        );
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoEncuesta.close();
        formEncuesta.reset();
        mostrarMensaje("¡Gracias! Tu encuesta fue registrada con éxito.", "ok");
    } catch (error) {
        mostrarMensaje("No se pudo enviar la encuesta: " + error.message);
        dialogoEncuesta.close();
    }
});

// ============================================================
// Inicio de la Vista
// ============================================================
cargarComisionesInscriptas();