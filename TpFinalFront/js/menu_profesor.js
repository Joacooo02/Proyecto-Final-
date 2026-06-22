// ============================================================
// Menu del profesor
// Verifica que haya sesion y muestra un saludo con el id de profesor.
// ============================================================

SesionProfesor.requerir();

document.getElementById("saludo").textContent =
    "Profesor #" + SesionProfesor.idProfesor() + ". Elegi una opcion.";
