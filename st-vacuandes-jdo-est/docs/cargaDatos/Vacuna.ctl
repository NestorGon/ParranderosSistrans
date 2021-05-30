load data into table VACUNA
append
fields terminated by ","
(
ID,
APLICADA,
TIPO,
DOSIS,
PRESERVACION,
LLEGADA date "DD-MM-YYYY",
FECHAAPLICADA date "DD-MM-YYYY"
)
