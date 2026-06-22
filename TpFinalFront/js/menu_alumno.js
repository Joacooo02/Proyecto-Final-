// ============================================================
// Menu del alumno
// Verifica que haya sesion y muestra un saludo con el legajo.
// ============================================================

SesionAlumno.requerir();

document.getElementById("saludo").textContent =
    "Legajo " + SesionAlumno.legajo() + ". Elegi una opcion.";
