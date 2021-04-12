package uniandes.isis2304.vacuandes.negocio;

/**
 * Interfaz para los métodos get de VACUNA
 * 
 * @author Mariana Zamora
 */
public interface VOVacuna {

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	/**
	 * @return id de la vacuna
	 */
	public String getId();
	
	/**
	 * @return condicion de preservacion de la vacuna
	 */
	public String getPreservacion();
	
	/**
	 * @return estado de aplicacion de la vacuna
	 */
	public String getAplicada();
	
	/**
	 * @return el numero de dosis de la vacuna
	 */
	public Long getDosis();
	
	/**
	 * @return el tipo de la vacuna (laboratorio que la elabora)
	 */
	public String getTipo();
	
	@Override
	/**
	 * @return cadena de caracteres con los atributos de la vacuna
	 */
	public String toString();
}
