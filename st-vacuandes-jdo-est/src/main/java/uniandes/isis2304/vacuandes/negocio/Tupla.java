package uniandes.isis2304.vacuandes.negocio;

import java.math.BigDecimal;

/**
 * Clase tupla para el manejo de respuestas para el requerimiento RFC12
 * @author mariana
 *
 */
public class Tupla {
	
	private String id_punto;
	
	private Long cantidadCitas;
	
	private String semana;
	
	
	public Tupla(String id_punto, BigDecimal cantidadCitas, String semana){
		this.id_punto=id_punto;
		this.cantidadCitas= cantidadCitas.longValue();
		this.semana = semana;
	}

	public String getIdPunto() {
		return id_punto;
	}
	
	public void setId_punto(String idPunto) {
		this.id_punto = idPunto;
	}

	public void setCantidadCitas(Long cantidadCitas) {
		this.cantidadCitas = cantidadCitas;
	}

	public void setSemana(String semana) {
		this.semana = semana;
	}

	public Long getCantidadCitas() {
		return cantidadCitas;
	}

	public String getSemana() {
		return semana;
	}

}
