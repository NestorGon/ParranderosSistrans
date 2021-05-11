--Sentencias RF10
--Con esta sentencia se conocen las condiciones de priorización del ciudadano
SELECT DESCRIPCION_CONDPRIOR FROM PRIORIZACION WHERE DOCUMENTO_CIUDADANO = '73849475';
--Con esta sentencia se conocen las condiciones de priorización que atiende el punto
SELECT DESCRIPCION_CONDPRIOR FROM ATENCION WHERE ID_PUNTO = '15';
--Luego de validar la información, con esta sentencia se asigna al ciudadano al punto de vacunación
--Además, se tiene en cuenta la capacidad del punto para la asignación
INSERT INTO VACUNACION(DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO) values ('73849475', '14', '15');

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