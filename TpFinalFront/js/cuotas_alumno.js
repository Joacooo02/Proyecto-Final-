// ============================================================
// Cuotas (alumno)
// Muestra la deuda total, las cuotas y los pagos del alumno, y permite
// registrar un pago automáticamente.
// ============================================================

SesionAlumno.requerir();

const idAlumno = SesionAlumno.idAlumno();

const deudaTotal = document.getElementById("deuda-total");
const tbodyCuotas = document.getElementById("tbody-cuotas");
const tbodyPagos = document.getElementById("tbody-pagos");
const btnCancelarPago = document.getElementById("btn-cancelar-pago");
const dialogoPago = document.getElementById("dialogo-pago");
const formPago = document.getElementById("form-pago");

// ============================================================
// Deuda total
// ============================================================
async function cargarDeuda() {
    try {
        const respuesta = await fetch(API_BASE + "/cuotas/deuda/" + idAlumno);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const deuda = await respuesta.json();
        deudaTotal.textContent = "$" + (deuda ?? 0);
    } catch (error) {
        mostrarMensaje("No se pudo cargar la deuda: " + error.message);
    }
}

// ============================================================
// Cuotas
// ============================================================
async function cargarCuotas() {
    try {
        const respuesta = await fetch(API_BASE + "/cuotas/alumno/" + idAlumno);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const cuotas = await respuesta.json();
        mostrarCuotas(cuotas);
    } catch (error) {
        mostrarMensaje("No se pudieron cargar las cuotas: " + error.message);
    }
}

function mostrarCuotas(lista) {
    if (!lista || lista.length === 0) {
        filaVacia(tbodyCuotas, 8, "No tenes cuotas registradas.");
        return;
    }
    tbodyCuotas.innerHTML = lista
        .map(
            (cuota) => `
            <tr>
                <td>${cuota.conceptoCuota ?? ""}</td>
                <td>${cuota.mes ?? "-"}</td>
                <td>${cuota.anio ?? "-"}</td>
                <td>$${cuota.valorCuota ?? "-"}</td>
                <td>${formatearFecha(cuota.fechaVencimiento)}</td>
                <td>${formatearFecha(cuota.fechaPago) || "-"}</td>
                <td>${etiquetaEstado(cuota.estadoCuota)}</td>
                <td>
                    ${cuota.estadoCuota === 'PENDIENTE' ? 
                        `<button class="btn btn-primario btn-sm" 
                                 onclick="prepararPago(${cuota.id}, ${cuota.valorCuota})">
                            Pagar
                         </button>` 
                        : "-"
                    }
                </td>
            </tr>`
        )
        .join("");
}

function etiquetaEstado(estado) {
    if (!estado) return "";
    const clase = "estado estado-" + estado.toLowerCase();
    return `<span class="${clase}">${estado}</span>`;
}

// ============================================================
// Pagos realizados
// ============================================================
async function cargarPagos() {
    try {
        const respuesta = await fetch(API_BASE + "/pagos-cuota/alumno/" + idAlumno);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const pagos = await respuesta.json();
        mostrarPagos(pagos);
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los pagos: " + error.message);
    }
}

function mostrarPagos(lista) {
    if (!lista || lista.length === 0) {
        filaVacia(tbodyPagos, 4, "No hay pagos registrados.");
        return;
    }
    tbodyPagos.innerHTML = lista
        .map(
            (pago) => `
            <tr>
                <td>${pago.idCuota ?? "-"}</td>
                <td>$${pago.monto ?? "-"}</td> <td>${formatearFecha(pago.fechaPago)}</td>
                <td>${pago.metodoPago ?? ""}</td>
            </tr>`
        )
        .join("");
}

// ============================================================
// Registrar pago
// ============================================================

// CAMBIO: Inyecta los valores correctos de la cuota seleccionada y abre el modal
function prepararPago(idCuota, valorCuota) {
    document.getElementById("input-id-cuota").value = idCuota;
    document.getElementById("input-monto").value = valorCuota;
    dialogoPago.showModal();
}

btnCancelarPago.addEventListener("click", () => dialogoPago.close());

formPago.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const formData = new FormData(formPago);
    
    // CORRECCIÓN AQUÍ: Cambiamos el nombre de la propiedad de 'monto' a 'montoPagado'
    const datos = {
        idCuota: parseInt(formData.get("idCuota")),
        montoPagado: parseFloat(formData.get("monto")), // <-- Usamos la clave que tu Mapper espera
        metodoPago: formData.get("metodoPago")
    };

    try {
        const respuesta = await fetch(API_BASE + "/pagos-cuota/pagar", {
            method: "POST",
            headers: { 
                "Content-Type": "application/json"
            },
            body: JSON.stringify(datos),
        });
        
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoPago.close();
        formPago.reset();
        mostrarMensaje("Pago registrado.", "ok");

        // Refrescamos todo
        cargarDeuda();
        cargarCuotas();
        cargarPagos();
    } catch (error) {
        mostrarMensaje("No se pudo registrar el pago: " + error.message);
        dialogoPago.close();
    }
});

function filaVacia(tbody, columnas, texto) {
    tbody.innerHTML = `<tr><td colspan="${columnas}" class="celda-vacia">${texto}</td></tr>`;
}

// ============================================================
// Arranque
// ============================================================
cargarDeuda();
cargarCuotas();
cargarPagos();