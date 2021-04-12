--CIUDADANO
--Pruebas de unicidad de tuplas
--Tupla con PK conocida y nueva
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'FELIPE RAMIREZ', TO_DATE('18-10-1998', 'DD-MM-YYYY'), 'T',1, '12',3);

--Tupla con la misma PK
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'DANIEL GARCIA', TO_DATE('31-05-1972', 'DD-MM-YYYY'), 'T',2, '14',2);

--Pruebas de integridad con FK
--Se prueba el FK de ESTADO(ID)
--Tupla con FK existente. ID_ESTADO = 1
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'FELIPE RAMIREZ', TO_DATE('18-10-1998', 'DD-MM-YYYY'), 'T',1, '12',3);
--Tupla con FK no existente. ID_ESTADO = 6
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651914', 'FELIPE RAMIREZ', TO_DATE('18-10-1998', 'DD-MM-YYYY'), 'T',6, '12',3);
--Borrado en tuplas dependientes. Se intenta borrar la eps asociada al ciudadano insertado
DELETE FROM EPS
WHERE ID = 1;
--Borrado en tuplas maestras. Se borra al ciudadano
DELETE FROM CIUDADANO
WHERE DOCUMENTO = '51651912';

--Pruebas de integridad de restricciones CK
--Tuplas que cumplen todas las restricciones
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'FELIPE RAMIREZ', TO_DATE('18-10-1998', 'DD-MM-YYYY'), 'T',1, '12',3);
--Tuplas que violan las restricciones
--El ciudadano tiene una fecha de nacimiento inválida y habilitado no corresponde a 'T' o 'F'
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'FELIPE RAMIREZ', TO_DATE('01-01-1890', 'DD-MM-YYYY'), 'X',1, '12',3);




--ROLES
--Pruebas de unicidad de tuplas
--Tupla con PK conocida y nueva
INSERT INTO ROLES (ID, ROL) 
VALUES (20, 'ADMINISTRADOR MUNICIPAL');

--Tupla con la misma PK
INSERT INTO ROLES (ID, ROL) 
VALUES (20, 'OPERADOR DE EPS');

--Pruebas de integridad con FK
--ROLES no contiene ninguna llave foránea

--Pruebas de integridad de restricciones CK
--Tuplas que cumplen todas las restricciones
INSERT INTO ROLES (ID, ROL) 
VALUES (20, 'ADMINISTRADOR MUNICIPAL');

--Tuplas que violan las restricciones
--No tiene un id o un rol válidos
INSERT INTO ROLES (ID, ROL) 
VALUES (-3, '');




--ESTADO
--Pruebas de unicidad de tuplas
--Tupla con PK conocida y nueva
INSERT INTO ESTADO (ID, DESCRIPCION)		
VALUES('8', 'AGENDADO');

--Tupla con la misma PK
INSERT INTO ESTADO (ID, DESCRIPCION)		
VALUES('8', 'SIN REGISTRAR');

--Pruebas de integridad con FK
--ESTADO no contiene ninguna llave foránea

--Pruebas de integridad de restricciones CK
--Tuplas que cumplen todas las restricciones
INSERT INTO ESTADO (ID, DESCRIPCION)		
VALUES(8, 'AGENDADO');

--Tuplas que violan las restricciones
--El estado no tiene una descripción válida
INSERT INTO ESTADO (ID, DESCRIPCION)		
VALUES(-3, '');




--USUARIO
--Setup: Creación del ciudadano e infousuario
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'FELIPE RAMIREZ', TO_DATE('18-10-1998', 'DD-MM-YYYY'), 'T',1, '12',3);
INSERT INTO INFOUSUARIO (LOGIN, CLAVE, TRABAJO, ID_ROLES, ID_PUNTO)
VALUES('F-RAMI', '12345', 'ADMINISTRADOR DEL PLAN DE VACUNACION', 1, '1');
INSERT INTO INFOUSUARIO (LOGIN, CLAVE, TRABAJO, ID_ROLES, ID_PUNTO)
VALUES('PGONZ', 'psw123', 'ADMINISTRADOR DEL PLAN DE VACUNACION', 1, '2');
 
--Pruebas de unicidad de tuplas
--Tupla con PK conocida y nueva
INSERT INTO USUARIO (DOCUMENTO_CIUDADANO, LOGIN_INFOUSUARIO)
VALUES ('51651912', 'F-RAMI');

--Tupla con la misma PK
INSERT INTO USUARIO (DOCUMENTO_CIUDADANO, LOGIN_INFOUSUARIO)
VALUES ('51651912', 'PGONZ');

