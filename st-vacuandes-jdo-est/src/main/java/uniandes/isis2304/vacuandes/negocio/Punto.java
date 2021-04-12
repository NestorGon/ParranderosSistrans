package uniandes.isis2304.vacuandes.negocio;

/**
 * Clase para modelar el concepto PUNTO del negocio VacuAndes
 *
 * @author Mariana Zamora 
 */
public class Punto implements VOPunto{

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * El identificador único del punto
	 */
	private String id;
	
	/**
	 * La región del punto de vacunacion
	 */
	private String region;
	
	/**
	 * La direccion del punto de vacunacion
	 */
	private String direccion;
	
	/**
	 * La cantidad de vacunas aplicadas en el punto de vacunacion
	 */
	private Long aplicadas;
	
	/**
	 * La capacidad del punto de vacunacion
	 */
	private Long capacidad; 
	
	/**
	 * Id único de la eps a la que pertenece el punto de vacunacion
	 */
	private String id_eps; 
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public Punto () {
		//Constructor vacío
	}
	
	public Punto( String id, String region, String direccion, Long aplicadas, Long capacidad, String id_eps) {
		this.id = id;
		this.region = region; 
		this.direccion = direccion; 
		this.aplicadas = aplicadas;
		this.capacidad = capacidad;
		this.id_eps = id_eps;
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Long getAplicadas() {
		return aplicadas;
	}

	public void setAplicadas(Long aplicadas) {
		this.aplicadas = aplicadas;
	}

	public Long getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Long capacidad) {
		this.capacidad = capacidad;
	}

	public String getId_eps() {
		return id_eps;
	}

	public void setId_eps(String id_eps) {
		this.id_eps = id_eps;
	}
	
	@Override
	/**
	 * @return cadena de caracteres con todos los atributos del punto
	 */
	public String toString() {
		return "Punto[id = "+ id+", region = "+ region+", direccion = "+ direccion+", aplicadas = "+aplicadas+"capacidad = "+capacidad+", id_eps = "+ id_eps+"]";
	}

}
