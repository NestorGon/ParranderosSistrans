package uniandes.isis2304.vacuandes.negocio;

/**
 * Interfaz para los métodos get de EPS
 * 
 * @author Mariana Zamora
 */
public interface VOEPS {

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	/**
	 * @return id de la eps
	 */
	public String getId();
	
	/**
	 * @return region de la eps
	 */
	public String getRegion();
	
	/**
	 * @return numero de vacunas de la eps
	 */
	public Long getVacunas();
	
	/**
	 * @return capacidad de almacenamiento de vacunas de la eps
	 */
	public Long getCapacidadVacunas();
	
	@Override
	/**
	 * @return cadena de caracteres con los atributos de la eps
	 */
	public String toString();
}
