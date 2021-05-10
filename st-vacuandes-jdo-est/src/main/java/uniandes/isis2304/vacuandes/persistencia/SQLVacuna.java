package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Vacuna;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto VACUNA de VacuAndes
 * 
 * @author Mariana Zamora 
 */
public class SQLVacuna {
	
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
	public SQLVacuna ( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una VACUNA a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param id - Id de la vacuna
	 * @param preservacion - Condiciones de preservacion de la vacuna 
	 * @param aplicada - Estado de aplicacion de la vacuna
	 * @param dosis - Numero de dosis de la vacuna
	 * @param tipo - Tipo de la vacuna
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarVacuna( PersistenceManager pm, String id, String preservacion, String aplicada, Long dosis, String tipo, String llegada ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaVacuna() + "(ID, PRESERVACION, APLICADA, DOSIS, TIPO, LLEGADA) values (?, ?, ?, ?, ?, TO_DATE(?, 'DD-MM-YYYY HH24:MI'))");
        q.setParameters( id, preservacion, aplicada, dosis, tipo, llegada);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar una VACUNA de la base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - Id de la vacuna a eliminar
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarVacuna( PersistenceManager pm, String id )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVacuna() + " WHERE ID = ?");
        q.setParameters( id );
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para cambiar el estado de una VACUNA de F a T
	 * @param pm - El manejador de persistencia
	 * @param id - id de la vacuna
	 * @return El número de tuplas modificadas
	 */
	public long cambiarEstadoAplicacionVacunaT(PersistenceManager pm, String id)
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaVacuna() + " SET APLICADA = 'T' WHERE ID = ?");
        q.setParameters(id);
        return (long) q.executeUnique();
	}	
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una VACUNA de la 
	 * base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - Id del punto de vacunacion
	 * @return El objeto VACUNA que tiene el identificador dado
	 */
	public Vacuna darVacuna( PersistenceManager pm, String id ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaVacuna() + " WHERE ID = ?" );
		q.setResultClass( Vacuna.class );
		q.setParameters( id );
		return (Vacuna) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las VACUNAS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos VACUNA
	 */
	public List<Vacuna> darVacunas( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaVacuna() );
		q.setResultClass( Vacuna.class );
		return (List<Vacuna>) q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontras todas las tecnologías de vacunas
	 * @param pm - El manejador de persistencia
	 * @return La lista con las tecnologías encontradas
	 */
	public List<String> darTecnologiasVacunas( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT UNIQUE(TIPO) FROM " + pp.darTablaVacuna());
		q.setResultClass( String.class );
		return (List<String>) q.executeList();
	}
}
