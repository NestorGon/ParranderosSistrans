package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Usuario;

public class SQLUsuario {
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
	public SQLUsuario( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un USUARIO a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param login - El login del usuario
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarUsuario( PersistenceManager pm, String documento, String login ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaUsuario() + "(DOCUMENTO_CIUDADANO, LOGIN_INFOUSUARIO) values (?, ?)");
        q.setParameters(documento, login);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar un USUARIO de la base de datos de VacuAndes por su documento
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarUsuario( PersistenceManager pm, String documento )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaUsuario() + " WHERE DOCUMENTO_CIUDADANO = ?");
        q.setParameters(documento);
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un USUARIO de la 
	 * base de datos de VacuAndes por su documento de identificación
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @return El objeto USUARIO que tiene el identificador dado
	 */
	public Usuario darUsuario( PersistenceManager pm, String documento ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaUsuario () + " WHERE DOCUMENTO_CIUDADANO = ?" );
		q.setResultClass( Usuario.class );
		q.setParameters(documento);
		return (Usuario) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los USUARIOS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos USUARIO
	 */
	public List<Usuario> darUsuarios( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaUsuario() );
		q.setResultClass( Usuario.class );
		return (List<Usuario>) q.executeList();
	}
}
