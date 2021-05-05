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
 * Clase para modelar el concepto CIUDADANO del negocio de VacuAndes
 *
 * @author Néstor González
 */
public class Ciudadano implements VOCiudadano {
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El documento de identificación único de los ciudadanos
	 */
	private String documento;
	
	/**
	 * El nombre del ciudadano
	 */
	private String nombre;
	
	/**
	 * La fecha de nacimiento del ciudadano
	 */
	private Date nacimiento;
	
	/**
	 * Si el ciudadano está o no habilitado para vacunación
	 */
	private String habilitado;
	
	/**
	 * El id del estado en el que se encuentra el ciudadano en la vacunación
	 */
	private Long id_estado;
	
	/**
	 * El id de la EPS a la que el ciuadano se encuentra afiliado
	 */
	private String id_eps;
	
	/**
	 * El número de la etapa a la que el ciudadano pertenece
	 */
	private Integer numero_etapa;
	
	/**
	 * El sexo del ciudadano
	 */
	private String sexo;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public Ciudadano() {
	}

	public Ciudadano( String documento, String nombre, Date nacimiento, String habilitado, Long id_estado, String id_eps, Integer numero_etapa, String sexo ) {
		this.documento = documento;
		this.nombre = nombre;
		this.nacimiento = nacimiento;
		this.habilitado = habilitado;
		this.id_estado = id_estado;
		this.id_eps = id_eps;
		this.numero_etapa = numero_etapa;
		this.sexo = sexo;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento( String documento ) {
		this.documento = documento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	public Date getNacimiento() {
		return nacimiento;
	}

	public void setNacimiento( Date nacimiento ) {
		this.nacimiento = nacimiento;
	}

	public String getHabilitado() {
		return habilitado;
	}

	public void setHabilitado( String habilitado ) {
		this.habilitado = habilitado;
	}

	public Long getId_estado() {
		return id_estado;
	}

	public void setId_estado( Long id_estado ) {
		this.id_estado = id_estado;
	}

	public String getId_eps() {
		return id_eps;
	}

	public void setId_eps( String id_eps ) {
		this.id_eps = id_eps;
	}

	public Integer getNumero_etapa() {
		return numero_etapa;
	}

	public void setNumero_etapa( Integer numero_etapa ) {
		this.numero_etapa = numero_etapa;
	}
	
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del ciudadano
	 */
	public String toString() 
	{
		return "Ciudadano [documento=" + documento + ", nombre=" + nombre + ", nacimiento=" + nacimiento + ", habilitado=" + habilitado
				+ ", id_estado=" + id_estado + ", id_eps=" + id_eps + ", numero_etapa=" + numero_etapa + ", sexo = "+sexo+"]";
	}
}
