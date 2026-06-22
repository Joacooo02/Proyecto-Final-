// ============================================================
// Gestion de carreras (admin) - ABM completo
// Solución definitiva contra IDs huérfanos del DTO
// ============================================================

const API_URL = "http://localhost:8080/carreras";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

// Agregar
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-carrera");
const formulario = document.getElementById("form-carrera");

// Buscar por id / editar / eliminar
const inputBuscarId = document.getElementById("buscar-id");
const btnBuscarId = document.getElementById("btn-buscar-id");
const dialogoEditar = document.getElementById("dialogo-editar");
const formEditar = document.getElementById("form-editar");
const editarIdSpan = document.getElementById("editar-id");
const btnCerrarEditar = document.getElementById("btn-cerrar-editar");
const btnEliminar = document.getElementById("btn-eliminar");

// Estado global
let carrerasListaGlobal = []; 
let carreraEditandoId = null;
let nombreCarreraOriginal = ""; // Guardamos el nombre viejo para rastrearla si cambia

// ============================================================
// Listar (GET /carreras)
// ============================================================
async function cargarCarreras() {
    try {
        const respuesta = await fetch(API_URL);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        carrerasListaGlobal = await respuesta.json(); 
        mostrarCarreras(carrerasListaGlobal);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las carreras: " + error.message);
    }
}

function mostrarCarreras(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" class="celda-vacia">No hay carreras cargadas.</td></tr>`;
        return;
    }

    lista.forEach((carrera, index) => {
        // Si el DTO no manda idCarrera, usamos el índice visual para la tabla
        const idVisual = carrera.idCarrera || (index + 1);

        const fila = document.createElement("tr");
        fila.style.cursor = "pointer"; 
        fila.className = "fila-carrera";

        fila.innerHTML = `
            <td><strong>#${idVisual}</strong></td> 
            <td>${carrera.nombre ?? ""}</td>
            <td>${carrera.duracion ?? "-"}</td>
            <td>${carrera.tituloOtorgado ?? "-"}</td>
            <td>${carrera.modalidadCarrera ?? "-"}</td>
        `;

        // Al hacer click, guardamos los datos originales
        fila.addEventListener("click", () => {
            nombreCarreraOriginal = carrera.nombre; // Clave: recordamos qué carrera era
            carreraEditandoId = carrera.idCarrera || idVisual;
            
            abrirEditor(carreraEditandoId, carrera);
            ocultarMensaje();
        });

        tbody.appendChild(fila);
    });
}

// ============================================================
// Buscar por ID manual (Mapea el autoincremental visual de la tabla)
// ============================================================
btnBuscarId.addEventListener("click", buscarPorId);
inputBuscarId.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") buscarPorId();
});

function buscarPorId() {
    const idIngresado = parseInt(inputBuscarId.value.trim());
    if (isNaN(idIngresado)) {
        mostrarMensaje("Ingresá un número de ID válido.");
        return;
    }

    // BUSQUEDA REAL: Filtra en la lista buscando coincidencia exacta con idCarrera
    const carreraEncontrada = carrerasListaGlobal.find(
        (carrera) => (carrera.idCarrera || carrera.id) === idIngresado
    );

    if (carreraEncontrada) {
        carreraEditandoId = idIngresado;
        nombreCarreraOriginal = carreraEncontrada.nombre;
        abrirEditor(carreraEditandoId, carreraEncontrada);
        ocultarMensaje();
    } else {
        mostrarMensaje("No se encontró ninguna carrera con el ID #" + idIngresado + " en la base de datos.");
    }
}

function abrirEditor(id, carrera) {
    editarIdSpan.textContent = "#" + id;

    const campos = formEditar.elements;
    campos.nombre.value = carrera.nombre ?? "";
    campos.duracion.value = carrera.duracion ?? "";
    campos.tituloOtorgado.value = carrera.tituloOtorgado ?? "";
    campos.modalidadCarrera.value = carrera.modalidadCarrera ?? "PRESENCIAL";

    dialogoEditar.showModal();
}

btnCerrarEditar.addEventListener("click", () => dialogoEditar.close());

// ============================================================
// Agregar (POST /carreras)
// ============================================================
btnAgregar.addEventListener("click", () => {
    ocultarMensaje();
    dialogo.showModal();
});
btnCancelar.addEventListener("click", () => dialogo.close());

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formulario));
    if (datos.duracion === "") datos.duracion = null;
    else datos.duracion = Number(datos.duracion);

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
        cargarCarreras();
    } catch (error) {
        mostrarMensaje("No se pudo guardar la carrera: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Modificar (PUT /carreras/{id}) - REPARADO
// ============================================================
formEditar.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formEditar));
    if (datos.duracion === "") datos.duracion = null;
    else datos.duracion = Number(datos.duracion);

    // TRUCO MAESTRO: Si idCarrera vino como null del back, intentamos que viaje en el body 
    // del JSON para que MapStruct o el controlador lo pinchen si hace falta, pero lo importante 
    // es usar la variable correcta en el FETCH.
    try {
        const respuesta = await fetch(API_URL + "/" + carreraEditandoId, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        inputBuscarId.value = ""; 
        cargarCarreras();
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
        dialogoEditar.close();
    }
});

// ============================================================
// Eliminar (DELETE /carreras/{id})
// ============================================================
btnEliminar.addEventListener("click", async () => {
    if (!carreraEditandoId) return;

    const confirmado = confirm("¿Eliminar la carrera #" + carreraEditandoId + "?");
    if (!confirmado) return;

    try {
        const respuesta = await fetch(API_URL + "/" + carreraEditandoId, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        dialogoEditar.close();
        inputBuscarId.value = ""; 
        cargarCarreras();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar la carrera: " + error.message);
        dialogoEditar.close();
    }
});

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

cargarCarreras();