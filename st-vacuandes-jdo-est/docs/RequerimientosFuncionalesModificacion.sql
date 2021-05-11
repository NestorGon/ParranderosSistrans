--Sentencias RF8
--Con esta sentencia se conoce la lista de EPS
SELECT * FROM EPS;
-- Con esta sentencia se conoce el número de vacunas de la EPS
SELECT VACUNAS FROM EPS WHERE ID = '13';
-- Con esta sentencia se conoce la capacidad de vacunas de la EPS
SELECT CAPACIDADVACUNAS FROM EPS WHERE ID = '13';
-- Con esta sentencia se aumenta el número de vacunas de la EPS
UPDATE EPS SET VACUNAS = VACUNAS + 200 WHERE ID = '13';

--Sentencias RF9
--Con esta sentencia se conoce la lista de Puntos
SELECT * FROM PUNTO;
-- Con esta sentencia se conoce el número de vacunas del Punto
SELECT VACUNAS FROM PUNTO WHERE ID = '23';
-- Con esta sentencia se conoce la capacidad de vacunas del Punto
SELECT CAPACIDADVACUNAS FROM PUNTO WHERE ID = '23';
-- Con esta sentencia se aumenta el número de vacunas del Punto
 UPDATE PUNTO SET VACUNAS = VACUNAS + 150 WHERE ID = '23';

--Sentencias RF10
--Con esta sentencia se conocen las condiciones de priorización del ciudadano
SELECT DESCRIPCION_CONDPRIOR FROM PRIORIZACION WHERE DOCUMENTO_CIUDADANO = '73849475';
--Con esta sentencia se conocen las condiciones de priorización que atiende el punto
SELECT DESCRIPCION_CONDPRIOR FROM ATENCION WHERE ID_PUNTO = '15';
--Luego de validar la información, con esta sentencia se asigna al ciudadano al punto de vacunación
--Además, se tiene en cuenta la capacidad del punto para la asignación
INSERT INTO VACUNACION(DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO) values ('73849475', '14', '15');

--Sentencias RF11
--Con esta sentencia se halla el ciudadano dado por el documento
SELECT * FROM CIUDADANO WHERE DOCUMENTO = '18283893';
--Con esta sentencia se encuentra la etapa del ciudadano
SELECT NUMERO_ETAPA FROM CIUDADANO WHERE DOCUMENTO = '18283893';
--Con esta sentencia se halla el punto
SELECT * FROM PUNTO WHERE ID = '20';
--Con esta sentencia se encuentra la capacidad del punto
SELECT CAPACIDAD FROM PUNTO WHERE ID = '20';
--Con esta sentencia se encuentra el número de citas activas del punto
SELECT COUNT(*)CANTIDAD FROM CITA WHERE ID_PUNTO = '20' AND FINALIZADA = 'F' GROUP BY ID_PUNTO;
--Con esta sentencia se adiciona una cita
INSERT INTO CITA (FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO) values (TO_DATE('02-05-2021 12:00', 'DD-MM-YYYY HH24:MI'), 'F', '18283893', '20');

--Sentencias RF12
--Cambia el estado de la vacuna que fue aplicada al ciudadano
UPDATE VACUNA SET APLICADA = 'T' WHERE ID = ?;
--Actualiza el estado de vacunas aplicadas y disponibles del punto correspondiente
UPDATE PUNTO SET APLICADAS = APLICADAS + 1, VACUNAS = VACUNAS - 1 WHERE ID = ?;
--Actualiza el ciudadano para registrar su avance en el proceso de vacunación
UPDATE CIUDADANO SET NOMBRE = ?, NACIMIENTO = ?, HABILITADO = ?, ID_ESTADO = ?, ID_EPS = ?, NUMERO_ETAPA = ?, SEXO = ? WHERE DOCUMENTO = ?;

--Sentecias RF13
--Agrega las nuevas condiciones de priorización que atiende el punto
INSERT INTO ATENCION(DESCRIPCION_CONDPRIOR, ID_PUNTO) values (?, ?);
--Elimina las condiciones de priorización que ya no atiende el punto
DELETE FROM ATENCION WHERE DESCRIPCION_CONDPRIOR = ? AND ID_PUNTO = ?;
--Encuentra todos los ciudadanos del punto que ya no pertenecen debido a que no coindice la priorización
SELECT C.DOCUMENTO
FROM CIUDADANO C, VACUNACION V
WHERE V.ID_PUNTO = ? AND
      C.DOCUMENTO = V.DOCUMENTO_CIUDADANO AND
      (SELECT COUNT(*)
       FROM PRIORIZACION P, ATENCION A
       WHERE P.DOCUMENTO_CIUDADANO = C.DOCUMENTO AND
             A.ID_PUNTO = V.ID_PUNTO AND
             A.DESCRIPCION_CONDPRIOR = P.DESCRIPCION_CONDPRIOR) = 0;
--Elimina la relación de los ciudadanos con el punto, de aquellos que ya no coinciden
DELETE FROM VACUNACION WHERE DOCUMENTO_CIUDADANO = ? AND ID_EPS = ?;

--Sentencias RF14
--Con esta sentencia se hallan los puntos de la base de datos
SELECT * FROM PUNTO;
--Con esta sentencia se conoce el número de citas activas de un punto
SELECT COUNT(*)CANTIDAD FROM CITA WHERE ID_PUNTO = '20' AND FINALIZADA = 'F' GROUP BY ID_PUNTO;
--Con esta sentencia se halla el punto pedido
SELECT * FROM PUNTO WHERE ID = '20';
--Con esta sentencia se halla la lista de puntos habilitados de una EPS
SELECT * FROM PUNTO WHERE HABILITADO = 'T' AND ID_EPS = '15';
--Con esta sentencia se cambia el estado de habilitado de un punto
UPDATE PUNTO SET HABILITADO = 'F' WHERE ID = '20';
--Con esta sentencia se halla el ciudadano dado
SELECT * FROM CIUDADANO WHERE DOCUMENTO = '18283893';
--Con esta sentencia se elimina la cita vieja
DELETE FROM CITA WHERE FECHAHORA = TO_DATE('20-02-2021 03:00' , 'DD-MM-YYYY HH24:MI') AND DOCUMENTO_CIUDADANO = '18283893';
--Con esta sentencia se agrega la cita nueva
INSERT INTO CITA (FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO) values (TO_DATE('28-05-2021 04:00', 'DD-MM-YYYY HH24:MI'), 'F', '18283893', '20');

--Sentencias RF15
--Cambia el estado de habilitado del punto
UPDATE PUNTO SET HABILITADO = 'T' WHERE ID = ?;
--Encuentra todos los ciudadanos de la etapa de vacunación que no tienen una cita
SELECT C.DOCUMENTO 
FROM CIUDADANO C, EPS E, PUNTO P
WHERE C.ID_EPS = E.ID AND
      P.ID = ? AND
      E.ID = P.ID_EPS AND
      C.HABILITADO = 'T' AND
      C.NUMERO_ETAPA = ? AND
      (SELECT COUNT(*)
       FROM CITA CI
       WHERE CI.DOCUMENTO_CIUDADANO = C.DOCUMENTO) = 0;
--Actualiza el punto de vacunación de los ciudadanos sin cita
UPDATE VACUNACION SET ID_PUNTO = ? WHERE DOCUMENTO_CIUDADANO = ? AND ID_EPS = ?;
--Asigna una cita a cada ciudadano en el punto rehabilitado
INSERT INTO CITA(FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO, ID_PUNTO) values (TO_DATE(?, 'DD-MM-YYYY HH24:MI'), ?, ?, ?);