// ============================================================
// Cargar notas (profesor) - ABM completo
// Usa TODOS los endpoints de NotaController:
//   GET    /examenes            -> lista de examenes (para los desplegables)
//   GET    /notas               -> listar todas las notas (NotaDTO)
//   GET    /notas/alumno/{id}   -> notas de un alumno
//   POST   /notas               -> registrar (NotaDTO)
//   PUT    /notas/{id}          -> modificar (NotaDTO)
//   DELETE /notas/{id}          -> eliminar
//
// OJO: el NotaDTO del backend tiene el campo escrito "idExamn" (sin la "e"),
// asi que el JSON tiene que mandar esa clave para que Jackson lo mapee.
// El backend valida que la nota este entre 0 y 10 y que el idAlumno (idPersona)
// y el idExamen existan. Al modificar, el backend pisa la fechaRegistro con la
// que mandemos, por eso el editor la preserva.
// ============================================================

SesionProfesor.requerir();

const formNota = document.getElementById("form-nota");
const selectExamen = document.getElementById("select-examen");

const tbodyNotas = document.getElementById("tbody-notas");
const filtroAlumno = document.getElementById("filtro-alumno");
const btnFiltrarAlumno = document.getElementById("btn-filtrar-alumno");
const btnVerTodas = document.getElementById("btn-ver-todas");

const dialogoEditar = document.getElementById("dialogo-editar");
const formEditar = document.getElementById("form-editar");
const editarExamen = document.getElementById("editar-examen");
const editarIdSpan = document.getElementById("editar-id");
const btnCerrarEditar = document.getElementById("btn-cerrar-editar");

// Mapa idExamen -> etiqueta legible, para mostrar el examen en la tabla.
let examenesMap = {};
// Ultimas notas cargadas (no imprescindible, pero util para depurar).
let notasActuales = [];

// ============================================================
// Cargar los desplegables de examenes (alta y edicion)
// ============================================================
async function cargarExamenes() {
    try {
        // GET /examenes (lista completa) es ADMIN-only en el backend, asi que como
        // PROFESOR daria 403. Usamos los endpoints por tipo (/examenes/tipo/PARCIAL
        // y /FINAL), que SI permiten rol PROFESOR, y unimos los resultados.
        const [parciales, finales] = await Promise.all([
            fetch(API_BASE + "/examenes/tipo/PARCIAL").then((r) => (r.ok ? r.json() : [])),
            fetch(API_BASE + "/examenes/tipo/FINAL").then((r) => (r.ok ? r.json() : [])),
        ]);
        const examenes = [...parciales, ...finales].sort(
            (a, b) => (a.idExamen ?? 0) - (b.idExamen ?? 0)
        );
        for (const examen of examenes) {
            const materia = examen.materia ? examen.materia.nombre : "Materia";
            const etiqueta =
                "#" + examen.idExamen + " - " + materia +
                " (" + (examen.tipoExamen ?? "") + ") " + formatearFecha(examen.fecha);
            examenesMap[examen.idExamen] = etiqueta;

            selectExamen.appendChild(nuevaOpcion(examen.idExamen, etiqueta));
            editarExamen.appendChild(nuevaOpcion(examen.idExamen, etiqueta));
        }
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los examenes: " + error.message);
    }
}

function nuevaOpcion(valor, texto) {
    const opcion = document.createElement("option");
    opcion.value = valor;
    opcion.textContent = texto;
    return opcion;
}

function etiquetaExamen(idExamen) {
    return examenesMap[idExamen] ?? ("#" + idExamen);
}

// ============================================================
// Listar notas (GET /notas  y  GET /notas/alumno/{id})
// ============================================================
async function cargarNotas(url = API_BASE + "/notas") {
    try {
        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        notasActuales = await respuesta.json();
        mostrarNotas(notasActuales);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las notas: " + error.message);
    }
}

