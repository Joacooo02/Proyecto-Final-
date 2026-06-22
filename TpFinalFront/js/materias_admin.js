// ============================================================
// Gestion de materias (admin) - ABM completo
// Usa TODOS los endpoints de MateriaController:
//   GET    /materias?nombre=...              -> listar / buscar
//   POST   /materias                         -> agregar
//   GET    /materias/{id}                    -> ver (abre el editor)
//   PUT    /materias/{id}                    -> modificar
//   DELETE /materias/{id}                    -> eliminar
//   GET    /materias/plan-academico/{idAlumno} -> plan academico de un alumno
//
// Nota: el "Id carrera" se ingresa a mano porque CarreraDTO no expone el id
// (no se puede armar un desplegable de carreras). Ver NOTAS_BACKEND.txt (punto 2).
// ============================================================

const API_URL = "http://localhost:8080/materias";

const tbody = document.querySelector(".tabla-datos tbody"); // primera tabla = materias
const mensaje = document.getElementById("mensaje");
const inputFiltro = document.getElementById("filtro");
const btnBuscar = document.getElementById("btn-buscar");

// Agregar
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-materia");
const formulario = document.getElementById("form-materia");

// Editar
const dialogoEditar = document.getElementById("dialogo-editar");
const formEditar = document.getElementById("form-editar");
const editarIdSpan = document.getElementById("editar-id");
const btnCerrarEditar = document.getElementById("btn-cerrar-editar");

// Plan academico
const inputAlumno = document.getElementById("input-alumno");
const btnVerPlan = document.getElementById("btn-ver-plan");
const tbodyPlan = document.getElementById("tbody-plan");

let materiaEditandoId = null;

// ============================================================
// Listar / buscar (GET /materias?nombre=...)
// ============================================================
async function cargarMaterias() {
    const nombre = inputFiltro.value.trim();
    let url = API_URL;
    if (nombre !== "") {
        url += "?nombre=" + encodeURIComponent(nombre);
    }

    try {
        const respuesta = await fetch(url);
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
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="celda-vacia">No hay materias.</td></tr>`;
        return;
    }

    for (const materia of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${materia.id}</td>
            <td>${materia.nombre ?? ""}</td>
            <td>${materia.idCarrera ?? "-"}</td>
            <td>${materia.cargaHoraria ?? "-"}</td>
            <td>${materia.cuatrimestre ?? "-"}</td>
            <td>${materia.anioCursado ?? "-"}</td>
            <td><button type="button" class="btn btn-peligro btn-eliminar">Eliminar</button></td>
        `;
        // Click en la fila -> editar (trae la materia por id).
        fila.addEventListener("click", () => verMateria(materia.id));
        // Boton eliminar (sin disparar el click de la fila).
        const btnEliminar = fila.querySelector(".btn-eliminar");
        btnEliminar.addEventListener("click", (evento) => {
            evento.stopPropagation();
            eliminarMateria(materia.id, materia.nombre);
        });
        tbody.appendChild(fila);
    }
}

// ============================================================
// Agregar (POST /materias)
// ============================================================
btnAgregar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const datos = Object.fromEntries(new FormData(formulario));
    normalizarVacios(datos);

    try {
        const respuesta = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogo.close();
        formulario.reset();
        cargarMaterias();
    } catch (error) {
        mostrarMensaje("No se pudo guardar la materia: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Ver / editar (GET /materias/{id} -> PUT /materias/{id})
// ============================================================
async function verMateria(id) {
    try {
        const respuesta = await fetch(API_URL + "/" + id);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const materia = await respuesta.json();
        abrirEditor(id, materia);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo cargar la materia: " + error.message);
    }
}

function abrirEditor(id, materia) {
    materiaEditandoId = id;
    editarIdSpan.textContent = "#" + id;

    const campos = formEditar.elements;
    campos.nombre.value = materia.nombre ?? "";
    campos.idCarrera.value = materia.idCarrera ?? "";
    campos.cargaHoraria.value = materia.cargaHoraria ?? "";
    campos.cuatrimestre.value = materia.cuatrimestre ?? "";
    campos.anioCursado.value = materia.anioCursado ?? "";

    dialogoEditar.showModal();
}

btnCerrarEditar.addEventListener("click", () => dialogoEditar.close());

formEditar.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const datos = Object.fromEntries(new FormData(formEditar));
    normalizarVacios(datos);

    try {
        const respuesta = await fetch(API_URL + "/" + materiaEditandoId, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        cargarMaterias();
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
        dialogoEditar.close();
    }
});

// ============================================================
// Eliminar (DELETE /materias/{id})
// ============================================================
async function eliminarMateria(id, nombre) {
    const confirmado = confirm(`¿Eliminar la materia "${nombre}"?`);
    if (!confirmado) return;

    try {
        const respuesta = await fetch(API_URL + "/" + id, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        cargarMaterias();
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar la materia: " + error.message);
    }
}

// ============================================================
// Plan academico por alumno (GET /materias/plan-academico/{idAlumno})
// ============================================================
btnVerPlan.addEventListener("click", verPlan);
inputAlumno.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") verPlan();
});

async function verPlan() {
    const idAlumno = inputAlumno.value.trim();
    if (idAlumno === "") {
        mostrarMensaje("Ingresa el id del alumno para ver su plan.");
        return;
    }

    try {
        const respuesta = await fetch(API_URL + "/plan-academico/" + idAlumno);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const plan = await respuesta.json();
        mostrarPlan(plan);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo cargar el plan: " + error.message);
    }
}

function mostrarPlan(lista) {
    if (!lista || lista.length === 0) {
        tbodyPlan.innerHTML = `<tr><td colspan="4" class="celda-vacia">Sin materias en el plan.</td></tr>`;
        return;
    }
    tbodyPlan.innerHTML = lista
        .map(
            (materia) => `
            <tr>
                <td>${materia.nombre ?? ""}</td>
                <td>${materia.anioCursado ?? "-"}</td>
                <td>${materia.cuatrimestre ?? "-"}</td>
                <td>${materia.cargaHoraria ?? "-"}</td>
            </tr>`
        )
        .join("");
}

// ============================================================
// Buscar
// ============================================================
btnBuscar.addEventListener("click", cargarMaterias);
inputFiltro.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") cargarMaterias();
});

// ============================================================
// Utilidades
// ============================================================
function normalizarVacios(datos) {
    for (const campo of ["cargaHoraria", "cuatrimestre", "anioCursado"]) {
        if (datos[campo] === "") datos[campo] = null;
    }
}

async function extraerMensajeError(respuesta) {
    try {
        const cuerpo = await respuesta.json();
        return cuerpo.mensaje || cuerpo.message || cuerpo.error || ("Error " + respuesta.status);
    } catch {
        return "Error " + respuesta.status;
    }
}

function mostrarMensaje(texto) {
    mensaje.textContent = texto;
    mensaje.hidden = false;
}

function ocultarMensaje() {
    mensaje.hidden = true;
}

// ============================================================
// Arranque
// ============================================================
cargarMaterias();
