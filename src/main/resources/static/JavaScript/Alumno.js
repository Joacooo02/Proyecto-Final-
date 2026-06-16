async function mostrarMaterias() {

    const token =
        localStorage.getItem("token");

    const response = await fetch(
        "http://localhost:8080/alumnos/1/materias",
        {
            headers:{
                "Authorization":
                    "Bearer " + token
            }
        }
    );

    const materias =
        await response.json();

    let html = "<h2>Materias</h2>";

    materias.forEach(m => {

        html += `
            <p>${m.nombre}</p>
        `;
    });

    document.getElementById("materias")
        .innerHTML = html;
}