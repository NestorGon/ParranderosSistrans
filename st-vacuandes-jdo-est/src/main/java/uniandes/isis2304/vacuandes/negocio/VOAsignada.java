package uniandes.isis2304.vacuandes.negocio;

/**
 * Interfaz para los métodos get de ASIGNADA
 * 
 * @author Mariana Zamora
 */
public interface VOAsignada {
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	/**
	 * @return el id de la eps del punto al que fue asignada una vacuna
	 */
	public String getId_eps();
	
	/**
	 * @return el id del punto de vacunacion al que fue asignada una vacuna
	 */
	public String getId_punto();
	
	/**
	 * @return el id de la vacuna asignada
	 */
	public String getId_vacuna();
	
	@Override
	/**
	 * @return cadena de caracteres con los atributos de la asignacion
	 */
	public String toString();
}
