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

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Cita;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto CITA de VacuAndes
 * 
 * @author Néstor González
 */
public class SQLCita {
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
	public SQLCita( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una CITA a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param fechaHora - Fecha y hora de la cita
	 * @param finalizada - Si la cita ya se finalizó o no
	 * @param ciudadano - El documento de identificación del ciudadano asociado a la cita
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarCita( PersistenceManager pm, Date fechaHora, String finalizada, String ciudadano ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCita() + "(FECHAHORA, FINALIZADA, DOCUMENTO_CIUDADANO) values (?, ?, ?)");
        q.setParameters( fechaHora, finalizada, ciudadano );
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar un CIUDADANO de la base de datos de VacuAndes por su documento
	 * @param pm - El manejador de persistencia
	 * @param fechaHora - Fecha y hora de la cita
	 * @param documento - El documento de identificación del ciudadano asociado a la cita
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarCitaPorFechaYDocumento( PersistenceManager pm, Date fechaHora, String documento )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCita() + " WHERE FECHAHORA = ? AND DOCUMENTO = ?");
        q.setParameters( fechaHora, documento );
        return (Long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una CITA de la 
	 * base de datos de VacuAndes por su fecha y hora y documento de identificación del ciudadano asociado
	 * @param pm - El manejador de persistencia
	 * @param documento - El documento de identificación del ciudadano
	 * @return El objeto CITA que tiene el identificador dado
	 */
	public Cita darCitaPorFechaYDocumento( PersistenceManager pm, Date fechaHora, String documento ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaCiudadano () + " WHERE FECHAHORA = ? AND DOCUMENTO = ?" );
		q.setResultClass( Cita.class );
		q.setParameters( fechaHora, documento );
		return (Cita) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de las CITAS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos CITA
	 */
	public List<Cita> darCitas( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaCita() );
		q.setResultClass( Cita.class );
		return (List<Cita>) q.executeList();
	}

}