--Pruebas de integridad con FK
--Se prueba el FK de ESTADO(ID)
--Tupla con FK existente
INSERT INTO USUARIO (DOCUMENTO_CIUDADANO, LOGIN_INFOUSUARIO)
VALUES ('51651912', 'F-RAMI');
--Tupla con FK no existente
INSERT INTO USUARIO (DOCUMENTO_CIUDADANO, LOGIN_INFOUSUARIO)
VALUES ('51651913', 'FRAMIR');
--Borrado en tuplas dependientes
DELETE FROM INFOUSUARIO
WHERE LOGIN = 'F-RAMI';
--Borrado en tuplas maestras
DELETE FROM USUARIO
WHERE DOCUMENTO_CIUDADANO = '51651912';

--Pruebas de integridad de restricciones CK
--Tuplas que cumplen todas las restricciones
INSERT INTO USUARIO (DOCUMENTO_CIUDADANO, LOGIN_INFOUSUARIO)
VALUES ('51651912', 'F-RAMI');
--Tuplas que violan las restricciones
--El usuario tiene un login inválido
INSERT INTO USUARIO (DOCUMENTO_CIUDADANO, LOGIN_INFOUSUARIO)
VALUES ('51651912', '');



--INFOUSUARIO
--Pruebas de unicidad de tuplas
--Tupla con PK conocida y nueva
INSERT INTO INFOUSUARIO (LOGIN, TRABAJO, ID_ROLES, ID_PUNTO)
VALUES('F-RAMI', 'ADMINISTRADOR DEL PLAN DE VACUNACION', 1, '1');

--Tupla con la misma PK
INSERT INTO INFOUSUARIO (LOGIN, TRABAJO, ID_ROLES, ID_PUNTO)
VALUES('F-RAMI', 'ADMINISTRADOR REGIONAL', 2, '2');

--Pruebas de integridad con FK
--Se prueba el FK
--Tupla con FK existente
INSERT INTO INFOUSUARIO (LOGIN, TRABAJO, ID_ROLES, ID_PUNTO)
VALUES('F-RAMI', 'ADMINISTRADOR DEL PLAN DE VACUNACION', 1, '1');
--Tupla con FK no existente
INSERT INTO INFOUSUARIO (LOGIN, TRABAJO, ID_ROLES, ID_PUNTO)
VALUES('F-RAMI', 'ADMINISTRADOR DEL PLAN DE VACUNACION', 7, '100');
--Borrado en tuplas dependientes
DELETE FROM ROLES
WHERE ID = 1;
--Borrado en tuplas maestras
DELETE FROM INFOUSUARIO
WHERE LOGIN = 'F-RAMI';

--Pruebas de integridad de restricciones CK
--Tuplas que cumplen todas las restricciones
INSERT INTO INFOUSUARIO (LOGIN, TRABAJO, ID_ROLES, ID_PUNTO)
VALUES('F-RAMI', 'ADMINISTRADOR DEL PLAN DE VACUNACION', 1, '1');
--Tuplas que violan las restricciones
--El rol es null
INSERT INTO INFOUSUARIO (LOGIN, TRABAJO, ID_ROLES, ID_PUNTO)
VALUES('F-RAMI', 'ADMINISTRADOR DEL PLAN DE VACUNACION', NULL, '1');




--VACUNACION
--Setup: Creación del ciudadano e infousuario
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'FELIPE RAMIREZ', TO_DATE('18-10-1998', 'DD-MM-YYYY'), 'T',1, '12',3);
 
--Pruebas de unicidad de tuplas
--Tupla con PK conocida y nueva
INSERT INTO VACUNACION (DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO)
VALUES('51651912', '13', '2');

--Tupla con la misma PK
INSERT INTO VACUNACION (DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO)
VALUES('51651913', '13', '3');

--Pruebas de integridad con FK
--Se prueba el FK de ESTADO(ID)
--Tupla con FK existente
INSERT INTO VACUNACION (DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO)
VALUES('51651912', '13', '2');
--Tupla con FK no existente
INSERT INTO VACUNACION (DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO)
VALUES('51651912', '12', '100');
--Borrado en tuplas dependientes
DELETE FROM PUNTO
WHERE ID = '2';
--Borrado en tuplas maestras
DELETE FROM VACUNACION
WHERE DOCUMENTO_CIUDADANO = '51651912';

--Pruebas de integridad de restricciones CK
--Tuplas que cumplen todas las restricciones
INSERT INTO VACUNACION (DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO)
VALUES('51651912', '13', '2');
--Tuplas que violan las restricciones
--El punto de vacunación es null
INSERT INTO VACUNACION (DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO)
VALUES('51651912', '13', NULL);




