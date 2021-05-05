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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Ciudadano;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto CIUDADANO de VacuAndes
 * 
 * @author Néstor González
 */
public class SQLCiudadano {
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
	public SQLCiudadano( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un CIUDADANO a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param nombre - El nombre del ciudadano
	 * @param nacimiento - La fecha de nacimiento del ciudadano
	 * @param habilitado - Si el ciudadano está o no habilitado para vacunación
	 * @param estado - El id del estado en el que se encuentra el ciudadano en la vacunación
	 * @param eps - El id de la EPS a la que el ciuadano se encuentra afiliado
	 * @param etapa - El número de la etapa a la que el ciudadano pertenece
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarCiudadano( PersistenceManager pm, String documento, String nombre, Date nacimiento, String habilitado, Long estado, String eps, Integer etapa, String sexo) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCiudadano() + "(DOCUMENTO, NOMBRE, NACIMIENTO, HABILITADO, ID_ESTADO, ID_EPS, NUMERO_ETAPA, SEXO) values (?, ?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(documento, nombre, nacimiento, habilitado, estado, eps, etapa, sexo);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un CIUDADANO a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @param nombre - El nombre del ciudadano
	 * @param nacimiento - La fecha de nacimiento del ciudadano
	 * @param habilitado - Si el ciudadano está o no habilitado para vacunación
	 * @param estado - El id del estado en el que se encuentra el ciudadano en la vacunación
	 * @param eps - El id de la EPS a la que el ciuadano se encuentra afiliado
	 * @param etapa - El número de la etapa a la que el ciudadano pertenece
	 * @return El número de tuplas actualizadas
	 */
	public Long actualizarCiudadano( PersistenceManager pm, String documento, String nombre, Date nacimiento, String habilitado, Long estado, String eps, Integer etapa, String sexo) 
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCiudadano() + " SET NOMBRE = ?, NACIMIENTO = ?, HABILITADO = ?, ID_ESTADO = ?, ID_EPS = ?, NUMERO_ETAPA = ?, SEXO = ? WHERE DOCUMENTO = ?");
        q.setParameters(nombre, nacimiento, habilitado, estado, eps, etapa, documento, sexo);
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar un CIUDADANO de la base de datos de VacuAndes por su documento
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarCiudadano( PersistenceManager pm, String documento )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCiudadano() + " WHERE DOCUMENTO = ?");
        q.setParameters(documento);
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un CIUDADANO de la 
	 * base de datos de VacuAndes por su documento de identificación
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @return El objeto CIUDADANO que tiene el identificador dado
	 */
	public Ciudadano darCiudadano( PersistenceManager pm, String documento ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaCiudadano () + " WHERE DOCUMENTO = ?" );
		q.setResultClass( Ciudadano.class );
		q.setParameters(documento);
		List<Ciudadano> resultado = (List<Ciudadano>) q.executeList();
		return resultado != null && !resultado.isEmpty() ? resultado.get(0):null;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar el número de etapa de un CIUDADANO de la
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param documento - Documento del ciudadano
	 * @return El objeto Long
	 */
	public Long darEtapaCiudadano( PersistenceManager pm, String documento ) 
	{
		Query q = pm.newQuery( SQL, "SELECT NUMERO_ETAPA FROM " + pp.darTablaCiudadano() + " WHERE DOCUMENTO = ?" );
		q.setParameters( documento );
		BigDecimal resp= (BigDecimal) q.executeUnique();
		return resp.longValue();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los CIUDADANOS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos CIUDADANO
	 */
	public List<Ciudadano> darCiudadanos( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaCiudadano() );
		q.setResultClass( Ciudadano.class );
		return (List<Ciudadano>) q.executeList();
	}
}
