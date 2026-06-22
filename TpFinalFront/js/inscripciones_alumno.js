// ============================================================
// Inscripciones (alumno)
// Tres flujos:
//   1. Inscripcion a una materia
//        GET  /materias
//        POST /inscripcion/materia/{idAlumno}/{idMateria}
//   2. Inscripcion a una comision (requiere idPeriodo habilitado)
//        GET  /inscripcion/materia/{idMateria}/comisiones-disponibles
//        POST /inscripcion/comision/{idAlumno}/{idComision}/{idPeriodo}
//   3. Inscripcion a un examen final
//        GET  /examenes/tipo/FINAL
//        POST /inscripcion/examenFinal/{idAlumno}/{idExamen}
// Todas las inscripciones usan idAlumno (idPersona).
// ============================================================

SesionAlumno.requerir();

const idAlumno = SesionAlumno.idAlumno();

// Materias
const btnVerMaterias = document.getElementById("btn-ver-materias");
const tbodyMaterias = document.getElementById("tbody-materias");

// Comisiones
const selectMateria = document.getElementById("select-materia");
const btnVerComisiones = document.getElementById("btn-ver-comisiones");
const inputPeriodo = document.getElementById("input-periodo");
const tbodyComisiones = document.getElementById("tbody-comisiones");

// Finales
const btnVerFinales = document.getElementById("btn-ver-finales");
const tbodyFinales = document.getElementById("tbody-finales");

// ============================================================
// 1) Inscripcion a una materia
// ============================================================
async function verMaterias() {
    try {
        const respuesta = await fetch(API_BASE + "/materias");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const materias = await respuesta.json();
        mostrarMaterias(materias);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las materias: " + error.message);
    }
}

function mostrarMaterias(lista) {
    tbodyMaterias.innerHTML = "";
    if (!lista || lista.length === 0) {
        filaVacia(tbodyMaterias, 5, "No hay materias.");
        return;
    }

    for (const materia of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${materia.nombre ?? ""}</td>
            <td>${materia.anioCursado ?? "-"}</td>
            <td>${materia.cuatrimestre ?? "-"}</td>
            <td>${materia.cargaHoraria ?? "-"}</td>
            <td><button type="button" class="btn btn-primario">Inscribirme</button></td>
        `;
        fila.querySelector("button").addEventListener("click", () =>
            inscribirMateria(materia.id, materia.nombre)
        );
        tbodyMaterias.appendChild(fila);
    }
}

async function inscribirMateria(idMateria, nombre) {
    try {
        const respuesta = await fetch(
            API_BASE + "/inscripcion/materia/" + idAlumno + "/" + idMateria,
            { method: "POST" }
        );
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        mostrarMensaje("Te inscribiste en la materia " + nombre + ".", "ok");
    } catch (error) {
        mostrarMensaje("No se pudo inscribir en la materia: " + error.message);
    }
}

btnVerMaterias.addEventListener("click", verMaterias);

// ============================================================
// 2) Inscripcion a una comision
// ============================================================

// Llena el desplegable de materias (para elegir de cual ver las comisiones).
async function cargarSelectMaterias() {
    try {
        const respuesta = await fetch(API_BASE + "/materias");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const materias = await respuesta.json();
        for (const materia of materias) {
            const opcion = document.createElement("option");
            opcion.value = materia.id;
            opcion.textContent = materia.nombre;
            selectMateria.appendChild(opcion);
        }
    } catch (error) {
        mostrarMensaje("No se pudo cargar el listado de materias: " + error.message);
    }
}

async function verComisiones() {
    const idMateria = selectMateria.value;
    if (!idMateria) {
        mostrarMensaje("Elegi una materia para ver sus comisiones.");
        return;
    }

    try {
        const respuesta = await fetch(
            API_BASE + "/inscripcion/materia/" + idMateria + "/comisiones-disponibles"
        );
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
        filaVacia(tbodyComisiones, 7, "No hay comisiones disponibles para esa materia.");
        return;
    }

    for (const comision of lista) {
        const profesor = comision.profesor
            ? (comision.profesor.nombre ?? "") + " " + (comision.profesor.apellido ?? "")
            : "-";
        const materia = comision.materia ? comision.materia.nombre : "";

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${comision.idComision}</td>
            <td>${comision.nroComision ?? "-"}</td>
            <td>${materia ?? ""}</td>
            <td>${profesor.trim() || "-"}</td>
            <td>${comision.aula ?? "-"}</td>
            <td>${comision.cantAlumnos ?? 0}</td>
            <td><button type="button" class="btn btn-primario">Inscribirme</button></td>
        `;
        fila.querySelector("button").addEventListener("click", () =>
            inscribirComision(comision.idComision)
        );
        tbodyComisiones.appendChild(fila);
    }
}

async function inscribirComision(idComision) {
    const idPeriodo = inputPeriodo.value.trim();
    if (!idPeriodo) {
        mostrarMensaje("Ingresa el id del periodo de inscripcion.");
        return;
    }

    try {
        const respuesta = await fetch(
            API_BASE + "/inscripcion/comision/" + idAlumno + "/" + idComision + "/" + idPeriodo,
            { method: "POST" }
        );
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        mostrarMensaje("Te inscribiste en la comision.", "ok");
    } catch (error) {
        mostrarMensaje("No se pudo inscribir en la comision: " + error.message);
    }
}

btnVerComisiones.addEventListener("click", verComisiones);

// ============================================================
// 3) Inscripcion a un examen final
// ============================================================
async function verFinales() {
    try {
        const respuesta = await fetch(API_BASE + "/examenes/tipo/FINAL");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const finales = await respuesta.json();
        mostrarFinales(finales);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los finales: " + error.message);
    }
}

function mostrarFinales(lista) {
    tbodyFinales.innerHTML = "";
    if (!lista || lista.length === 0) {
        filaVacia(tbodyFinales, 4, "No hay examenes finales disponibles.");
        return;
    }

    for (const examen of lista) {
        const materia = examen.materia ? examen.materia.nombre : "";
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${examen.idExamen}</td>
            <td>${materia ?? ""}</td>
            <td>${formatearFecha(examen.fecha)}</td>
            <td><button type="button" class="btn btn-primario">Inscribirme</button></td>
        `;
        fila.querySelector("button").addEventListener("click", () =>
            inscribirFinal(examen.idExamen)
        );
        tbodyFinales.appendChild(fila);
    }
}

async function inscribirFinal(idExamen) {
    try {
        const respuesta = await fetch(
            API_BASE + "/inscripcion/examenFinal/" + idAlumno + "/" + idExamen,
            { method: "POST" }
        );
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        mostrarMensaje("Te inscribiste en el examen final.", "ok");
    } catch (error) {
        mostrarMensaje("No se pudo inscribir en el final: " + error.message);
    }
}

btnVerFinales.addEventListener("click", verFinales);

// ============================================================
// Utilidad local
// ============================================================
function filaVacia(tbody, columnas, texto) {
    tbody.innerHTML = `<tr><td colspan="${columnas}" class="celda-vacia">${texto}</td></tr>`;
}

// ============================================================
// Arranque: cargamos el desplegable de materias para la seccion de comisiones.
// ============================================================
cargarSelectMaterias();
