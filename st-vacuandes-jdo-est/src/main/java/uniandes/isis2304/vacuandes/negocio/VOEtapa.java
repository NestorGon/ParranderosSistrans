package uniandes.isis2304.vacuandes.negocio;

/**
 * Interfaz para los métodos get de ETAPA
 * 
 * @author Mariana Zamora
 */
public interface VOEtapa {
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	/**
	 * @return numero de la etapa
	 */
	public Long getNumero();
	
	/**
	 * @return descripcion de la etapa
	 */
	public String getDescripcion();
	
	@Override
	/**
	 * @return cadena de caracteres con los atributos de la etapa
	 */
	public String toString();

}
