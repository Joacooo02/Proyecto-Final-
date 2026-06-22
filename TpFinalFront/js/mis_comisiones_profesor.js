// ============================================================
// Mis comisiones (profesor)
// Lista las comisiones que dicta el profesor y permite ver los alumnos
// de la materia de cada comision.
//
// Endpoints (todos permiten rol PROFESOR):
//   GET /profesores/{id}/comisiones                         -> comisiones del profesor (ComisionDTO)
//   GET /profesores/{id}/comisiones/{comisionId}/alumnos    -> alumnos inscriptos a esa comision (AlumnoDTO)
//
// OJO: NO usamos GET /comisiones/profesor/{id} ni GET /comisiones/{id} porque
// el ComisionController los dejo como ADMIN-only (darian 403 al profesor).
// ProfesorController, en cambio, expone los mismos datos permitiendo PROFESOR.
// ============================================================

SesionProfesor.requerir();

const idProfesor = SesionProfesor.idProfesor();

const tbodyComisiones = document.getElementById("tbody-comisiones");
const dialogoAlumnos = document.getElementById("dialogo-alumnos");
const tituloAlumnos = document.getElementById("titulo-alumnos");
const tbodyAlumnos = document.getElementById("tbody-alumnos");
const btnCerrarAlumnos = document.getElementById("btn-cerrar-alumnos");

// ============================================================
// Comisiones del profesor
// ============================================================
async function cargarComisiones() {
    try {
        const respuesta = await fetch(API_BASE + "/profesores/" + idProfesor + "/comisiones");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const comisiones = await respuesta.json();
        mostrarComisiones(comisiones);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las comisiones: " + error.message);
    }
}

function mostrarComisiones(lista) {
    tbodyComisiones.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbodyComisiones.innerHTML =
            `<tr><td colspan="6" class="celda-vacia">No tenes comisiones asignadas.</td></tr>`;
        return;
    }

    for (const comision of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${comision.idComision}</td>
            <td>${comision.nroComision ?? "-"}</td>
            <td>${comision.materiaNombre ?? "-"}</td>
            <td>${comision.aula ?? "-"}</td>
            <td><button type="button" class="btn">Ver alumnos</button></td>
        `;
        fila.querySelector("button").addEventListener("click", () =>
            verAlumnos(comision.idComision, comision.materiaNombre)
        );
        tbodyComisiones.appendChild(fila);
    }
}

// ============================================================
// Alumnos de la materia de una comision
// ============================================================
async function verAlumnos(idComision, materiaNombre) {
    try {
        // Alumnos inscriptos a ESA comision, directo y con rol PROFESOR.
        // (El backend valida ademas que la comision sea del profesor.)
        const respAlumnos = await fetch(
            API_BASE + "/profesores/" + idProfesor + "/comisiones/" + idComision + "/alumnos"
        );
        if (!respAlumnos.ok) {
            throw new Error(await extraerMensajeError(respAlumnos));
        }
        const alumnos = await respAlumnos.json();

        mostrarAlumnos(alumnos, materiaNombre);
        dialogoAlumnos.showModal();
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los alumnos: " + error.message);
    }
}

function mostrarAlumnos(lista, materiaNombre) {
    tituloAlumnos.textContent = "Alumnos de " + (materiaNombre ?? "la materia");

    if (!lista || lista.length === 0) {
        tbodyAlumnos.innerHTML =
            `<tr><td colspan="5" class="celda-vacia">No hay alumnos inscriptos.</td></tr>`;
        return;
    }

    tbodyAlumnos.innerHTML = lista
        .map(
            (alumno) => `
            <tr>
                <td>${alumno.legajo ?? "-"}</td>
                <td>${alumno.nombre ?? ""}</td>
                <td>${alumno.apellido ?? ""}</td>
                <td>${alumno.dni ?? ""}</td>
                <td>${alumno.email ?? ""}</td>
            </tr>`
        )
        .join("");
}

btnCerrarAlumnos.addEventListener("click", () => dialogoAlumnos.close());

// ============================================================
// Arranque
// ============================================================
cargarComisiones();
