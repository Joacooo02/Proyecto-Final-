// ============================================================
// Gestion de cuotas (admin)
// Usa los endpoints de CuotaController que faltaban (la parte de admin):
//   POST /cuotas         -> generar cuota (devuelve TEXTO, no JSON)
//   GET  /cuotas/estado   -> listar cuotas por estado (?estado=PENDIENTE/PAGADA/VENCIDA)
//   PUT  /cuotas/{id}    -> modificar cuota
//   DELETE /cuotas/{id}  -> eliminar cuota
// ============================================================

const API_URL = "http://localhost:8080/cuotas";

const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");

const filtroEstado = document.getElementById("filtro-estado");
const btnBuscar = document.getElementById("btn-buscar");

const btnGenerar = document.getElementById("btn-generar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-cuota");
const formulario = document.getElementById("form-cuota");

// ============================================================
// Elementos del modal de gestión (Modificar/Eliminar)
// ============================================================
const dialogoGestion = document.getElementById("dialogo-gestion-cuota");
const btnCerrarGestion = document.getElementById("btn-cerrar-gestion");
const btnModificarCuota = document.getElementById("btn-modificar-cuota");
const btnEliminarCuota = document.getElementById("btn-eliminar-cuota");

let cuotaVistaId = null;
let listaCuotasActuales = []; // Guarda las cuotas en memoria para poder editarlas al hacer clic

// ============================================================
// Listar por estado (GET /cuotas/estado?estado=...)
// ============================================================
async function cargarCuotas() {
    try {
        const respuesta = await fetch(API_URL + "/estado?estado=" + filtroEstado.value);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const cuotas = await respuesta.json();
        listaCuotasActuales = cuotas; // Guardamos la lista actual en memoria
        mostrarCuotas(cuotas);
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las cuotas: " + error.message);
    }
}

function mostrarCuotas(lista) {
    tbody.innerHTML = "";
    if (!lista || lista.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" class="celda-vacia">No hay cuotas con ese estado.</td></tr>`;
        return;
    }
    tbody.innerHTML = lista
        .map(
            (c) => {
                // Formateamos la fecha de vencimiento usando la utilidad mejorada
                const vencimiento = formatearFecha(c.fechaVencimiento);

                // Retornamos exactamente 8 celdas en el mismo orden que tus 8 encabezados
                return `
                <tr>
                    <td>
                        <button class="btn-link" onclick="abrirGestionCuota(${c.id})" style="background:none; border:none; color:#007bff; cursor:pointer; text-decoration:underline; font-weight:bold;">
                            #${c.id ?? "-"}
                        </button>
                    </td>
                    <td>${c.idAlumno ?? "-"}</td>
                    <td>${c.mes ?? "-"}</td>
                    <td>${c.anio ?? "-"}</td>
                    <td>$${c.valorCuota ?? "-"}</td>
                    <td>${c.conceptoCuota ?? "-"}</td>
                    <td>${vencimiento}</td>
                    <td>${c.estadoCuota ?? "-"}</td>
                </tr>`;
            }
        )
        .join("");
}

btnBuscar.addEventListener("click", cargarCuotas);
filtroEstado.addEventListener("change", cargarCuotas);

// ============================================================
// Nueva función para abrir el modal de gestión
// ============================================================
window.abrirGestionCuota = function(id) {
    const cuota = listaCuotasActuales.find(c => c.id === id);
    if (!cuota) return;

    cuotaVistaId = id;
    document.getElementById("gestion-id-cuota").textContent = "#" + id;
    document.getElementById("edit-valorCuota").value = cuota.valorCuota ?? 0;
    document.getElementById("edit-conceptoCuota").value = cuota.conceptoCuota ?? "CUOTA";
    document.getElementById("edit-estadoCuota").value = cuota.estadoCuota ?? "PENDIENTE";
    document.getElementById("edit-fechaVencimiento").value = cuota.fechaVencimiento ?? "";

    dialogoGestion.showModal();
};

btnCerrarGestion.addEventListener("click", () => dialogoGestion.close());

// ============================================================
// Acción para Guardar Cambios (PUT /cuotas/{idCuota})
// ============================================================
btnModificarCuota.addEventListener("click", async () => {
    const valor = Number(document.getElementById("edit-valorCuota").value);
    const concepto = document.getElementById("edit-conceptoCuota").value;
    const estado = document.getElementById("edit-estadoCuota").value;
    const vencimiento = document.getElementById("edit-fechaVencimiento").value;

    const body = {
        valorCuota: valor,
        conceptoCuota: concepto,
        estadoCuota: estado,
        fechaVencimiento: vencimiento === "" ? null : vencimiento
    };

    try {
        const respuesta = await fetch(`${API_URL}/${cuotaVistaId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });

        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoGestion.close();
        mostrarMensaje("Cuota modificada correctamente", "ok");
        cargarCuotas(); // Recarga la tabla
    } catch (error) {
        mostrarMensaje("No se pudo modificar la cuota: " + error.message);
    }
});

// ============================================================
// Acción para Eliminar (DELETE /cuotas/{idCuota})
// ============================================================
btnEliminarCuota.addEventListener("click", async () => {
    const confirmado = confirm("¿Estás seguro de que deseas eliminar la cuota #" + cuotaVistaId + "?");
    if (!confirmado) return;

    try {
        const respuesta = await fetch(`${API_URL}/${cuotaVistaId}`, {
            method: "DELETE",
        });

        const texto = await respuesta.text();
        if (!respuesta.ok) {
            throw new Error(texto || ("Error " + respuesta.status));
        }

        dialogoGestion.close();
        mostrarMensaje(texto, "ok");
        cargarCuotas(); // Recarga la tabla
    } catch (error) {
        mostrarMensaje("No se pudo eliminar la cuota: " + error.message);
    }
});

// ============================================================
// Generar cuota (POST /cuotas) - el backend devuelve un texto
// ============================================================
btnGenerar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formulario));

    const body = {
        idAlumno: Number(datos.idAlumno),
        mes: Number(datos.mes),
        anio: Number(datos.anio),
        valorCuota: Number(datos.valorCuota),
        conceptoCuota: datos.conceptoCuota,
        fechaVencimiento: datos.fechaVencimiento === "" ? null : datos.fechaVencimiento,
        estadoCuota: datos.estadoCuota === "" ? null : datos.estadoCuota,
    };

    try {
        const respuesta = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });
        
        const texto = await respuesta.text();
        if (!respuesta.ok) {
            throw new Error(texto || ("Error " + respuesta.status));
        }

        dialogo.close();
        formulario.reset();
        mostrarMensaje(texto, "ok");
        cargarCuotas();
    } catch (error) {
        mostrarMensaje("No se pudo generar la cuota: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Utilidades - Corregido el parsing ISO sin desvíos UTC
// ============================================================
function formatearFecha(valor) {
    if (!valor) return "-";
    
    // Si viene como string ISO "YYYY-MM-DD" desde Spring Boot
    if (typeof valor === "string" && valor.includes("-")) {
        const partes = valor.split("T")[0].split("-");
        if (partes.length === 3) {
            return `${partes[2]}/${partes[1]}/${partes[0]}`; // DD/MM/YYYY directo
        }
    }
    
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

function mostrarMensaje(texto, tipo = "error") {
    mensaje.textContent = texto;
    mensaje.classList.toggle("mensaje-ok", tipo === "ok");
    mensaje.hidden = false;
}

function ocultarMensaje() {
    mensaje.hidden = true;
}

// ============================================================
// Arranque
// ============================================================
cargarCuotas();