--PRIORIZACION
--Setup: Creación del ciudadano e infousuario
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'FELIPE RAMIREZ', TO_DATE('18-10-1998', 'DD-MM-YYYY'), 'T',1, '12',3);
 
--Pruebas de unicidad de tuplas
--Tupla con PK conocida y nueva
INSERT INTO PRIORIZACION (DOCUMENTO_CIUDADANO, DESCRIPCION_CONDPRIOR)
VALUES('51651912', '16 AÑOS O MAS');

--Tupla con la misma PK
INSERT INTO PRIORIZACION (DOCUMENTO_CIUDADANO, DESCRIPCION_CONDPRIOR)
VALUES('51651912', '16 AÑOS O MAS');

--Pruebas de integridad con FK
--Se prueba el FK de CONDICIONPRIORIZACION(DESCRIPCION)
--Tupla con FK existente
INSERT INTO PRIORIZACION (DOCUMENTO_CIUDADANO, DESCRIPCION_CONDPRIOR)
VALUES('51651912', 'VIH');
--Tupla con FK no existente
INSERT INTO PRIORIZACION (DOCUMENTO_CIUDADANO, DESCRIPCION_CONDPRIOR)
VALUES('51651912', 'ALZHEIMER');
--Borrado en tuplas dependientes
DELETE FROM CONDICIONPRIORIZACION
WHERE DESCRIPCION = 'VIH';
--Borrado en tuplas maestras
DELETE FROM PRIORIZACION
WHERE DOCUMENTO_CIUDADANO = '51651912' AND
      DESCRIPCION_CONDPRIOR = 'VIH';

--Pruebas de integridad de restricciones CK
--Tuplas que cumplen todas las restricciones
INSERT INTO PRIORIZACION (DOCUMENTO_CIUDADANO, DESCRIPCION_CONDPRIOR)
VALUES('51651912', 'VIH');
--Tuplas que violan las restricciones
--La condición de priorización es null
INSERT INTO PRIORIZACION (DOCUMENTO_CIUDADANO, DESCRIPCION_CONDPRIOR)
VALUES('51651912', NULL);




--CITA
--Setup: Creación del ciudadano
INSERT INTO CIUDADANO (DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA)
VALUES ('51651912', 'FELIPE RAMIREZ', TO_DATE('18-10-1998', 'DD-MM-YYYY'), 'T',1, '12',3);
 
--Pruebas de unicidad de tuplas
--Tupla con PK conocida y nueva
INSERT INTO CITA (FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO)
VALUES(TO_DATE('11-04-2021 16:30', 'DD-MM-YYYY HH24:MI'), 'F', '51651912', '2');

--Tupla con la misma PK
INSERT INTO CITA (FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO)
VALUES(TO_DATE('11-04-2021 16:30', 'DD-MM-YYYY HH24:MI'), 'T', '51651912', '3');

--Pruebas de integridad con FK
--Se prueba el FK
--Tupla con FK existente
INSERT INTO CITA (FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO)
VALUES(TO_DATE('11-04-2021 16:30', 'DD-MM-YYYY HH24:MI'), 'F', '51651912', '2');
--Tupla con FK no existente
INSERT INTO CITA (FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO)
VALUES(TO_DATE('11-04-2021 16:30', 'DD-MM-YYYY HH24:MI'), 'F', '51651913', '100');
--Borrado en tuplas dependientes
DELETE FROM PUNTO
WHERE ID = '2';
--Borrado en tuplas maestras
DELETE FROM CITA
WHERE FECHAHORA = TO_DATE('11-04-2021 16:30', 'DD-MM-YYYY HH24:MI') AND
      DOCUMENTO_CIUDADANO = '51651912';

--Pruebas de integridad de restricciones CK
--Tuplas que cumplen todas las restricciones
INSERT INTO CITA (FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO)
VALUES(TO_DATE('11-04-2021 16:30', 'DD-MM-YYYY HH24:MI'), 'F', '51651912', '2');
--Tuplas que violan las restricciones
--El ciudadano tiene una fecha de nacimiento inválida y habilitado no corresponde a T o F
INSERT INTO CITA (FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO)
VALUES(TO_DATE('11-04-2020 16:30', 'DD-MM-YYYY HH24:MI'), 'H', '51651912', '2');

--PUNTO 
--Insertar tupla con PK conocida y nueva
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
 VALUES('31', 'BOGOTA', 'CALLE 12 #43-3', 234, 782, '13');
 
 --Tupla con la misma PK
 INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
 VALUES('31', 'BOGOTA', 'CALLE 12 #43-3', 234, 782, '13');

