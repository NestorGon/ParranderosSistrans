package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Atencion;

public class SQLAtencion {
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
	public SQLAtencion( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un ATENCION a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param descripcion - La descripcion de la condicion de priorizacion
	 * @param punto - El identificador del punto de vacunación
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarAtencion( PersistenceManager pm, String descripcion, String punto ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaAtencion() + "(DESCRIPCION_CONDPRIOR, ID_PUNTO) values (?, ?)");
        q.setParameters(descripcion, punto);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar un ATENCION de la base de datos de VacuAndes por su documento
	 * @param pm - El manejador de persistencia
	 * @param descripcion - La descripcion de la condicion de priorizacion
	 * @param punto - El identificador del punto de vacunación
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarAtencion( PersistenceManager pm, String descripcion, String punto )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaAtencion() + " WHERE DESCRIPCION_CONDPRIOR = ? AND ID_PUNTO = ?");
        q.setParameters(descripcion, punto);
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un ATENCION de la 
	 * base de datos de VacuAndes por su documento de identificación
	 * @param pm - El manejador de persistencia
	 * @param descripcion - La descripcion de la condicion de priorizacion
	 * @param punto - El identificador del punto de vacunación
	 * @return El objeto ATENCION que tiene el identificador dado
	 */
	public Atencion darAtencion( PersistenceManager pm, String descripcion, String punto ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaAtencion () + " WHERE DESCRIPCION_CONDPRIOR = ? AND ID_PUNTO = ?" );
		q.setResultClass( Atencion.class );
		q.setParameters(descripcion, punto);
		return (Atencion) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las ATENCIONES de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos ATENCION
	 */
	public List<Atencion> darAtenciones( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaAtencion() );
		q.setResultClass( Atencion.class );
		return (List<Atencion>) q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar las condiciones de priorización de un punto
	 * @param pm - El manejador de persistencia
	 * @param punto - El identificador del punto de vacunación
	 * @return Una lista de objetos String con las condiciones de priorizacion encontradas
	 */
	public List<String> darCondicionesPunto( PersistenceManager pm, String punto ) {
		Query q = pm.newQuery( SQL, "SELECT DESCRIPCION_CONDPRIOR FROM " + pp.darTablaAtencion() + " WHERE ID_PUNTO = ?" );
		q.setResultClass(String.class);
		return (List<String>) q.executeList();
	}
	
}
