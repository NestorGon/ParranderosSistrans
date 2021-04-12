package uniandes.isis2304.vacuandes.negocio;

/**
 * Clase para modelar el concepto CONDICIONPRIORIZACION del negocio VacuAndes
 *
 * @author Mariana Zamora 
 */
public class CondicionPriorizacion implements VOCondicionPriorizacion {
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * Descripcion de la condicion de priorizacion
	 */
	private String descripcion; 
	
	/**
	 * Numero de la etapa a la cual pertenece la condicion de priorizacion
	 */
	private Long numero_etapa;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public CondicionPriorizacion() {
		//Constructor vacío
	}
	
	public CondicionPriorizacion( String descripcion, Long numero_etapa ) {
		this.descripcion = descripcion;
		this.numero_etapa = numero_etapa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getNumero_etapa() {
		return numero_etapa;
	}

	public void setNumero_etapa(Long numero_etapa) {
		this.numero_etapa = numero_etapa;
	}
	
	@Override
	/**
	 * @return cadena de caracteres con todos los atributos de la  condicion de priorizacion
	 */
	public String toString() {
		return "CondicionPriorizacion[numero_etapa = "+ numero_etapa+", descripcion = "+ descripcion+"]";
	}
}
