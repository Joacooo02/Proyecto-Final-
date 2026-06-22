// ============================================================
// Gestion Alumno-Materia (admin) - ABM completo
// Usa TODOS los endpoints de AlumnoMateriaController:
//   GET    /alumno-materia                  -> listar
//   GET    /alumno-materia/{id}             -> ver (abre el editor)
//   GET    /alumno-materia/historial/{idAlumno} -> historial de un alumno
//   POST   /alumno-materia                  -> crear
//   PUT    /alumno-materia/{id}             -> modificar
//   DELETE /alumno-materia/{id}             -> eliminar
//
// El DTO trae id, idAlumno, alumnoNombre, idMateria, materiaNombre, estado,
// notaFinal y fechaAprobacion. Como trae el id, se edita/elimina desde la fila.
// ============================================================

const API_URL = "http://localhost:8080/alumno-materia";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

const filtroAlumno = document.getElementById("filtro-alumno");
const btnHistorial = document.getElementById("btn-historial");
const btnVerTodos = document.getElementById("btn-ver-todos");

const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-am");
const formulario = document.getElementById("form-am");

const dialogoEditar = document.getElementById("dialogo-editar");
const formEditar = document.getElementById("form-editar");
const editarIdSpan = document.getElementById("editar-id");
const btnCerrarEditar = document.getElementById("btn-cerrar-editar");

let registroEditandoId = null;

// ============================================================
// Listar (GET /alumno-materia  y  GET /alumno-materia/historial/{idAlumno})
// ============================================================
async function cargar(url = API_URL) {
    try {
        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const lista = await respuesta.json();
        mostrar(lista);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los registros: " + error.message);
    }
}

function mostrar(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" class="celda-vacia">No hay registros.</td></tr>`;
        return;
    }

    for (const am of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${am.id}</td>
            <td>${am.alumnoNombre ?? ("#" + am.idAlumno)}</td>
            <td>${am.materiaNombre ?? ("#" + am.idMateria)}</td>
            <td>${am.estado ?? "-"}</td>
            <td>${am.notaFinal ?? "-"}</td>
            <td>${formatearFecha(am.fechaAprobacion)}</td>
            <td><button type="button" class="btn btn-peligro btn-eliminar">Eliminar</button></td>
        `;
        fila.addEventListener("click", () => ver(am.id));
        fila.querySelector(".btn-eliminar").addEventListener("click", (evento) => {
            evento.stopPropagation();
            eliminar(am.id);
        });
        tbody.appendChild(fila);
    }
}

btnVerTodos.addEventListener("click", () => {
    filtroAlumno.value = "";
    cargar();
});

btnHistorial.addEventListener("click", () => {
    const id = filtroAlumno.value.trim();
    if (id === "") {
        mostrarMensaje("Ingresa el id del alumno para ver su historial.");
        return;
    }
    cargar(API_URL + "/historial/" + id);
});

// ============================================================
// Armar cuerpo (AlumnoMateriaDTO)
// ============================================================
function armarBody(datos) {
    return {
        idAlumno: datos.idAlumno === "" ? null : Number(datos.idAlumno),
        idMateria: datos.idMateria === "" ? null : Number(datos.idMateria),
        estado: datos.estado,
        notaFinal: datos.notaFinal === "" ? null : Number(datos.notaFinal),
        fechaAprobacion: datos.fechaAprobacion === "" ? null : datos.fechaAprobacion,
    };
}

// ============================================================
// Agregar (POST /alumno-materia)
// ============================================================
btnAgregar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    try {
        const respuesta = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(armarBody(Object.fromEntries(new FormData(formulario)))),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogo.close();
        formulario.reset();
        cargar();
    } catch (error) {
        mostrarMensaje("No se pudo guardar el registro: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Ver / editar (GET /alumno-materia/{id} -> PUT /alumno-materia/{id})
// ============================================================
async function ver(id) {
    try {
        const respuesta = await fetch(API_URL + "/" + id);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        abrirEditor(id, await respuesta.json());
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo cargar el registro: " + error.message);
    }
}

function abrirEditor(id, am) {
    registroEditandoId = id;
    editarIdSpan.textContent = "#" + id;
    const campos = formEditar.elements;
    campos.idAlumno.value = am.idAlumno ?? "";
    campos.idMateria.value = am.idMateria ?? "";
    campos.estado.value = am.estado ?? "PENDIENTE";
    campos.notaFinal.value = am.notaFinal ?? "";
    campos.fechaAprobacion.value = am.fechaAprobacion ?? "";
    dialogoEditar.showModal();
}

btnCerrarEditar.addEventListener("click", () => dialogoEditar.close());

formEditar.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    try {
        const respuesta = await fetch(API_URL + "/" + registroEditandoId, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(armarBody(Object.fromEntries(new FormData(formEditar)))),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        cargar();
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
        dialogoEditar.close();
    }
});

// ============================================================
// Eliminar (DELETE /alumno-materia/{id})
// ============================================================
async function eliminar(id) {
    if (!confirm("¿Eliminar el registro #" + id + "?")) return;
    try {
        const respuesta = await fetch(API_URL + "/" + id, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        cargar();
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar el registro: " + error.message);
    }
}

// ============================================================
// Utilidades
// ============================================================
function formatearFecha(valor) {
    if (!valor) return "";
    const fecha = new Date(valor);
    if (isNaN(fecha.getTime())) return valor;
    return fecha.toLocaleDateString("es-AR");
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
cargar();
