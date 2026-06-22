// ============================================================
// Gestion de profesores (admin)
// Se conecta con la API de Spring para listar, buscar, crear,
// modificar y eliminar profesores.
// ============================================================

// URL del endpoint de profesores en el backend (ProfesorController -> @RequestMapping("/profesores")).
const API_URL = "http://localhost:8080/profesores";

// --- Referencias a los elementos del HTML que vamos a usar ---
const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");
const inputFiltro = document.getElementById("filtro");
const selectCampo = document.getElementById("campo-filtro");
const selectEstado = document.getElementById("filtro-estado");
const btnBuscar = document.getElementById("btn-buscar");
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-profesor");
const formulario = document.getElementById("form-profesor");
const dialogoPerfil = document.getElementById("dialogo-perfil");
const formPerfil = document.getElementById("form-perfil");
const btnCerrarPerfil = document.getElementById("btn-cerrar-perfil");

// ============================================================
// Cargar y buscar profesores
// ============================================================

// Pide los profesores al backend y los muestra en la tabla.
// Arma la query con el campo de texto (apellido/nombre/dni/email)
// y, si se eligio uno, el estado. Por ejemplo:
//   GET /profesores?apellido=perez&estado=ACTIVO
// Spring lo recibe en los @RequestParam de listarProfesores y filtra en la base.
async function cargarProfesores() {
    const texto = inputFiltro.value.trim();
    const estado = selectEstado.value;

    // URLSearchParams arma la query y escapa los caracteres especiales por nosotros.
    const params = new URLSearchParams();
    if (texto !== "") {
        params.set(selectCampo.value, texto);
    }
    if (estado !== "") {
        params.set("estado", estado);
    }

    let url = API_URL;
    if ([...params].length > 0) {
        url += "?" + params.toString();
    }

    try {
        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const profesores = await respuesta.json(); // convierte la respuesta JSON en un array de objetos
        mostrarProfesores(profesores);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los profesores: " + error.message);
    }
}