function mostrarNotas(lista) {
    tbodyNotas.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbodyNotas.innerHTML = `<tr><td colspan="6" class="celda-vacia">No hay notas.</td></tr>`;
        return;
    }

    for (const nota of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${nota.id}</td>
            <td>${etiquetaExamen(nota.idExamn)}</td>
            <td>${nota.idAlumno ?? "-"}</td>
            <td>${nota.nota ?? "-"}</td>
            <td>${formatearFecha(nota.fechaRegistro)}</td>
            <td>
                <button type="button" class="btn btn-editar">Editar</button>
                <button type="button" class="btn btn-peligro btn-eliminar">Eliminar</button>
            </td>
        `;
        fila.querySelector(".btn-editar").addEventListener("click", () => abrirEditor(nota));
        fila.querySelector(".btn-eliminar").addEventListener("click", () => eliminarNota(nota.id));
        tbodyNotas.appendChild(fila);
    }
}

btnVerTodas.addEventListener("click", () => {
    filtroAlumno.value = "";
    cargarNotas();
});

btnFiltrarAlumno.addEventListener("click", () => {
    const id = filtroAlumno.value.trim();
    if (id === "") {
        mostrarMensaje("Ingresa el id del alumno para filtrar.");
        return;
    }
    cargarNotas(API_BASE + "/notas/alumno/" + id);
});
filtroAlumno.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") btnFiltrarAlumno.click();
});

// ============================================================
// Crear nota (POST /notas)
// ============================================================
formNota.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const datos = Object.fromEntries(new FormData(formNota));

    // Armamos el NotaDTO. Convertimos a numero y usamos la clave "idExamn"
    // (asi se llama el campo en el backend) para que el mapeo funcione.
    const body = {
        idAlumno: Number(datos.idAlumno),
        idExamn: Number(datos.idExamen),
        nota: Number(datos.nota),
    };

    try {
        const respuesta = await fetch(API_BASE + "/notas", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        formNota.reset();
        mostrarMensaje("Nota registrada.", "ok");
        cargarNotas();
    } catch (error) {
        mostrarMensaje("No se pudo registrar la nota: " + error.message);
    }
});

// ============================================================
// Editar nota (PUT /notas/{id})
// ============================================================
function abrirEditor(nota) {
    editarIdSpan.textContent = "#" + nota.id;
    formEditar.dataset.id = nota.id;

    const campos = formEditar.elements;
    campos.idExamen.value = nota.idExamn ?? "";
    campos.idAlumno.value = nota.idAlumno ?? "";
    campos.nota.value = nota.nota ?? "";
    campos.fechaRegistro.value = nota.fechaRegistro ?? "";

    dialogoEditar.showModal();
}

btnCerrarEditar.addEventListener("click", () => dialogoEditar.close());

formEditar.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const datos = Object.fromEntries(new FormData(formEditar));
    const body = {
        idAlumno: Number(datos.idAlumno),
        idExamn: Number(datos.idExamen),
        nota: Number(datos.nota),
        // El backend pisa la fechaRegistro con esto: la preservamos.
        fechaRegistro: datos.fechaRegistro === "" ? null : datos.fechaRegistro,
    };

    try {
        const respuesta = await fetch(API_BASE + "/notas/" + formEditar.dataset.id, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoEditar.close();
        mostrarMensaje("Nota actualizada.", "ok");
        cargarNotas();
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
    }
});

// ============================================================
// Eliminar nota (DELETE /notas/{id})
// ============================================================
async function eliminarNota(id) {
    if (!confirm("¿Eliminar la nota #" + id + "?")) return;

    try {
        const respuesta = await fetch(API_BASE + "/notas/" + id, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        cargarNotas();
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar la nota: " + error.message);
    }
}

// ============================================================
// Arranque: primero los examenes (para las etiquetas), despues las notas.
// ============================================================
async function init() {
    await cargarExamenes();
    cargarNotas();
}
init();
