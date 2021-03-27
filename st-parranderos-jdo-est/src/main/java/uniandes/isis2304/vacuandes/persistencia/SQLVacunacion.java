package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Vacunacion;

public class SQLVacunacion {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
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
	public SQLVacunacion( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una VACUNACION a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @param punto - El identificador del punto de vacunación
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarVacunacion( PersistenceManager pm, String documento, String eps, String punto ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaVacunacion() + "(DOCUMENTO_CIUDADANO, ID_EPS, ID_PUNTO) values (?, ?, ?)");
        q.setParameters(documento, eps, punto);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar una VACUNACION de la base de datos de VacuAndes por su documento y eps
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarVacunacion( PersistenceManager pm, String documento, String eps )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVacunacion() + " WHERE DOCUMENTO_CIUDADNAO = ? AND ID_EPS = ?");
        q.setParameters(documento, eps);
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una VACUNACION de la 
	 * base de datos de VacuAndes por su documento de identificación
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @return El objeto VACUNACION que tiene el identificador dado
	 */
	public Vacunacion darVacunacion( PersistenceManager pm, String documento, String eps ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaVacunacion () + " WHERE DOCUMENTO_CIUDADANO = ? AND ID_EPS = ?" );
		q.setResultClass( Vacunacion.class );
		q.setParameters(documento);
		return (Vacunacion) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las VACUNACIONES de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos VACUNACION
	 */
	public List<Vacunacion> darVacunaciones( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaVacunacion() );
		q.setResultClass( Vacunacion.class );
		return (List<Vacunacion>) q.executeList();
	}
}
