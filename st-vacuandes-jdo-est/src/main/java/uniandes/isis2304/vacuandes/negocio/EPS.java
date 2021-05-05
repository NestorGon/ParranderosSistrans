package uniandes.isis2304.vacuandes.negocio;

/**
 * Clase para modelar el concepto EPS del negocio VacuAndes
 *
 * @author Mariana Zamora 
 */
public class EPS implements VOEPS{
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * Id único de la eps
	 */
	private String id;
	
	/**
	 * Región a la que pertenece la EPS
	 */
	private String region;
	
	/**
	 * Número de vacunas con el que cuenta la eps
	 */
	private Long vacunas;
	
	/**
	 * Número de vacunas que puede almacenar la eps
	 */
	private Long capacidadVacunas;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public EPS() {
		//Constructor vacío
	}
	
	public EPS( String id, String region, Long vacunas, Long capacidadVacunas ) {
		this.id = id; 
		this.region = region; 
		this.vacunas = vacunas;
		this.capacidadVacunas = capacidadVacunas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Long getVacunas() {
		return vacunas;
	}

	public void setVacunas(Long vacunas) {
		this.vacunas = vacunas;
	}
	
	public Long getCapacidadVacunas() {
		return capacidadVacunas;
	}

	public void setCapacidadVacunas(Long capacidadVacunas) {
		this.capacidadVacunas = capacidadVacunas;
	}

	@Override
	/**
	 * @return cadena de caracteres con todos los atributos de la eps
	 */
	public String toString() {
		return "EPS[id = "+ id+", region = "+ region+", vacunas = "+vacunas+", capacidad vacunas = "+capacidadVacunas+"]";
	}
}
