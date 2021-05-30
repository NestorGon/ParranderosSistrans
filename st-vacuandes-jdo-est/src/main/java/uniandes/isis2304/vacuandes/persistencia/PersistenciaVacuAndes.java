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


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uniandes.isis2304.vacuandes.negocio.Asignada;
import uniandes.isis2304.vacuandes.negocio.Atencion;
import uniandes.isis2304.vacuandes.negocio.Cita;
import uniandes.isis2304.vacuandes.negocio.Ciudadano;
import uniandes.isis2304.vacuandes.negocio.CondicionPriorizacion;
import uniandes.isis2304.vacuandes.negocio.EPS;
import uniandes.isis2304.vacuandes.negocio.Estado;
import uniandes.isis2304.vacuandes.negocio.Etapa;
import uniandes.isis2304.vacuandes.negocio.InfoUsuario;
import uniandes.isis2304.vacuandes.negocio.Priorizacion;
import uniandes.isis2304.vacuandes.negocio.Punto;
import uniandes.isis2304.vacuandes.negocio.Rol;
import uniandes.isis2304.vacuandes.negocio.Tupla;
import uniandes.isis2304.vacuandes.negocio.Usuario;
import uniandes.isis2304.vacuandes.negocio.VOCiudadano;
import uniandes.isis2304.vacuandes.negocio.Vacuna;
import uniandes.isis2304.vacuandes.negocio.Vacunacion;

/**
 * Clase para el manejador de persistencia de VacuAndes
 * Traduce la información entre objetos Java y tuplas de la base de datos, en ambos sentidos
 * Sigue un patrón SINGLETON (Sólo puede haber un objeto de esta clase) para comunicarse de manera correcta
 * con la base de datos
 * 
 * @author Néstor González
 */
