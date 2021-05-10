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

import java.sql.Date;

/**
 * Interfaz para los métodos get de CITA. 
 * 
 * @author Néstor González
 */
public interface VOCita {
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	/**
	 * @return La fecha y hora de la cita
	 */
	public String getFechaHora();
	
	/**
	 * @return Si la cita ya se finalizó o no
	 */
	public String getFinalizada();
	
	/**
	 * @return El documento de identificación del ciudadano asociado a la cita
	 */
	public String getDocumento_ciudadano();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la cita
	 */
	public String toString();
	
	/**
	 * @return Entrega el id del punto de vacunacion
	 */
	public String getId_punto();
}
