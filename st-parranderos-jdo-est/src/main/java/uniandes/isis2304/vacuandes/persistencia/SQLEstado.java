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

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Estado;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto CIUDADANO de VacuAndes
 * 
 * @author Néstor González
 */
public class SQLEstado {
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
	public SQLEstado( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un ESTADO a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador del estado
	 * @param descripcion - La descripción del estado
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarEstado( PersistenceManager pm, Long id, String descripcion) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaEstado() + "(ID, DESCRIPCION) values (?, ?)");
        q.setParameters( id, descripcion );
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar un ESTADO de la base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador del estado
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarEstado( PersistenceManager pm, Long id )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEstado() + " WHERE ID = ?");
        q.setParameters( id );
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un ESTADO de la 
	 * base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador del estado
	 * @return El objeto ESTADO que tiene el identificador dado
	 */
	public Estado darEstado( PersistenceManager pm, Long id ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaEstado () + " WHERE ID = ?" );
		q.setResultClass( Estado.class );
		q.setParameters( id );
		return (Estado) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los ESTADOS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos ESTADO
	 */
	public List<Estado> darEstados( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaEstado() );
		q.setResultClass( Estado.class );
		return (List<Estado>) q.executeList();
	}

}
