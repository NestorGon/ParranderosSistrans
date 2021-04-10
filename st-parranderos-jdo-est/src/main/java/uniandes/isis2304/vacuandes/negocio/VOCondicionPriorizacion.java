package uniandes.isis2304.vacuandes.negocio;

/**
 * Interfaz para los métodos get de CONDICIONPRIORIZACION
 * 
 * @author Mariana Zamora
 */
public interface VOCondicionPriorizacion {
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	/**
	 * @return la descripcion de la condicion de priorizacion
	 */
	public String getDescripcion();
	
	/**
	 * @return el numero de la etapa a la que pertenece la condicion de priorizacion
	 */
	public Long getNumero_etapa();
	
	@Override
	/**
	 * @return la cadena de caracteres con los atributos de la condicion de priorizacion
	 */
	public String toString();

}