-- Pruebas de integridad con FK
-- Se prueba la FK ID_EPS
-- valor conocido de la FK ID_EPS= '13'

INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
 VALUES('34', 'BOGOTA', 'CALLE 23 #53-63', 128, 875, '13');
 
 --Inserción con valor desconocido de FK
 INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
 VALUES('37', 'BOGOTA', 'CALLE 23 #53-63', 128, 875, '17');
 
 --Eliminación de tupla maestra
 DELETE FROM EPS
 WHERE ID= '13';
 
 --Eliminación de tupla dependiente
 DELETE FROM PUNTO
 WHERE ID = '34';

--Insercion de tuplas que cumplen restricciones CK

--Id no vacío
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('35', 'BOGOTA', 'CARRERA 43 #123-10',154, 784, '13');

--Región no vacía
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('36', 'ANTIOQUIA', 'CARRERA 53 #123-10',154, 784, '14');

--Dirección no vacía
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('37', 'ANTIOQUIA', 'CARRERA 63 #123-10',154, 784, '14');

--Aplicadas mayor o igual a cero
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('38', 'ANTIOQUIA', 'CARRERA 73 #123-10',154, 784, '14');

--Capacidad mayor o igual a cero
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('39', 'ANTIOQUIA', 'CARRERA 83 #123-10',154, 784, '14');

--Id vacío
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('', 'BOGOTA', 'CARRERA 43 #123-10',154, 784, '13');

--Región vacía
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('36', '', 'CARRERA 53 #123-10',154, 784, '14');

--Dirección vacía
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('37', 'ANTIOQUIA', '',154, 784, '14');

--Aplicadas no mayor o igual a cero
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('38', 'ANTIOQUIA', 'CARRERA 73 #123-10',-1, 784, '14');

--Capacidad no mayor a cero
INSERT INTO PUNTO(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS)
VALUES('39', 'ANTIOQUIA', 'CARRERA 83 #123-10',0, 784, '14');

-- VACUNA
--Insertar tupla con PK nueva y conocida
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('40', '-25°', 'F', 1, 'PFIZER');

--Insertar tupla con la misma PK
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('40', '-25°', 'F', 1, 'PFIZER');

-- Insercion de tuplas que cumples restricciones de CK

--Id no vacío
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('41', '-25°', 'F', 1, 'PFIZER');

--Preservacion no vacía
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('42', '-25°', 'F', 1, 'PFIZER');

--Aplicada 'T' o 'F'
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('43', '-25°', 'F', 1, 'PFIZER');

--Dosis mayor a cero
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('44', '-25°', 'F', 1, 'PFIZER');

--Tipo no vacío 
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('45', '-25°', 'F', 1, 'PFIZER');

--Inserción de tuplas que no cumplen restricción de CK

--Id vacío
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('', '-25°', 'F', 1, 'PFIZER');

--Preservacion vacía
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('42', '', 'F', 1, 'PFIZER');

--Aplicada no 'T' o 'F'
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('43', '-25°', 'K', 1, 'PFIZER');

--Dosis no mayor a cero
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('43', '-25°', 'F', -1, 'PFIZER');

--Tipo vacío 
INSERT INTO VACUNA(ID, PRESERVACION, APLICADA, DOSIS, TIPO)
VALUES('43', '-25°', 'F', 1, '');

--ETAPA
--Insertar tupla con PK nueva y conocida
 INSERT INTO ETAPA (NUMERO, DESCRIPCION)
 VALUES(6, 'OBESIDAD');

 --Insertar tupla con la misma PK
  INSERT INTO ETAPA (NUMERO, DESCRIPCION)
 VALUES(6, 'OBESIDAD');

-- Inserción de tuplas que cumplen restrcciones de CK
--Para poder insertar estas es necesario borrar otras con la misma PK

 --Etapa de número entre 0 y 6
INSERT INTO ETAPA (NUMERO, DESCRIPCION)
 VALUES(6, 'OBESIDAD');
 
 --Descripción no vacía
INSERT INTO ETAPA (NUMERO, DESCRIPCION)
 VALUES(5, 'OBESIDAD');
 
 -- Inserción de tuplas que no cumplen restrcciones de CK
 --Etapa de número no entre 0 y 6
INSERT INTO ETAPA (NUMERO, DESCRIPCION)
 VALUES(8, 'OBESIDAD');
 
 --Descripción vacía
INSERT INTO ETAPA (NUMERO, DESCRIPCION)
 VALUES(5, '');

