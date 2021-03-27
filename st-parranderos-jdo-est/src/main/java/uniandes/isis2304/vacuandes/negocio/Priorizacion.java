package uniandes.isis2304.vacuandes.negocio;

public class Priorizacion implements VOPriorizacion {
	/* ****************************************************************
	 * 			Atributos 
	 *****************************************************************/
     /**
	 * El documento de identificación del ciudadano
	 */
	private String documento_ciudadano;
	
	/**
	 * La descripción de la condición de priorización
	 */
	private String descripcion_condprior;
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	public Priorizacion(){
	}
	
	public Priorizacion( String documento_ciudadano, String descripcion_condprior ) {
		this.documento_ciudadano = documento_ciudadano;
		this.descripcion_condprior = descripcion_condprior;
	}

	public String getDocumento_ciudadano() {
		return documento_ciudadano;
	}

	public void setDocumento_ciudadano(String documento_ciudadano) {
		this.documento_ciudadano = documento_ciudadano;
	}

	public String getDescripcion_condprior() {
		return descripcion_condprior;
	}

	public void setDescripcion_condprior(String descripcion_condprior) {
		this.descripcion_condprior = descripcion_condprior;
	}
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la priorizacion
	 */
	public String toString() 
	{
		return "Priorizacion [documento_ciudadano=" + documento_ciudadano + ", descripcion_condprior=" + descripcion_condprior + "]";
	}
}
