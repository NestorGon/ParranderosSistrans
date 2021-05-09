package uniandes.isis2304.vacuandes.negocio;

/**
 * Clase para modelar el concepto ETAPA del negocio VacuAndes
 *
 * @author Mariana Zamora 
 */
public class Etapa implements VOEtapa{
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * Numero de la etapa
	 */
	private Long numero; 
	
	/**
	 * Descripcion de la etapa 
	 */
	private String descripcion;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public Etapa() {
		//Constructor vacío
	}
	
	public Etapa( Long numero, String descripcion ) {
		this.numero = numero; 
		this.descripcion = descripcion;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	/**
	 * @return cadena de caracteres con todos los atributos de la  etapa
	 */
	public String toString() {
		return "Etapa: "+ numero+" - "+ descripcion;
	}
}
