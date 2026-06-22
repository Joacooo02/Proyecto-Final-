// ============================================================
// Gestion de alumnos (admin)
// Se conecta con la API de Spring para listar, buscar y crear alumnos.
// ============================================================

// URL del endpoint de alumnos en el backend (AlumnoController -> @RequestMapping("/alumnos")).
const API_URL = "http://localhost:8080/alumnos";

// --- Referencias a los elementos del HTML que vamos a usar ---
const tbody = document.querySelector(".tabla-datos tbody");
const mensaje = document.getElementById("mensaje");
const inputFiltro = document.getElementById("filtro");
const selectCampo = document.getElementById("campo-filtro");
const btnBuscar = document.getElementById("btn-buscar");
const btnAgregar = document.getElementById("btn-agregar");
const btnCancelar = document.getElementById("btn-cancelar");
const dialogo = document.getElementById("dialogo-alumno");
const formulario = document.getElementById("form-alumno");
const dialogoPerfil = document.getElementById("dialogo-perfil");
const formPerfil = document.getElementById("form-perfil");
const btnCerrarPerfil = document.getElementById("btn-cerrar-perfil");

// ============================================================
// Cargar y buscar alumnos
// ============================================================

// Pide los alumnos al backend y los muestra en la tabla.
// Si hay texto en el buscador, lo manda como parámetro de consulta:
// por ejemplo GET /alumnos?apellido=perez
// Spring lo recibe en los @RequestParam de listarAlumnos y filtra en la base.
async function cargarAlumnos() {
    const texto = inputFiltro.value.trim();

    let url = API_URL;
    if (texto !== "") {
        // selectCampo.value es "apellido", "nombre", "dni", "email" o "legajo".
        // encodeURIComponent escapa caracteres especiales (espacios, acentos, etc.)
        url += "?" + selectCampo.value + "=" + encodeURIComponent(texto);
    }

    try {
        const respuesta = await fetch(url);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const alumnos = await respuesta.json(); // convierte la respuesta JSON en un array de objetos
        mostrarAlumnos(alumnos);
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudieron cargar los alumnos: " + error.message);
    }
}

