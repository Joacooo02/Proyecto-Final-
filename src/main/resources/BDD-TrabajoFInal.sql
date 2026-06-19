DROP DATABASE IF EXISTS TrabajoFinal;
CREATE DATABASE IF NOT EXISTS TrabajoFinal;
USE TrabajoFinal;


-- nivel 0: Tablas base (No dependen de ninguna otra)

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    `role` VARCHAR(50)
);

CREATE TABLE carrera (
    idCarrera BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    duracion INT,
    tituloOtorgado VARCHAR(50),
    modalidadCarrera ENUM ('PRESENCIAL', 'VIRTUAL')
);

CREATE TABLE PreguntaEncuesta (
    idPreguntaEncuesta BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden INT NOT NULL,
    enunciado VARCHAR(300) NOT NULL
);


-- nivel 1: Tablas que dependen del Nivel 0


CREATE TABLE Persona (
    idPersona BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(50) NOT NULL UNIQUE,
    telefono VARCHAR(50),
    fechaNacimiento DATE,
    email VARCHAR(100),
    rolUsuario ENUM('ALUMNO', 'PROFESOR', 'ADMIN'),
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE PlanEstudio (
    idPlanEstudio BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idCarrera BIGINT UNSIGNED NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    anioInicio INT NOT NULL,
    FOREIGN KEY (idCarrera) REFERENCES carrera(idCarrera) ON DELETE CASCADE
);

CREATE TABLE tokens (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    tokenType VARCHAR(50),
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE PeriodoInscripcion (
    idPeriodo BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idCarrera BIGINT UNSIGNED NOT NULL,
    tipo ENUM('CURSADA','FINAL') NOT NULL,
    anioLectivo INT NOT NULL,
    cuatrimestre INT NOT NULL,
    fechaInicio DATETIME NOT NULL,
    fechaCierre DATETIME NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (idCarrera) REFERENCES carrera(idCarrera) ON DELETE CASCADE
);


-- nivel 2: Tablas que dependen del Nivel 1

CREATE TABLE Alumno (
    idPersona BIGINT UNSIGNED PRIMARY KEY,
    legajo BIGINT NOT NULL UNIQUE,
    anioIngreso INT,
    analiticoParcial BOOLEAN,
    esRegular BOOLEAN,
    promedio DOUBLE,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Administrador (
    idPersona BIGINT UNSIGNED PRIMARY KEY,
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Profesor (
    idPersona BIGINT UNSIGNED PRIMARY KEY,
    horasSemanales INT,
    estadoProfesor ENUM('ACTIVO', 'INACTIVO', 'LICENCIA') NOT NULL DEFAULT 'ACTIVO',
    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);

CREATE TABLE Materia (
    idMateria BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idCarrera BIGINT UNSIGNED NOT NULL,
    idPlanEstudio BIGINT UNSIGNED,
    nombre VARCHAR(50),
    cargaHoraria INT,
    cuatrimestre INT,
    anioCursado INT,
    FOREIGN KEY (idCarrera) REFERENCES carrera(idCarrera) ON DELETE CASCADE,
    FOREIGN KEY (idPlanEstudio) REFERENCES PlanEstudio(idPlanEstudio)
);

CREATE TABLE Aviso (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_persona BIGINT UNSIGNED NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    contenido VARCHAR(1000) NOT NULL,
    fecha_aviso DATETIME NOT NULL,
    FOREIGN KEY (id_persona) REFERENCES Persona(idPersona) ON DELETE CASCADE
);


-- nivel 3: Tablas que dependen del Nivel 2


CREATE TABLE Examen (
    idExamen BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idMateria BIGINT UNSIGNED NOT NULL,
    fecha DATE,
    tipoExamen ENUM ('PARCIAL', 'FINAL'),
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE comision (
    idComision BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idMateria BIGINT UNSIGNED NOT NULL,
    idProfesor BIGINT UNSIGNED,
    nroComision INT,
    cantAlumnos INT DEFAULT 0,
    aula VARCHAR(50),
    cupo_maximo INT DEFAULT 50,
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria),
    FOREIGN KEY (idProfesor) REFERENCES Profesor(idPersona)
);

CREATE TABLE Cuota (
    idCuota BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    mes INT NOT NULL,
    anio INT NOT NULL,
    valorCuota INT,
    fechaVencimiento DATE,
    conceptoCuota ENUM('CUOTA', 'MATRICULA'),
    estadoCuota ENUM('PAGADA', 'PENDIENTE', 'VENCIDA') NOT NULL DEFAULT 'PENDIENTE',
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE
);

CREATE TABLE Alumno_Cursa_Carrera (
    idAlumno BIGINT UNSIGNED NOT NULL,
    idCarrera BIGINT UNSIGNED NOT NULL,
    fecha_inscripcion DATE,
    PRIMARY KEY (idAlumno, idCarrera),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idCarrera) REFERENCES carrera(idCarrera) ON DELETE CASCADE
);

CREATE TABLE Correlatividad (
    idCorrelatividad BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idMateria BIGINT UNSIGNED NOT NULL,
    idMateriaCorrelativa BIGINT UNSIGNED NOT NULL,
    estadoParaCursar ENUM('CURSADA','APROBADA'),
    estadoParaRendir ENUM('CURSADA','APROBADA'),
    FOREIGN KEY (idMateria) REFERENCES Materia(idMateria) ON DELETE CASCADE,
    FOREIGN KEY (idMateriaCorrelativa) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE Alumno_Materia (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_alumno BIGINT UNSIGNED NOT NULL,
    id_materia BIGINT UNSIGNED NOT NULL,
    estado ENUM('PENDIENTE','CURSANDO','CURSADA','APROBADA','REGULAR') NOT NULL DEFAULT 'PENDIENTE',
    notaParcial1 DOUBLE DEFAULT NULL,
    notaParcial2 DOUBLE DEFAULT NULL,
    notaFinal DOUBLE DEFAULT NULL,
    fechaInscripcion DATE DEFAULT NULL,
    fechaRegularizacion DATE DEFAULT NULL,
    fechaAprobacion DATE DEFAULT NULL,
    UNIQUE KEY uq_alumno_materia (id_alumno, id_materia),
    FOREIGN KEY (id_alumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (id_materia) REFERENCES Materia(idMateria) ON DELETE CASCADE
);

CREATE TABLE boleto_especial_educativo (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    fueSolicitado BOOLEAN DEFAULT FALSE,
    estaActivo BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE
);


-- nivel 4: Tablas que dependen del Nivel 3


CREATE TABLE Nota (
    idNota BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idExamen BIGINT UNSIGNED NOT NULL,
    idAlumno BIGINT UNSIGNED NOT NULL,
    nota INT,
    fechaRegistro DATE,
    FOREIGN KEY (idExamen) REFERENCES Examen(idExamen) ON DELETE CASCADE,
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE
);

CREATE TABLE RespuestaEncuesta (
    idRespuestaEncuesta BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idComision BIGINT UNSIGNED NOT NULL,
    hashAlumno VARCHAR(64) NOT NULL,
    fechaRespuesta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comentarioFinal VARCHAR(1000) NULL,
    CONSTRAINT fk_re_comision FOREIGN KEY (idComision) REFERENCES comision(idComision),
    CONSTRAINT uq_hash_comision UNIQUE (idComision, hashAlumno)
);

CREATE TABLE Alumno_Inscripcion_Comision (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    idComision BIGINT UNSIGNED NOT NULL,
    fechaInscripcion DATE NOT NULL,
    UNIQUE KEY uq_alumno_comision (idAlumno, idComision),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idComision) REFERENCES comision(idComision) ON DELETE CASCADE
);

CREATE TABLE Alumno_Inscripcion_Examen_Final (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idAlumno BIGINT UNSIGNED NOT NULL,
    idExamen BIGINT UNSIGNED NOT NULL,
    fechaInscripcion DATE NOT NULL,
    UNIQUE KEY uq_alumno_examen (idAlumno, idExamen),
    FOREIGN KEY (idAlumno) REFERENCES Alumno(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idExamen) REFERENCES Examen(idExamen) ON DELETE CASCADE
);

CREATE TABLE ComisionHorario (
    idComisionHorario BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idComision BIGINT UNSIGNED NOT NULL,
    diaSemana ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO') NOT NULL,
    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    FOREIGN KEY (idComision) REFERENCES comision(idComision) ON DELETE CASCADE
);

CREATE TABLE PagoCuota (
    idPagoCuota BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idCuota BIGINT UNSIGNED NOT NULL,
    montoPagado INT NOT NULL,
    fechaPago DATE NOT NULL,
    metodoPago ENUM('EFECTIVO','TRANSFERENCIA','TARJETA'),
    FOREIGN KEY (idCuota) REFERENCES Cuota(idCuota) ON DELETE CASCADE
);

CREATE TABLE periodo_inscripcion_comision (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idPeriodo BIGINT UNSIGNED NOT NULL,
    idComision BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (idPeriodo) REFERENCES PeriodoInscripcion(idPeriodo) ON DELETE CASCADE,
    FOREIGN KEY (idComision) REFERENCES comision(idComision) ON DELETE CASCADE
);



CREATE TABLE RespuestaPregunta (
    idRespuestaPregunta BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    idRespuestaEncuesta BIGINT UNSIGNED NOT NULL,
    idPreguntaEncuesta BIGINT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    CONSTRAINT fk_rp_respuesta FOREIGN KEY (idRespuestaEncuesta) REFERENCES RespuestaEncuesta(idRespuestaEncuesta),
    CONSTRAINT fk_rp_pregunta FOREIGN KEY (idPreguntaEncuesta) REFERENCES PreguntaEncuesta(idPreguntaEncuesta)
);




INSERT INTO PreguntaEncuesta (orden, enunciado) VALUES
(1, '¿El profesor cumplió con los temas del programa?'),
(2, '¿El profesor explicó con claridad?'),
(3, '¿El profesor estuvo disponible para consultas?'),
(4, '¿La cursada cumplió tus expectativas generales?'),
(5, '¿Recomendarías esta materia/comisión?');

INSERT INTO Carrera (nombre,duracion,tituloOtorgado,modalidadCarrera) VALUES
('Analista Programador Universitario',3,'Analista Programador Universitario','PRESENCIAL');

INSERT INTO PlanEstudio (idCarrera,nombre,anioInicio) VALUES
(1,'Plan 2025',2025);


INSERT INTO Persona (nombre,apellido,dni,telefono,fechaNacimiento,email,rolUsuario,user_id) VALUES
('Juan','Perez','30111222','2235551234','1985-05-10','juan.perez@gmail.com','PROFESOR', NULL);

INSERT INTO Profesor (idPersona,horasSemanales,estadoProfesor) VALUES
(1,20,'ACTIVO');

INSERT INTO Materia (idCarrera,nombre,cargaHoraria,cuatrimestre,anioCursado) VALUES
(1,'Programacion I',96,1,1),
(1,'Base de Datos I',96,2,1),
(1,'Programacion II',128,1,2);

INSERT INTO Comision (idMateria,idProfesor,nroComision,cantAlumnos,aula) VALUES
(1,1,101,35,'Aula 1'),
(2,1,102,30,'Aula 2'),
(3,1,103,28,'Aula 3');


INSERT INTO Persona (nombre,apellido,dni,telefono,fechaNacimiento,email,rolUsuario,user_id) VALUES
('Pedro','Gomez','40111222','2234441111','2004-03-15','pedro@gmail.com','ALUMNO', NULL);

INSERT INTO Alumno (idPersona,legajo,anioIngreso,analiticoParcial,esRegular,promedio) VALUES
(2,1001,2025,TRUE,TRUE,8.5);



INSERT INTO users (id, username, password, email, `role`) VALUES
(8, 'juan111', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'juan@correo.com', 'ALUMNO'),
(10, 'marialopez99', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'maria.lopez@correo.com', 'ALUMNO');



INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id) VALUES
(3, 'Maria', 'Lopez', '40222333', '2234442222', '2003-08-20', 'maria.lopez@correo.com', 'ALUMNO', 10);

INSERT INTO Alumno (idPersona, legajo, anioIngreso, analiticoParcial, esRegular, promedio) VALUES
(3, 1002, 2024, TRUE, TRUE, 9.1);

-- Perfil Juan Alumno (ID Persona = 4, user_id = 8)
INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id)
VALUES (4, 'Juan', 'Alumno', '40555666', '2234447777', '2002-05-12', 'juan@correo.com', 'ALUMNO', 8);

INSERT INTO Alumno (idPersona, legajo, anioIngreso, analiticoParcial, esRegular, promedio)
VALUES (4, 1003, 2024, TRUE, TRUE, 8.7);




INSERT INTO Alumno_Cursa_Carrera (idAlumno,idCarrera,fecha_inscripcion) VALUES
(2,1,'2025-03-01'),
(3,1,'2024-03-01'),
(4,1,'2024-03-01');

INSERT INTO Examen (idMateria,fecha,tipoExamen) VALUES
(1,'2026-06-20','PARCIAL'),
(1,'2026-07-10','FINAL');

INSERT INTO Nota (idExamen,idAlumno,nota,fechaRegistro) VALUES
(1,2,8,'2026-06-20'),
(1,3,10,'2026-06-20'),
(1,4,9,'2026-06-20');

INSERT INTO Cuota (idAlumno, valorCuota, fechaVencimiento, mes, anio, conceptoCuota, estadoCuota) VALUES
(2, 30000, '2026-06-10', 6, 2026, 'CUOTA', 'PAGADA'),
(3, 30000, '2026-06-10', 6, 2026, 'CUOTA', 'PENDIENTE'),
(4, 30000, '2026-06-10', 6, 2026, 'CUOTA', 'PENDIENTE');

INSERT INTO PagoCuota (idCuota, montoPagado, fechaPago, metodoPago) VALUES
(1, 30000, '2026-06-01', 'EFECTIVO');

INSERT INTO Alumno_Inscripcion_Comision (idAlumno,idComision,fechaInscripcion) VALUES
(2,1,'2026-06-15'),
(2,2,'2026-06-15'),
(3,1,'2026-06-15'),
(4,1,'2026-06-15');

INSERT INTO Alumno_Inscripcion_Examen_Final (idAlumno, idExamen, fechaInscripcion) VALUES
(2,2,'2026-07-01'),
(3,2,'2026-07-01'),
(4,2,'2026-07-01');

INSERT INTO Correlatividad (idMateria,idMateriaCorrelativa,estadoParaCursar,estadoParaRendir) VALUES
(3,1,'CURSADA','APROBADA');

INSERT INTO PeriodoInscripcion (idCarrera,tipo,anioLectivo,cuatrimestre,fechaInicio,fechaCierre,activa) VALUES
(1,'CURSADA',2026,1,'2026-06-01 00:00:00','2026-07-01 00:00:00',true);

DELETE FROM Alumno_Materia WHERE id_alumno = 4;

INSERT INTO Alumno_Materia (id_alumno, id_materia, estado, notaParcial1, notaParcial2, notaFinal, fechaInscripcion) VALUES
(4, 1, 'APROBADA', 8.0, 7.0, 8.0, '2026-03-01'),
(4, 2, 'CURSANDO', NULL, NULL, NULL, '2026-03-01');


INSERT INTO users (id, username, password, email, `role`) VALUES
(11, 'carlosprof', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'carlos.profesor@correo.com', 'PROFESOR');


INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id) VALUES
(5, 'Carlos', 'Maestro', '30999888', '2234449999', '1980-04-25', 'carlos.profesor@correo.com', 'PROFESOR', 11);


INSERT INTO Profesor (idPersona, horasSemanales, estadoProfesor) VALUES
(5, 40, 'ACTIVO');


INSERT INTO users (id, username, password, email, `role`) VALUES
(12, 'admin_general', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'admin@correo.com', 'ADMIN');


INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id) VALUES
(6, 'Ana', 'Administradora', '30111000', '2234440000', '1988-09-15', 'admin@correo.com', 'ADMIN', 12);


