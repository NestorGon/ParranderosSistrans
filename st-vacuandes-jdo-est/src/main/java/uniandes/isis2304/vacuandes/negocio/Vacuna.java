package uniandes.isis2304.vacuandes.negocio;

/**
 * Clase para modelar el concepto VACUNA del negocio VacuAndes
 *
 * @author Mariana Zamora 
 */
public class Vacuna implements VOVacuna{
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/** 
	 * El id único de la vacuna
	 */
	private String id;
	
	/**
	 * Las condiciones de preservacion de la vacuna
	 */
	private String preservacion;
	
	/**
	 * Estado de aplicación de la Vacuna. T si es aplicada, F si no
	 */
	private String aplicada;
	
	/**
	 * Número de dosis de la vacuna
	 */
	private Long dosis;
	
	/**
	 * Tipo de la vacuna. Contiene la información del laboratorio elabotrador de la vacuna
	 */
	private String tipo;
	
	/**
	 * Fecha de llegada de la vacuna
	 */
	private String llegada;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public Vacuna() {
		//Constructor vacío
	}
	
	public Vacuna(String id, String preservacion, String aplicada, Long dosis, String tipo, String llegada) {
		this.id = id;
		this.preservacion = preservacion;
		this.aplicada = aplicada;
		this.dosis = dosis;
		this.tipo = tipo;
		this.llegada = llegada;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPreservacion() {
		return preservacion;
	}

	public void setPreservacion(String preservacion) {
		this.preservacion = preservacion;
	}

	public String getAplicada() {
		return aplicada;
	}

	public void setAplicada(String aplicada) {
		this.aplicada = aplicada;
	}

	public Long getDosis() {
		return dosis;
	}

	public void setDosis(Long dosis) {
		this.dosis = dosis;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getLlegada() {
		return llegada;
	}

	public void setLlegada(String llegada) {
		this.llegada = llegada;
	}

	@Override
	/**
	 * @return cadena de caracteres con todos los atributos de la vacuna
	 */
	public String toString() {
		return "Vacuna[id = "+ id+", preservacion = "+ preservacion+", aplicada = "+ aplicada+", dosis = "+ dosis +"tipo = "+tipo+"]";
	}

}
