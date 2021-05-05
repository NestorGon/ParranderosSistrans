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
				usuarioEliminados, citaEliminados, atencionEliminados};
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
}
