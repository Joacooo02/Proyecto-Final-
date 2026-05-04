CREATE DATABASE TrabajoFinal;
USE TrabajoFinal;

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
    anioIngreso INT,
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

CREATE TABLE Cuota(
	idCuota INT AUTO_INCREMENT PRIMARY KEY,
    idPersona INT,
	valorCuota INT,
    fechaPago DATE,
    fechaVencimiento DATE,
    conceptoCuota ENUM('Cuota', 'Matricula') NOT NULL DEFAULT 'Cuota',
    estadoCuota ENUM('Pagada', 'Pendiente', 'Vencida') NOT NULL DEFAULT 'Pendiente',
	FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);



