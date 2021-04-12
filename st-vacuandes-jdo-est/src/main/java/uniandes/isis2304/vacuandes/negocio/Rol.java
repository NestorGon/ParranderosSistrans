package uniandes.isis2304.vacuandes.negocio;

public class Rol implements VORol {
	/* ****************************************************************
	 * 			Atributos 
	 *****************************************************************/
     /**
	 * El identificador del rol
	 */
	private Long id;
	
	/**
	 * La cadena de texto con el rol
	 */
	private String rol;
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	public Rol(){
	}
	
	public Rol( Long id, String rol ) {
		this.id = id;
		this.rol = rol;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la priorizacion
	 */
	public String toString() 
	{
		return "Roles [id=" + id + ", rol=" + rol + "]";
	}
}
