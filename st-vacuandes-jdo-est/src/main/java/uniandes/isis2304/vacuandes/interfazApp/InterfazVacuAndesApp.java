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

package uniandes.isis2304.vacuandes.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.vacuandes.negocio.Cita;
import uniandes.isis2304.vacuandes.negocio.Ciudadano;
import uniandes.isis2304.vacuandes.negocio.CondicionPriorizacion;
import uniandes.isis2304.vacuandes.negocio.EPS;
import uniandes.isis2304.vacuandes.negocio.Estado;
import uniandes.isis2304.vacuandes.negocio.Etapa;
import uniandes.isis2304.vacuandes.negocio.Punto;
import uniandes.isis2304.vacuandes.negocio.Rol;
import uniandes.isis2304.vacuandes.negocio.VOCiudadano;
import uniandes.isis2304.vacuandes.negocio.VOEPS;
import uniandes.isis2304.vacuandes.negocio.VOEstado;
import uniandes.isis2304.vacuandes.negocio.VOInfoUsuario;
import uniandes.isis2304.vacuandes.negocio.VOPunto;
import uniandes.isis2304.vacuandes.negocio.VOVacuna;
import uniandes.isis2304.vacuandes.negocio.VOVacunacion;
import uniandes.isis2304.vacuandes.negocio.VacuAndes;

/**
 * Clase principal de la interfaz
 * @author Néstor González
 * @author Mariana Zamora
 */
