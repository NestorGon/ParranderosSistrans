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

import java.util.Date;

/**
 * Interfaz para los métodos get de CIUDADANO. 
 * 
 * @author Néstor González
 */
public interface VOCiudadano {
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
     /**
	 * @return El documento de identificación del ciudadano
	 */
	public String getDocumento();
	
	/**
	 * @return El nombre del ciudadano
	 */
	public String getNombre();
	
	/**
	 * @return La fecha de nacimiento del ciudadano
	 */
	public Date getNacimiento();
	
	/**
	 * @return Si el ciudadano está o no habilitado para vacunación
	 */
	public String getHabilitado();
	
	/**
	 * @return El id del estado en el que se encuentra el ciudadano en la vacunación
	 */
	public Long getId_estado();
	
	/**
	 * @return El id de la EPS a la que el ciuadano se encuentra afiliado
	 */
	public String getId_eps();
	
	/**
	 * @return El número de la etapa a la que el ciudadano pertenece
	 */
	public Integer getNumero_etapa();
	
	/**
	 * @return El sexo del ciudadano
	 */
	public String getSexo();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del ciudadano
	 */
	public String toString();
}
