package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.EPS;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto EPS de VacuAndes
 * 
 * @author Mariana Zamora 
 */
public class SQLEPS {

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
	public SQLEPS ( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una EPS a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param id - id de la eps
	 * @param region - region en la que se encuentra la eps
	 * @param vacunas - cantidad de vacunas con la que cuenta la eps
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarEPS( PersistenceManager pm, String id, String region, Long vacunas,  Long capacidadVacunas) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaEps() + "(ID, REGION, VACUNAS, CAPACIDADVACUNAS) values (?, ?, ?, ?)");
        q.setParameters(id, region, vacunas, capacidadVacunas);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar una EPS de la base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador de la etapa
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarEPS( PersistenceManager pm, String id )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEps() + " WHERE ID = ?");
        q.setParameters( id );
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para aumentar la cantidad de vacunas de una eps dado su id
	 * @param pm - El manejador de persistencia
	 * @param id - id de la eps a la que se le aumentará el numero de vacunas
	 * @return El número de tuplas modificadas
	 */
	public long aumentarVacunasEPSId(PersistenceManager pm, String id, Long vacunas)
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaEps() + " SET VACUNAS = VACUNAS + ? WHERE ID = ?");
        q.setParameters(vacunas, id);
        return (long) q.executeUnique();
	}	
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una EPS de la 
	 * base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - id de la eps buscada
	 * @return El objeto EPS que tiene el numero dado
	 */
	public EPS darEPS( PersistenceManager pm, String id) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaEps() + " WHERE ID = ?" );
		q.setResultClass( EPS.class );
		q.setParameters( id );
		return (EPS) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las EPS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos EPS
	 */
	public List<EPS> darEPSs( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaEps() );
		q.setResultClass( EPS.class );
		return (List<EPS>) q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar las regiones de las EPS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos EPS
	 */
	public List<String> darRegiones( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT REGION FROM " + pp.darTablaEps() + " GROUP BY REGION" );
		q.setResultClass( String.class );
		return (List<String>) q.executeList();
	}
}
