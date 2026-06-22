// ============================================================
// Gestion de comisiones (admin) - ABM completo
// Usa TODOS los endpoints de ComisionController:
//   GET    /comisiones                  -> listar (?nroComision para filtrar)
//   POST   /comisiones                  -> agregar
//   GET    /comisiones/{id}             -> ver (abre el editor)
//   PUT    /comisiones/{id}             -> modificar
//   DELETE /comisiones/{id}             -> eliminar
//   GET    /comisiones/profesor/{id}    -> comisiones de un profesor (ComisionDTO)
//
// El backend recibe/devuelve la entidad Comision, con la materia y el profesor
// anidados. Para crear/editar mandamos { materia: { idMateria }, profesor: { idPersona } }.
// Campo nuevo del backend: cupoMaximo (default 50).
// ============================================================

const API_URL = "http://localhost:8080/comisiones";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

// Filtros
const filtroNro = document.getElementById("filtro-nro");
const btnBuscarNro = document.getElementById("btn-buscar-nro");
const filtroProfesor = document.getElementById("filtro-profesor");
const btnBuscarProfesor = document.getElementById("btn-buscar-profesor");
const btnVerTodas = document.getElementById("btn-ver-todas");

// Agregar
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-comision");
const formulario = document.getElementById("form-comision");

// Editar
const dialogoEditar = document.getElementById("dialogo-editar");
const formEditar = document.getElementById("form-editar");
const editarIdSpan = document.getElementById("editar-id");
const btnCerrarEditar = document.getElementById("btn-cerrar-editar");

let comisionEditandoId = null;

// ============================================================
// Listar (GET /comisiones, GET /comisiones?nroComision=, GET /comisiones/profesor/{id})
// ============================================================
async function cargarComisiones(url = API_URL) {
    try {
        const respuesta = await fetch(url);
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

// El render sirve tanto para la entidad Comision (materia/profesor anidados)
// como para el ComisionDTO de "por profesor" (materiaNombre, sin profesor/cupo).
function mostrarComisiones(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" class="celda-vacia">No hay comisiones.</td></tr>`;
        return;
    }

    for (const comision of lista) {
        const materia = comision.materia ? comision.materia.nombre : (comision.materiaNombre ?? "-");
        const profesor = comision.profesor
            ? `${comision.profesor.nombre ?? ""} ${comision.profesor.apellido ?? ""}`.trim()
            : "-";

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${comision.idComision}</td>
            <td>${materia}</td>
            <td>${profesor || "-"}</td>
            <td>${comision.nroComision ?? "-"}</td>
            <td>${comision.aula ?? "-"}</td>
            <td>${comision.cantAlumnos ?? 0}</td>
            <td>${comision.cupoMaximo ?? "-"}</td>
            <td><button type="button" class="btn btn-peligro btn-eliminar">Eliminar</button></td>
        `;
        fila.addEventListener("click", () => verComision(comision.idComision));
        const btnEliminar = fila.querySelector(".btn-eliminar");
        btnEliminar.addEventListener("click", (evento) => {
            evento.stopPropagation();
            eliminarComision(comision.idComision);
        });
        tbody.appendChild(fila);
    }
}

// ============================================================
// Filtros
// ============================================================
btnVerTodas.addEventListener("click", () => {
    filtroNro.value = "";
    filtroProfesor.value = "";
    cargarComisiones();
});

btnBuscarNro.addEventListener("click", () => {
    const nro = filtroNro.value.trim();
    cargarComisiones(nro === "" ? API_URL : API_URL + "?nroComision=" + encodeURIComponent(nro));
});

btnBuscarProfesor.addEventListener("click", () => {
    const id = filtroProfesor.value.trim();
    if (id === "") {
        mostrarMensaje("Ingresa el id del profesor.");
        return;
    }
    cargarComisiones(API_URL + "/profesor/" + id);
});

// ============================================================
// Armar el cuerpo (entidad Comision con materia/profesor anidados)
// ============================================================
function armarBody(datos, incluirCantAlumnos) {
    const body = {
        nroComision: datos.nroComision === "" ? null : Number(datos.nroComision),
        aula: datos.aula || null,
        cupoMaximo: datos.cupoMaximo === "" ? null : Number(datos.cupoMaximo),
        materia: datos.idMateria ? { idMateria: Number(datos.idMateria) } : null,
        profesor: datos.idProfesor ? { idPersona: Number(datos.idProfesor) } : null,
    };
    // Al modificar, el backend pisa cantAlumnos con lo que le mandemos:
    // por eso preservamos el valor actual (campo readonly del formulario).
    if (incluirCantAlumnos) {
        body.cantAlumnos = datos.cantAlumnos === "" ? 0 : Number(datos.cantAlumnos);
    }
    return body;
}

// ============================================================
// Agregar (POST /comisiones)
// ============================================================
btnAgregar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formulario));

    try {
        const respuesta = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(armarBody(datos, false)),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogo.close();
        formulario.reset();
        cargarComisiones();
    } catch (error) {
        mostrarMensaje("No se pudo guardar la comision: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Ver / editar (GET /comisiones/{id} -> PUT /comisiones/{id})
// ============================================================
async function verComision(id) {
    try {
        const respuesta = await fetch(API_URL + "/" + id);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const comision = await respuesta.json();
        abrirEditor(id, comision);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo cargar la comision: " + error.message);
    }
}

function abrirEditor(id, comision) {
    comisionEditandoId = id;
    editarIdSpan.textContent = "#" + id;

    const campos = formEditar.elements;
    campos.idMateria.value = comision.materia ? comision.materia.idMateria : "";
    campos.idProfesor.value = comision.profesor ? comision.profesor.idPersona : "";
    campos.nroComision.value = comision.nroComision ?? "";
    campos.aula.value = comision.aula ?? "";
    campos.cupoMaximo.value = comision.cupoMaximo ?? "";
    campos.cantAlumnos.value = comision.cantAlumnos ?? 0;

    dialogoEditar.showModal();
}

btnCerrarEditar.addEventListener("click", () => dialogoEditar.close());

formEditar.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formEditar));

    try {
        const respuesta = await fetch(API_URL + "/" + comisionEditandoId, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(armarBody(datos, true)),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        cargarComisiones();
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
        dialogoEditar.close();
    }
});

// ============================================================
// Eliminar (DELETE /comisiones/{id})
// ============================================================
async function eliminarComision(id) {
    const confirmado = confirm("¿Eliminar la comision #" + id + "?");
    if (!confirmado) return;

    try {
        const respuesta = await fetch(API_URL + "/" + id, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        cargarComisiones();
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar la comision: " + error.message);
    }
}

// ============================================================
// Utilidades
// ============================================================
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
cargarComisiones();
