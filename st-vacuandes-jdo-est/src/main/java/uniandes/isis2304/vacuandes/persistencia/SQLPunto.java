package uniandes.isis2304.vacuandes.persistencia;

import java.math.BigDecimal;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Punto;

/**
 * Clase que encapsula los métodos que acceden a la base de datos para el concepto PUNTO de VacuAndes
 * 
 * @author Mariana Zamora 
 */
public class SQLPunto {

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
	public SQLPunto ( PersistenciaVacuAndes pp )
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un PUNTO a la base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @param id - Id del punto de vacunacion
	 * @param region - Region del punto de vacunacion
	 * @param direccion - Direccion del putno de vacunacion
	 * @param aplicadas - Numero de vacunas aplicadas en el punto de vacunacion
	 * @param capacidad -Capacidad del punto de vacunacion
	 * @param id_eps - Id de la eps a la que pertenece el punto de vacunacion
	 * @return El número de tuplas insertadas
	 */
	public Long adicionarPunto( PersistenceManager pm, String id, String region, String direccion, Long aplicadas, Long capacidad, String id_eps, Long capacidadVacunas, Long vacunas, String habilitado ) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPunto() + "(ID, REGION, DIRECCION, APLICADAS, CAPACIDAD, ID_EPS, CAPACIDADVACUNAS, VACUNAS, HABILITADO) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        q.setParameters( id, region, direccion, aplicadas, capacidad, id_eps, capacidadVacunas, vacunas, habilitado );
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar un PUNTO de la base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - Id del punto a eliminar
	 * @return EL número de tuplas eliminadas
	 */
	public Long eliminarPunto( PersistenceManager pm, String id )
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPunto() + " WHERE ID = ?");
        q.setParameters( id );
        return (Long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para aumentar la cantidad de vacunas aplicadas de un punto de vacunacion dado su id
	 * @param pm - El manejador de persistencia
	 * @param id - id del punto de vacunacion al que se le aumentarán la cantidad de vacunas aplicadas
	 * @return El número de tuplas modificadas
	 */
	public long aumentarAplicadasPuntoId(PersistenceManager pm, String id)
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaPunto () + " SET APLICADAS = APLICADAS + 1, VACUNAS = VACUNAS - 1 WHERE ID = ?");
        q.setParameters(id);
        return (long) q.executeUnique();
	}	
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un PUNTO de la 
	 * base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - Id del punto de vacunacion
	 * @return El objeto PUNTO que tiene el identificador dado
	 */
	public Punto darPunto( PersistenceManager pm, String id ) 
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaPunto() + " WHERE ID = ?" );
		q.setResultClass( Punto.class );
		q.setParameters( id );
		return (Punto) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la capacidad de un PUNTO de la 
	 * base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - Id del punto de vacunacion
	 * @return El objeto Long de la capacidad del punto
	 */
	public Long darCapacidadPunto( PersistenceManager pm, String id ) 
	{
		Query q = pm.newQuery( SQL, "SELECT CAPACIDAD FROM " + pp.darTablaPunto() + " WHERE ID = ?" );
		q.setParameters( id );
		BigDecimal resp= (BigDecimal) q.executeUnique();
		return resp.longValue();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar las citas activas de un PUNTO de la 
	 * base de datos de VacuAndes por su id
	 * @param pm - El manejador de persistencia
	 * @param id - Id del punto de vacunacion
	 * @return El objeto Long de las citas activas del punto
	 */
	public Long darCitasActivasPunto( PersistenceManager pm, String id ) 
	{
		Query q = pm.newQuery( SQL, "SELECT COUNT(*)CANTIDAD FROM " + pp.darTablaCita() + " WHERE ID_PUNTO = ? AND FINALIZADA = 'T' GROUP BY ID_PUNTO" );
		q.setParameters( id );
		BigDecimal resp = (BigDecimal) q.executeUnique();
		if(resp == null)
			return 0L;
		return resp.longValue();
	}



	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los PUNTOS de la 
	 * base de datos de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos PUNTO
	 */
	public List<Punto> darPuntos( PersistenceManager pm )
	{
		Query q = pm.newQuery( SQL, "SELECT * FROM " + pp.darTablaPunto() );
		q.setResultClass( Punto.class );
		return (List<Punto>) q.executeList();
	}	
}
