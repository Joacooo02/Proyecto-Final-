CREATE DATABASE TrabajoFinal;
USE TrabajoFinal;

-- USUARIO Y PERFILES

CREATE TABLE Persona (
	idPersona INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR (50),
	apellido VARCHAR (50),
	dni VARCHAR (50),
	telefono VARCHAR (50),
	fechaNacimiento DATE,
    email VARCHAR (50)
);

CREATE TABLE Alumno (
	idAlumno INT AUTO_INCREMENT PRIMARY KEY,
    idPersona INT,
    legajo INT,
    anioIngreso YEAR,
    analiticoParcial BOOLEAN,
    esRegular BOOLEAN,
    planEstudio INT,
    promedio DOUBLE,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Administrador(
	idAdministrador INT AUTO_INCREMENT PRIMARY KEY,
    idPersona INT,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Profesor(
	idProfesor INT AUTO_INCREMENT PRIMARY KEY,
    idPersona INT,
    horasSemanales INT,
    estadoProfesor ENUM('ACTIVO', 'INACTIVO', 'LICENCIA') NOT NULL DEFAULT 'ACTIVO',
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

-- AREA ADMINISTRATIVA

CREATE TABLE Cuota(
	idCuota INT AUTO_INCREMENT PRIMARY KEY,
    idAlumno INT,
	valorCuota INT,
    fechaPago DATE,
    fechaVencimiento DATE,
    conceptoCuota ENUM('Cuota', 'Matricula') NOT NULL DEFAULT 'Cuota',
    estadoCuota ENUM('Pagada', 'Pendiente', 'Vencida') NOT NULL DEFAULT 'Pendiente',
	FOREIGN KEY (idAlumno) REFERENCES Alumno(idAlumno) ON DELETE CASCADE
);

CREATE TABLE Alumno_Cursa_Carrera (
	idAlumno INT,
    idCarrera INT,
    fecha_inscripcion DATE,
    PRIMARY KEY (idAlumno, idCarrera),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idAlumno),
    FOREIGN KEY (idCarrera) REFERENCES Carrera(idCarrera)
);

-- AREA ACADEMICA 

CREATE TABLE Carrera(
	idCarrera INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR (50),
    duracion INT,
    tituloOtorgado VARCHAR (50),
	modalidadCarrera ENUM ('Presencial', 'Virtual') NOT NULL DEFAULT 'Presencial',
    planDeEstudio year    
);

CREATE TABLE Materia(
	idMateria INT AUTO_INCREMENT PRIMARY KEY,
	idCarrera INT,
    nombre VARCHAR(50),
    cargaHoraria INT,
    cuatrimestre INT,
    anioCursado YEAR,
	FOREIGN KEY (idCarrera) REFERENCES Carrera(idCarrera) ON DELETE CASCADE
);

CREATE TABLE Examen(
	idExamen INT AUTO_INCREMENT PRIMARY KEY,
    idMateria INT,
    fecha DATE,
    tiporExamen ENUM ('Parcial', 'Final') NOT NULL DEFAULT 'Parcial',
	FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE Nota (
	idNota INT AUTO_INCREMENT PRIMARY KEY,
    idExamen INT,
    idAlumno int,
    nota int,
    fechaRegistro DATE,
	FOREIGN KEY (idExamen) REFERENCES Examen(idExamen) ON DELETE CASCADE,
	FOREIGN KEY (idAlumno) REFERENCES Alumno(idAlumno) ON DELETE CASCADE
);

CREATE TABLE Comision(
	idComision INT AUTO_INCREMENT PRIMARY KEY, 
    idMateria INT,
    idProfesor INT,
    nroComision INT,
    cantAlumnos INT,
    aula VARCHAR (50),
	FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE,
	FOREIGN KEY (idProfesor) REFERENCES Profesor(idProfesor) ON DELETE CASCADE
);

