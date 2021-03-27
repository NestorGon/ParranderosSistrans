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

package uniandes.isis2304.vacuandes.negocio;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.JsonObject;

import uniandes.isis2304.vacuandes.persistencia.PersistenciaVacuAndes;

/**
 * Clase principal del negocio
 *
 * @author Néstor González
 */
public class VacuAndes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger( VacuAndes.class.getName() );
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaVacuAndes pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public VacuAndes ()
	{
		pp = PersistenciaVacuAndes.getInstance ();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public VacuAndes( JsonObject tableConfig )
	{
		pp = PersistenciaVacuAndes.getInstance( tableConfig );
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia()
	{
		pp.cerrarUnidadPersistencia();
	}
//Ejemplo de cómo hacerlo
//	
//	/* ****************************************************************
//	 * 			Métodos para manejar los TIPOS DE BEBIDA
//	 *****************************************************************/
//	/**
//	 * Adiciona de manera persistente un tipo de bebida 
//	 * Adiciona entradas al log de la aplicación
//	 * @param nombre - El nombre del tipo de bebida
//	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepción
//	 */
//	public TipoBebida adicionarTipoBebida (String nombre)
//	{
//        log.info ("Adicionando Tipo de bebida: " + nombre);
//        TipoBebida tipoBebida = pp.adicionarTipoBebida (nombre);		
//        log.info ("Adicionando Tipo de bebida: " + tipoBebida);
//        return tipoBebida;
//	}
//	
//	/**
//	 * Elimina un tipo de bebida por su nombre
//	 * Adiciona entradas al log de la aplicación
//	 * @param nombre - El nombre del tipo de bebida a eliminar
//	 * @return El número de tuplas eliminadas
//	 */
//	public long eliminarTipoBebidaPorNombre (String nombre)
//	{
//		log.info ("Eliminando Tipo de bebida por nombre: " + nombre);
//        long resp = pp.eliminarTipoBebidaPorNombre (nombre);		
//        log.info ("Eliminando Tipo de bebida por nombre: " + resp + " tuplas eliminadas");
//        return resp;
//	}
//	
//	/**
//	 * Elimina un tipo de bebida por su identificador
//	 * Adiciona entradas al log de la aplicación
//	 * @param idTipoBebida - El id del tipo de bebida a eliminar
//	 * @return El número de tuplas eliminadas
//	 */
//	public long eliminarTipoBebidaPorId (long idTipoBebida)
//	{
//		log.info ("Eliminando Tipo de bebida por id: " + idTipoBebida);
//        long resp = pp.eliminarTipoBebidaPorId (idTipoBebida);		
//        log.info ("Eliminando Tipo de bebida por id: " + resp + " tuplas eliminadas");
//        return resp;
//	}
//	
//	/**
//	 * Encuentra todos los tipos de bebida en Parranderos
//	 * Adiciona entradas al log de la aplicación
//	 * @return Una lista de objetos TipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
//	 */
//	public List<TipoBebida> darTiposBebida ()
//	{
//		log.info ("Consultando Tipos de bebida");
//        List<TipoBebida> tiposBebida = pp.darTiposBebida ();	
//        log.info ("Consultando Tipos de bebida: " + tiposBebida.size() + " existentes");
//        return tiposBebida;
//	}
//
//	/**
//	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
//	 * Adiciona entradas al log de la aplicación
//	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
//	 */
//	public List<VOTipoBebida> darVOTiposBebida ()
//	{
//		log.info ("Generando los VO de Tipos de bebida");        
//        List<VOTipoBebida> voTipos = new LinkedList<VOTipoBebida> ();
//        for (TipoBebida tb : pp.darTiposBebida ())
//        {
//        	voTipos.add (tb);
//        }
//        log.info ("Generando los VO de Tipos de bebida: " + voTipos.size() + " existentes");
//        return voTipos;
//	}
//
//	/**
//	 * Encuentra el tipos de bebida en Parranderos con el nombre solicitado
//	 * Adiciona entradas al log de la aplicación
//	 * @param nombre - El nombre de la bebida
//	 * @return Un objeto TipoBebida con el tipos de bebida de ese nombre que conoce la aplicación, 
//	 * lleno con su información básica
//	 */
//	public TipoBebida darTipoBebidaPorNombre (String nombre)
//	{
//		log.info ("Buscando Tipo de bebida por nombre: " + nombre);
//		List<TipoBebida> tb = pp.darTipoBebidaPorNombre (nombre);
//		return !tb.isEmpty () ? tb.get (0) : null;
//	}

	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	/**
	 * Elimina todas las tuplas en las tablas de VacuAndes
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 14 números que indican el número de tuplas borradas en las tablas EPS, ROLES, 
	 * ESTADO, ETAPA, CONDICIONPRIORIZACION, PUNTO, VACUNA, ASIGNADA, CIUDADANO, VACUNACION,
	 * PRIORIZACION, INFOUSUARIO, USUARIO, CITA, AGENDADA
	 */
	public Long [] limpiarVacuAndes ()
	{
        log.info ("Limpiando la BD de VacuAndes");
        Long [] borrrados = pp.limpiarVacuAndes();	
        log.info ("Limpiando la BD de VacuAndes: Listo!");
        return borrrados;
	}
}
