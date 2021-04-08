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

import uniandes.isis2304.vacuandes.negocio.InfoUsuario;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto INFOUSUARIO de VacuAndes
 * 
 * @author Néstor González
 */
public class SQLInfoUsuario {
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
	public SQLInfoUsuario( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar la información de un usuario a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param login - El login del usuario
	 * @param trabajo - El trabajo del usuario
	 * @param roles - El rol del usuario en la aplicación
	 * @param punto - El punto de vacunación al que se encuentra asociado
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarInfoUsuario( PersistenceManager pm, String login, String trabajo, Long roles, String punto ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaInfoUsuario() + "(LOGIN, TRABAJO, ID_ROLES, ID_PUNTO) values (?, ?, ?, ?)");
        q.setParameters(login, trabajo, roles, punto);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar la información de un usuario de la base de datos de VacuAndes por su login
	 * @param pm - El manejador de persistencia
	 * @param login - El login único de un usuario
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarInfoUsuario( PersistenceManager pm, String login )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaInfoUsuario() + " WHERE LOGIN = ?");
        q.setParameters( login );
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un usuario de la 
	 * base de datos de VacuAndes por su login
	 * @param pm - El manejador de persistencia
	 * @param login - El login único de un usuario
	 * @return El objeto INFOUSUARIO que tiene el identificador dado
	 */
	public InfoUsuario darInfoUsuario( PersistenceManager pm, String login ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaInfoUsuario() + " WHERE DOCUMENTO = ?" );
		q.setResultClass( InfoUsuario.class );
		q.setParameters(login);
		return (InfoUsuario) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los usuarios de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos INFOUSUARIO
	 */
	public List<InfoUsuario> darInfoUsuarios( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaInfoUsuario() );
		q.setResultClass( InfoUsuario.class );
		return (List<InfoUsuario>) q.executeList();
	}
}
