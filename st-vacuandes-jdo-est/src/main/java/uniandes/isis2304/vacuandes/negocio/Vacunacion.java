package uniandes.isis2304.vacuandes.negocio;

public class Vacunacion implements VOVacunacion {
	/* ****************************************************************
	 * 			Atributos 
	 *****************************************************************/
     /**
	 * El documento de identificación del ciudadano
	 */
	private String documento_ciudadano;
	
	/**
	 * El identificador de la eps
	 */
	private String id_eps;
	
	/**
	 * El identificador del punto de vacunación
	 */
	private String id_punto;
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	public Vacunacion(){
	}

	public Vacunacion(String documento_ciudadano, String id_eps, String id_punto) {
		this.documento_ciudadano = documento_ciudadano;
		this.id_eps = id_eps;
		this.id_punto = id_punto;
	}

	public String getDocumento_ciudadano() {
		return documento_ciudadano;
	}

	public void setDocumento_ciudadano(String documento_ciudadano) {
		this.documento_ciudadano = documento_ciudadano;
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

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la vacunación
	 */
	public String toString() 
	{
		return "Vacunación [documento_ciudadano=" + documento_ciudadano + ", id_eps=" + id_eps+ ", id_punto=" + id_punto + "]";
	}
}
