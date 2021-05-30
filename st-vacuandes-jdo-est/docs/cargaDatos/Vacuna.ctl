load data into table VACUNA
append
fields terminated by ","
(
ID,
APLICADA,
TIPO,
DOSIS,
PRESERVACION,
LLEGADA date "YYYY-MM-DD",
FECHAAPLICADA date "YYYY-MM-DD"
)
