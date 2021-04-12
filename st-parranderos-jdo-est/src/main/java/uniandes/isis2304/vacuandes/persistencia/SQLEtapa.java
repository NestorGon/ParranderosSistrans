package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Etapa;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto ETAPA de VacuAndes
 * 
 * @author Mariana Zamora 
 */
public class SQLEtapa {
	
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
	public SQLEtapa ( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una ETAPA a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param numero - Numero de la etapa de vacunacion
	 * @param descripcion - descripcion de la etapa
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarEtapa( PersistenceManager pm, Long numero, String descripcion ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaEtapa() + "(NUMERO, DESCRIPCION) values (?, ?)");
        q.setParameters(numero, descripcion);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar una ETAPA de la base de datos de VacuAndes por su numero
	 * @param pm - El manejador de persistencia
	 * @param numero - El identificador de la etapa
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarEtapa( PersistenceManager pm, Long numero )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEtapa() + " WHERE NUMERO = ?");
        q.setParameters( numero );
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para cambiar la descripcion de una etapa dado su numero 
	 * @param pm - El manejador de persistencia
	 * @param numero - el numero de la etapa a la que se le cambiará la descripcion
	 * @return El número de tuplas modificadas
	 */
	public long cambiarDescripcionEtapa(PersistenceManager pm, Long numero, String descripcion)
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaEtapa() + " SET DESCRIPCION = ? WHERE NUMERO = ?");
        q.setParameters(descripcion, numero);
        return (long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una ETAPA de la 
	 * base de datos de VacuAndes por su numero
	 * @param pm - El manejador de persistencia
	 * @param numero - Numero de la etapa
	 * @return El objeto ETAPA que tiene el numero dado
	 */
	public Etapa darEtapa( PersistenceManager pm, Long numero ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaEtapa() + " WHERE NUMERO = ?" );
		q.setResultClass( Etapa.class );
		q.setParameters( numero );
		return (Etapa) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las ETAPAS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos ETAPA
	 */
	public List<Etapa> darEtapas( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaEtapa() );
		q.setResultClass( Etapa.class );
		return (List<Etapa>) q.executeList();
	}	
}
