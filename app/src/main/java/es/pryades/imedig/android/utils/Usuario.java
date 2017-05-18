package es.pryades.imedig.android.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author Dismer Ronda 
*/

@SuppressWarnings({ "unchecked", "rawtypes" })
@EqualsAndHashCode(callSuper=true)
@Data 
public class Usuario extends EnermetDto 
{
	public static final int PASS_OK			= 0;
	public static final int PASS_NEW 		= 1;
	public static final int PASS_FORGET 	= 2;
	public static final int PASS_EXPIRY 	= 3;
	public static final int PASS_CHANGED 	= 4;
	public static final int PASS_BLOCKED 	= 5;

	String login;
	String email;
	String pwd;
	Integer cambio;
	Integer intentos;
	Integer estado;
	Integer perfil;
	String nombre;
	String ape1;
	String ape2;
	String contactos; 
	String filtro; 
	String query; 
	String compresion; 
	String titulo; 
	
	public String getNombreCompleto()
	{
		return (titulo.isEmpty() ? "" : titulo + " ") + nombre + " " + ape1 + (ape2.isEmpty() ? "" : " " + ape2);
	}
}