INSERT INTO Administrador (idPersona) VALUES
(6);

UPDATE users SET password = 'PasswordSegura_2026' WHERE id IN (8, 10, 11, 12);

UPDATE users SET password = '$2a$10$X5pZg7Uv8Ew9WhP1E4SgCOsH9CbyFpWlW73u5Y6p5D9m8I3D2jXzq' WHERE id IN (8, 10, 11, 12);

UPDATE users
SET password = '$2a$10$7R8MByD4X9wZ.F56qE0uX.bZg7G71Wv8p3y1GfSmT3f6W6hV6eW2a'
WHERE id IN (8, 10, 11, 12);

UPDATE users SET password = '$2a$10$uVshZ2pZ6pS6Kx8Gk7eYI.f3l1pXN2Z4V7jN9vC7mD5vH2vE4S2v2' WHERE id IN (8, 10, 11, 12);

SELECT password FROM users WHERE email = 'juan@correo.com';


-- ---------------------------------

SELECT id, email, password, role
FROM users
WHERE email = 'admin@correo.com';

SELECT id, email, password, role
FROM users
WHERE email = 'carlos.profesor@correo.com';

UPDATE users
SET password = '$2a$10$IuAVQPzflxN3qfsD55Fi4OCuj0YXI8KX9Y.jEqEKgaOkkLaoBMje6'
WHERE id IN (8,10,11,12);

