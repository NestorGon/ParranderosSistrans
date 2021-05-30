import random 
import csv
import time
from datetime import datetime
from faker import Faker
from faker.providers import *

fake = Faker('es_MX')

ciudadanoFile = open('./datos/CIUDADANO.dat','w')
vacunaFile = open('./datos/VACUNA.dat','w')
vacunacionFile = open('./datos/VACUNACION.dat','w')
asignadaFile = open('./datos/ASIGNADA.dat','w')
citaFile = open('./datos/CITA.dat','w')
priorizacionFile = open('./datos/PRIORIZACION.dat','w')

ciudadanoWriter = csv.writer(ciudadanoFile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
vacunaWriter = csv.writer(vacunaFile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
vacunacionWriter = csv.writer(vacunacionFile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
asignadaWriter = csv.writer(asignadaFile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
citaWriter = csv.writer(citaFile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
priorizacionWriter = csv.writer(priorizacionFile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)

nacimientoInicial = datetime.strptime('1-01-1950 00:00:00', '%m-%d-%Y %H:%M:%S')
nacimientoFinal = datetime.strptime('12-31-2004 00:00:00', '%m-%d-%Y %H:%M:%S')
llegadaInicial = datetime.strptime('01-01-2021 00:00:00', '%m-%d-%Y %H:%M:%S')
llegadaFinal = datetime.strptime('5-29-2021 00:00:00', '%m-%d-%Y %H:%M:%S')
priorizaciones = ['80 ANIOS O MAS','60 A 79 ANIOS','ENFERMEDADES HIPERTENSIVAS','DIABETES','INSUFICIENCIA RENAL','VIH','CANCER','ASMA','PRIVACION DE LIBERTAD','16 ANIOS O MAS']
tiposVacunas = ['PFIZER','SINOVAC','ASTRAZENECA','JOHNSON']
boolean1 = ['T','F']
boolean2 = ['T','F','C']

#Refiere a la cantidad sobre el total de datos que se desea generar. Ej: 0.125, 0.25, 0.5, 1
cantidadDatos = float(input("Cantidad de datos: "))

timer = time.time()
for i in range(round(500000*cantidadDatos)):
    #Ciudadano
    documento = fake.unique.credit_card_number()
    sexo = random.choice(['MASCULINO','FEMENINO'])
    nombre = (fake.first_name_female() if sexo.startswith('F') else fake.first_name_male()) + ' ' + fake.last_name()
    nacimiento = fake.date_between(nacimientoInicial,nacimientoFinal)
    habilitado = random.choice(boolean1)
    id_estado = random.randint(1,9)
    id_eps = random.randint(1,20)
    numero_etapa = random.randint(1,5)
    ciudadanoWriter.writerow([documento, nombre, nacimiento, habilitado, id_estado, id_eps, numero_etapa, sexo,''])

    #Priorizacion
    descripcion_condprior = random.choice(priorizaciones)
    priorizacionWriter.writerow([documento, descripcion_condprior,''])

    if i <= round(300000*cantidadDatos):
        #Vacuna
        vacuna = i + 1
        aplicada = random.choice(boolean1)
        tipo = random.choice(tiposVacunas)
        dosis = 1 if tipo == 'JOHNSON' else 2
        preservacion = '-25C' if tipo == 'PFIZER' else '-14C' if tipo == 'SINOVAC' else '-11C' if tipo == 'ASTRAZENECA' else '-8C'
        llegada = fake.date_between(llegadaInicial, llegadaFinal)
        #llegada = datetime.strptime(str(llegada) + ' 00:00:00', '%Y-%m-%d %H:%M:%S')
        fechaAplicada = fake.date_between(llegada, llegadaFinal)
        vacunaWriter.writerow([vacuna, aplicada, tipo, dosis, preservacion, llegada, fechaAplicada,''])

        #Vacunacion
        id_punto = random.randint((id_eps*2)-1, id_eps*2)
        vacunacionWriter.writerow([documento, id_eps, id_punto,''])

        #Cita
        fechaHora = fake.date_time_between(llegadaInicial, llegadaFinal).strftime("%m-%d-%Y %H:%M")
        finalizada = random.choice(boolean2)
        citaWriter.writerow([fechaHora, documento, id_punto, finalizada,''])

        #Asignada
        id_eps = random.randint(1,20)
        id_punto = random.randint((id_eps*2)-1, id_eps*2)
        asignadaWriter.writerow([vacuna, id_punto, id_eps,''])

ciudadanoFile.close()
vacunacionFile.close()
vacunaFile.close()
asignadaFile.close()
citaFile.close()
priorizacionFile.close()

print("Tiempo :", time.time()-timer)