-- CONDICION PRIORIZACION
--Insertar tupla con PK nueva y conocida
INSERT INTO CONDICIONPRIORIZACION (DESCRIPCION, NUMERO_ETAPA)
 VALUES('ENFERMEDADES AUTOINMUNES', 2);

 --Insertar tupla con la misma PK
 INSERT INTO CONDICIONPRIORIZACION (DESCRIPCION, NUMERO_ETAPA)
 VALUES('ENFERMEDADES AUTOINMUNES', 2);

--Pruebas de integridad con FK
--Se prueba la FK NUMERO_ETAPA
--Valor conocido NUMERO_ETAPA = 5

--Inserción con valor conocido FK
 INSERT INTO CONDICIONPRIORIZACION (DESCRIPCION, NUMERO_ETAPA)
 VALUES('CANCER DE PULMON', 5);
 
 --Inserción con valor desconocido FK
 INSERT INTO CONDICIONPRIORIZACION (DESCRIPCION, NUMERO_ETAPA)
 VALUES('CANCER DE PULMON', 8);

--Borrado tupla maestra
DELETE FROM ETAPA
WHERE NUMERO= '5';

--Inserción de tuplas que cumplen con restriciones CK
--Descripcíon no vacía
 INSERT INTO CONDICIONPRIORIZACION (DESCRIPCION, NUMERO_ETAPA)
 VALUES('CANCER', 2);
 
 --Inserción de tuplas que no cumplen con restriciones CK
  INSERT INTO CONDICIONPRIORIZACION (DESCRIPCION, NUMERO_ETAPA)
 VALUES('', 2);

--EPS
--Insertar Tupla con PK nueva y conocida
 INSERT INTO EPS (ID, REGION, VACUNAS)
 VALUES('16','AMAZONAS',500);
 
 --Insertar tupla con misma PK
  INSERT INTO EPS (ID, REGION, VACUNAS)
 VALUES('16','AMAZONAS',500);

--Inserción de tuplas que cumplen con restriciones CK
--Id no vacío
INSERT INTO EPS (ID, REGION, VACUNAS)
VALUES('17','LLANOS ORIENTALES',550);
 
 --Vacunas mayores o igual a 0
INSERT INTO EPS (ID, REGION, VACUNAS)
VALUES('18','ISLAS SAN ANDRÉS Y PROVIDENCIA',550);

--Inserción de tuplas que no cumplen con restricciones CK
--Id vacío
INSERT INTO EPS (ID, REGION, VACUNAS)
VALUES('','LLANOS ORIENTALES',550);

--Vacunas no mayores o igual a 0
INSERT INTO EPS (ID, REGION, VACUNAS)
VALUES('18','ISLAS SAN ANDRÉS Y PROVIDENCIA',-2);

--ASIGNADA
--Insertar Tupla con PK nueva y conocida
 INSERT INTO ASIGNADA(ID_EPS, ID_PUNTO, ID_VACUNA)
 VALUES('13','5','43');
 
 --Insertar tupla con misma PK
INSERT INTO ASIGNADA(ID_EPS, ID_PUNTO, ID_VACUNA)
 VALUES('13','5','43');

--Pruebas de integridad con FK
--Se prueban las FK ID_EPS, ID_PUNTO,ID_VACUNA
-- Valores ID_EPS: 13, ID_PUNTO: 28, ID_VACUNA: 45

--Insercion con valores conociddos FK ID_EPS
INSERT INTO ASIGNADA(ID_EPS, ID_PUNTO, ID_VACUNA)
 VALUES('13','5','44');
 
 --Insercion con valores conocidos FK ID_PUNTO
 INSERT INTO ASIGNADA(ID_EPS, ID_PUNTO, ID_VACUNA)
 VALUES('13','28','43');
 
 --Insercion con valores conocidos FK ID_VACUNA
  INSERT INTO ASIGNADA(ID_EPS, ID_PUNTO, ID_VACUNA)
 VALUES('13','27','45');
 
 --Insercion con valor desconocido FK ID_EPS
INSERT INTO ASIGNADA(ID_EPS, ID_PUNTO, ID_VACUNA)
 VALUES('2043','5','43');
 
 --Insercion con valor desconocido FK ID_PUNTO
 INSERT INTO ASIGNADA(ID_EPS, ID_PUNTO, ID_VACUNA)
 VALUES('13','108','43');
 
 --Insercion con valor desconocido FK ID_VACUNA
  INSERT INTO ASIGNADA(ID_EPS, ID_PUNTO, ID_VACUNA)
 VALUES('13','27','122');


