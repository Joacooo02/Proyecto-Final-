// ============================================================
// Gestion de horarios de comision (admin) - ABM completo
// Usa TODOS los endpoints de ComisionHorarioCont:
//   POST   /comisionHorario/agregar -> agregar
//   GET    /comisionHorario/mostrar -> listar
//   GET    /comisionHorario/{id}    -> buscar por id (abre el editor)
//   PUT    /comisionHorario/{id}    -> modificar
//   DELETE /comisionHorario/{id}    -> eliminar
//
// Notas (ver NOTAS_BACKEND.txt):
//  - El POST /agregar del backend usa @PathVariable en vez de @RequestBody,
//    asi que hoy el alta falla del lado del back. El front igual manda el body
//    correcto (JSON) para cuando lo arreglen.
//  - ComisionHorarioDTO no trae el id, por eso el listado no lo muestra y para
//    editar/eliminar hay que buscar por id.
//  - El enum DiaSemana del backend usa PascalCase (Lunes, Martes, ...).
// ============================================================

const API_URL = "http://localhost:8080/comisionHorario";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

// Agregar
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-horario");
const formulario = document.getElementById("form-horario");

// Buscar por id / editar / eliminar
const inputBuscarId = document.getElementById("buscar-id");
const btnBuscarId = document.getElementById("btn-buscar-id");
const dialogoEditar = document.getElementById("dialogo-editar");
const formEditar = document.getElementById("form-editar");
const editarIdSpan = document.getElementById("editar-id");
const btnCerrarEditar = document.getElementById("btn-cerrar-editar");
const btnEliminar = document.getElementById("btn-eliminar");

let horarioEditandoId = null;

// ============================================================
// Listar (GET /comisionHorario/mostrar)
// ============================================================
async function cargarHorarios() {
    try {
        const respuesta = await fetch(API_URL + "/mostrar");
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const horarios = await respuesta.json();
        mostrarHorarios(horarios);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los horarios: " + error.message);
    }
}

function mostrarHorarios(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" class="celda-vacia">No hay horarios.</td></tr>`;
        return;
    }
    tbody.innerHTML = lista
        .map(
            (h) => `
            <tr>
                <td>${h.idComision ?? "-"}</td>
                <td>${h.diaSemana ?? "-"}</td>
                <td>${formatHora(h.horaInicio)}</td>
                <td>${formatHora(h.horaFin)}</td>
            </tr>`
        )
        .join("");
}

// ============================================================
// Agregar (POST /comisionHorario/agregar)
// ============================================================
btnAgregar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formulario));

    const body = {
        idComision: datos.idComision === "" ? null : Number(datos.idComision),
        diaSemana: datos.diaSemana,
        horaInicio: datos.horaInicio,
        horaFin: datos.horaFin,
    };

    try {
        const respuesta = await fetch(API_URL + "/agregar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogo.close();
        formulario.reset();
        cargarHorarios();
    } catch (error) {
        mostrarMensaje("No se pudo guardar el horario: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Buscar por id (GET /comisionHorario/{id}) -> editor
// ============================================================
btnBuscarId.addEventListener("click", buscarPorId);
inputBuscarId.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") buscarPorId();
});

async function buscarPorId() {
    const id = inputBuscarId.value.trim();
    if (id === "") {
        mostrarMensaje("Ingresa el id del horario.");
        return;
    }

    try {
        const respuesta = await fetch(API_URL + "/" + id);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const horario = await respuesta.json();
        abrirEditor(id, horario);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se encontro el horario: " + error.message);
    }
}

function abrirEditor(id, horario) {
    horarioEditandoId = id;
    editarIdSpan.textContent = "#" + id;

    const campos = formEditar.elements;
    campos.idComision.value = horario.idComision ?? "";
    campos.diaSemana.value = horario.diaSemana ?? "Lunes";
    campos.horaInicio.value = formatHora(horario.horaInicio);
    campos.horaFin.value = formatHora(horario.horaFin);

    dialogoEditar.showModal();
}

btnCerrarEditar.addEventListener("click", () => dialogoEditar.close());

// ============================================================
// Modificar (PUT /comisionHorario/{id})
// ============================================================
formEditar.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formEditar));

    const body = {
        idComision: datos.idComision === "" ? null : Number(datos.idComision),
        diaSemana: datos.diaSemana,
        horaInicio: datos.horaInicio,
        horaFin: datos.horaFin,
    };

    try {
        const respuesta = await fetch(API_URL + "/" + horarioEditandoId, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        cargarHorarios();
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
        dialogoEditar.close();
    }
});

// ============================================================
// Eliminar (DELETE /comisionHorario/{id})
// ============================================================
btnEliminar.addEventListener("click", async () => {
    const confirmado = confirm("¿Eliminar el horario #" + horarioEditandoId + "?");
    if (!confirmado) return;

    try {
        const respuesta = await fetch(API_URL + "/" + horarioEditandoId, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        cargarHorarios();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar el horario: " + error.message);
        dialogoEditar.close();
    }
});

// ============================================================
// Utilidades
// ============================================================
// LocalTime puede llegar como "HH:mm:ss" (string) o como [HH, mm] (array).
function formatHora(valor) {
    if (valor == null) return "";
    if (Array.isArray(valor)) {
        return String(valor[0]).padStart(2, "0") + ":" + String(valor[1]).padStart(2, "0");
    }
    return String(valor).slice(0, 5);
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
cargarHorarios();
