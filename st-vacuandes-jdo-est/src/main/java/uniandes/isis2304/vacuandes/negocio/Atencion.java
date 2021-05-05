package uniandes.isis2304.vacuandes.negocio;

public class Atencion implements VOAtencion {
	/* ****************************************************************
	 * 			Atributos 
	 *****************************************************************/
     /**
	 * La descripcion de la condicion de priorizacion
	 */
	private String descripcion_condprior;
	
	/**
	 * El identificador del punto de vacunación
	 */
	private String id_punto;
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	public Atencion(){
	}
	
	public Atencion(String descripcion_condprior, String id_punto) {
		super();
		this.descripcion_condprior = descripcion_condprior;
		this.id_punto = id_punto;
	}
	
	public String getDescripcion_condprior() {
		return descripcion_condprior;
	}

	public void setDescripcion_condprior(String descripcion_condprior) {
		this.descripcion_condprior = descripcion_condprior;
	}

	public String getId_punto() {
		return id_punto;
	}

	public void setId_punto(String id_punto) {
		this.id_punto = id_punto;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la atención
	 */
	public String toString() 
	{
		return "Atencion [descripcion_condprior = " + descripcion_condprior + ", id_punto = " + id_punto + "]";
	}
}
