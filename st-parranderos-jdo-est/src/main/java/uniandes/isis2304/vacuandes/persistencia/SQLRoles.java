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

import uniandes.isis2304.vacuandes.negocio.Rol;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto ROLES de VacuAndes
 * 
 * @author Néstor González
 */
public class SQLRoles {
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
	public SQLRoles( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un ROL a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador del rol
	 * @param rol - La cadena de texto con el rol
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarRol( PersistenceManager pm, Long id, String rol ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaRoles() + "(ID, ROL) values (?, ?)");
        q.setParameters(id, rol);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar un ROL de la base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador del rol
	 * @param rol - La cadena de texto con el rol
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarRol( PersistenceManager pm, Long id )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaRoles() + " WHERE ID = ?");
        q.setParameters(id);
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un ROL de la 
	 * base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador del rol
	 * @return El objeto ROL que tiene el identificador dado
	 */
	public Rol darRolPorId( PersistenceManager pm, String documento ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaRoles () + " WHERE ID = ?" );
		q.setResultClass( Rol.class );
		q.setParameters(documento);
		return (Rol) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los ROLES de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos ROL
	 */
	public List<Rol> darRoles( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaRoles() );
		q.setResultClass( Rol.class );
		return (List<Rol>) q.executeList();
	}
}
