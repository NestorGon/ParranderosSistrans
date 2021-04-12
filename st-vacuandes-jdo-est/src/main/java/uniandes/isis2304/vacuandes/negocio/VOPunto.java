package uniandes.isis2304.vacuandes.negocio;

/**
 * Interfaz para los métodos get de PUNTO 
 * 
 * @author Mariana Zamora
 */
public interface VOPunto {
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	/**
	 * @return el id del punto de vacunacion
	 */
	public String getId();
	
	/**
	 * @return la region del punto de vacunacion
	 */
	public String getRegion();
	
	/**
	 * @return la direccion del punto de vacunacion
	 */
	public String getDireccion();
	
	/**
	 * @return la cantidada de vacunas aplicadas en el punto
	 */
	public Long getAplicadas();
	
	/**
	 * @return la capacidad del punto
	 */
	public Long getCapacidad();
	
	/**
	 * @return el id de la eps a la que pertenece el punto
	 */
	public String getId_eps();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del punto
	 */
	public String toString();
}
