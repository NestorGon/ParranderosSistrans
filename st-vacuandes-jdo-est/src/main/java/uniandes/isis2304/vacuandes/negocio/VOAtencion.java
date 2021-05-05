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
 * Interfaz para los métodos get de ATENCION. 
 * 
 * @author Néstor González
 */
public interface VOAtencion {
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
     /**
	 * @return La descripcion de la condicion de priorizacion
	 */
	public String getDescripcion_condprior();
	
	/**
	 * @return El identificador del punto de vacunación
	 */
	public String getId_punto();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la atención
	 */
	public String toString();

}
