/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: VacuAndes
 * @version 1.0
 * @author Néstor González
 * Abril de 2021
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.vacuandes.persistencia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.datanucleus.store.rdbms.query.ForwardQueryResult;

import uniandes.isis2304.vacuandes.negocio.Ciudadano;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para la secuencia y limpieza de VacuAndes
 * 
 * @author Néstor González
 */
class SQLUtil
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaVacuAndes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaVacuAndes pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLUtil( PersistenciaVacuAndes pp ) {
		this.pp = pp;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public Long nextval( PersistenceManager pm ) {
		Query q = pm.newQuery( SQL, "SELECT "+ pp.darSeqVacuAndes() + ".nextval FROM DUAL" );
		q.setResultClass( Long.class );
		Long resp = (Long) q.executeUnique();
		return resp;
	}

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 14 números que indican el número de tuplas borradas en las tablas EPS, ROLES, 
	 * ESTADO, ETAPA, CONDICIONPRIORIZACION, PUNTO, VACUNA, ASIGNADA, CIUDADANO, VACUNACION,
	 * PRIORIZACION, INFOUSUARIO, USUARIO, CITA
	 */
	public Long[] limpiarVacuAndes( PersistenceManager pm )
	{
		Query qVacuAndes = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaVacuAndes() );
		Query qEps = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaEps() );
		Query qRoles = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaRoles() );
		Query qEstado = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaEstado() );
		Query qEtapa = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaEtapa() );
		Query qCondPrior = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaCondicionPriorizacion() );
		Query qPunto = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaPunto() );
		Query qVacuna = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaVacuna() );
		Query qAsignada = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaAsignada() );
		Query qCiudadano = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaCiudadano() );
		Query qVacunacion = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaVacunacion() );
		Query qPriorizacion = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaPriorizacion() );
		Query qInfoUsuario = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaInfoUsuario() );
		Query qUsuario = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaUsuario() );
		Query qCita = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaCita() );
		Query qAtencion = pm.newQuery( SQL, "DELETE FROM " + pp.darTablaAtencion() );

		Long vacuAndesEliminados = (Long) qVacuAndes.executeUnique();
		Long epsEliminados = (Long) qEps.executeUnique ();
		Long rolesEliminados = (Long) qRoles.executeUnique ();
		Long estadoEliminadas = (Long) qEstado.executeUnique ();
		Long etapaEliminadas = (Long) qEtapa.executeUnique ();
		Long condPriorEliminados = (Long) qCondPrior.executeUnique ();
		Long puntoEliminados = (Long) qPunto.executeUnique ();
		Long vacunaEliminados = (Long) qVacuna.executeUnique ();
		Long asignadaEliminados = (Long) qAsignada.executeUnique ();
		Long ciudadanoEliminados = (Long) qCiudadano.executeUnique ();
		Long vacunacionEliminados = (Long) qVacunacion.executeUnique ();
		Long priorizacionEliminados = (Long) qPriorizacion.executeUnique ();
		Long infoUsuarioEliminados = (Long) qInfoUsuario.executeUnique ();
		Long usuarioEliminados = (Long) qUsuario.executeUnique ();
		Long citaEliminados = (Long) qCita.executeUnique ();
		Long atencionEliminados = (Long) qAtencion.executeUnique ();
		return new Long[] {epsEliminados, rolesEliminados, estadoEliminadas, etapaEliminadas,
				condPriorEliminados, puntoEliminados, vacunaEliminados, asignadaEliminados,
				ciudadanoEliminados, vacunacionEliminados, priorizacionEliminados, infoUsuarioEliminados,
				usuarioEliminados, citaEliminados, atencionEliminados, vacuAndesEliminados};
	}

	/**
	 * Crea y ejecuta una sentencia SQL variable para encontrar el índice de vacunación de un grupo poblacional
	 * @param pm - El manejador de persistencia
	 * @param eps - Lista con los id de las eps de interés
	 * @param estado - Id del estado de interés
	 * @param priorizacion - Descripción de la condición de priorización de interés
	 * @param regiones - Lista con los nombres de las regiones de interés
	 * @param fechaInicio - Fecha y hora de inicio de interés
	 * @param fechaFin - Fecha y hora de fin de interés
	 * @return El índice de vacunación para el grupo poblacional filtrado con los parámetros
	 */
	public Double darIndiceVacunacion( PersistenceManager pm, List<String> eps, Long estado, String priorizacion, List<String> regiones, String fechaInicio, String fechaFin ) {
		Map parametros = new HashMap<>();
		String qEps = "AND E.ID IN ";
		String qEstado = "AND ES.ID = ";
		String qPrior = "AND P.DESCRIPCION_CONDPRIOR = ";
		String qRegion = "AND E.REGION IN ";
		String qFecha = "AND (CI.FECHAHORA BETWEEN ";

		if ( eps != null ) {
			String strOut = "";
			for(int i=0; i< eps.size(); i++){
				strOut += "'"+eps.get(i)+"'";
				if ( i < eps.size()-1) {
					strOut += ",";
				}
			}
			qEps += "(" + strOut + ") ";
		}

		if ( regiones != null ) {
			String strOut = "";
			for(int i=0; i< regiones.size(); i++){
				strOut += "'"+regiones.get(i)+"'";
				if ( i < regiones.size()-1) {
					strOut += ",";
				}
			}
			qRegion += "(" + strOut + ") ";
		}

		if ( estado != null ) {
			qEstado += "'" + estado + "' ";
		}
		if ( priorizacion != null ) {
			qPrior += "'" + priorizacion + "' ";
		}
		if ( fechaInicio != null ) {
			fechaInicio += " 00:00";
			qFecha += "TO_DATE('" + fechaInicio + "', 'DD-MM-YYYY HH24:MI')";
			if ( fechaFin != null) {
				fechaFin += " 23:59";
				qFecha += " AND TO_DATE('" + fechaFin + "', 'DD-MM-YYYY HH24:MI'))";
			} else {
				fechaFin = fechaInicio.split(" ")[0] + " 23:59";
				qFecha += " AND TO_DATE('" + fechaFin + "', 'DD-MM-YYYY HH24:MI'))";
			}
		}
		
		Query q = pm.newQuery( SQL, "CREATE TABLE CANTIDAD AS ( "
				+ "SELECT ES.DESCRIPCION AS DESCRIPCION, COUNT(*) AS CANT "
				+ "FROM " + pp.darTablaCiudadano() + " C," + pp.darTablaEps() + " E," + pp.darTablaEstado() + " ES,"
				+ pp.darTablaPriorizacion() + " P," + pp.darTablaCita() + " CI "
				+ "WHERE E.ID = C.ID_EPS AND "
				+ "ES.ID = C.ID_ESTADO AND "
				+ "C.DOCUMENTO = P.DOCUMENTO_CIUDADANO AND "
				+ "CI.DOCUMENTO_CIUDADANO = C.DOCUMENTO AND "
				+ "C.HABILITADO = 'T' "
				+ (eps != null ? qEps: "")
				+ (estado != null ? qEstado: "")
				+ (priorizacion != null ? qPrior: "")
				+ (regiones != null ? qRegion: "")
				+ (fechaInicio != null ? qFecha: "")
				+ " GROUP BY ES.DESCRIPCION) ");
		q.setNamedParameters( parametros );
		q.execute();
		
		q = pm.newQuery( SQL,"SELECT CANT/(SELECT SUM(CANT) "
				+ "FROM CANTIDAD) AS INDICE "
				+ "FROM CANTIDAD "
				+ "WHERE DESCRIPCION LIKE 'VACUNADO%'");
		
        q.setResultClass( Double.class );
        Double resp = (Double) q.executeUnique();
        
        q = pm.newQuery( SQL, "DROP TABLE \"CANTIDAD\"");
        q.execute();
        return resp;
	}
	
	/**
	 * Crea y ejecuta la sentecia SQL que encuentra los ciudadanos correspondientes a los cohortes especificados
	 * @param pm - El manejador de persistencia
	 * @param edad - La edad o rango de edades
	 * @param sexo - La lista con los sexos
	 * @param condiciones - La lista con las condiciones de priorización
	 * @param region - La lista con las regiones
	 * @param eps - La lista con las EPS
	 * @param punto - La lista con los puntos de vacunación
	 * @param dosis - La lista con las dosis
	 * @param tecnologiaVac - La lista con las tecnologías de las vacunas
	 * @return Una lista con objetos de tipo ciudadano que pertenecen al cohorte
	 */
	public List<Ciudadano> analizarCohortes( PersistenceManager pm, String edad, List<String> sexo, List<String> condiciones, List<String> region, List<String> eps, List<String> punto, List<String> dosis, List<String> tecnologiaVac )
	{
		if ( edad != null ) {
			if ( edad.contains("-") ) {
				String[] split = edad.split("-");
				edad = "BETWEEN " + split[0] + " AND " + split[1]; 
			} else {
				edad = "= " + edad;
			}
		}
		
		String qEdad = "AND (TRUNC(MONTHS_BETWEEN(SYSDATE, NACIMIENTO)/12) " + edad + ") ";
		String qSexo = "AND SEXO IN ";
		if ( sexo != null ) {
			String strOut = "";
			for(int i=0; i< sexo.size(); i++){
				strOut += "'"+sexo.get(i)+"'";
				if ( i < sexo.size()-1) {
					strOut += ",";
				}
			}
			qSexo += "(" + strOut + ") ";
		}
		
		String qCondiciones = "AND (SELECT COUNT(*) "
				+ "FROM PRIORIZACION P "
				+ "WHERE C.DOCUMENTO = P.DOCUMENTO_CIUDADANO AND "
				+ "P.DESCRIPCION_CONDPRIOR IN ";
		if ( condiciones != null ) {
			String strOut = "";
			for(int i=0; i< condiciones.size(); i++){
				strOut += "'"+condiciones.get(i)+"'";
				if ( i < condiciones.size()-1) {
					strOut += ",";
				}
			}
			qCondiciones += "(" + strOut + ")) > 0 ";
		}
		
		String qRegion = "AND (SELECT COUNT(*) "
				+ "FROM EPS E "
				+ "WHERE C.ID_EPS = E.ID AND "
				+ "E.REGION IN ";
		if ( region != null ) {
			String strOut = "";
			for(int i=0; i< region.size(); i++){
				strOut += "'"+region.get(i)+"'";
				if ( i < region.size()-1) {
					strOut += ",";
				}
			}
			qRegion += "(" + strOut + ")) > 0 ";
		}
		
		String qEPS = "AND C.ID_EPS IN  ";
		if ( eps != null ) {
			String strOut = "";
			for(int i=0; i< eps.size(); i++){
				strOut += "'"+eps.get(i)+"'";
				if ( i < eps.size()-1) {
					strOut += ",";
				}
			}
			qEPS += "(" + strOut + ") ";
		}
		
		String qPunto = "AND (SELECT COUNT(*) "
				+ "FROM VACUNACION V "
				+ "WHERE C.DOCUMENTO = V.DOCUMENTO_CIUDADANO AND "
				+ "V.ID_PUNTO IN ";
		if ( punto != null ) {
			String strOut = "";
			for(int i=0; i< punto.size(); i++){
				strOut += "'"+punto.get(i)+"'";
				if ( i < punto.size()-1) {
					strOut += ",";
				}
			}
			qPunto += "(" + strOut + ")) > 0 ";
		}
		
		String qDosisTec = "AND (SELECT COUNT(*) "
				+ "FROM ESTADO E "
				+ "WHERE C.ID_ESTADO = E.ID ";
		if ( dosis != null ) {
			String strOut = "";
			for(int i=0; i< dosis.size(); i++){
				strOut += "E.DESCRIPCION LIKE '%"+dosis.get(i)+"%' ";
				if ( i < dosis.size()-1) {
					strOut += "OR ";
				}
			}
			qDosisTec += "AND (" + strOut + ") ";
		}
		
		if ( tecnologiaVac != null ) {
			String strOut = "";
			for(int i=0; i< tecnologiaVac.size(); i++){
				strOut += "E.DESCRIPCION LIKE '%"+tecnologiaVac.get(i)+"%' ";
				if ( i < tecnologiaVac.size()-1) {
					strOut += "OR ";
				}
			}
			qDosisTec += "AND (" + strOut + ") ";
		}
		qDosisTec += ") > 0 ";
		Query q = pm.newQuery( SQL, "SELECT * FROM CIUDADANO C "
				+ "WHERE 1=1 "
				+ (edad != null ? qEdad: "")
				+ (sexo != null ? qSexo: "")
				+ (condiciones != null ? qCondiciones: "")
				+ (region != null ? qRegion: "")
				+ (eps != null ? qEPS: "")
				+ (punto != null ? qPunto: "")
				+ (dosis != null || tecnologiaVac != null ? qDosisTec: ""));
		q.setResultClass( Ciudadano.class );
		return (List<Ciudadano>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para hallar los ciudadanos atentidos de los puntos de una region en un rango de fechas
	 * @param region - region a la que pertenecen los puntos
	 * @patram fecha1 - fecha inicial
	 * @param fecha 2 - fecha final
	 * @return lista de documentos de los ciudadanos atentidos por el punto
	 */
	public List<String> darAtendidosRegionFechas( PersistenceManager pm, String region, String fecha1, String fecha2){
		Query q= pm.newQuery( SQL, "SELECT CITA.DOCUMENTO_CIUDADANO "
				+ "FROM (CITA JOIN CIUDADANO ON CITA.DOCUMENTO_CIUDADANO = CIUDADANO.DOCUMENTO)"
				+ "JOIN PUNTO ON CITA.ID_PUNTO = PUNTO.ID "
				+ "WHERE CITA.FECHAHORA BETWEEN TO_DATE( ?, 'DD-MM-YYYY HH24:MI') AND TO_DATE( ? , 'DD-MM-YYYY HH24:MI')"
				+ "AND PUNTO.REGION = ? "
				+ "AND CITA.FINALIZADA = 'T'");
		q.setParameters(fecha1, fecha2, region);
		q.setResultClass( String.class );
		return (List<String>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para hallar los ciudadanos atentidos de los puntos de una region en un rango de horas
	 * @param region - region a la que pertenecen los puntos
	 * @patram hora1 - hora inicial
	 * @param hora2 - hora final
	 * @param min1 - minuto inicial
	 * @param min2 - minuto final
	 * @return lista de documentos de los ciudadanos atentidos por el punto
	 */
	public List<String> darAtendidosRegionHoras( PersistenceManager pm, String region, Long hora1, Long hora2, Long min1, Long min2){
		Query q= pm.newQuery( SQL, "SELECT CITA.DOCUMENTO_CIUDADANO "
				+ "FROM (CITA JOIN CIUDADANO ON CITA.DOCUMENTO_CIUDADANO = CIUDADANO.DOCUMENTO)"
				+ "JOIN PUNTO ON CITA.ID_PUNTO = PUNTO.ID "
				+ "WHERE (EXTRACT(HOUR FROM CAST(CITA.FECHAHORA AS TIMESTAMP)) BETWEEN ? AND ?)"
				+ "AND (EXTRACT(MINUTE FROM CAST(CITA.FECHAHORA AS TIMESTAMP)) BETWEEN ? AND ?)"
				+ "AND PUNTO.REGION = ? "
				+ "AND CITA.FINALIZADA = 'T'");
		q.setParameters(hora1, hora2, min1, min2, region);
		q.setResultClass( String.class );
		return (List<String>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para hallar los ciudadanos atentidos en un punto en un rango de fechas
	 * @param id - id del punto de vacunacion
	 * @patram fecha1 - fecha inicial
	 * @param fecha 2 - fecha final
	 * @return lista de documentos de los ciudadanos atentidos por el punto
	 */
	public List<String> darAtendidosPuntoFechas( PersistenceManager pm, String id, String fecha1, String fecha2){
		Query q= pm.newQuery( SQL, "SELECT CITA.DOCUMENTO_CIUDADANO "
				+ "FROM (CITA JOIN CIUDADANO ON CITA.DOCUMENTO_CIUDADANO = CIUDADANO.DOCUMENTO)"
				+ "JOIN PUNTO ON CITA.ID_PUNTO = PUNTO.ID "
				+ "WHERE CITA.FECHAHORA BETWEEN TO_DATE( ?, 'DD-MM-YYYY HH24:MI') AND TO_DATE( ? , 'DD-MM-YYYY HH24:MI')"
				+ "AND PUNTO.ID = ? "
				+ "AND CITA.FINALIZADA = 'T'");
		q.setParameters(fecha1, fecha2, id);
		q.setResultClass( String.class );
		return (List<String>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para hallar los ciudadanos atentidos en un punto en un rango de horas
	 * @param id - id del punto de vacunacion
	 * @patram hora1 - hora inicial
	 * @param hora2 - hora final
	 * @param min1 - minuto inicial
	 * @param min2 - minuto final
	 * @return lista de documentos de los ciudadanos atentidos por el punto
	 */
	public List<String> darAtendidosPuntoHoras( PersistenceManager pm, String id, Long hora1, Long hora2, Long min1, Long min2){
		Query q= pm.newQuery( SQL, "SELECT CITA.DOCUMENTO_CIUDADANO "
				+ "FROM (CITA JOIN CIUDADANO ON CITA.DOCUMENTO_CIUDADANO = CIUDADANO.DOCUMENTO)"
				+ "JOIN PUNTO ON CITA.ID_PUNTO = PUNTO.ID "
				+ "WHERE (EXTRACT(HOUR FROM CAST(CITA.FECHAHORA AS TIMESTAMP)) BETWEEN ? AND ?)"
				+ "AND (EXTRACT(MINUTE FROM CAST(CITA.FECHAHORA AS TIMESTAMP)) BETWEEN ? AND ?)"
				+ "AND PUNTO.ID = ? "
				+ "AND CITA.FINALIZADA = 'T'");
		q.setParameters(hora1, hora2, min1, min2, id);
		q.setResultClass( String.class );
		return (List<String>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para hallar los 20 puntos de vacunación más efectivos en un rango de fechas
	 * @param fecha 1 - fecha inicial
	 * @param fecha 2 - fecha final
	 * @return lista de id de los 20 puntos más efectivos
	 */
	public List<String> darPuntosEfectivosFechas( PersistenceManager pm, String fecha1, String fecha2)
	{
		Query q= pm.newQuery( SQL, "CREATE TABLE INFO (ID, DESCRIP, APLIC) AS "
				+ "("
				+ "SELECT PUNTO.ID, ESTADO.DESCRIPCION, COUNT(*) AS APLICADAS "
				+ "FROM ((PUNTO JOIN CITA ON PUNTO.ID = CITA.ID_PUNTO) "
				+ "JOIN CIUDADANO ON CIUDADANO.DOCUMENTO = CITA.DOCUMENTO_CIUDADANO) "
				+ "JOIN ESTADO ON CIUDADANO.ID_ESTADO = ESTADO.ID "
				+ "WHERE CITA.FECHAHORA BETWEEN TO_DATE( '"+fecha1+"' , 'DD-MM-YYYY HH24:MI') AND TO_DATE( '"+fecha2+"' , 'DD-MM-YYYY HH24:MI') "
				+ "GROUP BY ESTADO.DESCRIPCION, PUNTO.ID HAVING ESTADO.DESCRIPCION LIKE '%VACUNA%') "
				+ "ORDER BY APLICADAS DESC");
		q.execute();

		Query q1 = pm.newQuery(SQL, "SELECT INFO.ID "
				+ "FROM INFO "
				+ "FETCH FIRST 20 ROWS ONLY");
		q.setResultClass( String.class );
		List<String> lista = (List<String>) q1.executeList();

		Query q2 = pm.newQuery( SQL, "DROP TABLE \"INFO\" CASCADE CONSTRAINTS");
		q2.execute();

		return lista;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para hallar los 20 puntos de vacunación más efectivos en un rango de horas
	 * @param hora1 - hora inicial
	 * @param hora2 - hora final
	 * @param min1 - minuto inicial
	 * @param min2 - minuto finak¿l
	 * @return lista de id de los 20 puntos más efectivos
	 */
	public List<String> darPuntosEfectivosHoras( PersistenceManager pm, Long hora1, Long hora2, Long min1, Long min2)
	{
		Query q= pm.newQuery( SQL, "CREATE TABLE INFO (ID, DESCRIP, APLIC) AS ( "
				+ "SELECT PUNTO.ID, ESTADO.DESCRIPCION, COUNT(*) AS APLICADAS "
				+ "FROM ((PUNTO JOIN CITA ON PUNTO.ID = CITA.ID_PUNTO) "
				+ "JOIN CIUDADANO ON CIUDADANO.DOCUMENTO = CITA.DOCUMENTO_CIUDADANO) "
				+ "JOIN ESTADO ON CIUDADANO.ID_ESTADO = ESTADO.ID "
				+ "WHERE (EXTRACT(HOUR FROM CAST(CITA.FECHAHORA AS TIMESTAMP)) BETWEEN "+hora1+" AND "+hora2+") "
				+ "AND EXTRACT(MINUTE FROM CAST(CITA.FECHAHORA AS TIMESTAMP)) BETWEEN "+min1+" AND "+min2+" "
				+ "GROUP BY ESTADO.DESCRIPCION, PUNTO.ID HAVING ESTADO.DESCRIPCION LIKE '%VACUNA%') "
				+ "ORDER BY APLICADAS DESC");
		q.execute();
		
		Query q1 = pm.newQuery(SQL, "SELECT INFO.ID "
				+ "FROM INFO "
				+ "FETCH FIRST 20 ROWS ONLY");
		q.setResultClass( String.class );
		List<String> lista = (List<String>) q1.execute();

		Query q2 = pm.newQuery( SQL, "DROP TABLE \"INFO\" CASCADE CONSTRAINTS");
		q2.execute();

		return lista;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para hallar los ciudadanos que estuvieron en contacto con un ciudadano en un punto de vacunación
	 * @param pm- El manejador de persistencia
	 * @param fechahora - Fecha y hora en la que se buscan los ciudadanos
	 * @return lista con los documentos de los ciudadanos encontrados
	 */
	public List<String> ciudadanosContacto( PersistenceManager pm, String fecha1, String fecha2, String id_punto, String fechahora)
	{
		Query q1= pm.newQuery(SQL, "CREATE TABLE INFO(FECHAHORA, DOCUMENTO) AS"
                 + " (SELECT CITA.FECHAHORA, CIUDADANO.DOCUMENTO"
                 + " FROM (CITA JOIN CIUDADANO ON CITA.DOCUMENTO_CIUDADANO = CIUDADANO.DOCUMENTO)"
                 + " JOIN PUNTO ON CITA.ID_PUNTO = PUNTO.ID"
                 + " WHERE CITA.FECHAHORA > TO_DATE('"+fecha1+"', 'DD-MM-YYYY HH24:MI') AND"
                 + " CITA.FECHAHORA < TO_DATE('"+fecha2+"', 'DD-MM-YYYY HH24:MI')"
                 + " AND PUNTO.ID = '"+id_punto+"')");
		q1.execute();
		
		Query q = pm.newQuery(SQL, "SELECT DOCUMENTO"
                  + " FROM INFO"
                  + " WHERE FECHAHORA = TO_DATE('"+fechahora+"', 'DD-MM-YYYY HH24:MI')");
		q.setResultClass(String.class);
		List<String> ciudadanos = (List<String>) q.execute();
		
		Query q2 = pm.newQuery( SQL, "DROP TABLE \"INFO\" CASCADE CONSTRAINTS");
		q2.execute();
		
		return ciudadanos;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para hallar los puntos más concurridos en un tiempo específicado
	 * @param tiempo - tiempo
	 * @param pm- Manejador de persistencia
	 * @return Lista con las cantidades de vacunas por tiempos
	 */
	public List<Long> analizarOperacionCantidad( PersistenceManager pm, String tiempo, String id)
	{
		Query q1 = pm.newQuery(SQL, "CREATE TABLE INFO (CANTIDAD, TIEMPO) AS"
                   + " (SELECT COUNT(*) AS CANTIDAD, TO_CHAR(CITA.FECHAHORA, '"+tiempo+"') AS TIEMPO"
                   + " FROM CITA"
                   + " WHERE CITA.ID_PUNTO = '"+id+"'"
                   + " GROUP BY TO_CHAR(CITA.FECHAHORA, '"+tiempo+"'))");
		q1.execute();
		
		Query q= pm.newQuery(SQL, "SELECT CANTIDAD"
                 + " FROM INFO");
		q.setResultClass(Long.class);
		List<Long> cantidades = (List<Long>) q.executeList();
		
		Query q2 = pm.newQuery( SQL, "DROP TABLE \"INFO\" CASCADE CONSTRAINTS");
		q2.execute();
		
		return cantidades;
	}
	
	/**
	 * Crea y ejecuta sentencia que haya el tiempo dado una cantidad específica
	 * @param pm - Manejador de persistencia
	 * @return String fecha con la cantidad dada
	 */
	public String analizarOperacionTiempoCantidad( PersistenceManager pm, Long cantidad, String tiempo, String id )
	{
		Query q1 = pm.newQuery(SQL, "CREATE TABLE INFO (CANTIDAD, TIEMPO) AS"
                + " (SELECT COUNT(*) AS CANTIDAD, TO_CHAR(CITA.FECHAHORA, '"+tiempo+"') AS TIEMPO"
                + " FROM CITA"
                + " WHERE CITA.ID_PUNTO = '"+id+"'"
                + " GROUP BY TO_CHAR(CITA.FECHAHORA, '"+tiempo+"'))");
		q1.execute();
		
		Query q= pm.newQuery(SQL, "SELECT TIEMPO"
              + " FROM INFO"
			  + " WHERE CANTIDAD = "+ cantidad);
		ForwardQueryResult<String> tiempoS =  (ForwardQueryResult<String>) q.execute();
		
		Query q2 = pm.newQuery( SQL, "DROP TABLE \"INFO\" CASCADE CONSTRAINTS");
		q2.execute();
		
		String tiempoSS = tiempoS.toString();
		
		return tiempoSS;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para hallar los tiempos y la cantidad de ciudadanos en el punto en ese tiempo
	 * @param tiempo - tiempo
	 * @param pm - Manejador de persistencia
	 * @return lista de afluencia por fechas
	 */
	public String analizarOperacionTiempo( PersistenceManager pm, String tiempo, String id)
	{
		
		Query q1 = pm.newQuery(SQL, "CREATE TABLE INFO (CANTIDAD, TIEMPO) AS"
                + " (SELECT COUNT(*) AS CANTIDAD, TO_CHAR(CITA.FECHAHORA, '"+tiempo+"') AS TIEMPO"
                + " FROM CITA"
                + " WHERE CITA.ID_PUNTO = '"+id+"'"
                + " GROUP BY TO_CHAR(CITA.FECHAHORA, '"+tiempo+"'))");
		q1.execute();
		
		Query q= pm.newQuery(SQL, "SELECT TIEMPO"
              + " FROM INFO"
			  + " ORDER BY CANTIDAD DESC"
              + " FETCH FIRST 1 ROWS ONLY");
		ForwardQueryResult<String> tiempoS =  (ForwardQueryResult<String>) q.execute();
		
		Query q2 = pm.newQuery( SQL, "DROP TABLE \"INFO\" CASCADE CONSTRAINTS");
		q2.execute();
		
        String tiempoSS = tiempoS.toString();
		
		return tiempoSS;
	}
	/**
	 * Crea y ejecuta la sentencia SQL para hallar los ciudadanos que están en un punto de vacunación equivocado
	 * debido a que sus condiciones de priorizacion no corresponden a las que el punto atiende
	 * @param pm - El manejador de persistencia
	 * @param id_punto - El identificador del punto de vacunación
	 * @return lista con los documentos de los ciudadanos encontrados
	 */
	public List<String> darCiudadanosPuntoEquivocado( PersistenceManager pm, String id_punto ) {
		Query q = pm.newQuery( SQL, "SELECT C.DOCUMENTO "
				+ "FROM CIUDADANO C, VACUNACION V "
				+ "WHERE V.ID_PUNTO = ? AND "
				+ "C.DOCUMENTO = V.DOCUMENTO_CIUDADANO AND "
				+ "(SELECT COUNT(*) "
				+ "FROM PRIORIZACION P, ATENCION A "
				+ "WHERE P.DOCUMENTO_CIUDADANO = C.DOCUMENTO AND "
				+ "A.ID_PUNTO = V.ID_PUNTO AND "
				+ "A.DESCRIPCION_CONDPRIOR = P.DESCRIPCION_CONDPRIOR) = 0 ");
		q.setParameters( id_punto );
		q.setResultClass(String.class);
		return (List<String>) q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para conocer la etapa actual de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return el número que indica la etapa actual de VacuAndes
	 */
	public Long darEtapaVacuAndes( PersistenceManager pm ) {
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaVacuAndes());
		q.setResultClass( Long.class );
		return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para actualizar la etapa actual de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param numero_etapa - El número de la nueva etapa
	 * @return el número que indica la cantidad de tuplas modificadas
	 */
	public Long actualizarEtapaVacuAndes( PersistenceManager pm, Long etapa ) {
		Query q = pm.newQuery( SQL, "UPDATE " + pp.darTablaVacuAndes() + " SET NUMERO_ETAPA = ?");
		q.setParameters( etapa );
		q.setResultClass( Long.class );
		return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar la etapa actual de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param numero_etapa - El número de la etapa
	 * @return el número que indica la cantidad de tuplas insertadas
	 */
	public Long adicionarEtapaVacuAndes( PersistenceManager pm, Long etapa ) {
		Query q = pm.newQuery( SQL, "INSERT INTO " + pp.darTablaVacuAndes() + "(NUMERO_ETAPA) VALUES (?)");
		q.setParameters( etapa );
		q.setResultClass( Long.class );
		return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para conocer los ciudadanos que pertenecen a la etapa y que no tienen citas asignadas 
	 * @param pm - El manejador de persistencia
	 * @param punto - El identificador del punto de vacunación
	 * @param etapa - El identificador de la etapa
	 * @return la lista con los documentos de los ciudadanos encontrados
	 */
	public List<String> darCiudadanosSinCita( PersistenceManager pm, String punto, Long etapa) {
		Query q = pm.newQuery( SQL, "SELECT C.DOCUMENTO "
				+ "FROM CIUDADANO C, EPS E, PUNTO P "
				+ "WHERE C.ID_EPS = E.ID AND "
				+ "P.ID = ? AND "
				+ "E.ID = P.ID_EPS AND "
				+ "C.HABILITADO = 'T' AND "
				+ "C.NUMERO_ETAPA = ? AND "
				+ "(SELECT COUNT(*) "
				+ "FROM CITA CI "
				+ "WHERE CI.DOCUMENTO_CIUDADANO = C.DOCUMENTO) = 0");
		q.setParameters( punto, etapa );
		q.setResultClass( String.class );
		return (List<String>) q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para hallar los ciudadanos no vacunados en un rango de fechas dado una serie de filtros
	 * @param eps - id de la eps
	 * @param punto - id del punto
	 * @param condiprior - condicion de priorizacion
	 * @return la lista de ciudadanos encontrados
	 */
	public List<Ciudadano> darCiudadanosNoVacunados(PersistenceManager pm, String punto, String eps, String condiprior, String fecha1, String fecha2){
	
		String sqlPuntoEPS = " JOIN VACUNACION V ON C.DOCUMENTO = V.DOCUMENTO_CIUDADANO";
		String sqlPriorizacion = " JOIN PRIORIZACION P ON C.DOCUMENTO = P.DOCUMENTO_CIUDADANO";
		String condicionPunto = " AND V.ID_PUNTO = '"+punto+"'";
		String condicionEPS = " AND V.ID_EPS = '"+eps+"'";
		String condicionCondiPrior = " AND P.DESCRIPCION_CONDPRIOR= "+condiprior;
		
		String sql1 = "SELECT C.DOCUMENTO, C.NOMBRE, C.NACIMIENTO, C.HABILITADO, C.ID_ESTADO, C.ID_EPS, C.NUMERO_ETAPA, C.SEXO "
		             +" FROM (CIUDADANO C LEFT OUTER JOIN CITA CI ON C.DOCUMENTO = CI.DOCUMENTO_CIUDADANO)";
		String sql2 = " WHERE ((CI.FINALIZADA = 'C' OR CI.FINALIZADA IS NULL) AND (CI.FECHAHORA NOT BETWEEN TO_DATE('"+fecha1+"' , 'DD-MM-YYYY HH24:MI') AND TO_DATE('"+fecha2+"', 'DD-MM-YYYY HH24:MI')))";
		
		if(punto!= null || eps != null)
		{
			sql1+= sqlPuntoEPS;
			
			if(punto !=null)
			{
				sql2+= condicionPunto;
			}
			
			if(eps != null)
			{
				sql2+= condicionEPS;
			}
		}
		
		if(condiprior!= null)
		{
			sql1+= sqlPriorizacion;
			sql2+=condicionCondiPrior;
		}
		
		Query q = pm.newQuery( SQL, sql1+sql2);
		q.setResultClass( Ciudadano.class );
		return (List<Ciudadano>) q.executeList();		
	}
	
}