@SuppressWarnings("serial")
public class InterfazVacuAndesApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger( InterfazVacuAndesApp.class.getName() );

	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 

	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD.json"; 

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
	 */
	private JsonObject tableConfig;

	/**
	 * Asociación a la clase principal del negocio.
	 */
	private VacuAndes vacuAndes;

	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
	/**
	 * Objeto JSON con la configuración de interfaz de la app.
	 */
	private JsonObject guiConfig;

	/**
	 * Panel de despliegue de interacción para los requerimientos
	 */
	private PanelDatos panelDatos;

	/**
	 * Menú de la aplicación
	 */
	private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Construye la ventana principal de la aplicación. <br>
	 * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
	 */
	public InterfazVacuAndesApp( )
	{
		// Carga la configuración de la interfaz desde un archivo JSON
		guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);

		// Configura la apariencia del frame que contiene la interfaz gráfica
		configurarFrame ( );
		if (guiConfig != null) 	   
		{
			crearMenu( guiConfig.getAsJsonArray("menuBar") );
		}

		tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
		vacuAndes = new VacuAndes (tableConfig);

		String path = guiConfig.get("bannerPath").getAsString();
		panelDatos = new PanelDatos ( );

		setLayout (new BorderLayout());
		add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
		add( panelDatos, BorderLayout.CENTER );
		this.setVisible(true);
		inicializarEtapa();
	}

	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
	/**
	 * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
	 * @param tipo - El tipo de configuración deseada
	 * @param archConfig - Archivo Json que contiene la configuración
	 * @return Un objeto JSON con la configuración del tipo especificado
	 * 			NULL si hay un error en el archivo.
	 */
	private JsonObject openConfig (String tipo, String archConfig)
	{
		JsonObject config = null;
		try {
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e) {
			//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
		return config;
	}

	/**
	 * Método para configurar el frame principal de la aplicación
	 */
	private void configurarFrame( )
	{
		int alto = 0;
		int ancho = 0;
		String titulo = "";	

		if ( guiConfig == null ) {
			log.info ( "Se aplica configuración por defecto" );			
			titulo = "VacuAndes APP Default";
			alto = 300;
			ancho = 500;
		}
		else {
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
			titulo = guiConfig.get( "title" ).getAsString();
			alto = guiConfig.get( "frameH" ).getAsInt();
			ancho = guiConfig.get( "frameW" ).getAsInt();
		}

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLocation (50,50);
		setResizable( true );
		setBackground( Color.WHITE );

		setTitle( titulo );
		setSize ( ancho, alto);        
	}

	/**
	 * Método para crear el menú de la aplicación con base em el objeto JSON leído
	 * Genera una barra de menú y los menús con sus respectivas opciones
	 * @param jsonMenu - Arreglo Json con los menùs deseados
	 */
	private void crearMenu(  JsonArray jsonMenu )
	{    	
		// Creación de la barra de menús
		menuBar = new JMenuBar();       
		for (JsonElement men : jsonMenu)
		{
			// Creación de cada uno de los menús
			JsonObject jom = men.getAsJsonObject(); 

			String menuTitle = jom.get( "menuTitle" ).getAsString();        	
			JsonArray opciones = jom.getAsJsonArray( "options" );

			JMenu menu = new JMenu( menuTitle);

			for (JsonElement op : opciones)
			{       	
				// Creación de cada una de las opciones del menú
				JsonObject jo = op.getAsJsonObject(); 
				String lb =   jo.get( "label" ).getAsString();
				String event = jo.get( "event" ).getAsString();

				JMenuItem mItem = new JMenuItem( lb );
				mItem.addActionListener( this );
				mItem.setActionCommand(event);

				menu.add(mItem);
			}       
			menuBar.add( menu );
		}        
		setJMenuBar ( menuBar );	
	}

	/* ****************************************************************
	 * 			Requerimientos Funcionales de Modificación
	 *****************************************************************/

	/**
	 * Agrega una condición de priorizacipon al plan de VacuAndes
	 * Se crea una tupla de CONDICION PRIORIZACION en la base de datos
	 */
	public void registrarPriorizacion()
	{
		try 
		{ 
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(1L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}

			List<Etapa> etapas = vacuAndes.darEtapas();
			String[] etapasS = new String[etapas.size()];
			String[] listaEtapas = new String [etapas.size()];

			for( int i=0; i<etapas.size(); i++)
			{
				listaEtapas= etapas.get(i).toString().split("-");
				etapasS[i] = listaEtapas[0];
			}

			JList<String> list = new JList<>( etapasS );
			JOptionPane.showMessageDialog(null, list, "Selecccione la etapa a la que quiere que pertenezca la Condición de Priorización que va a registrar (seleccione únicamente una)", JOptionPane.PLAIN_MESSAGE);

			String seleccionado = list.getSelectedValue();

			if(seleccionado == null )
			{
				throw new Exception( "Ningúna etapa fue seleccionada");
			}

			String[] etapa = seleccionado.split(":");
			String informacion = etapa[1];
			String [] partido = informacion.split("-");
			String numero = partido[0];

			numero = numero.trim();			

			Long numeroEtapa = Long.parseLong(numero);

			String descripcionCondicion = JOptionPane.showInputDialog(this, "Ingrese la descripcion de la Condicion de Priorizacion que desea registrar", "Registrar Condicion Priorizacion", JOptionPane.QUESTION_MESSAGE);
			if ( numeroEtapa != null && descripcionCondicion != null && !descripcionCondicion.trim().equals("") )
			{
				descripcionCondicion = descripcionCondicion.trim();

				vacuAndes.adicionarCondicionPriorizacion(numeroEtapa, descripcionCondicion);

				String mensaje = "Condicion de Priorización adicionada correctamente \n" + vacuAndes.darCondicionPriorizacion(descripcionCondicion);

				panelDatos.actualizarInterfaz(mensaje);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		}
		catch( Exception e )
		{
			String error = generarMensajeError(e);
			panelDatos.actualizarInterfaz(error);
		}
	}

	/**
	 * Registra la secuencia de estados válidos para el proceso de vacunación
	 * Se crea una nueva tupla de ESTADO en la base de datos
	 */
	public void registrarEstados()
	{
		try 
		{ 
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(1L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}

			int cantidadI =0;

			String cantidad = JOptionPane.showInputDialog(this, "¿Cuántos estados desea registrar?", "Registrar Estado", JOptionPane.QUESTION_MESSAGE);
			if(!cantidad.equals("") && cantidad != null)
			{
				cantidadI = Integer.parseInt(cantidad);
			}
			for(int i=0; i<cantidadI; i++)
			{
				String descripcionEstado = JOptionPane.showInputDialog(this, "Ingrese la descripcion del estado a registrar", "Registrar Estado", JOptionPane.QUESTION_MESSAGE);
				if ( descripcionEstado != null && !descripcionEstado.trim().equals("") )
				{
					descripcionEstado = descripcionEstado.trim();

					vacuAndes.adicionarEstado(descripcionEstado);

					List<Estado> lista = vacuAndes.darEstados();

					int size = lista.size();

					Long id= 0L;

					for(int j=0; j< size; j++)
					{
						if(lista.get(j).toString().contains(descripcionEstado))
						{
							id = lista.get(j).getId();
							break;
						}

					}

					String mensaje = "Estado adicionado correctamente \n" + vacuAndes.darEstado(id);

					panelDatos.actualizarInterfaz(mensaje);
				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			}
		}
		catch( Exception e )
		{
			String error = generarMensajeError(e);
			panelDatos.actualizarInterfaz(error);
		}
	}

	/**
	 * Registra la secuencia de estados válidos para el proceso de vacunación
	 * Se crea una nueva tupla de ESTADO en la base de datos
	 */
	public void registrarEPS()
	{
		try 
		{ 
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(1L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}

			String id = JOptionPane.showInputDialog(this, "Ingrese el Id de la EPS que desea registrar", "Registrar EPS", JOptionPane.QUESTION_MESSAGE);
			String region = JOptionPane.showInputDialog(this, "Ingrese la región de la EPS que desea registrar", "Registrar EPS", JOptionPane.QUESTION_MESSAGE);
			String vacunas = JOptionPane.showInputDialog(this, "Ingrese el número de Vacunas de la EPS que desea registrar", "Registrar EPS", JOptionPane.QUESTION_MESSAGE);
			String capacidad = JOptionPane.showInputDialog(this, "Ingrese la capacidad de vacunas de la EPS que desea registrar", "Registrar EPS", JOptionPane.QUESTION_MESSAGE);
			if ( id != null && !id.trim().equals("") && region != null && !region.trim().equals("") && vacunas != null && !vacunas.trim().equals("") && capacidad != null && !capacidad.trim().equals("") )
			{
				id= id.trim();
				region= region.trim();
				Long vacunasL= Long.parseLong(vacunas.trim());
				Long capacidadL= Long.parseLong(capacidad.trim());

				vacuAndes.adicionarEps(id, region, vacunasL, capacidadL);

				String mensaje = "EPS adicionada correctamente \n" + vacuAndes.darEPS(id);

				panelDatos.actualizarInterfaz(mensaje);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		}
		catch( Exception e )
		{
			String error = generarMensajeError(e);
			panelDatos.actualizarInterfaz(error);
		}
	}

	/**
	 * Registra un usuario en VacuAndes
	 * Se crea una nueva tupla en la tabla de USUARIO e INFOUSUARIO
	 */
	public void registrarUsuario( ) {
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(1L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}
			String documento = JOptionPane.showInputDialog (this, "Ingrese el documento del usuario", "Registrar usuario", JOptionPane.QUESTION_MESSAGE);
			String login = JOptionPane.showInputDialog (this, "Ingrese el login del usuario", "Registrar usuario", JOptionPane.QUESTION_MESSAGE);
			String clave = JOptionPane.showInputDialog (this, "Ingrese la clave del usuario", "Registrar usuario", JOptionPane.QUESTION_MESSAGE);
			String trabajo = JOptionPane.showInputDialog (this, "Ingrese el trabajo del usuario", "Registrar usuario", JOptionPane.QUESTION_MESSAGE);

			JList<String> list = new JList<>( darRoles() );
			JOptionPane.showMessageDialog(null, list, "Selecccione el rol del usuario", JOptionPane.PLAIN_MESSAGE);
			String rol = list.getSelectedValue();

			String puntoVac = JOptionPane.showInputDialog (this, "Ingrese el identificador del punto de vacunación", "Registrar usuario", JOptionPane.QUESTION_MESSAGE);
			if ( documento != null && !documento.trim().equals("") && puntoVac != null && !puntoVac.trim().equals("") && login != null && !login.trim().equals("") && clave != null && !clave.trim().equals("") 
					&& trabajo != null && !trabajo.trim().equals("") && rol != null && !rol.trim().equals(""))
			{
				documento = documento.trim();
				login = login.trim();
				clave = clave.trim();
				trabajo = trabajo.trim();
				rol = rol.trim();
				puntoVac = puntoVac.trim();
				VOCiudadano ciudadano = vacuAndes.darCiudadano( documento );
				VOPunto punto = vacuAndes.darPunto( puntoVac );
				VOInfoUsuario info = vacuAndes.darInfoUsuario(login);
				if ( ciudadano == null ) {
					throw new Exception("El usuario no ha sido registrado como ciudadano previamente");
				}
				if ( punto == null ) {
					throw new Exception ("El punto de vacunación con id: " + puntoVac + " no se encuentra registrado en VacuAndes");
				}
				if ( info != null ) {
					info = vacuAndes.adicionarInfoUsuario( documento, login, clave, trabajo, Long.parseLong(rol.split(":")[0]), puntoVac);
				}
				else {
					throw new Exception ("El usuario ya está registrado en VacuAndes");
				}
				if ( info == null ) {
					throw new Exception ("No se pudo registrar al usuario con login: " + login);
				}
				String resultado = "En registrarUsuario\n\n";
				resultado += "Usuario registrado correctamente: " + info;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}

	}

	/**
	 * Registra un punto de vacunación en VacuAndes
	 * Se crea una nueva tupla en la tabla de PUNTO
	 */
	public void registrarPunto( ) {
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(2L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}
			String id = JOptionPane.showInputDialog (this, "Ingrese el id del punto", "Registrar punto", JOptionPane.QUESTION_MESSAGE);
			String region = JOptionPane.showInputDialog (this, "Ingrese la región del punto", "Registrar punto", JOptionPane.QUESTION_MESSAGE);
			String direccion = JOptionPane.showInputDialog (this, "Ingrese la dirección del punto", "Registrar punto", JOptionPane.QUESTION_MESSAGE);
			String aplicadasS = JOptionPane.showInputDialog (this, "Ingrese la cantidad de vacunas aplicadas en el punto", "Registrar punto", JOptionPane.QUESTION_MESSAGE);
			String capacidadS = JOptionPane.showInputDialog (this, "Ingrese la cantidad de personas que pueden estar en el punto", "Registrar punto", JOptionPane.QUESTION_MESSAGE);
			String id_eps = JOptionPane.showInputDialog (this, "Ingrese id de la eps asociada al punto", "Registrar punto", JOptionPane.QUESTION_MESSAGE);
			String capacidadVacunasS = JOptionPane.showInputDialog (this, "Ingrese la cantidad de vacunas que pueden ser almacenadas en el punto", "Registrar punto", JOptionPane.QUESTION_MESSAGE);
			String vacunasS = JOptionPane.showInputDialog (this, "Ingrese la cantidad actual de vacunas del punto", "Registrar punto", JOptionPane.QUESTION_MESSAGE);
			String habilitado = JOptionPane.showInputDialog (this, "Ingrese 'T' o 'F' si el punto está o no habilitado para prestar servicio", "Registrar punto", JOptionPane.QUESTION_MESSAGE);

			if ( id != null && !id.trim().equals("") && region != null && !region.trim().equals("") && direccion != null && !direccion.trim().equals("") && aplicadasS != null && !aplicadasS.trim().equals("") 
					&& capacidadS != null && !capacidadS.trim().equals("") && id_eps != null && !id_eps.trim().equals("") && capacidadVacunasS != null && !capacidadVacunasS.trim().equals("")
					&& vacunasS != null && !vacunasS.trim().equals("") && habilitado != null && !habilitado.trim().equals("") )
			{
				id = id.trim();
				region = region.trim();
				direccion = direccion.trim();
				Long aplicadas = Long.parseLong(aplicadasS.trim());
				Long capacidad = Long.parseLong(capacidadS.trim());
				Long capacidadVacunas = Long.parseLong(capacidadVacunasS.trim());
				Long vacunas = Long.parseLong(vacunasS.trim());
				habilitado = habilitado.trim();

				VOEPS eps = vacuAndes.darEPS(id_eps);
				VOPunto punto = vacuAndes.darPunto(id);

				if ( eps == null ) {
					throw new Exception("La EPS no se encuentra registrada en VacuAndes");
				}
				if ( punto == null ) {
					throw new Exception ("El punto de vacunación con id: " + punto + " ya se encuentra registrado en VacuAndes");
				}
				punto = vacuAndes.adicionarPunto(id, region, direccion, aplicadas, capacidad, id_eps, capacidadVacunas, vacunas, habilitado);
				if ( punto == null ) {
					throw new Exception("No se pudo registra el punto con id: " + id);
				}
				String resultado = "En registrarPunto\n\n";
				resultado += "Punto registrado correctamente: " + punto;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}

	}

	/**
	 * Registra un lote de vacunas a una EPS regional
	 * Cambia el número de vacunas en una tupla de EPS
	 */
	public void registrarLoteEPS()
	{
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(2L) && !usuario.getId_roles().equals(1L)) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}

			List<EPS> epss = vacuAndes.darEPSs();
			String[] epsS = new String[epss.size()];
			String[] listaEpss = new String [epss.size()];

			for( int i=0; i<epss.size(); i++)
			{
				listaEpss= epss.get(i).toString().split(",");
				epsS[i] = listaEpss[0]+ ", "+ listaEpss[1]+"]";
			}

			JList<String> list = new JList<>( epsS );
			JOptionPane.showMessageDialog(null, list, "Seleccione la EPS a la que quiere registrarle el lote de vacunas (Selecciona solo una)", JOptionPane.PLAIN_MESSAGE);
			String seleccionado = list.getSelectedValue();

			if(seleccionado == null )
			{
				throw new Exception( "Ningúna EPS fue seleccionada");
			}

			String[] eps = seleccionado.split(",");
			String informacion = eps[0];
			String [] partido = informacion.split("=");
			String numero = partido[1];

			numero = numero.trim();			

			String lote = JOptionPane.showInputDialog (this, "Ingrese el número de vacunas en el lote", "Registrar Lote de vacunas a EPS", JOptionPane.QUESTION_MESSAGE);

			if(!lote.equals("") && lote != null)
			{
				Long loteL = Long.parseLong(lote.trim());

				Long vacunasActuales = vacuAndes.darVacunasEPS(numero);
				Long capacidadVacunas = vacuAndes.darCapacidadEPS(numero);

				if( vacunasActuales+loteL > capacidadVacunas )
				{
					panelDatos.actualizarInterfaz("La cantidad de vacunas que desea agregar excede la capacidad de vacunas de la EPS (se tiene en cuenta el inventario actual)");
				}
				else
				{
					vacuAndes.aumentarVacunasEPSId(loteL, numero);

					panelDatos.actualizarInterfaz( "Lote registrado correctamente: \n"+ "Número de vacunas original EPS: "+ vacunasActuales+ "\nEps actaulizada: \n"+ vacuAndes.darEPS(numero) );
				}
			}

		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Registra un lote de vacunas a un punto de vacunacion
	 * Cambia el número de vacunas en una tupla de PUNTOVACUNACION
	 */
	public void registraLotePunto()
	{
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(3L)) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}

			List<Punto> puntos = vacuAndes.darPuntos();
			String[] puntosS = new String[puntos.size()];
			String[] listaPuntos = new String [puntos.size()];

			for( int i=0; i<puntos.size(); i++)
			{
				listaPuntos= puntos.get(i).toString().split(",");
				puntosS[i] = listaPuntos[0]+ ", "+ listaPuntos[1]+"]";
			}

			JList<String> list = new JList<>( puntosS );
			JOptionPane.showMessageDialog(null, list, "Seleccione el Punto al que quiere registrarle el lote de vacunas (Selecciona solo uno)", JOptionPane.PLAIN_MESSAGE);
			String seleccionado = list.getSelectedValue();

			if(seleccionado == null )
			{
				throw new Exception( "Ningún punto fue seleccionado");
			}

			String[] punto = seleccionado.split(",");
			String informacion = punto[0];
			String [] partido = informacion.split("=");
			String numero = partido[1];

			numero = numero.trim();			

			String lote = JOptionPane.showInputDialog (this, "Ingrese el número de vacunas en el lote", "Registrar Lote de vacunas a un Punto", JOptionPane.QUESTION_MESSAGE);

			if(!lote.equals("") && lote != null)
			{
				Long loteL = Long.parseLong(lote.trim());

				Long vacunasActuales = vacuAndes.darVacunasPunto(numero);
				Long capacidadVacunas = vacuAndes.darCapacidadVacunasPunto(numero);

				if( vacunasActuales+loteL > capacidadVacunas )
				{
					panelDatos.actualizarInterfaz("La cantidad de vacunas que desea agregar excede la capacidad de vacunas del Punto (se tiene en cuenta el inventario actual)");
				}
				else
				{
					vacuAndes.aumentarVacunasPuntoId(loteL, numero);

					panelDatos.actualizarInterfaz( "Lote registrado correctamente: \n"+ "Número de vacunas original del Punto: "+ vacunasActuales+ "\nPunto actaulizado: \n"+ vacuAndes.darPunto(numero) );
				}
			}

		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Asigna un ciudadano a un único punto de vacunación
	 * Se crea una nueva tupla de VACUNACION en la base de datos
	 */
	public void adicionarVacunacion( )
	{
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(2L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}
			String documento = JOptionPane.showInputDialog (this, "Ingrese el documento del ciudadano", "Adicionar vacunación", JOptionPane.QUESTION_MESSAGE);
			String puntoVac = JOptionPane.showInputDialog (this, "Ingrese el identificador del punto de vacunación", "Adicionar vacunación", JOptionPane.QUESTION_MESSAGE);
			if ( documento != null && !documento.trim().equals("") && puntoVac != null && !puntoVac.trim().equals("") )
			{
				documento = documento.trim();
				puntoVac = puntoVac.trim();
				VOCiudadano ciudadano = vacuAndes.darCiudadano( documento );
				VOPunto punto = vacuAndes.darPunto( puntoVac );
				VOVacunacion vac = vacuAndes.darVacunacion( documento, puntoVac );
				if ( punto == null ) {
					throw new Exception ("El punto de vacunación con id: " + puntoVac + " no se encuentra registrado en VacuAndes");
				}
				else if ( (punto.getCapacidad() - vacuAndes.darCantidadPunto(puntoVac)) == 0 ) {
					throw new Exception ("El punto de vacunación no tiene capacidad para agregar al ciudadano con documento: " + documento );
				}
				if ( vac != null ) {
					throw new Exception ("El ciudadano con documento " + documento + " ya se encuentra asignado al punto de vacunación con id " + puntoVac );
				}
				Boolean intersecta = false;
				for ( String actual: vacuAndes.darCondicionesCiudadano(documento) ) {
					for ( String actualP: vacuAndes.darCondicionesPunto(puntoVac) ) {
						if ( actual.equals(actualP) ) {
							intersecta = true;
							break;
						}
					}
					if ( intersecta ) {
						break;
					}
				}
				if ( !intersecta ) {
					throw new Exception ("El ciudadano no puede ser atendido en el punto debido a que sus condiciones de priorización no son atendidas por el punto");
				}

				if ( ciudadano != null ) {
					vac = vacuAndes.adicionarVacunacion( documento, ciudadano.getId_eps(), puntoVac );
				}
				if (vac == null)
				{
					throw new Exception ("No se pudo asignar al ciudadano " + documento + " al punto de vacunación " + puntoVac);
				}
				String resultado = "En adicionarVacunacion\n\n";
				resultado += "Ciudadano asignado correctamente al punto de vacunación: " + vac;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Registra el avance en el proceso de vacunación de un ciudadano
	 * Para esto realiza una actualización en el registro del ciudadano
	 */
	public void registrarAvanceVacunacion( )
	{
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(4L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			} 
			else {
				return;
			}
			String documento = JOptionPane.showInputDialog (this, "Ingrese el documento del ciudadano", "Registrar avance vacunación", JOptionPane.QUESTION_MESSAGE);
			String idEstado = JOptionPane.showInputDialog (this, "Ingrese el identificador del nuevo estado", "Registrar avance vacunación", JOptionPane.QUESTION_MESSAGE);
			if ( documento != null && !documento.trim().equals("") && idEstado != null && !idEstado.trim().equals("") )
			{
				VOCiudadano ciudadano = vacuAndes.darCiudadano( documento );
				if ( ciudadano != null ) {
					VOEstado estado = vacuAndes.darEstado( ciudadano.getId_estado() );
					if ( estado.getId().equals(Long.parseLong(idEstado) ) ) {
						throw new Exception( "El estado ingresado es igual al estado actual del ciudadano" );
					}
					VOEstado nuevoEstado = vacuAndes.darEstado(Long.parseLong(idEstado)); 
					if ( nuevoEstado.getDescripcion().startsWith("VACUNADO") ) {
						if ( estado.getDescripcion().startsWith("VACUNADO") ) {
							String tipoVacuna = estado.getDescripcion().split(" ")[3];
							if ( !nuevoEstado.getDescripcion().contains(tipoVacuna) ) {
								throw new Exception("No se puede registrar el avance en el proceso debido a que no coincide el tipo de vacuna: " + tipoVacuna);
							}
						}
						VOVacunacion vacunacion = vacuAndes.darVacunacion( documento, ciudadano.getId_eps() );
						Boolean aplicada = false;
						if ( vacunacion != null ) {
							String id_punto = vacunacion.getId_punto();
							for( String actual: vacuAndes.darAsignadasPunto(id_punto) ) {
								VOVacuna vacuna = vacuAndes.darVacuna( actual );
								if ( vacuna.getAplicada().equals("F") && vacuna.getTipo().equalsIgnoreCase(nuevoEstado.getDescripcion().split(" ")[3]) ) {
									vacuAndes.cambiarEstadoAplicacionVacunaT(actual);
									vacuAndes.aumentarAplicadasPuntoId(id_punto);
									aplicada = true;
									break;
								}
							}
							if ( !aplicada ) {
								throw new Exception("No se pudo registrar el avance en el proceso debido a que no hubo vacunas disponibles");
							}
						}
					}
					ciudadano = vacuAndes.actualizarCiudadano( documento, ciudadano.getNombre(), ciudadano.getNacimiento(), ciudadano.getHabilitado(), Long.parseLong(idEstado), ciudadano.getId_eps(), ciudadano.getNumero_etapa(), ciudadano.getSexo() );
				} 
				else {
					throw new Exception ("No se pudo actualizar al ciudadano " + documento );
				}
				String resultado = "En registrarAvanceVacunacion\n\n";
				resultado += "Ciudadano actualizado correctamente: " + ciudadano;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/** 
	 * Asigna una cita de vacunacion a un ciudadano 
	 */ 
	public void asignarCita() 
	{ 
		try 
		{ 
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(3L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			} 
			else {
				return;
			}
			String documento = JOptionPane.showInputDialog(this, "Ingrese el documento del ciudadano", "Asignar cita de vacunación", JOptionPane.QUESTION_MESSAGE); 
			String idPunto = JOptionPane.showInputDialog(this, "Ingrese el id del Punto de Vacunación", "Asignar cita de vacunación", JOptionPane.QUESTION_MESSAGE);
			if ( documento != null && !documento.trim().equals("") && idPunto != null && !idPunto.trim().equals("") ) 
			{ 
				VOCiudadano ciudadano = vacuAndes.darCiudadano(documento); 
				if(ciudadano!=null) { 
					Long etapa = vacuAndes.darEtapaCiudadano(documento);
					Punto punto = vacuAndes.darPunto(idPunto); 
					if(punto != null) { 
						Long capacidad = vacuAndes.darCapacidad(idPunto); 
						Long activas = vacuAndes.darCitasActivasPunto( idPunto ); 

						if(capacidad>=activas){ 

							try
							{
								String fechaHora = JOptionPane.showInputDialog(this, "El ciudadano pertenece a la etapa: "+etapa+"\nIngrese la fecha y hora en la cuál asignará la cita\nFormato DD-MM-YYYY HH:MI", "Asignar cita de vacunación", JOptionPane.QUESTION_MESSAGE);
								Cita cita = vacuAndes.adicionarCita(fechaHora, "F", documento, idPunto); 

								if(cita == null) {
									throw new Exception("La cita no se pudo asignar. Existen errores de restricción");
								}

								String resultado = "En asignarCita\n\n"; 
								resultado += "Cita asignada correctamente: " + cita.toString(); 
								resultado += "\n Operación terminada"; 
								panelDatos.actualizarInterfaz(resultado); 
							}
							catch(Exception e){
								throw new Exception(e.getMessage());
							}
						} 
						else { 
							throw new Exception("No se puede hacer la cita porque se excede la capacidad del punto");  
						} 
					} 
					else { 
						throw new Exception("El punto con los datos ingresados no existe en la base de datos"); 
					} 

				} 
				else { 
					throw new Exception( "El ciudadano con los datos ingresados no existe en la base de datos" ); 
				} 

			} 
		}  
		catch (Exception e)  
		{ 
			//		e.printStackTrace(); 
			String resultado = generarMensajeError(e); 
			panelDatos.actualizarInterfaz(resultado); 
		} 
	}

	/**
	 * Cambia las condiciones de priorización que atiende un punto de vacunación y actualiza los ciudadanos asociados
	 */
	public void cambioEstadoPunto() {
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(2L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			} 
			else {
				return;
			}
			String id_punto = JOptionPane.showInputDialog(this, "Ingrese el id del punto de vacunación", "Cambiar estado de un punto", JOptionPane.QUESTION_MESSAGE);

			JList<String> list = new JList<>( darGruposDePriorizacion() );
			JOptionPane.showMessageDialog(null, list, "Selecccione los grupos poblacionales que va a atender el punto (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
			List<String> seleccionados = list.getSelectedValuesList();

			if ( seleccionados != null && seleccionados.size() > 0  && id_punto != null && !id_punto.trim().equals("") ) {
				if( vacuAndes.darPunto(id_punto) == null ) {
					throw new Exception("El punto ingresado no se encuentra registrado en VacuAndes");
				}
				List<String> condiciones = vacuAndes.darCondicionesPunto(id_punto);

				List<String> ciudadanos = vacuAndes.cambioEstadoPunto( id_punto, seleccionados, condiciones );
				if ( ciudadanos == null ) {
					throw new Exception("No se pudo eliminar a los ciudadanos que ahora no pertenecen al punto");
				}
				String resultado = "En cambioEstadoPunto\n\n";
				resultado += "Punto actualizado correctamente: " + id_punto;
				if ( ciudadanos.size() == 0 ) {
					resultado += "\n No hubo ciudadanos para eliminarlos del punto";
				}
				else {
					resultado += "\n Ciuadanos eliminados del punto de vacunación: ";
					for ( String actual: ciudadanos) {
						resultado += "\n " + actual;
					}
				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Deshabilita un punto y re asigna sus citas a otro punto de la misma eps
	 */
	@SuppressWarnings("deprecation")
	public void deshabilitarPunto()
	{
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(2L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			} 
			else {
				return;
			}

			List<Punto> puntos = vacuAndes.darPuntos();
			String[] puntosS = new String[puntos.size()];
			String[] listaPuntos = new String [puntos.size()];

			for( int i=0; i<puntos.size(); i++)
			{
				listaPuntos= puntos.get(i).toString().split(",");
				puntosS[i] = listaPuntos[0]+ ", "+ listaPuntos[1]+"]";
			}

			JList<String> list = new JList<>( puntosS );
			JOptionPane.showMessageDialog(null, list, "Seleccione el Punto que va a deshabilitar (Selecciona solo uno)", JOptionPane.PLAIN_MESSAGE);
			String seleccionado = list.getSelectedValue();

			if(seleccionado == null )
			{
				throw new Exception( "Ningún punto fue seleccionado");
			}

			String[] punto = seleccionado.split(",");
			String informacion = punto[0];
			String [] partido = informacion.split("=");
			String numero = partido[1];

			numero = numero.trim();		

			Long activasPunto = vacuAndes.darCitasActivasPunto(numero);

			String id_eps = vacuAndes.darPunto(numero).getId_eps();

			List<Punto> puntosH = vacuAndes.darPuntosHabilitadosEPS(id_eps);
			String[] puntosHS = new String[puntosH.size()];
			String[] listaPuntosH = new String [puntosH.size()];

			for( int i=0; i<puntosH.size(); i++)
			{
				Punto actual = puntosH.get(i);
				if(!actual.getId().equals(numero))
				{
					listaPuntosH= actual.toString().split(",");
					Long capacidad = actual.getCapacidad();
					Long activas = vacuAndes.darCitasActivasPunto(actual.getId());
					Long disponible = capacidad - activas;
					puntosHS[i] = listaPuntosH[0]+", numero de citas disponible: "+ disponible+ "]";
				}
			}

			JOptionPane.showMessageDialog(this, "La cantidad de citas activas que había en el punto que desea deshabilitar son: \n"+ activasPunto);
			if(activasPunto == 0)
			{
				panelDatos.actualizarInterfaz("No hay citas activas en el punto deshabilitado");
				vacuAndes.cambiarHabilitadoPunto(numero, "F");
				return;
			}
			JList<String> listaP = new JList<>( puntosHS );
			JOptionPane.showMessageDialog(this, "A continuación seleccione el punto en el que asignará las citas");
			JOptionPane.showMessageDialog(null, listaP, "Tenga en cuenta disponibilidad", JOptionPane.PLAIN_MESSAGE);

			String seleccionadoP = listaP.getSelectedValue();

			if(seleccionadoP == null )
			{
				throw new Exception( "Ningún punto fue seleccionado");
			}

			String[] puntoH = seleccionadoP.split(",");
			String informacionH = puntoH[0];
			String [] partidoH = informacionH.split("=");
			String id_punto = partidoH[1];

			id_punto = id_punto.trim();	

			String[] citas = vacuAndes.deshabilitarPunto(numero, id_punto, id_eps);

			String mensaje = "";
			for(int i=0; i<citas.length; i++)
			{
				mensaje+= citas[i]+"\n";
			}
			panelDatos.actualizarInterfaz(mensaje);

		}
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Rehabilita un punto y le asigna nuevas citas
	 */
	public void rehabilitarPunto()
	{
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(2L) ) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			} 
			else {
				return;
			}
			String id_punto = JOptionPane.showInputDialog(this, "Ingrese el id del punto de vacunación", "Rehabilitar punto", JOptionPane.QUESTION_MESSAGE);

			if ( id_punto == null || !id_punto.trim().equals("") ) {

				VOPunto punto = vacuAndes.darPunto( id_punto );
				if ( punto == null ) {
					throw new Exception("El punto que desea habiitar no existe en VacuAndes");
				} else if ( punto.getHabilitado().equals("T") ) {
					throw new Exception("El punto ya se encuentra habilitado");
				}
				Long etapa = vacuAndes.darEtapaVacuAndes();
				List<String> ciudadanos = vacuAndes.rehabilitarPunto( id_punto, etapa );

				if ( ciudadanos != null ) {

					String resultado = "En rehabilitarPunto\n\n";
					resultado += "Punto habilitado correctamente: " + id_punto;
					if ( ciudadanos.size() == 0 ) {
						resultado += "\n No hubo ciudadanos para asignarles cita en el punto";
					}
					else {
						resultado += "\n Ciudadanos asignados con cita en el punto de vacunación: ";
						for ( String actual: ciudadanos) {
							resultado += "\n " + actual;
						}
					}
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}
				else {
					throw new Exception("No se pudo rehabilitar el punto correctamente");
				}
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/* ****************************************************************
	 * 			Requerimientos Funcionales de Consulta
	 *****************************************************************/
	/**
	 * Consulta la base de datos para hallar los ciudadanos atentidos por un punto
	 */
	public void mostrarCiudadanosPunto()
	{
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(1L) && !usuario.getId_roles().equals(2L) && !usuario.getId_roles().equals(3L)) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
				else if( usuario.getId_roles().equals(1L) || usuario.getId_roles().equals(2L)){
					//Admin plan y de EPS
					String region = JOptionPane.showInputDialog(this, "Ingrese la región", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE);
					if (!vacuAndes.darRegiones().contains(region))
						throw new Exception("La región ingresada no se encuentra en la base de datos");

					JList<String> list = new JList<>( new String[]{"Una fecha","Rango de fechas","Rango de horas"} );
					JOptionPane.showMessageDialog(null, list, "Selecccione la opcion para realizar la consulta (seleccione únicamente una)", JOptionPane.PLAIN_MESSAGE);
					List<String> seleccionados = list.getSelectedValuesList();
					boolean fecha = seleccionados.contains("Una fecha") ? true: false;
					boolean fechas = seleccionados.contains("Rango de fechas") ? true: false;
					boolean horas = seleccionados.contains("Rango de horas") ? true: false;

					if(fecha == true){
						String fechaResp = JOptionPane.showInputDialog(this, "Ingrese la fecha en formato DD-MM-YYYY", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE);
						String fecha1 = fechaResp+" 00:00";
						String fecha2 = fechaResp+ " 23:59";
						try{

							List<String> documentos = vacuAndes.darAtendidosRegionFechas(region, fecha1, fecha2);

							String resultado = "En mostrarCiudadaqnosPuntos\n\n"; 
							resultado += "Ciudadanos hallados correctamente: \n";

							for(int i= 0; i<documentos.size(); i++){
								resultado+=documentos.get(i)+"\n";
							}
							resultado += "\n Operación terminada"; 

							if(documentos.size()==0)
								resultado = "No se hallaron ciudadanos atendidos con las especificaciones dadas";

							panelDatos.actualizarInterfaz(resultado); 

						}
						catch(Exception e){
							throw new Exception("Existen errores en el formato de fecha y hora");
						}
					}
					else if(fechas == true){
						String fecha1 = JOptionPane.showInputDialog(this, "Ingrese la primera fecha en formato DD-MM-YYYY", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE) + " 00:00";
						String fecha2 = JOptionPane.showInputDialog(this, "Ingrese la segunda fecha en formato DD-MM-YYYY", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE)+ " 23:59";
						try{
							List<String> documentos = vacuAndes.darAtendidosRegionFechas(region, fecha1, fecha2);

							String resultado = "En mostrarCiudadaqnosPuntos\n\n"; 
							resultado += "Ciudadanos hallados correctamente: \n";

							for(int i= 0; i<documentos.size(); i++){
								resultado+=documentos.get(i)+"\n";
							}
							resultado += "\n Operación terminada"; 

							if(documentos.size()==0)
								resultado = "No se hallaron ciudadanos atendidos con las especificaciones dadas";

							panelDatos.actualizarInterfaz(resultado);
						}
						catch(Exception e){

							throw new Exception("Existen errores en el formato de fecha y hora");
						}
					}
					else if(horas == true){
						Long hora1 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese la hora de inicio en formato HH", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
						Long min1 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese el minuto de inicio en formato MI", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
						Long hora2 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese la hora final en formato HH", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
						Long min2 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese el minuto de fin en formato MI", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
						try{
							List<String> documentos = vacuAndes.darAtendidosRegionHoras(region, hora1, hora2,min1, min2);

							String resultado = "En mostrarCiudadaqnosPuntos\n\n"; 
							resultado += "Ciudadanos hallados correctamente: \n";

							for(int i= 0; i<documentos.size(); i++){
								resultado+=documentos.get(i)+"\n";
							}
							resultado += "\n Operación terminada"; 

							if(documentos.size()==0)
								resultado = "No se hallaron ciudadanos atendidos con las especificaciones dadas";

							panelDatos.actualizarInterfaz(resultado);
						}
						catch(Exception e){
							throw new Exception("Existen errores en el formato de fecha y hora");

						}

					}

				}
				else if(usuario.getId_roles().equals(3L)){
					//Admin punto 
					String idPunto = JOptionPane.showInputDialog(this, "Ingrese el id del Punto de Vacunación", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE);
					if (vacuAndes.darPunto(idPunto)== null)
						throw new Exception("El punto ingresado no se encuentra en la base de datos");

					JList<String> list = new JList<>( new String[]{"Una fecha","Rango de fechas","Rango de horas"} );
					JOptionPane.showMessageDialog(null, list, "Selecccione la opcion para relizar la consulta (seleccione únicamente una)", JOptionPane.PLAIN_MESSAGE);
					List<String> seleccionados = list.getSelectedValuesList();
					boolean fecha = seleccionados.contains("Una fecha") ? true: false;
					boolean fechas = seleccionados.contains("Rango de fechas") ? true: false;
					boolean horas = seleccionados.contains("Rango de horas") ? true: false;

					if(fecha == true){
						String fechaResp = JOptionPane.showInputDialog(this, "Ingrese la fecha en formato DD-MM-YYYY", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE);
						String fecha1 = fechaResp+" 00:00";
						String fecha2 = fechaResp+ " 23:59";
						try{
							List<String> documentos = vacuAndes.darAtendidosPuntoFechas(idPunto, fecha1, fecha2);

							String resultado = "En mostrarCiudadanosPuntos\n\n"; 
							resultado += "Ciudadanos hallados correctamente: \n";

							for(int i= 0; i<documentos.size(); i++){
								resultado+=documentos.get(i)+"\n";
							}
							resultado += "\n Operación terminada";

							if(documentos.size()==0)
								resultado = "No se hallaron ciudadanos atendidos con las especificaciones dadas";

							panelDatos.actualizarInterfaz(resultado);

						}
						catch(Exception e){
							throw new Exception("Existen errores en el formato de fecha y hora");
						}
					}
					else if(fechas == true){
						String fecha1 = JOptionPane.showInputDialog(this, "Ingrese la primera fecha en formato DD-MM-YYYY", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE) + " 00:00";
						String fecha2 = JOptionPane.showInputDialog(this, "Ingrese la segunda fecha en formato DD-MM-YYYY", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE)+ " 23:59";
						try{
							List<String> documentos = vacuAndes.darAtendidosPuntoFechas(idPunto, fecha1, fecha2);

							String resultado = "En mostrarCiudadaqnosPuntos\n\n"; 
							resultado += "Ciudadanos hallados correctamente: \n";

							for(int i= 0; i<documentos.size(); i++){
								resultado+=documentos.get(i)+"\n";
							}
							resultado += "\n Operación terminada"; 

							if(documentos.size()==0)
								resultado = "No se hallaron ciudadanos atendidos con las especificaciones dadas";

							panelDatos.actualizarInterfaz(resultado);

						}
						catch(Exception e){
							throw new Exception("Existen errores en el formato de fecha y hora");
						}
					}
					else if(horas == true){
						Long hora1 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese la hora de inicio en formato HH", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
						Long min1 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese el minuto de inicio en formato MI", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
						Long hora2 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese la hora final en formato HH", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
						Long min2 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese el minuto de fin en formato MI", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
						try{
							List<String> documentos = vacuAndes.darAtendidosPuntoHoras(idPunto, hora1, hora2,min1, min2);

							String resultado = "En mostrarCiudadaqnosPuntos\n\n"; 
							resultado += "Ciudadanos hallados correctamente: \n";

							for(int i= 0; i<documentos.size(); i++){
								resultado+=documentos.get(i)+"\n";
							}
							resultado += "\n Operación terminada"; 

							if(documentos.size()==0)
								resultado = "No se hallaron ciudadanos atendidos con las especificaciones dadas";

							panelDatos.actualizarInterfaz(resultado);
						}
						catch(Exception e){
							throw new Exception("Existen errores en el formato de fecha y hora");
						}

					}

				}
			}

		}
		catch (Exception e) {
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Consulta la base de datos para hallar los 20 puntos de vacunación más efectivos
	 */
	public void mostrarPuntosMasEfectivos() {
		try
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(1L) && !usuario.getId_roles().equals(2L) && !usuario.getId_roles().equals(3L)) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}

			JList<String> list = new JList<>( new String[]{"Una fecha","Rango de fechas","Rango de horas"} );
			JOptionPane.showMessageDialog(null, list, "Selecccione la opcion para realizar la consulta (seleccione únicamente una)", JOptionPane.PLAIN_MESSAGE);
			List<String> seleccionados = list.getSelectedValuesList();
			boolean fecha = seleccionados.contains("Una fecha") ? true: false;
			boolean fechas = seleccionados.contains("Rango de fechas") ? true: false;
			boolean horas = seleccionados.contains("Rango de horas") ? true: false;

			if(fecha == true){
				String fechaResp = JOptionPane.showInputDialog(this, "Ingrese la fecha en formato DD-MM-YYYY", "Hallar 20 puntos más efectivos", JOptionPane.QUESTION_MESSAGE);
				String fecha1 = fechaResp+" 00:00";
				String fecha2 = fechaResp+ " 23:59";
				try{

					List<String> puntos = vacuAndes.darPuntosEfectivosFechas(fecha1, fecha2);

					String resultado = "En mostrarPuntosMasEfectivos\n\n"; 
					resultado += "Puntos hallados correctamente: \n";

					for(int i= 0; i<puntos.size(); i++){
						resultado+=puntos.get(i)+"\n";
					}
					resultado += "\n Operación terminada"; 

					if(puntos.size()==0)
						resultado = "No se hallaron puntos efectivos con las especificaciones dadas";

					panelDatos.actualizarInterfaz(resultado); 

				}
				catch(Exception e){
					String resultado = generarMensajeError(e);
					panelDatos.actualizarInterfaz(resultado);

				}
			}
			else if(fechas == true){
				String fecha1 = JOptionPane.showInputDialog(this, "Ingrese la primera fecha en formato DD-MM-YYYY", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE) + " 00:00";
				String fecha2 = JOptionPane.showInputDialog(this, "Ingrese la segunda fecha en formato DD-MM-YYYY", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE)+ " 23:59";
				try{

					List<String> puntos = vacuAndes.darPuntosEfectivosFechas(fecha1, fecha2);

					String resultado = "En mostrarPuntosMasEfectivos\n\n"; 
					resultado += "Puntos hallados correctamente: \n";

					for(int i= 0; i<puntos.size(); i++){
						resultado+=puntos.get(i)+"\n";
					}
					resultado += "\n Operación terminada"; 

					if(puntos.size()==0)
						resultado = "No se hallaron puntos efectivos con las especificaciones dadas";

					panelDatos.actualizarInterfaz(resultado); 
				}
				catch(Exception e){
					String resultado = generarMensajeError(e);
					panelDatos.actualizarInterfaz(resultado);

				}
			}
			else if(horas == true){
				Long hora1 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese la hora de inicio en formato HH", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
				Long min1 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese el minuto de inicio en formato MI", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
				Long hora2 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese la hora final en formato HH", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
				Long min2 = Long.parseLong(JOptionPane.showInputDialog(this, "Ingrese el minuto de fin en formato MI", "Hallar ciudadanos atentidos", JOptionPane.QUESTION_MESSAGE));
				try{
					List<String> puntos = vacuAndes.darPuntosEfectivosHoras( hora1, hora2,min1, min2);

					String resultado = "En mostrarPuntosMasEfectivos\n\n"; 
					resultado += "Puntos hallados correctamente: \n";

					for(int i= 0; i<puntos.size(); i++){
						resultado+=puntos.get(i)+"\n";
					}
					resultado += "\n Operación terminada"; 

					if(puntos.size()==0)
						resultado = "No se hallaron puntos efectivos con las especificaciones dadas";

					panelDatos.actualizarInterfaz(resultado); 
				}
				catch(Exception e){
					String resultado = generarMensajeError(e);
					panelDatos.actualizarInterfaz(resultado);;
				}
			}
		}
		catch (Exception e) {
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Consulta la base de datos para hallar el índice de vacunación para un grupo poblacional
	 */
	public void mostrarIndiceGrupoPoblacional() 
	{
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(1L) && !usuario.getId_roles().equals(2L)) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}
			Boolean region, eps, estado, grupo;
			List<String> selecRegion = null, selecEps = null;
			String selecEstado = null, selecGrupo = null;
			JList<String> list = new JList<>( new String[]{"Región","Eps","Estado","Grupo de priorización"} );
			JOptionPane.showMessageDialog(null, list, "Selecccione las opciones para filtrar el grupo poblacional (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
			List<String> seleccionados = list.getSelectedValuesList();
			region = seleccionados.contains("Región") ? true: false;
			eps = seleccionados.contains("Eps") ? true: false;
			estado = seleccionados.contains("Estado") ? true: false;
			grupo = seleccionados.contains("Grupo de priorización") ? true: false;

			if ( region ) {
				list = new JList<>( darRegiones() );
				JOptionPane.showMessageDialog(null, list, "Selecccione la región (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
				selecRegion = list.getSelectedValuesList();
			}
			if ( eps ) {
				list = new JList<>( darTodasEps() );
				JOptionPane.showMessageDialog(null, list, "Selecccione la eps (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
				selecEps = list.getSelectedValuesList();
			}
			if ( estado ) {
				list = new JList<>( darEstados() );
				JOptionPane.showMessageDialog(null, list, "Selecccione el estado (uno solo)", JOptionPane.PLAIN_MESSAGE);
				selecEstado = list.getSelectedValue().split(":")[0];
			}
			if ( grupo ) {
				list = new JList<>( darGruposDePriorizacion() );
				JOptionPane.showMessageDialog(null, list, "Selecccione el grupo (uno solo)", JOptionPane.PLAIN_MESSAGE);
				selecGrupo = list.getSelectedValue();
			}
			int answ = JOptionPane.showConfirmDialog(null, "¿Desea filtrar por fecha?", "Fecha", JOptionPane.YES_NO_OPTION);
			String fechaInicial = null, fechaFinal = null;
			if ( answ == JOptionPane.YES_OPTION ) {
				fechaInicial = JOptionPane.showInputDialog (this, "Ingrese la fecha inicial en formato DD-MM-YYYY", "Adicionar vacunación", JOptionPane.QUESTION_MESSAGE);
				fechaFinal = JOptionPane.showInputDialog (this, "Ingrese la fecha final en formato DD-MM-YYYY. Si solo desea consultar 1 fecha solo oprima OK", "Adicionar vacunación", JOptionPane.QUESTION_MESSAGE);
				if ( fechaFinal.equals("") ) {
					fechaFinal = null;
				}
			}
			Double respuesta = vacuAndes.darIndiceVacunacion(selecEps, selecEstado == null? null: Long.parseLong(selecEstado), selecGrupo, selecRegion, fechaInicial, fechaFinal);
			if ( respuesta == null ) {
				throw new Exception("No hay ningún ciudadano registrado en VacuAndes que pertenezca al grupo poblacional");
			}
			String resultado = "En Indice grupo poblacional\n\n";
			resultado += "Indice encontrado: " + respuesta;
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
		}
		catch (Exception e) {
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}

	}
	
	/**
	 * Consulta la base de datos para hallar los puntos de vacunación con dosis disponibles en un rango de fechas y una hora
	 */
	public void puntosDosisDisponibles(){
		try 
		{
			VOInfoUsuario usuario = panelValidacionUsuario();
			if ( usuario != null ) {
				if ( !usuario.getId_roles().equals(1L) && !usuario.getId_roles().equals(2L) && !usuario.getId_roles().equals(3L)
						 && !usuario.getId_roles().equals(4L) && !usuario.getId_roles().equals(5L) && !usuario.getId_roles().equals(6L)) {
					throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				}
			}
			else {
				return;
			}
			
			String hora = JOptionPane.showInputDialog(this, "Ingrese la hora en el formato HH24:MI", "Hallar puntos con dosis disponibles", JOptionPane.QUESTION_MESSAGE);
			String fecha1 = JOptionPane.showInputDialog(this, "Ingrese la primera fecha en formato DD-MM-YYYY", "Hallar puntos con dosis disponibles", JOptionPane.QUESTION_MESSAGE) + " "+hora;
			String fecha2 = JOptionPane.showInputDialog(this, "Ingrese la segunda fecha en formato DD-MM-YYYY", "Hallar puntos con dosis disponibles", JOptionPane.QUESTION_MESSAGE)+ " "+ hora;
			
			List<String> ids = vacuAndes.darIdsPuntos();
			
			String mensaje = "Los puntos con dosis disponibles en el rango de fechas dado son: ";
			
			for(int i=0; i<ids.size(); i++)
			{
				String actual = ids.get(i);
				Long vacunas = vacuAndes.darVacunasPunto(actual);
				Long gastadas= vacuAndes.darCitasFinalizadasFechasPunto(actual, fecha1, fecha2);
				
				if((vacunas-gastadas) > 0){
				   mensaje+=actual+" - Dosis disponibles entre las fechas dadas: "+ (vacunas-gastadas)+ "\n" ;
				}
			}
			
			panelDatos.actualizarInterfaz(mensaje);
			
		}	
			catch (Exception e) {
				//			e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
	}

	/**
	 * Consulta la base de datos para hallar los ciudadanos que entraron en contacto con otro ciudadano
	 */public void ciudadanosContacto()
	 {
		 try 
		 {
			 VOInfoUsuario usuario = panelValidacionUsuario();
			 if ( usuario != null ) {
				 if ( !usuario.getId_roles().equals(1L) && !usuario.getId_roles().equals(2L) && !usuario.getId_roles().equals(3L)
						 && !usuario.getId_roles().equals(4L) && !usuario.getId_roles().equals(5L) && !usuario.getId_roles().equals(6L)) {
					 throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				 }
			 }
			 else {
				 return;
			 }
			 String documento = JOptionPane.showInputDialog(this, "Ingrese el documento del ciudadano", "Hallar ciudadanos en contacto", JOptionPane.QUESTION_MESSAGE);
			 String fecha2 = JOptionPane.showInputDialog(this, "Ingrese la fecha de búsqueda en formato DD-MM-YYYY", "Hallar ciudadanos en contacto", JOptionPane.QUESTION_MESSAGE)+ " 00:00";

			 DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			 LocalDate fecha = LocalDate.parse(fecha2.trim(), format);

			 LocalDate fecha10 = fecha.plusDays(-10);
			 String fechaatras = fecha10.toString();
			 String[] fechapartida = fechaatras.split("-");
			 String fecha1 = fechapartida[2]+"-"+fechapartida[1]+"-"+fechapartida[0]+" 00:00";

			 documento= documento.trim();

			 String id_punto = vacuAndes.darIdPuntoVacunacionDocumento(documento);

			 List<Cita> citasCiudadano = vacuAndes.darCitasCiudadanoPunto(id_punto, documento);

			 List<String> ciudadanos= new ArrayList<String>();

			 for(int i=0; i<citasCiudadano.size(); i++)
			 {
				 String fechaS = citasCiudadano.get(i).getFechaHora().toString();
				 fechaS = fechaS.substring(0,fechaS.length()-5);
				 String[] fechaho = fechaS.split(" ");
				 String hora= fechaho[1];
				 String[] añomesdia = fechaho[0].split("-");
				 String fechahora = añomesdia[2]+"-"+añomesdia[1]+"-"+añomesdia[0]+" "+hora;

				 List<String> documentos = vacuAndes.ciudadanosContacto(documento, fecha1, fecha2, id_punto, fechahora);

				 for(int j=0; j<documentos.size(); j++)
				 {
					 if(!documentos.get(j).equals(documento))
					 {
						 ciudadanos.add(documentos.get(j));
					 }
				 }

			 }

			 String resultado = "Los documentos de los ciudadanos que entraron en contacto con el ciudadano de documento "+documento+" son: \n";
			 for(int k=0; k<ciudadanos.size();k++)
			 {
				 resultado+= ciudadanos.get(k)+"\n";
			 }

			 if(ciudadanos.size() == 0 )
			 {
				 resultado= "El ciudadano de documento "+ documento+ " no se encontraba en ningún punto en los 10 dias previos a la fecha dada";
			 }

			 panelDatos.actualizarInterfaz(resultado);

		 }
		 catch (Exception e) {
			 //			e.printStackTrace();
			 String resultado = generarMensajeError(e);
			 panelDatos.actualizarInterfaz(resultado);
		 }
	 }

	 /**
	  * Consulta la base de datos para encontrar los ciudadanos que pertenecen a grupos poblacionales variables
	  */
	 public void analizarCohortes( )
	 {
		 try 
		 {
			 VOInfoUsuario usuario = panelValidacionUsuario();
			 if ( usuario != null ) {
				 if ( !usuario.getId_roles().equals(1L)) {
					 throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				 }
			 }
			 else {
				 return;
			 }
			 Boolean edad, sexo, condiciones, region, eps, punto, dosis, tecnologiaVac;
			 String selecEdad = null;
			 List<String> selecSexo=null, selecCondiciones=null, selecRegion=null, selecEps=null, selecPunto=null, selecDosis=null, selecTecnologiaVac=null;
			 JList<String> list = new JList<>( new String[]{"Edad","Sexo","Condiciones de priorización","Región", "Eps", "Punto de vac.", "Dosis aplicadas", "Tecnología vacuna"} );
			 JOptionPane.showMessageDialog(null, list, "Selecccione las opciones para filtrar el cochorte (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
			 List<String> seleccionados = list.getSelectedValuesList();
			 edad = seleccionados.contains("Edad") ? true: false;
			 sexo = seleccionados.contains("Sexo") ? true: false;
			 condiciones = seleccionados.contains("Condiciones de priorización") ? true: false;
			 region = seleccionados.contains("Región") ? true: false;
			 eps = seleccionados.contains("Eps") ? true: false;
			 punto = seleccionados.contains("Punto de vac.") ? true: false;
			 dosis = seleccionados.contains("Dosis aplicadas") ? true: false;
			 tecnologiaVac = seleccionados.contains("Tecnología vacuna") ? true: false;

			 if ( edad ) {
				 selecEdad = JOptionPane.showInputDialog(null, "Ingrese la edad (EDAD) o rango de edades (EDAD1-EDAD2)", "Edades", JOptionPane.QUESTION_MESSAGE);
			 }
			 if ( sexo ) {
				 list = new JList<>( new String[]{"FEMENINO","MASCULINO"} );
				 JOptionPane.showMessageDialog(null, list, "Selecccione el sexo (cmd o ctrl sostenido para seleeccionar varios)", JOptionPane.PLAIN_MESSAGE);
				 selecSexo = list.getSelectedValuesList();
			 }
			 if ( condiciones ) {
				 list = new JList<>( darGruposDePriorizacion() );
				 JOptionPane.showMessageDialog(null, list, "Selecccione la condición de priorización (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
				 selecCondiciones = list.getSelectedValuesList();
			 }
			 if ( region ) {
				 list = new JList<>( darRegiones() );
				 JOptionPane.showMessageDialog(null, list, "Selecccione la región (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
				 selecRegion = list.getSelectedValuesList();
			 }
			 if ( eps ) {
				 list = new JList<>( darTodasEps() );
				 JOptionPane.showMessageDialog(null, list, "Selecccione la eps (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
				 selecEps = list.getSelectedValuesList();
			 }
			 if ( punto ) {
				 list = new JList<>( darPuntos() );
				 JOptionPane.showMessageDialog(null, list, "Selecccione el punto (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
				 selecPunto = list.getSelectedValuesList();
			 }
			 if ( dosis ) {
				 list = new JList<>( new String[]{"DOSIS 1", "DOSIS 2"} );
				 JOptionPane.showMessageDialog(null, list, "Selecccione la dosis (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
				 selecDosis = list.getSelectedValuesList();
			 }
			 if ( tecnologiaVac ) {
				 list = new JList<>( darTecnologiaVacunas() );
				 JOptionPane.showMessageDialog(null, list, "Selecccione la tecnología (cmd o ctrl sostenido para seleeccionar varias)", JOptionPane.PLAIN_MESSAGE);
				 selecTecnologiaVac = list.getSelectedValuesList();
			 }
			 List<Ciudadano> ciudadanos = vacuAndes.analizarCohortes(selecEdad, selecSexo, selecCondiciones, selecRegion, selecEps, selecPunto, selecDosis, selecTecnologiaVac);
			 if ( ciudadanos == null ) {
				 throw new Exception("No hay ningún ciudadano registrado en VacuAndes que pertenezca al cohorte");
			 }
			 String resultado = "En analizarCohortesl\n\n";
			 if ( ciudadanos.size() == 0 ) {
				 resultado += "\n No se encontraron ciudadanos en el cohorte";
			 } else {
				 resultado += "\n Los ciudadanos encontrados en el cohorte son: \n";
				 for ( Ciudadano actual: ciudadanos ) {
					 resultado += actual.toString() + "\n";
				 }
			 }
			 resultado += "\n Operación terminada";
			 panelDatos.actualizarInterfaz(resultado);
		 }
		 catch (Exception e) {
			 //			e.printStackTrace();
			 String resultado = generarMensajeError(e);
			 panelDatos.actualizarInterfaz(resultado);
		 }
	 }

	 /**
	  * Consulta la bnase de datos para hallar el tiempo con mayor afluencia, el más riesgoso y el menos riesgoso
	  */
	 public void analizarOperacion()
	 {
		 try 
		 {
			 VOInfoUsuario usuario = panelValidacionUsuario();
			 if ( usuario != null ) {
				 if ( !usuario.getId_roles().equals(1L) && !usuario.getId_roles().equals(2L) && !usuario.getId_roles().equals(3L)
						 && !usuario.getId_roles().equals(4L) && !usuario.getId_roles().equals(5L) && !usuario.getId_roles().equals(6L)) {
					 throw new Exception( "El usuario validado no tiene acceso a este requerimiento" );
				 }
			 }
			 else {
				 return;
			 }

			 String[] lista = {"Año", "Mes", "Dia", "Hora"};
			 JList<String> list = new JList<>( lista );
			 JOptionPane.showMessageDialog(null, list, "Seleccione la unidad de tiempo (Seleccione solo uno)", JOptionPane.PLAIN_MESSAGE);
			 String seleccionado = list.getSelectedValue();

			 if(seleccionado == null)
			 {
				 throw new Exception( "No seleccionó ninguna unidad de tiempo" );
			 }

			 String tiempo = "";

			 if( seleccionado.equals("Año") )
			 {
				 tiempo= "YYYY";
			 }
			 else if ( seleccionado.equals("Mes") )
			 {
				 tiempo = "MM";
			 }
			 else if( seleccionado.equals("Dia") )
			 {
				 tiempo = "DD";
			 }
			 else if( seleccionado.equals("Hora") )
			 {
				 tiempo= "HH24";
			 }

			 String id_punto = JOptionPane.showInputDialog(this, "Ingrese el id del punto de vacunación", "Ananlizar operación", JOptionPane.QUESTION_MESSAGE);

			 if( id_punto == null && id_punto.equals(""))
			 {
				 throw new Exception("No ha ingresado un id");
			 }

			 Long capacidadPunto = vacuAndes.darCapacidad(id_punto);
			 
			 String ti = vacuAndes.analizarOperacionTiempo(tiempo, id_punto);
		 
			 if(ti.equals("[]"))
			 {
				 panelDatos.actualizarInterfaz("El punto "+id_punto+" no tiene citas");
				 return;
			 }

			 String mensaje = "El "+seleccionado+" con mayor afluencia en el punto fue: "+ ti +"\n";

			 List<Long> cantidades = vacuAndes.analizarOperacionCantidad(tiempo, id_punto);

			 List<Long> mayor = new ArrayList<Long>();
			 List<Long> menor = new ArrayList<Long>();

			 double div=0.0;

			 for(int i = 0; i<cantidades.size(); i++)
			 {
				 div = (double) cantidades.get(i)/capacidadPunto;

				 if(div>= 0.9)
				 {
				    if(!mayor.contains(cantidades.get(i)))
					   mayor.add(cantidades.get(i));
				 }

				 if(div <= 0.1)
				 {
					 if(!menor.contains(cantidades.get(i)))
					      menor.add(cantidades.get(i));
				 }
			 }

			 String mayorS = "";

			 if(seleccionado.equals("Mes"))
			 {
				 mayorS+= "\nLos/as "+seleccionado+"es"+ " de mayor riesgo fueron: \n";
			 }
			 else
			 {
				 mayorS+= "\nLos/as "+seleccionado+"s"+ " de mayor riesgo fueron: \n";
			 }

			 for(int i=0; i<mayor.size();i++)
			 {
				 mayorS+= vacuAndes.analizarOperacionTiempoCantidad(mayor.get(i), tiempo, id_punto)+"\n";
			 }
			 if(mayor.size()==0)
				 mayorS= "\nNo hubo afluencia mayor al 90% en el punto\n";
			 
			 String menorS="";

			 if(seleccionado.equals("Mes"))
			 {
				 menorS+= "\nLos/as "+seleccionado+"es"+ " de menor riesgo fueron: \n";
			 }
			 else
			 {
				 menorS+= "\nLos/as "+seleccionado+"s"+ " de menor riesgo fueron: \n";
			 }

			 for(int i=0; i<menor.size();i++)
			 {
				 menorS+= vacuAndes.analizarOperacionTiempoCantidad(menor.get(i), tiempo, id_punto)+"\n";
			 }
			 if(menor.size()==0)
				 menorS= "\nNo hubo afluencia menor al 10% en el punto\n";

			 panelDatos.actualizarInterfaz(mensaje+mayorS+menorS);
		 }
		 catch (Exception e) {
			 //			e.printStackTrace();
			 String resultado = generarMensajeError(e);
			 panelDatos.actualizarInterfaz(resultado);
		 }
	 }

	 /* ****************************************************************
	  * 			Métodos administrativos
	  *****************************************************************/
	 /**
	  * Muestra el log de VacuAndes
	  */
	 public void mostrarLogVacuAndes()
	 {
		 mostrarArchivo( "vacuandes.log" );
	 }

	 /**
	  * Muestra el log de datanucleus
	  */
	 public void mostrarLogDatanuecleus ()
	 {
		 mostrarArchivo( "datanucleus.log" );
	 }

	 /**
	  * Limpia el contenido del log de vacuandes
	  * Muestra en el panel de datos la traza de la ejecución
	  */
	 public void limpiarLogVacuAndes()
	 {
		 // Ejecución de la operación y recolección de los resultados
		 boolean resp = limpiarArchivo( "vacuandes.log" );

		 // Generación de la cadena de caracteres con la traza de la ejecución de la demo
		 String resultado = "\n\n************ Limpiando el log de vacuandes ************ \n";
		 resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		 resultado += "\nLimpieza terminada";

		 panelDatos.actualizarInterfaz(resultado);
	 }

	 /**
	  * Limpia el contenido del log de datanucleus
	  * Muestra en el panel de datos la traza de la ejecución
	  */
	 public void limpiarLogDatanucleus()
	 {
		 // Ejecución de la operación y recolección de los resultados
		 boolean resp = limpiarArchivo("datanucleus.log");

		 // Generación de la cadena de caracteres con la traza de la ejecución de la demo
		 String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		 resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		 resultado += "\nLimpieza terminada";

		 panelDatos.actualizarInterfaz(resultado);
	 }

	 /**
	  * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	  * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	  */
	 public void limpiarBD()
	 {
		 try 
		 {
			 // Ejecución de la demo y recolección de los resultados
			 Long eliminados [] = vacuAndes.limpiarVacuAndes();

			 // Generación de la cadena de caracteres con la traza de la ejecución de la demo
			 String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			 resultado += eliminados [0] + " EPS eliminados\n";
			 resultado += eliminados [1] + " ROLES eliminados\n";
			 resultado += eliminados [2] + " ESTADOS eliminados\n";
			 resultado += eliminados [3] + " ETAPAS eliminadas\n";
			 resultado += eliminados [4] + " CONDICIONPRIORIZACION eliminadas\n";
			 resultado += eliminados [5] + " PUNTOS eliminados\n";
			 resultado += eliminados [6] + " VACUNAS eliminadas\n";
			 resultado += eliminados [7] + " ASIGNADAS eliminadas\n";
			 resultado += eliminados [8] + " CIUDADANOS eliminados\n";
			 resultado += eliminados [9] + " VACUNACIONES eliminadas\n";
			 resultado += eliminados [10] + " PRIORIZACIONES eliminadas\n";
			 resultado += eliminados [11] + " INFOUSUARIOS eliminadas\n";
			 resultado += eliminados [12] + " USUARIOS eliminados\n";
			 resultado += eliminados [13] + " CITAS eliminadas\n";
			 resultado += "\nLimpieza terminada";

			 panelDatos.actualizarInterfaz(resultado);
		 } 
		 catch (Exception e) 
		 {
			 //			e.printStackTrace();
			 String resultado = generarMensajeError(e);
			 panelDatos.actualizarInterfaz(resultado);
		 }
	 }

	 /**
	  * Muestra el modelo conceptual de VacuAndes
	  */
	 public void mostrarModeloConceptual ()
	 {
		 mostrarArchivo ("data/ModeloConceptualVacuAndes.pdf");
	 }

	 /**
	  * Muestra el esquema de la base de datos de VacuAndes
	  */
	 public void mostrarEsquemaBD ()
	 {
		 mostrarArchivo ("data/ModeloRelacionalVacuAndes.pdf");
	 }

	 /**
	  * Muestra el script de creación de la base de datos
	  */
	 public void mostrarScriptBD ()
	 {
		 mostrarArchivo ("data/CreacionEsquemaVacuAndes.sql");
	 }

	 /**
	  * Muestra la arquitectura de referencia para VacuAndes
	  */
	 public void mostrarArqRef ()
	 {
		 mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	 }

	 /**
	  * Muestra la documentación Javadoc del proyectp
	  */
	 public void mostrarJavadoc ()
	 {
		 mostrarArchivo ("doc/index.html");
	 }

	 /**
	  * Muestra la información acerca del desarrollo de esta apicación
	  */
	 public void acercaDe ()
	 {
		 String resultado = "\n\n ************************************\n\n";
		 resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		 resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		 resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		 resultado += " * \n";		
		 resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		 resultado += " * Proyecto: VacuAndes\n";
		 resultado += " * @version 1.0\n";
		 resultado += " * @author Néstor González\n";
		 resultado += " * @author Mariana Zamora\n";
		 resultado += " * Abril de 2021\n";
		 resultado += " * \n";
		 resultado += "\n ************************************\n\n";

		 panelDatos.actualizarInterfaz(resultado);		
	 }


	 /* ****************************************************************
	  * 			Métodos privados para la presentación de resultados y otras operaciones
	  *****************************************************************/

	 /**
	  * Valida el login y clave de un usuario de VacuAndes para conocer su rol y acceso a los requerimientos
	  * @return Un objeto VOInfoUsuario con la información del usuario encontrado o null si no lo encuentra
	  */
	 private VOInfoUsuario panelValidacionUsuario() {
		 try {
			 String login = JOptionPane.showInputDialog (this, "Ingrese el login del usuario", "Validar usuario", JOptionPane.QUESTION_MESSAGE);
			 String clave = JOptionPane.showInputDialog (this, "Ingrese la contraseña del usuario", "Validar usuario", JOptionPane.QUESTION_MESSAGE);

			 if ( login != null && !login.trim().equals("") && clave != null && !clave.trim().equals("") ) {
				 VOInfoUsuario usuario = vacuAndes.darInfoUsuario( login );
				 if ( usuario == null ) {
					 throw new Exception( "El usuarion con el login " + login + " no está registrado" );
				 }
				 if ( !usuario.getClave().equals(clave.trim()) ) {
					 throw new Exception( "La clave ingresada no corresponde al usuario con el login " + login );
				 }
				 String resultado = "En validar usuario\n\n";
				 resultado += "Usuario validado correctamente";
				 resultado += "\n Operación terminada";
				 panelDatos.actualizarInterfaz(resultado);
				 return usuario;
			 }
		 } catch (Exception e) {
			 //			e.printStackTrace();
			 String resultado = generarMensajeError(e);
			 panelDatos.actualizarInterfaz(resultado);
		 }
		 return null;
	 }

	 /**
	  * Obtiene las regiones registradas en vacuandes
	  * @return un arreglo de tamaño fijo de String con las regiones registradas
	  */
	 private String[] darRegiones() {
		 List<String> regiones = vacuAndes.darRegiones();
		 String[] retorno = new String[regiones.size()];
		 regiones.toArray(retorno);
		 return retorno;
	 }

	 /**
	  * Obtiene los roles registrados en vacuandes
	  * @return un arreglo de tamaño fijo de String con las regiones registradas
	  */
	 private String[] darRoles() {
		 List<String> roles = new LinkedList();
		 for ( Rol actual : vacuAndes.darRoles() ) {
			 roles.add( actual.getId() + ":" +actual.getRol() );
		 }
		 String[] retorno = new String[roles.size()];
		 roles.toArray(retorno);
		 return retorno;
	 }

	 /**
	  * Obtiene los estados registrados en vacuandes
	  * @return un arreglo de tamaño fijo de String con los estados registradas
	  */
	 private String[] darEstados() {
		 List<String> estados = new LinkedList<>();
		 for ( Estado actual: vacuAndes.darEstados()) {
			 estados.add( actual.getId() + ": " + actual.getDescripcion() );
		 }
		 String[] retorno = new String[estados.size()];
		 estados.toArray(retorno);
		 return retorno; 
	 }

	 /**
	  * Obtiene las EPS registradas en vacuandes
	  * @return un arreglo de tamaño fijo de String con las EPS registradas
	  */
	 private String[] darTodasEps() {
		 List<String> eps = new LinkedList<>();
		 for ( EPS actual: vacuAndes.darEPSs()) {
			 eps.add( actual.getId() );
		 }
		 String[] retorno = new String[eps.size()];
		 eps.toArray(retorno);
		 return retorno;
	 }

	 /**
	  * Obtiene los Puntos de vacunación registrados en vacuandes
	  * @return un arreglo de tamaño fijo de String con los Puntos registrados
	  */
	 private String[] darPuntos() {
		 List<String> puntos = new LinkedList<>();
		 for ( Punto actual: vacuAndes.darPuntos() ) {
			 puntos.add( actual.getId() );
		 }
		 String[] retorno = new String[puntos.size()];
		 puntos.toArray(retorno);
		 return retorno;
	 }

	 /**
	  * Obtiene las tecnologías de las vacunas registradas en vacuandes
	  * @return un arreglo de tamaño fijo de String con las tecnologías registrados
	  */
	 private String[] darTecnologiaVacunas() {
		 List<String> tecnologias = new LinkedList<>();
		 for ( String actual: vacuAndes.darTecnologiasVacunas() ) {
			 tecnologias.add( actual );
		 }
		 String[] retorno = new String[tecnologias.size()];
		 tecnologias.toArray(retorno);
		 return retorno;
	 }

	 /**
	  * Obtiene las condigicones de priorización registradas en vacuandes
	  * @return un arreglo de tamaño fijo de String con las condiciones de priorización registradas
	  */
	 private String[] darGruposDePriorizacion() {
		 List<String> grupos = new LinkedList<>();
		 for ( CondicionPriorizacion actual: vacuAndes.darCondicionesPriorizacion()) {
			 grupos.add( actual.getDescripcion() );
		 }
		 String[] retorno = new String[grupos.size()];
		 grupos.toArray(retorno);
		 return retorno;
	 }

	 /**
	  * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
	  * @param e - La excepción recibida
	  * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
	  */
	 private String darDetalleException(Exception e) 
	 {
		 String resp = "";
		 if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		 {
			 JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			 return je.getNestedExceptions() [0].getMessage();
		 }
		 return resp;
	 }

	 /**
	  * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	  * @param e - La excepción generada
	  * @return La cadena con la información de la excepción y detalles adicionales
	  */
	 private String generarMensajeError(Exception e) 
	 {
		 String resultado = "************ Error en la ejecución\n";
		 resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		 resultado += "\n\nRevise datanucleus.log y vacuandes.log para más detalles";
		 return resultado;
	 }

	 /**
	  * Limpia el contenido de un archivo dado su nombre
	  * @param nombreArchivo - El nombre del archivo que se quiere borrar
	  * @return true si se pudo limpiar
	  */
	 private boolean limpiarArchivo(String nombreArchivo) 
	 {
		 BufferedWriter bw;
		 try 
		 {
			 bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			 bw.write ("");
			 bw.close ();
			 return true;
		 } 
		 catch (IOException e) 
		 {
			 //			e.printStackTrace();
			 return false;
		 }
	 }

	 /**
	  * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	  * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	  */
	 private void mostrarArchivo (String nombreArchivo)
	 {
		 try
		 {
			 Desktop.getDesktop().open(new File(nombreArchivo));
		 }
		 catch (IOException e)
		 {
			 e.printStackTrace();
		 }
	 }

	 /**
	  * Inicializa la etapa de VacuAndes en caso de que no haya sido inicializada previamente
	  */
	 private void inicializarEtapa() {
		 try {
			 if ( vacuAndes.darEtapaVacuAndes() == null ) {
				 String etapa = JOptionPane.showInputDialog (this, "Ingrese la etapa actual de vacuAndes", "Etapa VacuAndes", JOptionPane.QUESTION_MESSAGE);
				 if ( etapa != null && !etapa.trim().equals("") ) {
					 Long numero_etapa = Long.parseLong(etapa);
					 if ( vacuAndes.darEtapa(numero_etapa) != null ) {
						 if ( vacuAndes.adicionarEtapaVacuAndes(numero_etapa) == null ) {
							 throw new Exception ("La etapa no se pudo adicionar");
						 }
						 String resultado = "En inicializarEtapa\n\n"; 
						 resultado += "Etapa adicionada correctamente: " + numero_etapa; 
						 resultado += "\n Operación terminada"; 
						 panelDatos.actualizarInterfaz(resultado);
					 }
				 }
			 }
		 }
		 catch (Exception e) {
			 //			e.printStackTrace(); 
			 String resultado = generarMensajeError(e);
			 panelDatos.actualizarInterfaz(resultado);
		 }
	 }

	 /* ****************************************************************
	  * 			Métodos de la Interacción
	  *****************************************************************/
	 /**
	  * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
	  * Invoca al método correspondiente según el evento recibido
	  * @param pEvento - El evento del usuario
	  */
	 @Override
	 public void actionPerformed(ActionEvent pEvento)
	 {
		 String evento = pEvento.getActionCommand( );		
		 try 
		 {
			 Method req = InterfazVacuAndesApp.class.getMethod ( evento );			
			 req.invoke ( this );
		 } 
		 catch (Exception e) 
		 {
			 e.printStackTrace();
		 } 
	 }

	 /* ****************************************************************
	  * 			Programa principal
	  *****************************************************************/
	 /**
	  * Este método ejecuta la aplicación, creando una nueva interfaz
	  * @param args Arreglo de argumentos que se recibe por línea de comandos
	  */
	 public static void main( String[] args )
	 {
		 try
		 {

			 // Unifica la interfaz para Mac y para Windows.
			 UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
			 new InterfazVacuAndesApp( );

		 }
		 catch( Exception e )
		 {
			 e.printStackTrace( );
		 }
	 }
}
