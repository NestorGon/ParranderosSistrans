/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
@param documento - El documento de identificación del ciudadano * Universidad	de	los	Andes	(Bogotá	- Colombia)
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

import java.util.Date;
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
	
	/* ****************************************************************
	 * 			Métodos para manejar las CITAS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una cita
	 * Adiciona entradas al log de la aplicación
	 * @param fechaHora - Fecha y hora de la cita
	 * @param finalizada - Si la cita ya se finalizó o no
	 * @param ciudadano - El documento de identificación del ciudadano asociado a la cita
	 * @return El objeto Cita adicionado. null si ocurre alguna Excepción
	 */
	public Cita adicionarCita( Date fechaHora, String finalizada, String ciudadano )
	{
       log.info( "Adicionando Cita: " + fechaHora + " - " + ciudadano );
       Cita cita = pp.adicionarCita( fechaHora, finalizada, ciudadano );		
       log.info( "Cita adicionada: " + cita );
       return cita;
	}
	
	/**
	 * Elimina una cita por su fecha y hora y el documento del ciudadano
	 * Adiciona entradas al log de la aplicación
	 * @param fechaHora - Fecha y hora de la cita
	 * @param documento - El documento de identificación del ciudadano asociado a la cita
	 * @return El número de tuplas eliminadas
	 */
	public Long eliminarCita( Date fechaHora, String ciudadano )
	{
		log.info( "Eliminando Cita: " + fechaHora + " - " + ciudadano  );
		Long resp = pp.eliminarCita( fechaHora, ciudadano );		
		log.info( "Cita eliminada: " + resp + " tuplas eliminadas" );
		return resp;
	}
	
	/**
	 * Encuentra todas las citas en VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Cita con todas las citas que conoce la aplicación
	 */
	public List<Cita> darCitas()
	{
		log.info ("Consultando Citas");
		List<Cita> citas = pp.darCitas();	
		log.info ("Consultando Citas: " + citas.size() + " existentes");
		return citas;
	}

	/**
	 * Encuentra todas las citas en VacuAndes y las devuelve como una lista de VOCita
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOCita con todas las citas que conoce la aplicación
	 */
	public List<VOCita> darVOCitas()
	{
		log.info ("Generando los VO de Citas");        
       List<VOCita> voCitas = new LinkedList<VOCita>();
       for( Cita cita : pp.darCitas() ) {
    	   voCitas.add (cita);
       }
       log.info ("Generando los VO de Citas: " + voCitas.size() + " existentes");
       return voCitas;
	}

	/**
	 * Encuentra la cita en VacuAndes con la fecha y hora y ciudadano
	 * Adiciona entradas al log de la aplicación
	 * @param fechaHora - Fecha y hora de la cita
	 * @param documento - El documento de identificación del ciudadano asociado a la cita
	 * @return Un objeto Cita con la fecha y hora y ciudadano, lleno con su información básica
	 */
	public Cita darCita( Date fechaHora, String ciudadano )
	{
		log.info ("Buscando Cita: " + fechaHora + " - " + ciudadano);
		Cita cita = pp.darCita( fechaHora, ciudadano );
		return cita;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar la PRIORIZACION
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente una Priorizacion
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return El objeto Priorizacion adicionado. null si ocurre alguna Excepción
	 */
	public Priorizacion adicionarPriorizacion( String documento, String descripcion )
	{
       log.info( "Adicionando Priorización: " + documento + " - " + descripcion );
       Priorizacion prior = pp.adicionarPriorizacion( documento, descripcion );		
       log.info( "Priorización adicionada: " + prior );
       return prior;
	}
	
	/**
	 * Elimina una priorización por su documento y descripcion
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return El número de tuplas eliminadas
	 */
	public Long eliminarPriorizacion( String documento, String descripcion )
	{
		log.info( "Eliminando Priorización: " + documento + " - " + descripcion );
		Long resp = pp.eliminarPriorizacion( documento, descripcion );		
		log.info( "Priorización eliminada: " + resp + " tuplas eliminadas" );
		return resp;
	}
	
	/**
	 * Encuentra todas las priorizaciones en VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Priorizacion con todas las priorizaciones que conoce la aplicación
	 */
	public List<Priorizacion> darPriorizaciones()
	{
		log.info ("Consultando Priorizaciones");
		List<Priorizacion> prior = pp.darPriorizaciones();	
		log.info ("Consultando Priorizaciones: " + prior.size() + " existentes");
		return prior;
	}

	/**
	 * Encuentra todas las priorizaciones en VacuAndes y las devuelve como una lista de VOPriorizacion
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOPriorizacion con todas las priorizaciones que conoce la aplicación
	 */
	public List<VOPriorizacion> darVOPriorizaciones()
	{
		log.info ("Generando los VO de Priorizaciones");        
       List<VOPriorizacion> voPriorizaciones = new LinkedList<VOPriorizacion>();
       for( Priorizacion prior : pp.darPriorizaciones() ) {
    	   voPriorizaciones.add( prior );
       }
       log.info ("Generando los VO de Priorizaciones: " + voPriorizaciones.size() + " existentes");
       return voPriorizaciones;
	}

	/**
	 * Encuentra la priorización en VacuAndes con el documento y descripcion
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param descripcion - La descripción de la condición de priorización
	 * @return Un objeto Priorizacion con la priorización del documento y descripcion, lleno con su información básica
	 */
	public Priorizacion darPriorizacion( String documento, String descripcion )
	{
		log.info ("Buscando Priorizacion: " + documento + " - " + descripcion);
		Priorizacion prior = pp.darPriorizacion( documento, descripcion );
		return prior;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los ROLES
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un rol
	 * Adiciona entradas al log de la aplicación
	 * @param id - El identificador del rol
	 * @param rol - La cadena de texto con el rol
	 * @return El objeto Rol adicionado. null si ocurre alguna Excepción
	 */
	public Rol adicionarRol( String rol )
	{
       log.info( "Adicionando Rol: " + rol );
       Rol rolObj = pp.adicionarRol( rol );		
       log.info( "Rol adicionado: " + rolObj );
       return rolObj;
	}
	
	/**
	 * Elimina un rol por su id
	 * Adiciona entradas al log de la aplicación
	 * @param id - El identificador del rol
	 * @return El número de tuplas eliminadas
	 */
	public Long eliminarRol( Long id )
	{
		log.info( "Eliminando Rol: " + id  );
		Long resp = pp.eliminarRol( id );		
		log.info( "Rol eliminado: " + resp + " tuplas eliminadas" );
		return resp;
	}
	
	/**
	 * Encuentra todos los roles en VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Rol con todos los roles que conoce la aplicación
	 */
	public List<Rol> darRoles()
	{
		log.info ("Consultando Roles");
		List<Rol> roles = pp.darRoles();	
		log.info ("Consultando Roles: " + roles.size() + " existentes");
		return roles;
	}

	/**
	 * Encuentra todos los roles en VacuAndes y los devuelve como una lista de VORol
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VORol con todos los roles que conoce la aplicación
	 */
	public List<VORol> darVORoles()
	{
		log.info ("Generando los VO de Roles");        
		List<VORol> voRoles = new LinkedList<VORol>();
		for( Rol rol : pp.darRoles() ) {
			voRoles.add (rol);
		}
		log.info ("Generando los VO de Roles: " + voRoles.size() + " existentes");
		return voRoles;
	}

	/**
	 * Encuentra el rol en VacuAndes con el id
	 * Adiciona entradas al log de la aplicación
	 * @param id - El identificador del rol
	 * @return Un objeto Rol lleno con su información básica
	 */
	public Rol darRol( Long id )
	{
		log.info( "Buscando Rol: " + id );
		Rol rol = pp.darRol( id );
		return rol;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar la INFORMACION DE LOS USUARIOS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente la información de un usuario
	 * Adiciona entradas al log de la aplicación
	 * @param login - El login del usuario
	 * @param trabajo - El trabajo del usuario
	 * @param roles - El rol del usuario en la aplicación
	 * @param punto - El punto de vacunación al que se encuentra asociado
	 * @return El objeto InfoUsuario adicionado. null si ocurre alguna Excepción
	 */
	public InfoUsuario adicionarInfoUsuario( String login, String trabajo, Long roles, String punto )
	{
       log.info( "Adicionando InfoUsuario: " + login + " - " + trabajo );
       InfoUsuario info = pp.adicionarInfoUsuario( login, trabajo, roles, punto );		
       log.info( "InfoUsuario adicionada: " + info );
       return info;
	}
	
	/**
	 * Elimina la información de un usuario por su login
	 * Adiciona entradas al log de la aplicación
	 * @param login - El login único de un usuario
	 * @return El número de tuplas eliminadas
	 */
	public Long eliminarInfoUsuario( String login )
	{
		log.info( "Eliminando InfoUsuario: " + login );
		Long resp = pp.eliminarInfoUsuario( login );		
		log.info( "InfoUsuario eliminada: " + resp + " tuplas eliminadas" );
		return resp;
	}
	
	/**
	 * Encuentra toda la información de todos los usuarios en VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos InfoUsuario con toda la información de los usuarios que conoce la aplicación
	 */
	public List<InfoUsuario> darInfoUsuarios()
	{
		log.info ("Consultando InfoUsuarios");
		List<InfoUsuario> infos = pp.darInfoUsuarios();	
		log.info ("Consultando InfoUsuarios: " + infos.size() + " existentes");
		return infos;
	}

	/**
	 * Encuentra toda la información de todos los usuarios en VacuAndes y las devuelve como una lista de VOInfoUsuario
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOInfoUsuario con toda la información de los usuarios que conoce la aplicación
	 */
	public List<VOInfoUsuario> darVOInfoUsuarios()
	{
		log.info ("Generando los VO de InfoUsuarios");        
       List<VOInfoUsuario> voInfos = new LinkedList<VOInfoUsuario>();
       for( InfoUsuario info : pp.darInfoUsuarios() ) {
    	   voInfos.add (info);
       }
       log.info ("Generando los VO de InfoUsuarios: " + voInfos.size() + " existentes");
       return voInfos;
	}

	/**
	 * Encuentra la cita en VacuAndes con la fecha y hora y ciudadano
	 * Adiciona entradas al log de la aplicación
	 * @param login - El login único de un usuario
	 * @return Un objeto Cita con la fecha y hora y ciudadano, lleno con su información básica
	 */
	public InfoUsuario darInfoUsuario( String login )
	{
		log.info( "Buscando InfoUsuario: " + login );
		InfoUsuario info = pp.darInfoUsuario( login );
		return info;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los ESTADOS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un estado
	 * Adiciona entradas al log de la aplicación
	 * @param descripcion - La descripción del estado
	 * @return El objeto Estado adicionado. null si ocurre alguna Excepción
	 */
	public Estado adicionarEstado( String descripcion )
	{
       log.info( "Adicionando Estado: " + descripcion );
       Estado estado = pp.adicionarEstado( descripcion );		
       log.info( "Estado adicionado: " + estado );
       return estado;
	}
	
	/**
	 * Elimina un estado por su id
	 * Adiciona entradas al log de la aplicación
	 * @param id - El identificador del estado
	 * @return El número de tuplas eliminadas
	 */
	public Long eliminarEstado( Long id )
	{
		log.info( "Eliminando Estado: " + id );
		Long resp = pp.eliminarEstado( id );		
		log.info( "Estado eliminado: " + resp + " tuplas eliminadas" );
		return resp;
	}
	
	/**
	 * Encuentra todos los estados en VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Estado con todos los estados que conoce la aplicación
	 */
	public List<Estado> darEstados()
	{
		log.info ("Consultando Estados");
		List<Estado> estados = pp.darEstados();	
		log.info ("Consultando Estados: " + estados.size() + " existentes");
		return estados;
	}

	/**
	 * Encuentra todos los estados en VacuAndes y los devuelve como una lista de VOEstado
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOEstado con todos los estados que conoce la aplicación
	 */
	public List<VOEstado> darVOEstados()
	{
		log.info ("Generando los VO de Estados");        
       List<VOEstado> voEstados = new LinkedList<VOEstado>();
       for( Estado estado : pp.darEstados() ) {
    	   voEstados.add (estado);
       }
       log.info ("Generando los VO de Estados: " + voEstados.size() + " existentes");
       return voEstados;
	}

	/**
	 * Encuentra el estado en VacuAndes con el id
	 * Adiciona entradas al log de la aplicación
	 * @param id - El identificador del estado
	 * @return Un objeto Estado con el id, lleno con su información básica
	 */
	public Estado darEstado( Long id )
	{
		log.info ("Buscando Estado: " + id );
		Estado estado = pp.darEstado( id );
		return estado;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los CIUDADANOS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un ciudadano
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
	public Ciudadano adicionarCiudadano( String documento, String nombre, Date nacimiento, String habilitado, Long estado, String eps, Integer etapa )
	{
       log.info( "Adicionando Ciudadano: " + documento + " - " + nombre );
       Ciudadano ciudadano = pp.adicionarCiudadano( documento, nombre, nacimiento, habilitado, estado, eps, etapa );		
       log.info( "Ciudadano adicionado: " + ciudadano );
       return ciudadano;
	}
	
	/**
	 * Actualiza de manera persistente un ciudadano
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
	public Ciudadano actualizarCiudadano( String documento, String nombre, Date nacimiento, String habilitado, Long estado, String eps, Integer etapa )
	{
       log.info( "Actualizando Ciudadano: " + documento + " - " + nombre );
       Ciudadano ciudadano = pp.actualizarCiudadano( documento, nombre, nacimiento, habilitado, estado, eps, etapa );		
       log.info( "Ciudadano actualizado: " + ciudadano );
       return ciudadano;
	}
	
	/**
	 * Elimina un ciudadano por su documento
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @return El número de tuplas eliminadas
	 */
	public Long eliminarCiudadano( String documento )
	{
		log.info( "Eliminando Ciudadano: " + documento );
		Long resp = pp.eliminarCiudadano( documento );		
		log.info( "Ciudadano eliminado: " + resp + " tuplas eliminadas" );
		return resp;
	}
	
	/**
	 * Encuentra todos los ciudadanos en VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Ciudadano con todos los ciudadanos que conoce la aplicación
	 */
	public List<Ciudadano> darCiudadanos()
	{
		log.info ("Consultando Ciudadanos");
		List<Ciudadano> ciudadanos = pp.darCiudadanos();	
		log.info ("Consultando Ciudadanos: " + ciudadanos.size() + " existentes");
		return ciudadanos;
	}

	/**
	 * Encuentra todos los ciudadanos en VacuAndes y los devuelve como una lista de VOCiudadano
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOCiudadano con todos los ciudadanos que conoce la aplicación
	 */
	public List<VOCiudadano> darVOCiudadanos()
	{
		log.info ("Generando los VO de Ciudadanos");        
       List<VOCiudadano> voCiudadanos = new LinkedList<VOCiudadano>();
       for( Ciudadano ciudadano : pp.darCiudadanos() ) {
    	   voCiudadanos.add (ciudadano);
       }
       log.info ("Generando los VO de Ciudadanos: " + voCiudadanos.size() + " existentes");
       return voCiudadanos;
	}

	/**
	 * Encuentra el ciudadano en VacuAndes con el documento
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @return Un objeto Ciudadano con el documento, lleno con su información básica
	 */
	public Ciudadano darCiudadano( String documento )
	{
		log.info ("Buscando Ciudadano: " + documento );
		Ciudadano ciudadano = pp.darCiudadano( documento );
		return ciudadano;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los USUARIOS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un usuario
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param login - El login del usuario
	 * @return El objeto Usuario adicionado. null si ocurre alguna Excepción
	 */
	public Usuario adicionarUsuario( String documento, String login )
	{
       log.info( "Adicionando Usuario: " + documento + " - " + login );
       Usuario usuario = pp.adicionarUsuario( documento, login );		
       log.info( "Usuario adicionado: " + usuario );
       return usuario;
	}
	
	/**
	 * Elimina un usuario por su documento
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @return El número de tuplas eliminadas
	 */
	public Long eliminarUsuario( String documento )
	{
		log.info( "Eliminando Usuario: " + documento );
		Long resp = pp.eliminarUsuario( documento );		
		log.info( "Usuario eliminado: " + resp + " tuplas eliminadas" );
		return resp;
	}
	
	/**
	 * Encuentra todos los usuarios en VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Usuario con todos los usuarios que conoce la aplicación
	 */
	public List<Usuario> darUsuarios()
	{
		log.info ("Consultando Usuarios");
		List<Usuario> usuarios = pp.darUsuarios();	
		log.info ("Consultando Usuarios: " + usuarios.size() + " existentes");
		return usuarios;
	}

	/**
	 * Encuentra todos los usuarios en VacuAndes y los devuelve como una lista de VOUsuario
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOUsuario con todos los usuarios que conoce la aplicación
	 */
	public List<VOUsuario> darVOUsuarios()
	{
		log.info ("Generando los VO de Usuarios");        
       List<VOUsuario> voUsuarios = new LinkedList<VOUsuario>();
       for( Usuario usuario : pp.darUsuarios() ) {
    	   voUsuarios.add (usuario);
       }
       log.info ("Generando los VO de Usuarios: " + voUsuarios.size() + " existentes");
       return voUsuarios;
	}

	/**
	 * Encuentra el usuario en VacuAndes con el documento
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @return Un objeto Usuario con el documento, lleno con su información básica
	 */
	public Usuario darUsuario( String documento )
	{
		log.info ("Buscando Usuario: " + documento );
		Usuario usuario = pp.darUsuario( documento );
		return usuario;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar la VACUNACION
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una vacunación
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @param punto - El identificador del punto de vacunación
	 * @return El objeto Vacunacion adicionado. null si ocurre alguna Excepción
	 */
	public Vacunacion adicionarVacunacion( String documento, String eps, String punto )
	{
       log.info( "Adicionando Vacunacion: " + documento + " - " + eps );
       Vacunacion vacunacion = pp.adicionarVacunacion( documento, eps, punto );		
       log.info( "Vacunacion adicionada: " + vacunacion );
       return vacunacion;
	}
	
	/**
	 * Elimina una cita por su fecha y hora y el documento del ciudadano
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @return El número de tuplas eliminadas
	 */
	public Long eliminarVacunacion( String documento, String eps )
	{
		log.info( "Eliminando Vacunacion: " + documento + " - " + eps  );
		Long resp = pp.eliminarVacunacion( documento, eps );		
		log.info( "Vacunacion eliminada: " + resp + " tuplas eliminadas" );
		return resp;
	}
	
	/**
	 * Encuentra todas las vacunaciones en VacuAndes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Vacunacion con todas las vacunaciones que conoce la aplicación
	 */
	public List<Vacunacion> darVacunaciones()
	{
		log.info ("Consultando Vacunaciones");
		List<Vacunacion> vacunaciones = pp.darVacunaciones();	
		log.info ("Consultando Vacunaciones: " + vacunaciones.size() + " existentes");
		return vacunaciones;
	}

	/**
	 * Encuentra todas las vacunaciones en VacuAndes y las devuelve como una lista de VOVacunacion
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOVacunacion con todas las vacunaciones que conoce la aplicación
	 */
	public List<VOVacunacion> darVOVacunaciones()
	{
		log.info ("Generando los VO de Vacunaciones");        
       List<VOVacunacion> voVacunaciones = new LinkedList<VOVacunacion>();
       for( Vacunacion vacunacion : pp.darVacunaciones() ) {
    	   voVacunaciones.add (vacunacion);
       }
       log.info ("Generando los VO de Vacunaciones: " + voVacunaciones.size() + " existentes");
       return voVacunaciones;
	}

	/**
	 * Encuentra la vacunación en VacuAndes con el documento y eps
	 * Adiciona entradas al log de la aplicación
	 * @param documento - El documento de identificación del ciudadano
	 * @param eps - El identificador de la eps
	 * @return Un objeto Vacunacion con el documento y eps, lleno con su información básica
	 */
	public Vacunacion darVacunacion( String documento, String eps )
	{
		log.info ("Buscando Vacunacion: " + documento + " - " + eps );
		Vacunacion vacunacion = pp.darVacunacion( documento, eps );
		return vacunacion;
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
	 * PRIORIZACION, INFOUSUARIO, USUARIO, CITA
	 */
	public Long [] limpiarVacuAndes ()
	{
        log.info ("Limpiando la BD de VacuAndes");
        Long [] borrrados = pp.limpiarVacuAndes();	
        log.info ("Limpiando la BD de VacuAndes: Listo!");
        return borrrados;
	}
}