// Dibuja las filas de la tabla a partir de una lista de profesores.
function mostrarProfesores(lista) {
    tbody.innerHTML = ""; // vacia la tabla antes de volver a llenarla

    for (const profesor of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${profesor.idPersona}</td>
            <td>${profesor.nombre}</td>
            <td>${profesor.apellido}</td>
            <td>${profesor.dni}</td>
            <td>${profesor.telefono ?? ""}</td>
            <td>${profesor.email ?? ""}</td>
            <td>${profesor.estadoProfesor ?? ""}</td>
            <td>
                <button type="button" class="btn btn-peligro btn-eliminar">Eliminar</button>
            </td>
        `;

        // Al hacer click en la fila se abre el perfil de ese profesor.
        fila.addEventListener("click", () => verPerfil(profesor.idPersona));

        // El boton eliminar va dentro de la fila: usamos stopPropagation para
        // que al apretarlo NO se dispare tambien el click de la fila (abrir perfil).
        const btnEliminar = fila.querySelector(".btn-eliminar");
        btnEliminar.addEventListener("click", (evento) => {
            evento.stopPropagation();
            eliminarProfesor(profesor.idPersona, profesor.nombre, profesor.apellido);
        });

        tbody.appendChild(fila);
    }
}

// ============================================================
// Perfil del profesor
// ============================================================

// Pide al backend los datos completos de un profesor
// (GET /profesores/{id} -> buscarProfesorPorId del controller)
// y los muestra en una ventana emergente.
async function verPerfil(id) {
    try {
        const respuesta = await fetch(API_URL + "/" + id);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const profesor = await respuesta.json();
        mostrarPerfil(profesor);
        dialogoPerfil.showModal();
    } catch (error) {
        mostrarMensaje("No se pudo cargar el perfil: " + error.message);
    }
}

// Llena los inputs del formulario de perfil con los datos del profesor.
function mostrarPerfil(profesor) {
    const campos = formPerfil.elements;

    // "?? ''" significa: si el valor es null, usar texto vacio.
    campos.idPersona.value = profesor.idPersona;
    campos.nombre.value = profesor.nombre ?? "";
    campos.apellido.value = profesor.apellido ?? "";
    campos.dni.value = profesor.dni ?? "";
    campos.telefono.value = profesor.telefono ?? "";
    campos.email.value = profesor.email ?? "";
    campos.fechaNacimiento.value = profesor.fechaNacimiento ?? "";
    campos.horasSemanales.value = profesor.horasSemanales ?? "";
    campos.estadoProfesor.value = profesor.estadoProfesor ?? "ACTIVO";
}

// Al guardar, mandamos el profesor modificado al backend
// (PUT /profesores/{id} -> modificarProfesor del controller).
formPerfil.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const datos = Object.fromEntries(new FormData(formPerfil));

    // El backend recibe la entidad Profesor: mantenemos el rol como PROFESOR.
    datos.rolUsuario = "PROFESOR";

    // Los inputs vacios llegan como texto "" y Spring espera numeros/fechas:
    // los convertimos a null para que Jackson no falle.
    normalizarVacios(datos);

    try {
        const respuesta = await fetch(API_URL + "/" + datos.idPersona, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoPerfil.close();
        cargarProfesores(); // recarga la tabla con los datos actualizados
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
        dialogoPerfil.close();
    }
});

btnCerrarPerfil.addEventListener("click", () => dialogoPerfil.close());

// ============================================================
// Eliminar profesor
// ============================================================

// Borra un profesor (DELETE /profesores/{id} -> eliminarProfesor del controller).
// Pide confirmacion antes para evitar borrados accidentales.
async function eliminarProfesor(id, nombre, apellido) {
    const confirmado = confirm(`¿Eliminar al profesor ${nombre} ${apellido}?`);
    if (!confirmado) {
        return;
    }

    try {
        const respuesta = await fetch(API_URL + "/" + id, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        cargarProfesores(); // recarga la tabla sin el profesor borrado
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar el profesor: " + error.message);
    }
}

// ============================================================
// Buscar
// ============================================================

// Busca al hacer click en el boton, al apretar Enter en el input
// o al cambiar el filtro de estado. Si todo esta vacio, trae la lista completa.
btnBuscar.addEventListener("click", cargarProfesores);
selectEstado.addEventListener("change", cargarProfesores);
inputFiltro.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") {
        cargarProfesores();
    }
});

// ============================================================
// Agregar profesor
// ============================================================

// Abre y cierra la ventana del formulario.
btnAgregar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

// Cuando se envia el formulario, mandamos el profesor nuevo al backend
// (POST /profesores -> agregarProfesor del controller).
formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault(); // evita que la pagina se recargue

    const datos = Object.fromEntries(new FormData(formulario));

    // El backend recibe la entidad Profesor, que hereda de Persona:
    // el rol siempre es PROFESOR en esta pantalla.
    datos.rolUsuario = "PROFESOR";
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
        formulario.reset(); // limpia los inputs para la proxima vez
        cargarProfesores(); // recarga la tabla para que aparezca el nuevo profesor
    } catch (error) {
        mostrarMensaje("No se pudo guardar el profesor: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Utilidades
// ============================================================

// Convierte a null los campos opcionales que llegaron vacios,
// asi Spring/Jackson no falla al parsear numeros y fechas.
function normalizarVacios(datos) {
    for (const campo of ["telefono", "email", "fechaNacimiento", "horasSemanales"]) {
        if (datos[campo] === "") {
            datos[campo] = null;
        }
    }
}

// Intenta leer el mensaje que devuelve el GlobalExceptionHandler del backend.
// Si la respuesta no trae un JSON con mensaje, muestra el codigo de error HTTP.
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
// Arranque: al cargar la pagina, traemos los profesores.
// ============================================================
cargarProfesores();
