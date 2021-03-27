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
 * Clase para modelar el concepto ESTADO del negocio de VacuAndes
 *
 * @author Néstor González
 */
public class Estado implements VOEstado {
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador del estado
	 */
	private Long id;
	
	/**
	 * La descripción del estado
	 */
	private String descripcion;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public Estado(){
	}
	
	public Estado( Long id, String descripcion ) {
		this.id = id;
		this.descripcion = descripcion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del estado
	 */
	public String toString() 
	{
		return "Estado [id=" + id + ", descripcion=" + descripcion + "]";
	}
}
