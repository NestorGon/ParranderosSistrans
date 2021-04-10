package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Asignada;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto ASIGNADA de VacuAndes
 * 
 * @author Mariana Zamora 
 */
public class SQLAsignada {
	
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
	public SQLAsignada( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una vacuna ASIGNADA a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param id_eps - Id de la eps a la que pertenece la vacuna
	 * @param id_punto - Id del punto al que pertenece la vacuna 
	 * @param id_vacuna - Id de la vacuna asignada
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarAsignada( PersistenceManager pm, String id_eps, String id_punto, String id_vacuna ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaAsignada() + "(ID_EPS, IS_PUNTO, ID_VACUNA) values (?, ?, ?)");
        q.setParameters(id_eps, id_punto, id_vacuna);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar una vacuna ASIGNADA de la base de datos de VacuAndes por su numero
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador de la vacuna asignada y el punto de vacunacion
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarAsignada( PersistenceManager pm, String id_vacuna, String id_punto )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAsignada() + " WHERE ID_PUNTO = ? AND ID_VACUNA = ?");
        q.setParameters( id_punto, id_vacuna );
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una vacuna ASIGNADA de la 
	 * base de datos de VacuAndes por su id_vacuna y id_punto
	 * @param pm - El manejador de persistencia
	 * @param id_vacuna - id de la vacuna asignada
	 * @param id_punto - id del punto de vacunacion al que se asigna la vacuna
	 * @return El objeto ASIGNADA que tiene el id de vacuna y de punto dado
	 */
	public Asignada darAsignada( PersistenceManager pm, String id_punto, String id_vacuna ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaAsignada() + " WHERE ID_PUNTO = ? AND ID_VACUNA = ?" );
		q.setResultClass( Asignada.class );
		q.setParameters( id_punto, id_vacuna );
		return (Asignada) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las vacunas ASIGNADAS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos ASIGNADA
	 */
	public List<Asignada> darAsignadas( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaAsignada() );
		q.setResultClass( Asignada.class );
		return (List<Asignada>) q.executeList();
	}	
}
