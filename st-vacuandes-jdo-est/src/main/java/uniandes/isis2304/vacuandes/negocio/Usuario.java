package uniandes.isis2304.vacuandes.negocio;

public class Usuario implements VOUsuario {
	/* ****************************************************************
	 * 			Atributos 
	 *****************************************************************/
     /**
	 * El documento de identificación del ciudadano
	 */
	private String documento_ciudadano;
	
	/**
	 * El login del usuario
	 */
	private String login_infousuario;
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	public Usuario(){
	}
	
	public Usuario( String documento_ciudadano, String login_infousuario ) {
		this.documento_ciudadano = documento_ciudadano;
		this.login_infousuario = login_infousuario;
	}

	public String getDocumento_ciudadano() {
		return documento_ciudadano;
	}

	public void setDocumento_ciudadano( String documento_ciudadano ) {
		this.documento_ciudadano = documento_ciudadano;
	}

	public String getLogin_infousuario() {
		return login_infousuario;
	}

	public void setLogin_infousuario( String login_infousuario ) {
		this.login_infousuario = login_infousuario;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del usuario
	 */
	public String toString() 
	{
		return "Usuario [documento_ciudadano=" + documento_ciudadano + ", login_infousuario=" + login_infousuario + "]";
	}
}
