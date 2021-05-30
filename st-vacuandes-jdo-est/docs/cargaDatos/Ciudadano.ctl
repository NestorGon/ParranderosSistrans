load data into table CIUDADANO
append
fields terminated by ","
(
DOCUMENTO,
NOMBRE,
NACIMIENTO date "YYYY-MM-DD",
HABILITADO,
ID_ESTADO,
ID_EPS,
NUMERO_ETAPA,
SEXO
)