SELECT email, password
FROM users
WHERE email = 'carlos.profesor@correo.com';

select * from alumno;
select * from persona;
select * from comision;

-- ---------------------------------------------------------------------
--           DATOS DE PRUEBA FINALES (PARA VER BIEN EL FRONT)
-- ---------------------------------------------------------------------
-- NIVEL 0: carrera
-- ---------------------------------------------------------------------
INSERT INTO carrera (nombre, duracion, tituloOtorgado, modalidadCarrera) VALUES
('Tecnicatura en Programacion', 2, 'Tecnico en Programacion', 'VIRTUAL'),
('Licenciatura en Sistemas', 5, 'Licenciado en Sistemas', 'PRESENCIAL'),
('Tecnicatura en Redes y Telecomunicaciones', 2, 'Tecnico en Redes', 'PRESENCIAL');
-- ---------------------------------------------------------------------
-- NIVEL 1: PlanEstudio
-- ---------------------------------------------------------------------
INSERT INTO PlanEstudio (idCarrera, nombre, anioInicio) VALUES
(2, 'Plan 2024', 2024),
(3, 'Plan 2023', 2023),
(4, 'Plan 2025', 2025);
-- ---------------------------------------------------------------------
-- NIVEL 1: users
-- ---------------------------------------------------------------------
INSERT INTO users (id, username, password, email, `role`) VALUES
(13, 'lucia_fer', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'lucia.fernandez@correo.com', 'ALUMNO'),
(14, 'roberto_diaz', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'roberto.diaz@correo.com', 'PROFESOR'),
(15, 'diego_acosta', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'diego.acosta@correo.com', 'ADMIN'),
(16, 'sofia_romero', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'sofia.romero@correo.com', 'ALUMNO'),
(17, 'martin_suarez', '$2a$10$yhfQtq22.PU7VIXuAYihpO84LuvwY823I14cb0/aPbOpn/5FYAhl2', 'martin.suarez@correo.com', 'ALUMNO');
-- ---------------------------------------------------------------------
-- NIVEL 1: Persona
-- ---------------------------------------------------------------------
INSERT INTO Persona (idPersona, nombre, apellido, dni, telefono, fechaNacimiento, email, rolUsuario, user_id) VALUES
(7,  'Lucia',   'Fernandez', '40333444', '2236661111', '2003-11-02', 'lucia.fernandez@correo.com', 'ALUMNO',   13),
(8,  'Roberto', 'Diaz',      '28555666', '2236662222', '1979-02-18', 'roberto.diaz@correo.com',    'PROFESOR', 14),
(9,  'Diego',   'Acosta',    '30777888', '2236663333', '1990-07-22', 'diego.acosta@correo.com',    'ADMIN',    15),
(10, 'Sofia',   'Romero',    '41999000', '2236664444', '2004-01-30', 'sofia.romero@correo.com',    'ALUMNO',   16),
(11, 'Martin',  'Suarez',    '40888777', '2236665555', '2002-09-09', 'martin.suarez@correo.com',   'ALUMNO',   17);
-- ---------------------------------------------------------------------
-- NIVEL 2: Alumno (PK = idPersona)
-- ---------------------------------------------------------------------
INSERT INTO Alumno (idPersona, legajo, anioIngreso, analiticoParcial, esRegular, promedio) VALUES
(7,  1004, 2024, TRUE,  TRUE,  7.8),
(10, 1005, 2025, FALSE, TRUE,  6.9),
(11, 1006, 2023, TRUE,  FALSE, 5.4);
-- ---------------------------------------------------------------------
-- NIVEL 2: Profesor (PK = idPersona)
-- ---------------------------------------------------------------------
INSERT INTO Profesor (idPersona, horasSemanales, estadoProfesor) VALUES
(8, 30, 'LICENCIA');
-- ---------------------------------------------------------------------
-- NIVEL 2: Administrador (PK = idPersona)
-- ---------------------------------------------------------------------
INSERT INTO Administrador (idPersona) VALUES
(9);
-- ---------------------------------------------------------------------
-- NIVEL 2: Materia
-- ---------------------------------------------------------------------
INSERT INTO Materia (idCarrera, idPlanEstudio, nombre, cargaHoraria, cuatrimestre, anioCursado) VALUES
(2, 2, 'Algoritmos y Estructuras de Datos', 96, 1, 1),
(2, 2, 'Redes de Datos',                    64, 2, 1),
(3, 3, 'Matematica Discreta',               80, 1, 1),
(4, 4, 'Fundamentos de Redes',              96, 1, 1);
-- ---------------------------------------------------------------------
-- NIVEL 2: Aviso
-- ---------------------------------------------------------------------
INSERT INTO Aviso (id_persona, titulo, contenido, fecha_aviso) VALUES
(1, 'Suspension de clases',        'Se informa que el dia 25/06 no habra clases por paro de transporte.', '2026-06-18 09:00:00'),
(5, 'Cambio de aula',               'La comision 102 de Base de Datos I pasa a dictarse en el Aula 5 desde la proxima semana.', '2026-06-17 14:30:00'),
(6, 'Inscripcion a finales abierta','Recordamos que la inscripcion a examenes finales del turno de julio se encuentra abierta hasta el 30/06.', '2026-06-15 10:00:00'),
(1, 'Entrega de TP final',          'El trabajo practico final de Programacion I debe entregarse antes del 28/06 a las 23:59.', '2026-06-19 08:00:00');
-- ---------------------------------------------------------------------
-- NIVEL 3: tokens
-- ---------------------------------------------------------------------
INSERT INTO tokens (token, tokenType, revoked, expired, user_id) VALUES
('a1b2c3d4-e5f6-4a7b-8c9d-000000000001', 'BEARER', FALSE, FALSE, 8),
('a1b2c3d4-e5f6-4a7b-8c9d-000000000002', 'BEARER', TRUE,  TRUE,  10),
('a1b2c3d4-e5f6-4a7b-8c9d-000000000003', 'BEARER', FALSE, FALSE, 11),
('a1b2c3d4-e5f6-4a7b-8c9d-000000000004', 'BEARER', FALSE, TRUE,  13);
-- ---------------------------------------------------------------------
-- NIVEL 3: PeriodoInscripcion
-- ---------------------------------------------------------------------
INSERT INTO PeriodoInscripcion (idCarrera, tipo, anioLectivo, cuatrimestre, fechaInicio, fechaCierre, activa) VALUES
(1, 'FINAL',   2026, 1, '2026-06-25 00:00:00', '2026-07-05 00:00:00', TRUE),
(2, 'CURSADA', 2026, 1, '2026-03-01 00:00:00', '2026-03-15 00:00:00', FALSE),
(3, 'CURSADA', 2026, 1, '2026-03-01 00:00:00', '2026-03-15 00:00:00', FALSE),
(4, 'CURSADA', 2026, 1, '2026-03-01 00:00:00', '2026-03-15 00:00:00', FALSE);
-- ---------------------------------------------------------------------
-- NIVEL 3: Examen
-- ---------------------------------------------------------------------
INSERT INTO Examen (idMateria, fecha, tipoExamen) VALUES
(2, '2026-06-22', 'PARCIAL'),
(2, '2026-07-12', 'FINAL'),
(4, '2026-06-24', 'PARCIAL'),
(5, '2026-06-26', 'PARCIAL'),
(6, '2026-06-27', 'PARCIAL'),
(7, '2026-06-29', 'PARCIAL');
-- ---------------------------------------------------------------------
-- NIVEL 3: comision
-- ---------------------------------------------------------------------
INSERT INTO comision (idMateria, idProfesor, nroComision, cantAlumnos, aula, cupo_maximo) VALUES
(2, 1, 201, 25, 'Aula 4', 40),
(4, 8, 401, 20, 'Aula 6', 35),
(5, 5, 501, 18, 'Aula 7', 30),
(6, 8, 601, 22, 'Aula 8', 35),
(7, 5, 701, 15, 'Aula 9', 30),
(6, 8, 602, 14, 'Aula 10', 30);
-- ---------------------------------------------------------------------
-- NIVEL 3: Cuota
-- ---------------------------------------------------------------------
INSERT INTO Cuota (idAlumno, valorCuota, fechaVencimiento, mes, anio, conceptoCuota, estadoCuota) VALUES
(7,  30000, '2026-06-10', 6, 2026, 'CUOTA',    'PENDIENTE'),
(10, 30000, '2026-06-10', 6, 2026, 'CUOTA',    'VENCIDA'),
(11, 30000, '2026-06-10', 6, 2026, 'CUOTA',    'PAGADA'),
(2,  15000, '2025-03-05', 3, 2025, 'MATRICULA','PAGADA');
-- ---------------------------------------------------------------------
-- NIVEL 3: Alumno_Cursa_Carrera
-- ---------------------------------------------------------------------
INSERT INTO Alumno_Cursa_Carrera (idAlumno, idCarrera, fecha_inscripcion) VALUES
(7,  2, '2024-03-01'),
(10, 3, '2025-03-01'),
(11, 4, '2023-03-01');
-- ---------------------------------------------------------------------
-- NIVEL 3: Correlatividad
-- ---------------------------------------------------------------------
INSERT INTO Correlatividad (idMateria, idMateriaCorrelativa, estadoParaCursar, estadoParaRendir) VALUES
(2, 1, 'APROBADA', 'APROBADA'),
(3, 2, 'CURSADA',  'APROBADA'),
(5, 4, 'CURSADA',  'APROBADA');
-- ---------------------------------------------------------------------
-- NIVEL 3: Alumno_Materia
-- ---------------------------------------------------------------------
INSERT INTO Alumno_Materia (id_alumno, id_materia, estado, notaParcial1, notaParcial2, notaFinal, fechaInscripcion, fechaRegularizacion, fechaAprobacion) VALUES
(2,  1, 'APROBADA',  9.0, 8.0, 9.0,  '2025-03-01', '2025-06-20', '2025-07-15'),
(3,  1, 'APROBADA',  8.0, 9.0, 8.0,  '2024-03-01', '2024-06-20', '2024-07-10'),
(7,  4, 'CURSANDO',  7.0, NULL, NULL,'2026-03-01', NULL, NULL),
(10, 6, 'REGULAR',   6.0, 7.0, NULL,'2026-03-01', '2026-06-10', NULL),
(11, 7, 'PENDIENTE', NULL, NULL, NULL, '2026-03-01', NULL, NULL);
-- ---------------------------------------------------------------------
-- NIVEL 3: boleto_especial_educativo
-- ---------------------------------------------------------------------
INSERT INTO boleto_especial_educativo (idAlumno, fueSolicitado, estaActivo) VALUES
(2,  TRUE,  TRUE),
(3,  TRUE,  FALSE),
(4,  FALSE, FALSE),
(7,  TRUE,  TRUE),
(10, TRUE,  TRUE);
-- ---------------------------------------------------------------------
-- NIVEL 4: Nota
-- ---------------------------------------------------------------------
INSERT INTO Nota (idExamen, idAlumno, nota, fechaRegistro) VALUES
(3, 2,  7, '2026-06-22'),
(5, 7,  6, '2026-06-24'),
(7, 10, 8, '2026-06-27'),
(8, 11, 5, '2026-06-29');
-- ---------------------------------------------------------------------
-- NIVEL 4: PagoCuota
-- ---------------------------------------------------------------------
INSERT INTO PagoCuota (idCuota, montoPagado, fechaPago, metodoPago) VALUES
(6, 30000, '2026-06-05', 'TRANSFERENCIA'),
(7, 15000, '2025-03-04', 'TARJETA'),
(4, 30000, '2026-06-08', 'EFECTIVO');
-- ---------------------------------------------------------------------
-- NIVEL 4: Alumno_Inscripcion_Comision
-- ---------------------------------------------------------------------
INSERT INTO Alumno_Inscripcion_Comision (idAlumno, idComision, fechaInscripcion) VALUES
(7,  5, '2026-03-02'),
(10, 7, '2026-03-02'),
(11, 8, '2026-03-02'),
(2,  4, '2026-03-03');
-- ---------------------------------------------------------------------
-- NIVEL 4: Alumno_Inscripcion_Examen_Final
-- ---------------------------------------------------------------------
INSERT INTO Alumno_Inscripcion_Examen_Final (idAlumno, idExamen, fechaInscripcion) VALUES
(7,  5, '2026-07-02'),
(10, 7, '2026-07-02'),
(11, 8, '2026-07-03');
-- ---------------------------------------------------------------------
-- NIVEL 4: ComisionHorario
-- ---------------------------------------------------------------------
INSERT INTO ComisionHorario (idComision, diaSemana, horaInicio, horaFin) VALUES
(1, 'LUNES',     '18:00:00', '22:00:00'),
(2, 'MARTES',    '18:00:00', '22:00:00'),
(3, 'MIERCOLES', '18:00:00', '22:00:00'),
(4, 'JUEVES',    '19:00:00', '23:00:00'),
(5, 'VIERNES',   '08:00:00', '12:00:00'),
(6, 'LUNES',     '19:00:00', '23:00:00'),
(7, 'SABADO',    '09:00:00', '13:00:00'),
(8, 'MARTES',    '08:00:00', '12:00:00'),
(9, 'JUEVES',    '08:00:00', '12:00:00');
-- ---------------------------------------------------------------------
-- NIVEL 4: periodo_inscripcion_comision
-- ---------------------------------------------------------------------
INSERT INTO periodo_inscripcion_comision (idPeriodo, idComision) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 5),
(2, 6),
(3, 7),
(4, 8),
(3, 9);
-- ---------------------------------------------------------------------
-- NIVEL 4: RespuestaEncuesta
-- ---------------------------------------------------------------------
INSERT INTO RespuestaEncuesta (idComision, hashAlumno, fechaRespuesta, comentarioFinal) VALUES
(1, 'f3a1c9e2b7d4a6f8c1e3b5d7a9f1c3e5b7d9f1a3c5e7b9d1f3a5c7e9b1d3f5a', '2026-06-16 10:15:00', 'Muy buena cursada, el profesor explica con claridad.'),
(1, '7b2d4f6a8c0e2b4d6f8a0c2e4b6d8f0a2c4e6b8d0f2a4c6e8b0d2f4a6c8e0b2', '2026-06-16 11:00:00', NULL),
(2, '1c3e5a7b9d1f3a5c7e9b1d3f5a7c9e1b3d5f7a9c1e3b5d7f9a1c3e5b7d9f1a3', '2026-06-16 12:30:00', 'Faltaron mas ejercicios practicos.'),
(4, '9d1f3a5c7e9b1d3f5a7c9e1b3d5f7a9c1e3b5d7f9a1c3e5b7d9f1a3c5e7b9d1', '2026-06-17 09:45:00', 'Todo excelente.'),
(5, '5e7b9d1f3a5c7e9b1d3f5a7c9e1b3d5f7a9c1e3b5d7f9a1c3e5b7d9f1a3c5e7', '2026-06-17 16:20:00', NULL);
-- ---------------------------------------------------------------------
-- NIVEL 5: RespuestaPregunta
-- ---------------------------------------------------------------------
INSERT INTO RespuestaPregunta (idRespuestaEncuesta, idPreguntaEncuesta, calificacion) VALUES
(1, 1, 5),
(1, 2, 5),
(2, 1, 4),
(2, 3, 4),
(3, 4, 2),
(3, 5, 3),
(4, 1, 5),
(4, 5, 5),
(5, 2, 4),
(5, 3, 4);