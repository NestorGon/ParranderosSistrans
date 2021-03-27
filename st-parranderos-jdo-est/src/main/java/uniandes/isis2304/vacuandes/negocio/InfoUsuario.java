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
 * Clase para modelar el concepto INFOUSUARIO del negocio de VacuAndes
 *
 * @author Néstor González
 */
public class InfoUsuario implements VOInfoUsuario {
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El login del usuario
	 */
	private String login;
	
	/**
	 * El trabajo del usuario
	 */
	private String trabajo;
	
	/**
	 * El rol del usuario en la aplicación
	 */
	private Long id_roles;
	
	/**
	 * El punto de vacunación al que se encuentra asociado
	 */
	private String id_punto;
	
	public InfoUsuario(){
	}
	
	public InfoUsuario( String login, String trabajo, Long id_roles, String id_punto ) {
		this.login = login;
		this.trabajo = trabajo;
		this.id_roles = id_roles;
		this.id_punto = id_punto;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getTrabajo() {
		return trabajo;
	}

	public void setTrabajo(String trabajo) {
		this.trabajo = trabajo;
	}

	public Long getId_roles() {
		return id_roles;
	}

	public void setId_roles(Long id_roles) {
		this.id_roles = id_roles;
	}

	public String getId_punto() {
		return id_punto;
	}

	public void setId_punto(String id_punto) {
		this.id_punto = id_punto;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del InfoUsuario
	 */
	public String toString(){
		return "InfoUsuario [login=" + login + ", trabajo=" + trabajo + ", id_roles=" + id_roles + ", id_punto=" + id_punto + "]";
	}
}
