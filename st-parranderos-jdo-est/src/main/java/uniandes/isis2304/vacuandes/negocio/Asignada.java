package uniandes.isis2304.vacuandes.negocio;

/**
 * Clase para modelar el concepto ASIGNADA del negocio VacuAndes
 *
 * @author Mariana Zamora 
 */
public class Asignada implements VOAsignada {
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	 /**
	  * Id de la eps del punto al que fue asignada una vacuna
	  */
	private String id_eps;
	
	/**
	 * Id del punto al que fue asignada una vacuna
	 */
	private String id_punto;
	
	/**
	 * Id de la vacuna que fue asignada a un punto de vacunacion
	 */
	private String id_vacuna;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public Asignada() {
		//Constructor vacío
	}
	
	public Asignada( String id_eps, String id_punto, String id_vacuna ) {
		this.id_eps = id_eps;
		this.id_punto = id_punto;
		this.id_vacuna = id_vacuna;		
	}

	public String getId_eps() {
		return id_eps;
	}

	public void setId_eps(String id_eps) {
		this.id_eps = id_eps;
	}

	public String getId_punto() {
		return id_punto;
	}

	public void setId_punto(String id_punto) {
		this.id_punto = id_punto;
	}

	public String getId_vacuna() {
		return id_vacuna;
	}

	public void setId_vacuna(String id_vacuna) {
		this.id_vacuna = id_vacuna;
	}
	
	@Override
	/**
	 * @return cadena de caracteres con todos los atributos de la asignacion
	 */
	public String toString() {
		return "Asignada[id_eps = "+ id_eps+", id_punto = "+ id_punto+", id_vacuna = "+id_vacuna+"]";
	}
}
