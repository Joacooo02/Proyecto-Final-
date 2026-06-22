// ============================================================
// Gestion de avisos (admin)
// Usa TODOS los endpoints de AvisoController:
//   POST   /avisos/persona/{idPersona}      -> crear (autor por la URL; body titulo/contenido)
//   GET    /avisos                          -> listar
//   GET    /avisos/{idAviso}                -> ver por id
//   DELETE /avisos/{idAviso}/{idPersona}    -> eliminar
//
// Nota (ver NOTAS_BACKEND.txt): AvisoDTO no trae ids (ni idAviso ni idPersona),
// solo nombre/apellido del autor. Por eso el listado no muestra ids y para ver
// el detalle o eliminar hay que ingresar el id a mano. Crear/eliminar lo permite
// solo PROFESOR o ADMIN (lo valida el backend).
// ============================================================

const API_URL = "http://localhost:8080/avisos";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

// Crear
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-aviso");
const formulario = document.getElementById("form-aviso");

// Ver por id / eliminar
const inputBuscarId = document.getElementById("buscar-id");
const btnBuscarId = document.getElementById("btn-buscar-id");
const dialogoDetalle = document.getElementById("dialogo-detalle");
const btnCerrarDetalle = document.getElementById("btn-cerrar-detalle");
const btnEliminar = document.getElementById("btn-eliminar");
const btnModificar = document.getElementById("btn-modificar");
const delIdPersona = document.getElementById("del-idPersona");

let avisoVistoId = null;

// ============================================================
// Listar (GET /avisos)
// ============================================================
async function cargarAvisos() {
    try {
        const respuesta = await fetch(API_URL);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const avisos = await respuesta.json();
        mostrarAvisos(avisos);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los avisos: " + error.message);
    }
}

function mostrarAvisos(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" class="celda-vacia">No hay avisos.</td></tr>`;
        return;
    }
    tbody.innerHTML = lista
        .map(
            (a) => `
            <tr>
                <td><strong>#${a.id ?? "-"}</strong></td>
                <td>${`${a.nombre ?? ""} ${a.apellido ?? ""}`.trim() || "-"}</td>
                <td>${a.titulo ?? ""}</td>
                <td>${a.contenido ?? ""}</td>
                <td>${formatFechaHora(a.fechaAviso)}</td>
            </tr>`
        )
        .join("");
}

// ============================================================
// Crear (POST /avisos/persona/{idPersona})
// ============================================================
btnAgregar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formulario));

    // El autor (idPersona) va en la URL; el cuerpo lleva titulo y contenido.
    const body = { titulo: datos.titulo, contenido: datos.contenido };

    try {
        const respuesta = await fetch(API_URL + "/persona/" + datos.idPersona, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogo.close();
        formulario.reset();
        cargarAvisos();
    } catch (error) {
        mostrarMensaje("No se pudo crear el aviso: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Ver por id (GET /avisos/{idAviso})
// ============================================================
btnBuscarId.addEventListener("click", verPorId);
inputBuscarId.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") verPorId();
});

async function verPorId() {
    const id = inputBuscarId.value.trim();
    if (id === "") {
        mostrarMensaje("Ingresa el id del aviso.");
        return;
    }

    try {
        const respuesta = await fetch(API_URL + "/" + id);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const aviso = await respuesta.json();
        mostrarDetalle(id, aviso);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se encontro el aviso: " + error.message);
    }
}

function mostrarDetalle(id, aviso) {
    avisoVistoId = id;
    document.getElementById("detalle-id").textContent = "#" + id;
    document.getElementById("det-persona").textContent =
        `${aviso.nombre ?? ""} ${aviso.apellido ?? ""}`.trim() || "-";
    
    // MODIFICADO: Ahora cargamos los datos dentro de los inputs .value en vez de .textContent
    document.getElementById("edit-titulo").value = aviso.titulo ?? "";
    document.getElementById("edit-contenido").value = aviso.contenido ?? "";
    
    document.getElementById("det-fecha").textContent = formatFechaHora(aviso.fechaAviso);
    delIdPersona.value = "";
    dialogoDetalle.showModal();
}

btnCerrarDetalle.addEventListener("click", () => dialogoDetalle.close());

// ============================================================
// Eliminar (DELETE /avisos/{idAviso}/{idPersona})
// ============================================================
btnEliminar.addEventListener("click", async () => {
    const idPersona = delIdPersona.value.trim();
    if (idPersona === "") {
        mostrarMensaje("Ingresa tu id (profesor/admin) para eliminar.");
        return;
    }
    const confirmado = confirm("¿Eliminar el aviso #" + avisoVistoId + "?");
    if (!confirmado) return;

    try {
        const respuesta = await fetch(API_URL + "/" + avisoVistoId + "/" + idPersona, {
            method: "DELETE",
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoDetalle.close();
        cargarAvisos();
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar el aviso: " + error.message);
    }
});



btnModificar.addEventListener("click", async () => {
    const idPersona = delIdPersona.value.trim();
    const nuevoTitulo = document.getElementById("edit-titulo").value.trim();
    const nuevoContenido = document.getElementById("edit-contenido").value.trim();

    if (idPersona === "") {
        mostrarMensaje("Ingresa tu id (profesor/admin) para modificar.");
        return;
    }
    if (nuevoTitulo === "" || nuevoContenido === "") {
        mostrarMensaje("El título y el contenido no pueden estar vacíos.");
        return;
    }

    const body = { titulo: nuevoTitulo, contenido: nuevoContenido };

    try {
        const respuesta = await fetch(`${API_URL}/${avisoVistoId}/persona/${idPersona}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });

        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoDetalle.close();
        cargarAvisos(); // Recarga la grilla principal
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo modificar el aviso: " + error.message);
    }
});

// ============================================================
// Utilidades
// ============================================================
function formatFechaHora(valor) {
    if (!valor) return "";
    const fecha = new Date(valor);
    if (isNaN(fecha.getTime())) return valor;
    return fecha.toLocaleString("es-AR");
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
cargarAvisos();
