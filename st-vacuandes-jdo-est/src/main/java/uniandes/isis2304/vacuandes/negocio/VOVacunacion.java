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
public interface VOVacunacion {
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
     /**
	 * @return El documento de identificación del ciudadano
	 */
	public String getDocumento_ciudadano();
	
	/**
	 * @return El identificador de la eps
	 */
	public String getId_eps();
	
	/**
	 * @return El identificador del punto de vacunacion
	 */
	public String getId_punto();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la vacunación
	 */
	public String toString();

}
