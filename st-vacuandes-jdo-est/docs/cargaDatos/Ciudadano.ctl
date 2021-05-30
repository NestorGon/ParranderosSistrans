load data into table CIUDADANO
append
fields terminated by ","
(
DOCUMENTO,
NOMBRE,
NACIMIENTO date "DD-MM-YYYY",
HABILITADO,
ID_ESTADO,
ID_EPS,
NUMERO_ETAPA,
SEXO
)
