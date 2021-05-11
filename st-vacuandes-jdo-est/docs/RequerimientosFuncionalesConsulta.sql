--Sentencia RFC8
--Esta sentencia es un ejemplo de la consulta incluyendo todos los parámetros disponibles
--La sentencia puede variar dependiendo de los parámetros seleccionados
SELECT *
FROM CIUDADANO C
WHERE (TRUNC(MONTHS_BETWEEN(SYSDATE, NACIMIENTO)/12) BETWEEN 20 AND 22) AND
      SEXO IN ('FEMENINO') AND
      (SELECT COUNT(*)
       FROM PRIORIZACION P
       WHERE C.DOCUMENTO = P.DOCUMENTO_CIUDADANO AND
             P.DESCRIPCION_CONDPRIOR IN ('16 AÑOS O MAS', 'ASMA')) > 0 AND
      (SELECT COUNT(*)
       FROM EPS E
       WHERE C.ID_EPS = E.ID AND
             E.REGION IN ('BOGOTA','ATLANTICO')) > 0 AND
      C.ID_EPS IN ('15','13') AND
      (SELECT COUNT(*)
       FROM VACUNACION V
       WHERE C.DOCUMENTO = V.DOCUMENTO_CIUDADANO AND
             V.ID_PUNTO IN ('28')) > 0 AND
      (SELECT COUNT(*)
       FROM ESTADO E
       WHERE C.ID_ESTADO = E.ID AND
             (E.DESCRIPCION LIKE '%DOSIS 1%' or E.DESCRIPCION LIKE '%DOSIS 2%') AND
             E.DESCRIPCION LIKE '%PFIZER%') > 0;

SENTENCIA RFC9
-- Esta sentencia es un ejemplo de consulta para el documento de ciudadano 2637738, la fecha ingresada 20-03-2021 y la fecha inicial 10-03-2021 para el punto 11 y la fecha 11-03-2021 12:00
-- Primero se crea una tabla info con la informacion de las fechas y documentos de los ciudadanos con citas en el punto especificado entre el rango de fechas dado
-- De esta tabla se toman únicamente los documentos cuya fecha y hora es igual a la dada
CREATE TABLE INFO(FECHAHORA, DOCUMENTO) AS 
 (SELECT CITA.FECHAHORA, CIUDADANO.DOCUMENTO
 FROM (CITA JOIN CIUDADANO ON CITA.DOCUMENTO_CIUDADANO = CIUDADANO.DOCUMENTO)
 JOIN PUNTO ON CITA.ID_PUNTO = PUNTO.ID
 WHERE CITA.FECHAHORA > TO_DATE('10-03-2021', 'DD-MM-YYYY HH24:MI') AND
 CITA.FECHAHORA < TO_DATE('20-03-2021', 'DD-MM-YYYY HH24:MI')"
 AND PUNTO.ID = '11');

SELECT DOCUMENTO
FROM INFO
WHERE FECHAHORA = TO_DATE('11-03-2021 12:00', 'DD-MM-YYYY HH24:MI');