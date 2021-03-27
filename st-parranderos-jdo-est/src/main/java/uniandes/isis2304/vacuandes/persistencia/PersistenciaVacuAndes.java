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
		tablas.add ("AGENDADA");
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
	 * @return La cadena de caracteres con el nombre de la tabla de Agendada de VacuAndes
	 */
	public String darTablaAgendada()
	{
		return tablas.get( 15 );
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
	
//Ejemplo de cómo hacerlo

//	/* ****************************************************************
//	 * 			Métodos para manejar los TIPOS DE BEBIDA
//	 *****************************************************************/
//
//	/**
//	 * Método que inserta, de manera transaccional, una tupla en la tabla TipoBebida
//	 * Adiciona entradas al log de la aplicación
//	 * @param nombre - El nombre del tipo de bebida
//	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepción
//	 */
//	public TipoBebida adicionarTipoBebida(String nombre)
//	{
//		PersistenceManager pm = pmf.getPersistenceManager();
//        Transaction tx=pm.currentTransaction();
//        try
//        {
//            tx.begin();
//            long idTipoBebida = nextval ();
//            long tuplasInsertadas = sqlTipoBebida.adicionarTipoBebida(pm, idTipoBebida, nombre);
//            tx.commit();
//            
//            log.trace ("Inserción de tipo de bebida: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
//            
//            return new TipoBebida (idTipoBebida, nombre);
//        }
//        catch (Exception e)
//        {
////        	e.printStackTrace();
//        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
//        	return null;
//        }
//        finally
//        {
//            if (tx.isActive())
//            {
//                tx.rollback();
//            }
//            pm.close();
//        }
//	}
//
//	/**
//	 * Método que elimina, de manera transaccional, una tupla en la tabla TipoBebida, dado el nombre del tipo de bebida
//	 * Adiciona entradas al log de la aplicación
//	 * @param nombre - El nombre del tipo de bebida
//	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
//	 */
//	public long eliminarTipoBebidaPorNombre (String nombre) 
//	{
//		PersistenceManager pm = pmf.getPersistenceManager();
//        Transaction tx=pm.currentTransaction();
//        try
//        {
//            tx.begin();
//            long resp = sqlTipoBebida.eliminarTipoBebidaPorNombre(pm, nombre);
//            tx.commit();
//            return resp;
//        }
//        catch (Exception e)
//        {
////        	e.printStackTrace();
//        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
//            return -1;
//        }
//        finally
//        {
//            if (tx.isActive())
//            {
//                tx.rollback();
//            }
//            pm.close();
//        }
//	}
//
//	/**
//	 * Método que elimina, de manera transaccional, una tupla en la tabla TipoBebida, dado el identificador del tipo de bebida
//	 * Adiciona entradas al log de la aplicación
//	 * @param idTipoBebida - El identificador del tipo de bebida
//	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
//	 */
//	public long eliminarTipoBebidaPorId (long idTipoBebida) 
//	{
//		PersistenceManager pm = pmf.getPersistenceManager();
//        Transaction tx=pm.currentTransaction();
//        try
//        {
//            tx.begin();
//            long resp = sqlTipoBebida.eliminarTipoBebidaPorId(pm, idTipoBebida);
//            tx.commit();
//            return resp;
//        }
//        catch (Exception e)
//        {
////        	e.printStackTrace();
//        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
//            return -1;
//        }
//        finally
//        {
//            if (tx.isActive())
//            {
//                tx.rollback();
//            }
//            pm.close();
//        }
//	}
//
//	/**
//	 * Método que consulta todas las tuplas en la tabla TipoBebida
//	 * @return La lista de objetos TipoBebida, construidos con base en las tuplas de la tabla TIPOBEBIDA
//	 */
//	public List<TipoBebida> darTiposBebida ()
//	{
//		return sqlTipoBebida.darTiposBebida (pmf.getPersistenceManager());
//	}
// 
//	/**
//	 * Método que consulta todas las tuplas en la tabla TipoBebida que tienen el nombre dado
//	 * @param nombre - El nombre del tipo de bebida
//	 * @return La lista de objetos TipoBebida, construidos con base en las tuplas de la tabla TIPOBEBIDA
//	 */
//	public List<TipoBebida> darTipoBebidaPorNombre (String nombre)
//	{
//		return sqlTipoBebida.darTiposBebidaPorNombre (pmf.getPersistenceManager(), nombre);
//	}
// 
//	/**
//	 * Método que consulta todas las tuplas en la tabla TipoBebida con un identificador dado
//	 * @param idTipoBebida - El identificador del tipo de bebida
//	 * @return El objeto TipoBebida, construido con base en las tuplas de la tabla TIPOBEBIDA con el identificador dado
//	 */
//	public TipoBebida darTipoBebidaPorId (long idTipoBebida)
//	{
//		return sqlTipoBebida.darTipoBebidaPorId (pmf.getPersistenceManager(), idTipoBebida);
//	}
	
	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 14 números que indican el número de tuplas borradas en las tablas EPS, ROLES, 
	 * ESTADO, ETAPA, CONDICIONPRIORIZACION, PUNTO, VACUNA, ASIGNADA, CIUDADANO, VACUNACION,
	 * PRIORIZACION, INFOUSUARIO, USUARIO, CITA, AGENDADA
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
        	return new Long[] {-1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L};
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
