/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: VacuAndes
 * @version 1.0
 * @author Néstor González
 * Abril de 2021
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.vacuandes.negocio;

import java.util.Date;

/**
 * Clase para modelar el concepto CITA del negocio de VacuAndes
 *
 * @author Néstor González
 */
public class Cita implements VOCita {
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * La fecha y hora de la cita
	 */
	private String fechaHora;
	
	/**
	 * Si la cita ya se finalizó o no
	 */
	private String finalizada;
	
	/**
	 * El documento de identificación del ciudadano asociado a la cita
	 */
	private String documento_ciudadano;
	
	/**
	 * El id del punto de vacunaciomn
	 */
	private String id_punto;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public Cita() {
	}

	public Cita( String fechaHora, String finalizada, String documento_ciudadano, String punto ) {
		super();
		this.fechaHora = fechaHora;
		this.finalizada = finalizada;
		this.documento_ciudadano = documento_ciudadano;
		this.id_punto = punto;
	}

	public String getId_punto() {
		return id_punto;
	}

	public void setId_punto(String id_punto) {
		this.id_punto = id_punto;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora( String fechaHora ) {
		this.fechaHora = fechaHora;
	}

	public String getFinalizada() {
		return finalizada;
	}

	public void setFinalizada( String finalizada ) {
		this.finalizada = finalizada;
	}

	public String getDocumento_ciudadano() {
		return documento_ciudadano;
	}

	public void setDocumento( String documento_ciudadano ) {
		this.documento_ciudadano = documento_ciudadano;
	}
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la cita
	 */
	public String toString() 
	{
		return "Cita [fechaHora = " + fechaHora + ", finzalidada = " + finalizada + ", documento_ciudadano = " + documento_ciudadano + "]";
	}
}
