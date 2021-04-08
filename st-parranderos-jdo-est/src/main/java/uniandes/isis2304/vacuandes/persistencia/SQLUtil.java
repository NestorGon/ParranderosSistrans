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
        return new Long[] {epsEliminados, rolesEliminados, estadoEliminadas, etapaEliminadas,
        		condPriorEliminados, puntoEliminados, vacunaEliminados, asignadaEliminados,
        		ciudadanoEliminados, vacunacionEliminados, priorizacionEliminados, infoUsuarioEliminados,
        		usuarioEliminados, citaEliminados};
	}
	
	//TODO Condicionales de acuerdo a los parámetros
	//TODO Parametros
	public Long darIndiceVacunacion( PersistenceManager pm ) {
		Query q = pm.newQuery( SQL, "SELECT COUNT(*) "
				+ "FROM " + pp.darTablaCiudadano() + " C," + pp.darTablaEps() + " E," + pp.darTablaEstado() + " ES,"
				+ pp.darTablaPriorizacion() + " P," + pp.darTablaCita() + " CI "
				+ "WHERE E.ID = C.IS_EPS AND "
				+ "ES.ID = C.ID_ESTADO AND "
				+ "C.DOCUMENTO = P.DOCUMENTO_CIUDADANO AND "
				+ "CI.DOCUMENTO_CIUDADANO = C.DOCUMENTO AND "
				+ "E.ID IN (?) AND "
				+ "ES.ID IN (?) AND "
				+ "P.DESCRIPCION_CONDPRIOR = ? AND"
				+ "E.REGION IN (?) AND\r\n"
				+ "(CI.FECHAHORA BETWEEN ? AND ? OR "
				+ "CI.FECHAHORA = ?);" );
        q.setResultClass( Long.class );
        Long resp = (Long) q.executeUnique();
        return resp;
	}

}
