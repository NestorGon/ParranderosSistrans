package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.CondicionPriorizacion;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto CONDICIONPRIORIZACION de VacuAndes
 * 
 * @author Mariana Zamora 
 */
public class SQLCondicionPriorizacion {

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
	public SQLCondicionPriorizacion( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una CONDICIONPRIORIZACION a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param numero_etapa - Numero de la etapa a la que pertenece la condicion de priorizacion
	 * @param descripcion - descripcion de la condicion de priorizacion
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarCondicionPriorizacion( PersistenceManager pm, Long numero_etapa, String descripcion ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCondicionPriorizacion() + "(NUEMRO_ETAPA, DESCRIPCION) values (?, ?)");
        q.setParameters(numero_etapa, descripcion);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar una CONDICIONPRIORIZACION de la base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param descripcion - La descripción de la condicion de priorizacion
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarCondicionPriorizacion( PersistenceManager pm, String descripcion )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCondicionPriorizacion() + " WHERE DESCRIPCION = ?");
        q.setParameters( descripcion );
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una CONDICIONPRIORIZACION de la 
	 * base de datos de VacuAndes por su descripcion
	 * @param pm - El manejador de persistencia
	 * @param descripcion - descripcion de la condicion de priorizacion
	 * @return El objeto CONDICIONPRIORIZACION que tiene el numero dado
	 */
	public CondicionPriorizacion darCondicionPriorizacion( PersistenceManager pm, String descripcion ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaCondicionPriorizacion() + " WHERE DESCRIPCION = ?" );
		q.setResultClass( CondicionPriorizacion.class );
		q.setParameters( descripcion );
		return (CondicionPriorizacion) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las CONDICIONES DE PRIORIZACION de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos CONDICIONPRIORIZACION
	 */
	public List<CondicionPriorizacion> darCondicionesPriorizacion( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaCondicionPriorizacion() );
		q.setResultClass( CondicionPriorizacion.class );
		return (List<CondicionPriorizacion>) q.executeList();
	}	
}
