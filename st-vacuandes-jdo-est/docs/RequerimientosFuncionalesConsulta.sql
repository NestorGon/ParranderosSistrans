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