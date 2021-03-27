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
 * Interfaz para los métodos get de INFOUSUARIO.
 *
 * @author Néstor González
 */
public interface VOInfoUsuario {
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
     /**
	 * @return El login del usuario
	 */
	public String getLogin();
	
	/**
	 * @return El trabajo del usuario
	 */
	public String getTrabajo();
	
	/**
	 * @return El rol del usuario en la aplicación
	 */
	public Long getId_roles();
	
	/**
	 * @return El punto de vacunación al que se encuentra asociado
	 */
	public String getId_punto();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del InfoUsuario
	 */
	public String toString();
}
