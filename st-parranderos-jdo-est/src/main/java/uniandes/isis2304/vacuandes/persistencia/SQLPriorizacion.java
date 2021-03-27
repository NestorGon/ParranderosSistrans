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

import uniandes.isis2304.vacuandes.negocio.Priorizacion;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto PRIORIZACION de VacuAndes
 * 
 * @author Néstor González
 */
public class SQLPriorizacion {
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
	public SQLPriorizacion( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una condición de priorización a un ciudadano
	 * a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarPriorizacion( PersistenceManager pm, String documento, String descripcion ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPriorizacion() + "(DOCUMENTO_CIUDADANO, DESCRIPCION_CONDPRIOR) values (?, ?)");
        q.setParameters(documento, descripcion);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar una condición de priorización de un ciudadano
	 * de la base de datos de VacuAndes por su documento
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarPriorización( PersistenceManager pm, String documento, String descripcion )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPriorizacion() + " WHERE DOCUMENTO_CIUDADANO = ? AND DESCRIPCION_CONDPRIOR = ?");
        q.setParameters( documento, descripcion );
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una condición de priorización de un ciudadano de la 
	 * base de datos de VacuAndes por su documento de identificación y descripción de condición de priorización
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return El objeto PRIORIZACION que tiene el identificador dado
	 */
	public Priorizacion darPriorizacion( PersistenceManager pm, String documento, String descripcion ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaPriorizacion() + " WHERE DOCUMENTO_CIUDADANO = ? AND DESCRIPCION_CONDPRIOR = ?" );
		q.setResultClass( Priorizacion.class );
		q.setParameters( documento, descripcion );
		return (Priorizacion) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las PRIORIZACIONES de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos PRIORIZACION
	 */
	public List<Priorizacion> darPriorizaciones( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaPriorizacion() );
		q.setResultClass( Priorizacion.class );
		return (List<Priorizacion>) q.executeList();
	}
}
