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

/**
 * Interfaz para los métodos get de PRIORIZACION. 
 * 
 * @author Néstor González
 */
public interface VOPriorizacion {
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
     /**
	 * @return El documento de identificación del ciudadano
	 */
	public String getDocumento_ciudadano();
	
	/**
	 * @return La descripción de la condición de priorización
	 */
	public String getDescripcion_condprior();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la priorizacion
	 */
	public String toString();

}