// Dibuja las filas de la tabla a partir de una lista de alumnos.
function mostrarAlumnos(lista) {
    tbody.innerHTML = ""; // vacía la tabla antes de volver a llenarla
    

    for (const alumno of lista) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${alumno.idPersona ?? ''}</td>
            <td>${alumno.legajo}</td>
            <td>${alumno.nombre}</td>
            <td>${alumno.apellido}</td>
            <td>${alumno.dni}</td>
            <td>${alumno.telefono}</td>
            <td>${alumno.email}</td>
            <td><button type="button" class="btn btn-peligro btn-eliminar">Eliminar</button></td>
        `;
        // Al hacer click en la fila, se abre el perfil de ese alumno.
        fila.addEventListener("click", () => verPerfil(alumno.legajo));
        // El boton eliminar no debe abrir el perfil: frenamos la propagacion.
        const btnEliminar = fila.querySelector(".btn-eliminar");
        btnEliminar.addEventListener("click", (evento) => {
            evento.stopPropagation();
            eliminarAlumno(alumno.legajo, alumno.nombre, alumno.apellido);
        });
        tbody.appendChild(fila);
    }
}

// Borra un alumno (DELETE /alumnos/{legajo} -> eliminarAlumno del controller).
// Pide confirmacion antes para evitar borrados accidentales.
async function eliminarAlumno(legajo, nombre, apellido) {
    const confirmado = confirm(`¿Eliminar al alumno ${nombre} ${apellido} (legajo ${legajo})?`);
    if (!confirmado) {
        return;
    }
    try {
        const respuesta = await fetch(API_URL + "/" + legajo, { method: "DELETE" });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        cargarAlumnos(); // recarga la tabla sin el alumno borrado
        ocultarMensaje();
    } catch (error) {
        mostrarMensaje("No se pudo eliminar el alumno: " + error.message);
    }
}

// ============================================================
// Perfil del alumno
// ============================================================

// Pide al backend los datos completos de un alumno
// (GET /alumnos/{legajo} -> buscarAlumnoPorLegajo del controller)
// y los muestra en una ventana emergente.
async function verPerfil(legajo) {
    try {
        const respuesta = await fetch(API_URL + "/" + legajo);
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }
        const alumno = await respuesta.json();
        mostrarPerfil(alumno);
        dialogoPerfil.showModal();
    } catch (error) {
        mostrarMensaje("No se pudo cargar el perfil: " + error.message);
    }
}

// Llena los inputs del formulario de perfil con los datos del alumno.
// "formPerfil.elements" permite acceder a cada input por su atributo "name".
function mostrarPerfil(alumno) {
    const campos = formPerfil.elements;

    // "?? ''" significa: si el valor es null, usar texto vacío.
    campos.legajo.value = alumno.legajo;
    campos.nombre.value = alumno.nombre ?? "";
    campos.apellido.value = alumno.apellido ?? "";
    campos.dni.value = alumno.dni ?? "";
    campos.telefono.value = alumno.telefono ?? "";
    campos.email.value = alumno.email ?? "";
    campos.fechaNacimiento.value = alumno.fechaNacimiento ?? "";
    campos.anioIngreso.value = alumno.anioIngreso ?? "";
    campos.promedio.value = alumno.promedio ?? "";
    campos.esRegular.checked = alumno.esRegular;
    campos.analiticoParcial.checked = alumno.analiticoParcial;
}

// Al guardar, mandamos el alumno modificado al backend
// (PUT /alumnos/{legajo} -> modificarAlumno del controller).
formPerfil.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const datos = Object.fromEntries(new FormData(formPerfil));

    // Los checkboxes no se incluyen en FormData cuando están destildados,
    // así que los agregamos a mano como true/false.
    datos.esRegular = formPerfil.elements.esRegular.checked;
    datos.analiticoParcial = formPerfil.elements.analiticoParcial.checked;

    // Los inputs vacíos llegan como texto "" y Spring espera números/fechas:
    // los convertimos a null para que Jackson no falle.
    for (const campo of ["telefono", "fechaNacimiento", "anioIngreso", "promedio"]) {
        if (datos[campo] === "") {
            datos[campo] = null;
        }
    }

    try {
        const respuesta = await fetch(API_URL + "/" + datos.legajo, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos),
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogoPerfil.close();
        cargarAlumnos(); // recarga la tabla con los datos actualizados
    } catch (error) {
        mostrarMensaje("No se pudieron guardar los cambios: " + error.message);
        dialogoPerfil.close();
    }
});

btnCerrarPerfil.addEventListener("click", () => dialogoPerfil.close());

// Busca al hacer click en el botón o al apretar Enter en el input.
// Si el input está vacío, trae la lista completa.
btnBuscar.addEventListener("click", cargarAlumnos);
inputFiltro.addEventListener("keydown", (evento) => {
    if (evento.key === "Enter") {
        cargarAlumnos();
    }
});

// ============================================================
// Agregar alumno
// ============================================================

// Abre y cierra la ventana del formulario.
btnAgregar.addEventListener("click", () => dialogo.showModal());
btnCancelar.addEventListener("click", () => dialogo.close());

// Cuando se envía el formulario, mandamos el alumno nuevo al backend.
formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault(); // evita que la página se recargue (comportamiento por defecto)

    // Arma un objeto { nombre: "...", apellido: "...", ... }
    // con los valores de los inputs (usa el atributo "name" de cada uno).
    const datos = Object.fromEntries(new FormData(formulario));

    datos.esRegular = formulario.elements.esRegular ? formulario.elements.esRegular.checked : false;
    datos.analiticoParcial = formulario.elements.analiticoParcial ? formulario.elements.analiticoParcial.checked : false;
    
    if (!datos.esRegular) datos.esRegular = false;
    if (!datos.analiticoParcial) datos.analiticoParcial = false;
    // El backend espera un AltaAlumnoDTO: { alumno: {...AlumnoDTO}, idCarrera }.
    // idCarrera va aparte y es obligatorio (el alumno se inscribe a esa carrera).
    const idCarrera = datos.idCarrera;
    delete datos.idCarrera;
    if (datos.telefono === "") datos.telefono = null;
    const body = {
        alumno: datos,
        idCarrera: idCarrera === "" ? null : Number(idCarrera),
    };

    console.log("JSON enviado al Backend:", JSON.stringify(body, null, 2));

    try {
        const respuesta = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body), // AltaAlumnoDTO
        });
        if (!respuesta.ok) {
            throw new Error(await extraerMensajeError(respuesta));
        }

        dialogo.close();
        formulario.reset(); // limpia los inputs para la próxima vez
        cargarAlumnos();    // recarga la tabla para que aparezca el nuevo alumno
    } catch (error) {
        mostrarMensaje("No se pudo guardar el alumno: " + error.message);
        dialogo.close();
    }
});

// ============================================================
// Mensajes de error
// ============================================================

// Intenta leer el mensaje que devuelve el GlobalExceptionHandler del backend.
// Si la respuesta no trae un JSON con mensaje, muestra el código de error HTTP.
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
// Arranque: al cargar la página, traemos los alumnos.
// ============================================================
cargarAlumnos();