public class PersistenciaVacuAndes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger( PersistenciaVacuAndes.class.getName() );

	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaVacuAndes instance;

	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;

	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan
	 */
	private List<String> tablas;

	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaVacuAndes
	 */
	private SQLUtil sqlUtil;

	/**
	 * Atributo para el acceso a la tabla CITA de la base de datos
	 */
	private SQLCita sqlCita;

	/**
	 * Atributo para el acceso a la tabla CIUDADANO de la base de datos
	 */
	private SQLCiudadano sqlCiudadano;

	/**
	 * Atributo para el acceso a la tabla ESTADO de la base de datos
	 */
	private SQLEstado sqlEstado;

	/**
	 * Atributo para el acceso a la tabla INFOUSUARIO de la base de datos
	 */
	private SQLInfoUsuario sqlInfoUsuario;

	/**
	 * Atributo para el acceso a la tabla PRIORIZACION de la base de datos
	 */
	private SQLPriorizacion sqlPriorizacion;

	/**
	 * Atributo para el acceso a la tabla ROLES de la base de datos
	 */
	private SQLRoles sqlRoles;

	/**
	 * Atributo para el acceso a la tabla USUARIO de la base de datos
	 */
	private SQLUsuario sqlUsuario;

	/**
	 * Atributo para el acceso a la tabla VACUNACION de la base de datos
	 */
	private SQLVacunacion sqlVacunacion;

	/**
	 * Atributo para el acceso a la tabla PUNTO de la base de datos
	 */
	private SQLPunto sqlPunto;

	/**
	 * Atributo para el acceso a la tabla VACUNA de la base de datos 
	 */
	private SQLVacuna sqlVacuna;

	/**
	 * Atributo para el acceso a la tabla ETAPA de la base de datos
	 */
	private SQLEtapa sqlEtapa;

	/**
	 * Atributo para el acceso a la tabla CONDICIONPRIORIZACION
	 */
	private SQLCondicionPriorizacion sqlCondicionPriorizacion;

	/**
	 * Atributo para el acceso a la tabla EPS
	 */
	private SQLEPS sqlEps;

	/**
	 *Atributo para el acceso a la tabla ASIGNADA
	 */
	private SQLAsignada sqlAsignada;

	/**
	 * Atributo para el acceso a la tabla ATENCION
	 */
	private SQLAtencion sqlAtencion;

	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaVacuAndes ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");		
		crearClasesSQL ();

		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("VACUANDES_SEQUENCE");
		tablas.add ("EPS");
		tablas.add ("ROLES");
		tablas.add ("ESTADO");
		tablas.add ("ETAPA");
		tablas.add ("CONDICIONPRIORIZACION");
		tablas.add ("PUNTO");
		tablas.add ("VACUNA");
		tablas.add ("ASIGNADA");
		tablas.add ("CIUDADANO");
		tablas.add ("VACUNACION");
		tablas.add ("PRIORIZACION");
		tablas.add ("INFOUSUARIO");
		tablas.add ("USUARIO");
		tablas.add ("CITA");
		tablas.add ("ATENCION");
		tablas.add ("VACUANDES");
	}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrón SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaVacuAndes (JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas (tableConfig);

		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaVacuAndes getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaVacuAndes ();
		}
		return instance;
	}

	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaVacuAndes getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaVacuAndes (tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}

	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}

		return resp;
	}

	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{

		sqlCiudadano = new SQLCiudadano( this );
		sqlEstado = new SQLEstado( this );
		sqlInfoUsuario = new SQLInfoUsuario( this );
		sqlPriorizacion = new SQLPriorizacion( this );
		sqlRoles = new SQLRoles( this );
		sqlUsuario = new SQLUsuario( this );
		sqlVacunacion = new SQLVacunacion( this );
		sqlUtil = new SQLUtil( this );
		sqlCita = new SQLCita( this );
		sqlPunto = new SQLPunto( this );
		sqlVacuna = new SQLVacuna( this );
		sqlEtapa = new SQLEtapa( this );
		sqlCondicionPriorizacion = new SQLCondicionPriorizacion( this );
		sqlEps = new SQLEPS( this );
		sqlAsignada = new SQLAsignada( this );
		sqlAtencion = new SQLAtencion( this );
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de VacuAndes
	 */
	public String darSeqVacuAndes ()
	{
		return tablas.get (0);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Eps de VacuAndes
	 */
	public String darTablaEps ()
	{
		return tablas.get (1);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Roles de VacuAndes
	 */
	public String darTablaRoles ()
	{
		return tablas.get (2);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Estado de VacuAndes
	 */
	public String darTablaEstado ()
	{
		return tablas.get (3);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Etapa de VacuAndes
	 */
	public String darTablaEtapa ()
	{
		return tablas.get (4);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de CondicionPriorizacion de VacuAndes
	 */
	public String darTablaCondicionPriorizacion()
	{
		return tablas.get( 5 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Punto de VacuAndes
	 */
	public String darTablaPunto()
	{
		return tablas.get( 6 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Vacuna de VacuAndes
	 */
	public String darTablaVacuna()
	{
		return tablas.get( 7 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Asignada de VacuAndes
	 */
	public String darTablaAsignada()
	{
		return tablas.get( 8 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Ciudadano de VacuAndes
	 */
	public String darTablaCiudadano()
	{
		return tablas.get( 9 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Vacunación de VacuAndes
	 */
	public String darTablaVacunacion()
	{
		return tablas.get( 10 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Priorización de VacuAndes
	 */
	public String darTablaPriorizacion()
	{
		return tablas.get( 11 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de InfoUsuario de VacuAndes
	 */
	public String darTablaInfoUsuario()
	{
		return tablas.get( 12 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Usuario de VacuAndes
	 */
	public String darTablaUsuario()
	{
		return tablas.get( 13 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cita de VacuAndes
	 */
	public String darTablaCita()
	{
		return tablas.get( 14 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Atención de VacuAndes
	 */
	public String darTablaAtencion()
	{
		return tablas.get( 15 );
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Vacuandes de VacuAndes
	 */
	public String darTablaVacuAndes()
	{
		return tablas.get( 16 );
	}

	/**
	 * Transacción para el generador de secuencia de VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return El siguiente número del secuenciador de VacuAndes
	 */
	private long nextval()
	{
		long resp = sqlUtil.nextval( pmf.getPersistenceManager() );
		log.trace( "Generando secuencia: " + resp );
		return resp;
	}

	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle específico del problema encontrado
	 * @param e - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
	 */
	private String darDetalleException( Exception e ) 
	{
		String resp = "";
		if ( e.getClass().getName().equals( "javax.jdo.JDODataStoreException" ) )
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/* ****************************************************************
	 * 			Métodos para manejar las CITAS
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla CITA
	 * Adiciona entradas al log de la aplicación
	 * @param fechaHora - Fecha y hora de la cita
	 * @param finalizada - Si la cita ya se finalizó o no
	 * @param ciudadano - El documento de identificación del ciudadano asociado a la cita
	 * @return El objeto Cita adicionado. null si ocurre alguna Excepción
	 */
	public Cita adicionarCita( String fechaHora, String finalizada, String ciudadano, String punto )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			
			Long tuplasInsertadas = sqlCita.adicionarCita( pm, fechaHora, finalizada, ciudadano, punto );
			tx.commit();

			log.trace( "Inserción de cita: " + fechaHora + " - " + ciudadano + ": " + tuplasInsertadas + " tuplas insertadas" );

			fechaHora = fechaHora+":00.000000";
			
			String[] fechah = fechaHora.split(" ");
			String hora= fechah[1];
			String[] añomesdia = fechah[0].split("-");
			
			String fecha = añomesdia[2]+"-"+añomesdia[1]+"-"+añomesdia[0]+" "+hora;
			
			Timestamp ts = Timestamp.valueOf(fecha);
			
			return new Cita( ts , finalizada, ciudadano, punto );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla CITA
	 * Adiciona entradas al log de la aplicación
	 * @param fechaHora - Fecha y hora de la cita
	 * @param ciudadano - El documento de identificación del ciudadano asociado a la cita
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarCita( String fechaHora, String ciudadano ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlCita.eliminarCita( pm, fechaHora, ciudadano );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla CITA que tiene la fecha y hora y documento
	 * de identificación del ciudadano asociado
	 * @param fechaHora - Fecha y hora de la cita
	 * @param ciudadano - El documento de identificación del ciudadano
	 * @return El objeto Cita, construidos con base en las tuplas de la tabla CITA
	 */
	public Cita darCita( String fechaHora, String ciudadano )
	{
		return sqlCita.darCita( pmf.getPersistenceManager(), fechaHora, ciudadano );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla CITA
	 * @return La lista de objetos Cita, construidos con base en las tuplas de la tabla CITA
	 */
	public List<Cita> darCitas()
	{
		return sqlCita.darCitas( pmf.getPersistenceManager() );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla CITA que tengan un id de punto específico
	 * @return La lista de objetos Cita, construidos con base en las tuplas de la tabla CITA
	 */
	public List<Cita> darCitasNoFPunto( String id )
	{
		return sqlCita.darCitasNoFPunto( pmf.getPersistenceManager(), id );
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla CITA que tengan un id de punto específico y un documento de ciudadano específico
	 * @return La lista de objetos Cita, construidos con base en las tuplas de la tabla CITA
	 */
	public List<Cita> darCitasCiudadanoPunto( String id, String documento )
	{
		return sqlCita.darCitasCiudadanoPunto( pmf.getPersistenceManager(), id, documento );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla CITA que tengan un id de punto específico
	 * @return La lista de objetos Cita, construidos con base en las tuplas de la tabla CITA
	 */
	public List<Cita> darCitasPunto( String id )
	{
		return sqlCita.darCitasNoFPunto( pmf.getPersistenceManager(), id );
	}

	/* ****************************************************************
	 * 			Métodos para manejar la PRIORIZACION
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla PRIORIZACION
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return El objeto Priorizacion adicionado. null si ocurre alguna Excepción
	 */
	public Priorizacion adicionarPriorizacion( String documento, String descripcion )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long tuplasInsertadas = sqlPriorizacion.adicionarPriorizacion( pm, documento, descripcion );
			tx.commit();

			log.trace( "Inserción de priorizacion: " + documento + " - " + descripcion + ": " + tuplasInsertadas + " tuplas insertadas" );

			return new Priorizacion( documento, descripcion );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla PRIORIZACION
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarPriorizacion( String documento, String descripcion ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlPriorizacion.eliminarPriorizacion( pm, documento, descripcion );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla PRIORIZACION que tiene el documento y descripción
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return El objeto Priorizacion, construidos con base en las tuplas de la tabla PRIORIZACION
	 */
	public Priorizacion darPriorizacion( String documento, String descripcion )
	{
		return sqlPriorizacion.darPriorizacion( pmf.getPersistenceManager(), documento, descripcion );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla PRIORIZACION
	 * @return La lista de objetos Priorizacion, construidos con base en las tuplas de la tabla PRIORIZACION
	 */
	public List<Priorizacion> darPriorizaciones()
	{
		return sqlPriorizacion.darPriorizaciones( pmf.getPersistenceManager() );
	}

	/**
	 * Método que consulta todas las condiciones de priorizacion asociadas al ciudadano
	 * @return La lista de objetos String con las condiciones de priorizacion encontradas
	 */
	public List<String> darCondicionesCiudadano( String documento ) {
		return sqlPriorizacion.darCondicionesCiudadano( pmf.getPersistenceManager(), documento);
	}

	/* ****************************************************************
	 * 			Métodos para manejar los ROLES
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla ROLES
	 * Adiciona entradas al log de la aplicación
	 * @param rol - La cadena de texto con el rol
	 * @return El objeto Rol adicionado. null si ocurre alguna Excepción
	 */
	public Rol adicionarRol( String rol )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long idRol = nextval();
			Long tuplasInsertadas = sqlRoles.adicionarRol( pm, idRol, rol );
			tx.commit();

			log.trace( "Inserción de rol: " + idRol + " - " + rol + ": " + tuplasInsertadas + " tuplas insertadas" );

			return new Rol( idRol, rol );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla ROLES
	 * Adiciona entradas al log de la aplicación
	 * @param id - El identificador del rol
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarRol( Long id ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlRoles.eliminarRol( pm, id );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla ROLES que el id
	 * @param id - El identificador del rol
	 * @return El objeto Rol, construidos con base en las tuplas de la tabla ROLES
	 */
	public Rol darRol( Long id )
	{
		return sqlRoles.darRol( pmf.getPersistenceManager(), id );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla ROLES
	 * @return La lista de objetos Rol, construidos con base en las tuplas de la tabla ROLES
	 */
	public List<Rol> darRoles()
	{
		return sqlRoles.darRoles( pmf.getPersistenceManager() );
	}

	/* ****************************************************************
	 * 			Métodos para manejar la INFORMACION DE LOS USUARIOS
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla INFOUSUARIO y USUARIO
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento del ciudadano asociado al usuario
	 * @param login - El login del usuario
	 * @param trabajo - El trabajo del usuario
	 * @param roles - El rol del usuario en la aplicación
	 * @param punto - El punto de vacunación al que se encuentra asociado
	 * @return El objeto InfoUsuario adicionado. null si ocurre alguna Excepción
	 */
	public InfoUsuario adicionarInfoUsuario( String documento, String login, String clave, String trabajo, Long roles, String punto )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long tuplasInsertadas = sqlInfoUsuario.adicionarInfoUsuario( pm, login, clave, trabajo, roles, punto );
			sqlUsuario.adicionarUsuario(pm, documento, login);
			tx.commit();

			log.trace( "Inserción de la información de un usuario: " + login + " - " + trabajo + ": " + tuplasInsertadas + " tuplas insertadas" );

			return new InfoUsuario( login, clave, trabajo, roles, punto );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla INFOUSUARIO y USUARIO
	 * Adiciona entradas al log de la aplicación
	 * @param login - El login único de un usuario
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarInfoUsuario( String documento, String login ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			sqlUsuario.eliminarUsuario(pm, documento);
			Long resp = sqlInfoUsuario.eliminarInfoUsuario( pm, login );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla INFOUSUARIO que tiene el login
	 * @param login - El login único de un usuario
	 * @return El objeto InfoUsuario, construidos con base en las tuplas de la tabla INFOUSUARIO
	 */
	public InfoUsuario darInfoUsuario( String login )
	{
		return sqlInfoUsuario.darInfoUsuario( pmf.getPersistenceManager(), login );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla INFOUSUARIO
	 * @return La lista de objetos InfoUsuario, construidos con base en las tuplas de la tabla INFOUSUARIO
	 */
	public List<InfoUsuario> darInfoUsuarios()
	{
		return sqlInfoUsuario.darInfoUsuarios( pmf.getPersistenceManager() );
	}

	/* ****************************************************************
	 * 			Métodos para manejar los ESTADOS
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla ESTADO
	 * Adiciona entradas al log de la aplicación
	 * @param descripcion - La descripción del estado
	 * @return El objeto Estado adicionado. null si ocurre alguna Excepción
	 */
	public Estado adicionarEstado( String descripcion )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long idEstado = nextval();
			Long tuplasInsertadas = sqlEstado.adicionarEstado( pm, idEstado, descripcion );
			tx.commit();

			log.trace( "Inserción de estado: " + idEstado + " - " + descripcion + ": " + tuplasInsertadas + " tuplas insertadas" );

			return new Estado( idEstado, descripcion );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla ESTADO
	 * Adiciona entradas al log de la aplicación
	 * @param id - El identificador del estado
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarEstado( Long id ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlEstado.eliminarEstado( pm, id  );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla ESTADO que tiene el id
	 * @param id - El identificador del estado
	 * @return El objeto Estado, construidos con base en las tuplas de la tabla ESTADO
	 */
	public Estado darEstado( Long id )
	{
		return sqlEstado.darEstado( pmf.getPersistenceManager(), id );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla ESTADO
	 * @return La lista de objetos Estado, construidos con base en las tuplas de la tabla ESTADO
	 */
	public List<Estado> darEstados()
	{
		return sqlEstado.darEstados( pmf.getPersistenceManager() );
	}

	/* ****************************************************************
	 * 			Métodos para manejar los CIUDADANOS
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla CIUDADANO
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param nombre - El nombre del ciudadano
	 * @param nacimiento - La fecha de nacimiento del ciudadano
	 * @param habilitado - Si el ciudadano está o no habilitado para vacunación
	 * @param estado - El id del estado en el que se encuentra el ciudadano en la vacunación
	 * @param eps - El id de la EPS a la que el ciuadano se encuentra afiliado
	 * @param etapa - El número de la etapa a la que el ciudadano pertenece
	 * @return El objeto Ciudadano adicionado. null si ocurre alguna Excepción
	 */
	public Ciudadano adicionarCiudadano( String documento, String nombre, Date nacimiento, String habilitado, Long estado, String eps, Integer etapa, String sexo )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long tuplasInsertadas = sqlCiudadano.adicionarCiudadano( pm, documento, nombre, nacimiento, habilitado, estado, eps, etapa, sexo );
			tx.commit();

			log.trace( "Inserción de ciudadano: " + documento + " - " + nombre + ": " + tuplasInsertadas + " tuplas insertadas" );

			return new Ciudadano( documento, nombre, nacimiento, habilitado, estado, eps, etapa, sexo );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que actualiza, de manera transaccional, una tupla en la tabla CIUDADANO
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param nombre - El nombre del ciudadano
	 * @param nacimiento - La fecha de nacimiento del ciudadano
	 * @param habilitado - Si el ciudadano está o no habilitado para vacunación
	 * @param estado - El id del estado en el que se encuentra el ciudadano en la vacunación
	 * @param eps - El id de la EPS a la que el ciuadano se encuentra afiliado
	 * @param etapa - El número de la etapa a la que el ciudadano pertenece
	 * @return El objeto Ciudadano actualizado. null si ocurre alguna Excepción
	 */
	public Ciudadano actualizarCiudadano( String documento, String nombre, Date nacimiento, String habilitado, Long estado, String eps, Integer etapa, String sexo )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long tuplasActualizadas = sqlCiudadano.actualizarCiudadano( pm, documento, nombre, nacimiento, habilitado, estado, eps, etapa, sexo );
			tx.commit();

			log.trace( "Actualización de ciudadano: " + documento + " - " + nombre + ": " + tuplasActualizadas + " tuplas actualizadas" );

			return new Ciudadano( documento, nombre, nacimiento, habilitado, estado, eps, etapa, sexo );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla CIUDADANO
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarCiudadano( String documento ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlCiudadano.eliminarCiudadano( pm, documento );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla CIUDADANO que tiene el documento
	 * @param documento - El documento de identificación del ciudadano
	 * @return El objeto Ciudadano, construidos con base en las tuplas de la tabla CIUDADANO
	 */
	public Ciudadano darCiudadano( String documento )
	{
		return sqlCiudadano.darCiudadano( pmf.getPersistenceManager(), documento);
	}

	/** 
	 * Método que consulta la ETAPA a la que pertenece un ciudadano dado su ID 
	 * @param documento - el id del ciudadano a buscar 
	 * @return El objeto Long, construidos con base en las tuplas de la tabla CIUDADANO 
	 */ 
	public Long darEtapaCiudadano( String documento ) 
	{ 
		return sqlCiudadano.darEtapaCiudadano( pmf.getPersistenceManager(), documento ); 
	} 

	/**
	 * Método que consulta todas las tuplas en la tabla CIUDADANO
	 * @return La lista de objetos Ciudadano, construidos con base en las tuplas de la tabla CIUDADANO
	 */
	public List<Ciudadano> darCiudadanos()
	{
		return sqlCiudadano.darCiudadanos( pmf.getPersistenceManager() );
	}

	/* ****************************************************************
	 * 			Métodos para manejar los USUARIOS
	 *****************************************************************/

	/**
	 * Método que consulta la tupla en la tabla USUARIO que tiene el documento
	 * @param documento - El documento de identificación del ciudadano
	 * @return El objeto Usuario, construidos con base en las tuplas de la tabla USUARIO
	 */
	public Usuario darUsuario( String documento )
	{
		return sqlUsuario.darUsuario( pmf.getPersistenceManager(), documento );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla USUARIO
	 * @return La lista de objetos Usuario, construidos con base en las tuplas de la tabla USUARIO
	 */
	public List<Usuario> darUsuarios()
	{
		return sqlUsuario.darUsuarios( pmf.getPersistenceManager() );
	}

	/* ****************************************************************
	 * 			Métodos para manejar las ATENCIONES
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla ATENCION
	 * Adiciona entradas al log de la aplicación
	 * @param descripcion - La descripcion de la condicion de priorizacion
	 * @param punto - El identificador del punto de vacunación
	 * @return El objeto Atencion adicionado. null si ocurre alguna Excepción
	 */
	public Atencion adicionarAtencion( String descripcion, String punto )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long tuplasInsertadas = sqlAtencion.adicionarAtencion( pm, descripcion, punto );
			tx.commit();

			log.trace( "Inserción de atencion: " + descripcion + " - " + punto + ": " + tuplasInsertadas + " tuplas insertadas" );

			return new Atencion( descripcion, punto );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla ATENCION
	 * Adiciona entradas al log de la aplicación
	 * @param descripcion - La descripcion de la condicion de priorizacion
	 * @param punto - El identificador del punto de vacunación
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarAtencion( String descripcion, String punto ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlAtencion.eliminarAtencion( pm, descripcion, punto );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla ATENCION que tiene la condicion y el punto
	 * @param descripcion - La descripcion de la condicion de priorizacion
	 * @param punto - El identificador del punto de vacunación
	 * @return El objeto Atencion, construidos con base en las tuplas de la tabla ATENCION
	 */
	public Atencion darAtencion( String descripcion, String punto )
	{
		return sqlAtencion.darAtencion( pmf.getPersistenceManager(), descripcion, punto );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla ATENCION
	 * @return La lista de objetos Atencion, construidos con base en las tuplas de la tabla ATENCION
	 */
	public List<Atencion> darAtenciones()
	{
		return sqlAtencion.darAtenciones( pmf.getPersistenceManager() );
	}

	/**
	 * Método que consulta todas las condiciones de priorizacion asociadas a un punto
	 * @return La lista de objetos String con las condiciones de priorizacion encontradas
	 */
	public List<String> darCondicionesPunto( String punto ) {
		return sqlAtencion.darCondicionesPunto( pmf.getPersistenceManager(), punto );
	}

	/* ****************************************************************
	 * 			Métodos para manejar la VACUNACION
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla VACUNACION
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @param punto - El identificador del punto de vacunación
	 * @return El objeto Vacunacion adicionado. null si ocurre alguna Excepción
	 */
	public Vacunacion adicionarVacunacion( String documento, String eps, String punto )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long tuplasInsertadas = sqlVacunacion.adicionarVacunacion( pm, documento, eps, punto );
			tx.commit();

			log.trace( "Inserción de vacunación: " + documento + " - " + eps + ": " + tuplasInsertadas + " tuplas insertadas" );

			return new Vacunacion( documento, eps, punto );
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que modifica, de manera transaccional, una tupla en la tabla VACUNACION
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la EPS
	 * @param punto - El identificador del PUNTO
	 * @return El número de tuplas modificadas
	 */
	public Long actualizarVacunacion( String documento, String eps, String punto )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlVacunacion.actualizarVacunacion( pm, documento, eps, punto );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla VACUNACION
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarVacunacion( String documento, String eps ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlVacunacion.eliminarVacunacion( pm, documento, eps );
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla VACUNACION que tiene el documento y eps
	 * de identificación del ciudadano asociado
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @return El objeto Vacunacion, construidos con base en las tuplas de la tabla VACUNACION
	 */
	public Vacunacion darVacunacion( String documento, String eps )
	{
		return sqlVacunacion.darVacunacion( pmf.getPersistenceManager(), documento, eps );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla VACUNACION
	 * @return La lista de objetos Vacunacion, construidos con base en las tuplas de la tabla VACUNACION
	 */
	public List<Vacunacion> darVacunaciones()
	{
		return sqlVacunacion.darVacunaciones( pmf.getPersistenceManager() );
	}
		
	/**
	 * Método que consulta el id de un punto de una vacunación dado el documento de un ciudadano
	 * @return id- id del punto
	 */
	public String darIdPuntoVacunacionDocumento (String documento)
	{
		return sqlVacunacion.darIdPuntoVacunacionDocumento( pmf.getPersistenceManager(), documento );
	}

	/**
	 * Método que consulta la cantidad de tuplas en la tabla VACUNACION para un punto de vacunacion
	 * @return La cantidad de vacunaciones del punto
	 */
	public Long darCantidadPunto( String id_punto ) {
		return sqlVacunacion.darCantidadPunto( pmf.getPersistenceManager(), id_punto );
	}

	/* ****************************************************************
	 * 			Métodos para manejar los PUNTOS 
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla PUNTO
	 * Adiciona entradas al log de la aplicación
	 * @param id - Id del punto de vacunacion
	 * @param region - Region del punto de vacunacion
	 * @param direccion - Direccion del putno de vacunacion
	 * @param aplicadas - Numero de vacunas aplicadas en el punto de vacunacion
	 * @param capacidad -Capacidad del punto de vacunacion
	 * @param id_eps - Id de la eps a la que pertenece el punto de vacunacion
	 * @return El objeto Punto insertado. Null si ocurre alguna Excepción
	 */
	public Punto adicionarPunto( String id, String region, String direccion, Long aplicadas, Long capacidad, String id_eps, Long capacidadVacunas, Long vacunas, String habilitado )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long ti = sqlPunto.adicionarPunto( pm, id, region, direccion, aplicadas, capacidad, id_eps, capacidadVacunas, vacunas, habilitado );
			tx.commit();

			log.trace( "Inserción de punto: " + id + " - " + region + " - "+ direccion+" - "+aplicadas+" - "+ capacidad+" - "+ id_eps+": " + ti + " tuplas insertadas" );

			return new Punto(id, region, direccion, aplicadas, capacidad, id_eps, capacidadVacunas, vacunas, habilitado);
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla PUNTO
	 * Adiciona entradas al log de la aplicación
	 * @param id - Id del punto a eliminar
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarPunto( String id ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlPunto.eliminarPunto( pm, id);
			log.trace("Eliminación Punto id: "+id);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que aumenta el numero de vacunas aplicadas de un PUNTO dado su id
	 * @param id - id del punto de vacunacion al que se le aumentarán la cantidad de vacunas aplicadas
	 */
	public long aumentarAplicadasPuntoId( String id ) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlPunto.aumentarAplicadasPuntoId( pm, id);
			log.trace("Actualizacion aplicadas Punto id: "+id);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}

	}

	/**
	 * Método que consulta la tupla en la tabla PUNTO con el id dado
	 * @param id - el id del punto a buscar
	 * @return El objeto Punto, construidos con base en las tuplas de la tabla PUNTO
	 */
	public Punto darPunto( String id )
	{
		return sqlPunto.darPunto( pmf.getPersistenceManager(), id );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla PUNTO
	 * @return La lista de objetos Punto, construidos con base en las tuplas de la tabla PUNTO
	 */
	public List<Punto> darPuntos()
	{
		return sqlPunto.darPuntos( pmf.getPersistenceManager() );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla PUNTO habilitados
	 * @return La lista de objetos Punto, construidos con base en las tuplas de la tabla PUNTO
	 */
	public List<Punto> darPuntosHabilitadosEPS(String id)
	{
		return sqlPunto.darPuntosHabilitadosEPS( pmf.getPersistenceManager(), id );
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla PUNTO 
	 * @return La lista de objetos Punto, construidos con base en las tuplas de la tabla PUNTO
	 */
	public List<Punto> darPuntosEPS(String id)
	{
		return sqlPunto.darPuntosEPS( pmf.getPersistenceManager(), id );
	}


	/**
	 * Método que cambia el numero de vacunas de un Punto dado su id
	 * @param id - id del punto al que se le cambiará el  numero de vacunas
	 * @param vacunas - numero de vacunas que se adicionará
	 */
	public long aumentarVacunasPuntoId( Long vacunas, String id ) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlPunto.aumentarVacunasPuntoId( pm, id, vacunas );
			log.trace("Cambio de vacunas del punto id: "+ id);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}

	}

	/** 
	 * Método que consulta la capacidad de un PUNTO dado su id 
	 * @param id - el id del punto a buscar 
	 * @return El objeto Long, construidos con base en las tuplas de la tabla PUNTO 
	 */ 
	public Long darCapacidadPunto( String id ) 
	{ 
		return sqlPunto.darCapacidadPunto( pmf.getPersistenceManager(), id ); 
	} 


	/**
	 * Método que consulta el número de vacunas de una EPS dado su Id
	 * @return El número de vacunas de una EPS
	 */
	public Long darVacunasPunto( String id)
	{
		return sqlPunto.darVacunas(pmf.getPersistenceManager(), id);
	}

	/**
	 * Método que consulta la capacidad de vacunas de una EPS dado su Id
	 * @return El número de vacunas de una EPS
	 */
	public Long darCapacidadVacunasPunto( String id)
	{
		return sqlPunto.darCapacidadVacunas(pmf.getPersistenceManager(), id);
	}

	/** 
	 * Método que consulta la cantidad de citas activas de un PUNTO dado su id 
	 * @param id - el id del punto a buscar 
	 * @return El objeto Long, construidos con base en las tuplas de la tabla PUNTO 
	 */ 
	public Long darCitasActivasPunto( String id ) 
	{ 
		return sqlPunto.darCitasActivasPunto( pmf.getPersistenceManager(), id ); 
	}
	
	/** 
	 * Método que consulta la cantidad de citas finalizadas de un PUNTO dado su id en un rango de fechas
	 * @param id - el id del punto a buscar 
	 * @param fecha1 - la fecha1 del rango
	 * @param fecha2 - la fecha2 del rango 
	 * @return El objeto Long, construidos con base en las tuplas de la tabla PUNTO 
	 */ 
	public Long darCitasFinalizadasFechasPunto( String id, String fecha1, String fecha2 ) 
	{ 
		return sqlPunto.darCitasFinalizadasFechasPunto( pmf.getPersistenceManager(), id, fecha1, fecha2 ); 
	}
	
	/**
	 * Método que consulta la lista de Ids de todos los puntos de la base de datos
     *@return La lista de tipos tsring con todos los ids de los puntos de la base de datos	 
	 */
	public List<String> darIdsPuntos(){
		return sqlPunto.darIdsPuntos(pmf.getPersistenceManager());
	}
	
	

	/**
	 * Método que cambia el estado de habilitado de un punto de vacunacion dado su id
	 * @param id - El identificador del punto de vacunacion
	 * @param habilitado - El nuevo estado de habilitado
	 * @return El número de tuplas modificadas
	 */
	public Long cambiarHabilitadoPunto( String id, String habilitado )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlPunto.cambiarHabilitadoPunto( pm, id, habilitado );
			tx.commit();

			log.trace( "Actualización habilitado del punto con id: " + id );

			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}


	/* ****************************************************************
	 * 			Métodos para manejar las VACUNAS 
	 *****************************************************************/
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla VACUNA
	 * Adiciona entradas al log de la aplicación
	 * @param id - Id de la vacuna
	 * @param preservacion - Condiciones de preservacion de la vacuna 
	 * @param aplicada - Estado de aplicacion de la vacuna
	 * @param dosis - Numero de dosis de la vacuna
	 * @param tipo - Tipo de la vacuna
	 * @return Ls cantidad de tuplas modificadas. -1 si ocurre alguna Excepción
	 */
	public Long adicionarVacuna( String id, String preservacion, String aplicada, Long dosis, String tipo, String llegada )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long ti = sqlVacuna.adicionarVacuna( pm, id, preservacion, aplicada, dosis, tipo, llegada );
			tx.commit();

			log.trace( "Inserción de vacuna: " + id + " - " + id + " - "+ preservacion +" - "+aplicada +" - "+ dosis +" - "+ tipo +": " + ti + " tuplas insertadas" );

			return ti;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla VACUNA
	 * Adiciona entradas al log de la aplicación
	 * @param id - Id de la vacuna a eliminar
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarVacuna( String id ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlVacuna.eliminarVacuna( pm, id);
			log.trace("Eliminación Vacuna id: "+id);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que cambia el estado de aplicacion de una vacuna
	 * @param id - id de la vacuna a la cual se le cambiará el estado
	 */
	public long cambiarEstadoAlplicacionVacunaT( String id ) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlVacuna.cambiarEstadoAplicacionVacunaT( pm, id);
			log.trace("Cambio de estado aplicación vacuna id: "+id);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}

	}

	/**
	 * Método que consulta la tupla en la tabla VACUNA con el id dado
	 * @param id - el id de la vacuna a buscar
	 * @return El objeto Vacuna, construidos con base en las tuplas de la tabla VACUNA
	 */
	public Vacuna darVacuna( String id )
	{
		return sqlVacuna.darVacuna( pmf.getPersistenceManager(), id );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla VACUNA
	 * @return La lista de objetos Vacuna, construidos con base en las tuplas de la tabla VACUNA
	 */
	public List<Vacuna> darVacunas()
	{
		return sqlVacuna.darVacunas( pmf.getPersistenceManager() );
	}
	
	/**
	 * Método que consulta todas las tecnologías de vacunas
	 * @return La lista con las tecnologías encontradas
	 */
	public List<String> darTecnologiasVacunas()
	{
		return sqlVacuna.darTecnologiasVacunas( pmf.getPersistenceManager() );
	}
	

	/* ****************************************************************
	 * 			Métodos para manejar las ETAPAS
	 *****************************************************************/
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla ETAPA
	 * Adiciona entradas al log de la aplicación
	 * @param numero - Numero de la etapa de vacunacion
	 * @param descripcion - descripcion de la etapa
	 * @return Ls cantidad de tuplas modificadas. -1 si ocurre alguna Excepción
	 */
	public Long adicionarEtapa( Long numero, String descripcion )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long ti = sqlEtapa.adicionarEtapa( pm, numero, descripcion );
			tx.commit();

			log.trace( "Inserción de etapa: " + numero + " - " + descripcion +": " + ti + " tuplas insertadas" );

			return ti;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla ETAPA
	 * Adiciona entradas al log de la aplicación
	 * @param etapa - numero de la etapa a eliminar
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarEtapa( Long etapa ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlEtapa.eliminarEtapa( pm, etapa);
			log.trace("Eliminación Etapa numero: "+etapa);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que cambia la descripcion de una etapa dado su número
	 * @param numero - numero de la etapa a la que se le cambiará la descripcion
	 */
	public long cambiarDescripcionEtapa( Long numero, String descripcion ) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlEtapa.cambiarDescripcionEtapa( pm, numero, descripcion);
			log.trace("Cambio de descripcion de la etapa numero: "+numero);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}

	}

	/**
	 * Método que consulta la tupla en la tabla ETAPA con el numero
	 * @param numero - numero de la etapa a buscar
	 * @return El objeto Etapa, construidos con base en las tuplas de la tabla ETAPA
	 */
	public Etapa darEtapa( Long numero )
	{
		return sqlEtapa.darEtapa( pmf.getPersistenceManager(), numero );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla ETAPA
	 * @return La lista de objetos Etapa, construidos con base en las tuplas de la tabla ETAPA
	 */
	public List<Etapa> darEtapas()
	{
		return sqlEtapa.darEtapas( pmf.getPersistenceManager() );
	}

	/* ****************************************************************
	 * 			Métodos para manejar las CONDICIONES DE PRIORIZACION
	 *****************************************************************/
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla CONDICIONPRIORIZACION
	 * Adiciona entradas al log de la aplicación
	 * @param numero_etapa - Numero de la etapa a la que pertenece la condicion de priorizacion
	 * @param descripcion - descripcion de la condicion de priorizacion
	 * @return Ls cantidad de tuplas modificadas. -1 si ocurre alguna Excepción
	 */
	public Long adicionarCondicionPriorizacion( Long numero_etapa, String descripcion )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long ti = sqlCondicionPriorizacion.adicionarCondicionPriorizacion( pm, numero_etapa, descripcion );
			tx.commit();

			log.trace( "Inserción de condicion de priorizacion: " + numero_etapa + " - " + descripcion +": " + ti + " tuplas insertadas" );

			return ti;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla CONDICIONPRIORIZACION
	 * Adiciona entradas al log de la aplicación
	 * @param descripcion - descripcion de la condicion de priorizacion a eliminar
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarCondicionPriorizacion( String descripcion ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlCondicionPriorizacion.eliminarCondicionPriorizacion( pm, descripcion);
			log.trace("Eliminación Condicion priorizacion descripcion: "+descripcion);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla CONDICIONPRIORIZACION con la descripcion
	 * @param descripcion - descripcion de la condicion de priorizacion a buscar
	 * @return El objeto Condicion Priorizacion, construidos con base en las tuplas de la tabla CONDICIONPRIORIZACION
	 */
	public CondicionPriorizacion darCondicionPriorizacion( String descripcion )
	{
		return sqlCondicionPriorizacion.darCondicionPriorizacion( pmf.getPersistenceManager(), descripcion );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla CONDICIONPRIORIZACION
	 * @return La lista de objetos CondcionPriorizacion, construidos con base en las tuplas de la tabla CONDICIONPRIORIZACION
	 */
	public List<CondicionPriorizacion> darCondicionesPriorizacion()
	{
		return sqlCondicionPriorizacion.darCondicionesPriorizacion( pmf.getPersistenceManager() );
	}

	/* ****************************************************************
	 * 			Métodos para manejar las EPS
	 *****************************************************************/
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla EPS
	 * Adiciona entradas al log de la aplicación
	 * @param id - id de la eps
	 * @param region - region en la que se encuentra la eps
	 * @param vacunas - cantidad de vacunas con la que cuenta la eps
	 * @return Ls cantidad de tuplas modificadas. -1 si ocurre alguna Excepción
	 */
	public Long adicionarEps( String id, String region, Long vacunas, Long capacidadVacunas )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long ti = sqlEps.adicionarEPS( pm, id, region, vacunas, capacidadVacunas );
			tx.commit();

			log.trace( "Inserción de EPS: " + id + " - " + region +" - "+ vacunas+ ": " + ti + " tuplas insertadas" );

			return ti;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla EPS
	 * Adiciona entradas al log de la aplicación
	 * @param id - id de la eps a eliminar 
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarEps( String id ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlEps.eliminarEPS( pm, id);
			log.trace("Eliminación EPS id: "+id);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que cambia el numero de vacunas de una eps dado su id
	 * @param id - id de la eps a la que se le cambiará el  numero de vacunas
	 * @param vacunas - numero de vacunas que se adicionará
	 */
	public long aumentarVacunasEPSId( Long vacunas, String id ) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlEps.aumentarVacunasEPSId( pm, id, vacunas );
			log.trace("Cambio de descripcion de la eps id: "+ id);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}

	}

	/**
	 * Método que consulta la tupla en la tabla EPS con el id
	 * @param id - id de la eps a buscar
	 * @return El objeto Eps, construidos con base en las tuplas de la tabla EPS
	 */
	public EPS darEPS( String id )
	{
		return sqlEps.darEPS( pmf.getPersistenceManager(), id );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla EPS
	 * @return La lista de objetos Eps, construidos con base en las tuplas de la tabla EPS
	 */
	public List<EPS> darEPSs()
	{
		return sqlEps.darEPSs( pmf.getPersistenceManager() );
	}

	/**
	 * Método que consulta todas las regiones de la tabla EPS
	 * @return La lista de objetos String, construidos con base en las tuplas de la tabla EPS
	 */
	public List<String> darRegiones()
	{
		return sqlEps.darRegiones( pmf.getPersistenceManager() );
	}

	/**
	 * Método que consulta el número de vacunas de una EPS dado su Id
	 * @return El número de vacunas de una EPS
	 */
	public Long darVacunasEPS( String id)
	{
		return sqlEps.darVacunas(pmf.getPersistenceManager(), id);
	}

	/**
	 * Método que consulta la capacidad de vacunas de una EPS dado su Id
	 * @return El número de vacunas de una EPS
	 */
	public Long darCapacidadEPS( String id)
	{
		return sqlEps.darCapacidad(pmf.getPersistenceManager(), id);
	}

	/* ****************************************************************
	 * 			Métodos para manejar las vacunas ASIGNADAS 
	 *****************************************************************/
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla ASIGNADA
	 * Adiciona entradas al log de la aplicación
	 * @param id_eps - Id de la eps a la que pertenece la vacuna
	 * @param id_punto - Id del punto al que pertenece la vacuna 
	 * @param id_vacuna - Id de la vacuna asignada
	 * @return Ls cantidad de tuplas modificadas. -1 si ocurre alguna Excepción
	 */
	public Long adicionarAsignada( String id_eps, String id_punto, String id_vacuna )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long ti = sqlAsignada.adicionarAsignada( pm, id_eps, id_punto, id_vacuna );
			tx.commit();

			log.trace( "Inserción de vacuna asignada: " + id_eps + " - " + id_punto +" - "+ id_vacuna+ ": " + ti + " tuplas insertadas" );

			return ti;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla ASIGNADA
	 * Adiciona entradas al log de la aplicación
	 * @param id_punto - id del punto de vacunacion
	 * @param id_vacuna - id de la vacuna
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public Long eliminarAsignada( String id_punto, String id_vacuna ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlAsignada.eliminarAsignada( pm, id_punto, id_vacuna);
			log.trace("Eliminación Asignacion id_punto, id_vacuna: "+id_punto+", "+id_vacuna);
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return -1L;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta la tupla en la tabla ASIGNADA el id del punto y el de la vacuna
	 * @param id_punto - id del punto de vacunacion de la vacuna asignada
	 * @param id_vacuna - id de la vacuna asignada 
	 * @return El objeto Asignada, construidos con base en las tuplas de la tabla ASIGNADA
	 */
	public Asignada darAsignada( String id_punto, String id_vacuna )
	{
		return sqlAsignada.darAsignada( pmf.getPersistenceManager(), id_punto, id_vacuna );
	}

	/**
	 * Método que consulta todas las tuplas en la tabla ASIGNADA
	 * @return La lista de objetos Asignada, construidos con base en las tuplas de la tabla ASIGNADA
	 */
	public List<Asignada> darAsignadas()
	{
		return sqlAsignada.darAsignadas( pmf.getPersistenceManager() );
	}

	/**
	 * Método que consulta las vacunas asignadas a un punto de vacunación
	 * @param id_punto - El identificador del punto de vacunación
	 * @return La lista de objetos String con las vacunas encontradas
	 */
	public List<String> darAsignadasPunto( String id_punto ) {
		return sqlAsignada.darAsignadasPunto( pmf.getPersistenceManager(), id_punto );
	}

	/* ****************************************************************
	 * 			Métodos para consultas
	 *****************************************************************/

	/**
	 * Método que consulta el índice de vacunación de un grupo poblacional
	 * @param eps - Lista con los id de las eps de interés
	 * @param estado - Id del estado de interés
	 * @param priorizacion - Descripción de la condición de priorización de interés
	 * @param regiones - Lista con los nombres de las regiones de interés
	 * @param fechaInicio - Fecha y hora de inicio de interés
	 * @param fechaFin - Fecha y hora de fin de interés
	 * @return El índice de vacunación para el grupo poblacional filtrado con los parámetros
	 */
	public Double darIndiceVacunacion( List<String> eps, Long estado, String priorizacion, List<String> regiones, String fechaInicio, String fechaFin )
	{
		Double resultado = sqlUtil.darIndiceVacunacion(pmf.getPersistenceManager(), eps, estado, priorizacion, regiones, fechaInicio, fechaFin);
		return resultado == null ? 0: resultado;
	}

	/**
	 * Metodo que consulta los ciudadanos atentidos de puntos de una region en rangos de horas
	 * @param region - region de los puntos
	 * @param hora1 - hora inicial
	 * @param hora2 - hora final
	 * @param min1 - minuto incial
	 * @param min2 - minuto final 
	 * @return - lista con documentos de ciudadanos
	 */
	public List<String> darAtendidosRegionHoras(  String region, Long hora1, Long hora2, Long min1, Long min2 )
	{
		return sqlUtil.darAtendidosRegionHoras(pmf.getPersistenceManager(), region, hora1, hora2, min1, min2);
	}

	/**
	 * Metodo que consulta los ciudadanos atentidos de puntos de una region en rangos de fechas
	 * @param region - region de los puntos
	 * @param fecha1 - fecha inicial
	 * @param fecha2 - fecha final
	 * @return - lista con documentos de ciudadanos
	 */
	public List<String> darAtendidosRegionFechas(  String region, String fecha1, String fecha2 )
	{
		return sqlUtil.darAtendidosRegionFechas(pmf.getPersistenceManager(), region, fecha1, fecha2);
	}

	/**
	 * Metodo que consulta los ciudadanos atentidos de un punto dado su id en un rango de horas
	 * @param id - id del punto
	 * @param hora1 - hora inicial
	 * @param hora2 - hora final
	 * @param min1 - minuto incial
	 * @param min2 - minuto final 
	 * @return - lista con documentos de ciudadanos
	 */
	public List<String> darAtendidosPuntoHoras(  String id, Long hora1, Long hora2, Long min1, Long min2 )
	{
		return sqlUtil.darAtendidosPuntoHoras(pmf.getPersistenceManager(), id, hora1, hora2, min1, min2);
	}

	/**
	 * Metodo que consulta los ciudadanos atentidos de un punto dado su id en un rango de fechas
	 * @param id - id del punto
	 * @param fecha1 - fecha inicial
	 * @param fecha2 - fecha final
	 * @return - lista con documentos de ciudadanos
	 */
	public List<String> darAtendidosPuntoFechas(  String id, String fecha1, String fecha2 )
	{
		return sqlUtil.darAtendidosPuntoFechas(pmf.getPersistenceManager(), id, fecha1, fecha2);
	}

	/**
	 * Metodo que consulta los puntos efectivos en un rango de fechas
	 * @param fecha1 - fecha inicial
	 * @param fecha2 - fecha final
	 * @return - lista con documentos de ciudadanos
	 */
	public List<String> darPuntosEfectivosFechas( String fecha1, String fecha2 )
	{
		return sqlUtil.darPuntosEfectivosFechas(pmf.getPersistenceManager(), fecha1, fecha2);
	}

	/**
	 * Metodo que consulta los puntos efectivos en un rango de horas
	 * @param hora1 - hora inicial
	 * @param hora2 - hora final
	 * @param min1 - minuto incial
	 * @param min2 - minuto final 
	 * @return - lista con documentos de ciudadanos
	 */
	public List<String> darPuntosEfectivosHoras( Long hora1, Long hora2, Long min1, Long min2 )
	{
		return sqlUtil.darPuntosEfectivosHoras(pmf.getPersistenceManager(), hora1, hora2, min1, min2);
	}

	/**
	 * Método que cambia las condiciones de priorización que atiende un punto y elimina a los ciudadanos que 
	 * no correspondan a las nuevas condiciones
	 * @param id_punto - El identificador del punto de vacunación
	 * @param seleccionados - La lista con las nuevas condiciones de priorización
	 * @param condiciones - La lista con las condiciones de priorización viejas
	 * @return Lista con los documentos de los ciudadanos eliminados o null si ocurre una excepción
	 */
	public List<String> cambioEstadoPunto( String id_punto, List<String> seleccionados, List<String> condiciones ) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			log.trace("Adicionando las nuevas condiciones de priorización");
			for ( String actual: seleccionados ) {
				if ( !condiciones.contains( actual ) ) {
					sqlAtencion.adicionarAtencion( pm, actual, id_punto );
				}
			}
			log.trace("Eliminando las condiciones de priorización que ya no atiende el punto");
			for ( String actual: condiciones ) {
				if ( !seleccionados.contains( actual ) ) {
					sqlAtencion.eliminarAtencion( pm, actual, id_punto );
				}
			}
			log.trace("Buscando ciudadanos punto equivocado con id: " + id_punto);
			List<String> resp = sqlUtil.darCiudadanosPuntoEquivocado( pm, id_punto );
			log.trace("Eliminando ciudadanos punto equivocado con id: " + id_punto);
			for ( String actual: resp ) {
				VOCiudadano ciudadano = sqlCiudadano.darCiudadano(pm, actual);
				sqlVacunacion.eliminarVacunacion( pm, ciudadano.getDocumento(), ciudadano.getId_eps() );
			}
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que rehabilita un punto, asignandole citas de los ciudadanos que están en la etapa correspondiente
	 * @param punto - El identificador del punto de vacunación
	 * @param etapa - El número de la etapa actual
	 * @return La lista con los documentos de los ciudadanos con nuevas citas
	 */
	@SuppressWarnings("deprecation")
	public List<String> rehabilitarPunto( String punto, Long etapa )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			sqlPunto.cambiarHabilitadoPunto(pm, punto, "T");
			log.trace("Buscando ciudadanos sin cita");
			List<String> resp = sqlUtil.darCiudadanosSinCita( pm, punto, etapa );
			log.trace("Asignando ciudadanos sin cita al punto de vacunación con id: " + punto);
			Date fecha = new Date();
			fecha.setMinutes(0);
			fecha.setSeconds(0);            
			for( String actual: resp ) {
				VOCiudadano ciudadano = sqlCiudadano.darCiudadano( pm, actual );
				if ( sqlVacunacion.darVacunacion(pm, actual, ciudadano.getId_eps()) != null ) {
					sqlVacunacion.actualizarVacunacion( pm, ciudadano.getDocumento(), ciudadano.getId_eps(), punto );
				} 
				else {
					sqlVacunacion.adicionarVacunacion(pm, actual, ciudadano.getId_eps(), punto);
				}
				fecha.setTime((long) (fecha.getTime() + 3.6e6));
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String fechaHora = format.format( fecha );
				sqlCita.adicionarCita(pm, fechaHora, "F", actual, punto);
			}            
			tx.commit();
			return resp;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que deshabilita un punto, asignandole citas de los ciudadanos que tenian citas en ese punto
	 * @param punto - El identificador del punto de vacunación al que se asignarán las citas
	 * @return La lista de las nuevas citas
	 */
	@SuppressWarnings("deprecation")
	public String[] deshabilitarPunto( String id_puntoV, String id, String id_eps )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			sqlPunto.cambiarHabilitadoPunto(pm, id_puntoV, "F");
			log.trace("Asignando citas al punto de vacunación con id: " + id);   
			List<Cita> citasNoF = darCitasNoFPunto(id_puntoV);
			List<Cita> citasP = darCitasPunto(id);
			Date mayor = new Date();
			for(int i=0; i< citasP.size(); i++)
			{
				Timestamp actual = citasP.get(i).getFechaHora();
				if(actual.compareTo(mayor)>0)
				{
					mayor = actual;
				}
			}
			Date fecha = mayor;
			System.out.println(mayor);
			fecha.setMinutes(0);
			fecha.setSeconds(0); 
			String[] citas = new String[citasNoF.size()];
			int contador = 0;
			for( Cita actual: citasNoF ) {
				String documento = actual.getDocumento_ciudadano();
				VOCiudadano ciudadano = sqlCiudadano.darCiudadano( pm, documento );
				if ( sqlVacunacion.darVacunacion(pm, documento, id_eps) != null ) 
				{
					sqlVacunacion.actualizarVacunacion( pm, ciudadano.getDocumento(), id_eps, id );
				} 
				else 
				{
					sqlVacunacion.adicionarVacunacion(pm, documento, id_eps, id);
				}
				String fechaS = actual.getFechaHora().toString();
				fechaS = fechaS.substring(0,fechaS.length()-5);
				String[] fechahora = fechaS.split(" ");
				String hora= fechahora[1];
				String[] añomesdia = fechahora[0].split("-");
				sqlCita.eliminarCita(pm, añomesdia[2]+"-"+añomesdia[1]+"-"+añomesdia[0]+" "+hora , actual.getDocumento_ciudadano());
				fecha.setTime((long) (fecha.getTime() + 3.6e6));
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String fechaHora = format.format( fecha );
				sqlCita.adicionarCita(pm, fechaHora, "F", documento, id);
				String cita = sqlCita.darCita(pm, fechaHora, documento).toString();
				citas[contador] = cita;
				contador++;            	
			}            
			tx.commit();
			return citas;
		}
		catch( Exception e )
		{
			//        	e.printStackTrace();
			log.error( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			return null;
		}
		finally
		{
			if( tx.isActive() ) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta los ciudadanos correspondientes a los cohortes especificados
	 * @param edad - La edad o rango de edades
	 * @param sexo - La lista con los sexos
	 * @param condiciones - La lista con las condiciones de priorización
	 * @param region - La lista con las regiones
	 * @param eps - La lista con las EPS
	 * @param punto - La lista con los puntos de vacunación
	 * @param dosis - La lista con las dosis
	 * @param tecnologiaVac - La lista con las tecnologías de las vacunas
	 * @return Una lista con objetos de tipo ciudadano que pertenecen al cohorte
	 */
	public List<Ciudadano> analizarCohortes( String edad, List<String> sexo, List<String> condiciones, List<String> region, List<String> eps, List<String> punto, List<String> dosis, List<String> tecnologiaVac )
	{
		return sqlUtil.analizarCohortes( pmf.getPersistenceManager(), edad, sexo, condiciones, region, eps, punto, dosis, tecnologiaVac);
	}
	
	/**
	 * Método que consulta los tiempos más concurridos de un punto
	 */
	public String analizarOperacionTiempo( String id, String tiempo ){
		
		return  sqlUtil.analizarOperacionTiempo(pmf.getPersistenceManager(), tiempo, id);
	}
	
	/**
	 * Método que consulta las cantidades de personas agrupado por fechas
	 */
	public List<Long> analizarOperacionCantidad( String id, String tiempo ){
		
		return  sqlUtil.analizarOperacionCantidad(pmf.getPersistenceManager(), tiempo, id);
	}
	
	/**
	 * Método que consulta las cantidades de personas agrupado por fechas
	 */
	public String analizarOperacionTiempoCantidad( String id, String tiempo, Long cantidad ){
		
		return  sqlUtil.analizarOperacionTiempoCantidad(pmf.getPersistenceManager(),cantidad, tiempo, id);
	}
	
	/**
	 * Consulta ciudadanos que estuvieron al mismo tiempo en el mismo punto que otro ciudadano
	 */
	public List<String> ciudadanosContacto(String documento, String fecha1, String fecha2, String id_punto, String fechahora)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlUtil.ciudadanosContacto(pm, fecha1, fecha2, id_punto, fechahora);
	}
    
	/**
	 * Consulta los ciudadanos no vacunados en un rango de fechas
	 */
	public List<Ciudadano> darCiudadanosNoVacunados( String punto, String eps, String condiprior, String fecha1, String fecha2){
		
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlUtil.darCiudadanosNoVacunados(pm, punto, eps, condiprior, fecha1, fecha2);
		
	}
	
	/**
	 * Consulta la cantidad de citas de los puntos en las semanas del año
	 */
	public List<Tupla> citasPuntosSemanas(String eps)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlUtil.citasPuntosSemanas(pm, eps);
	}
	
	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos
	 * @return Un arreglo con 14 números que indican el número de tuplas borradas en las tablas EPS, ROLES, 
	 * ESTADO, ETAPA, CONDICIONPRIORIZACION, PUNTO, VACUNA, ASIGNADA, CIUDADANO, VACUNACION,
	 * PRIORIZACION, INFOUSUARIO, USUARIO, CITA
	 */
	public Long[] limpiarVacuAndes()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			Long [] resp = sqlUtil.limpiarVacuAndes (pm);
			tx.commit ();
			log.info ("Borrada la base de datos");
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return new Long[] {-1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L};
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}

	/**
	 * Métodon que obtiene la etapa actual de VacuAndes
	 * @return el número de la etapa actual
	 */
	public Long darEtapaVacuAndes()
	{
		Long resp = sqlUtil.darEtapaVacuAndes( pmf.getPersistenceManager() );
		return resp;
	}

	/**
	 * Método que actualiza la etapa actual de VacuAndes
	 * @param numero_etapa - El número de la nueva etapa
	 * @return la cantidad de tuplas modificadas o -1 si ocurre una excepción
	 */
	public Long actualizarEtapaVacuAndes( Long numero_etapa )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlUtil.actualizarEtapaVacuAndes( pm, numero_etapa );
			tx.commit ();
			log.info ("Etapa de VacuAndes actualizada");
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que adiciona la etapa actual de VacuAndes
	 * @param numero_etapa - El número de la etapa
	 * @return el número que indica la cantidad de tuplas insertadas
	 */
	public Long adicionarEtapaVacuAndes( Long numero_etapa )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			Long resp = sqlUtil.adicionarEtapaVacuAndes( pm, numero_etapa );
			tx.commit ();
			log.info ("Etapa de VacuAndes adicionada");
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}


}
