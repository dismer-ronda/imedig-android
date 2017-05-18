package es.pryades.imedig.android.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author dismer.ronda 
* @since 1.0.0.0
*/

@EqualsAndHashCode(callSuper=true)
@Data
public class Informe extends EnermetDto 
{
	static public final int STATUS_REQUESTED 	= 0;
	static public final int STATUS_INFORMED 	= 1;
	static public final int STATUS_APROVED 		= 2;
	static public final int STATUS_FINISHED 	= 3;
	
    Long fecha;
	String paciente_id;
	String paciente_nombre;
	String estudio_id;
	String estudio_uid;
	String estudio_acceso;
	String modalidad;
	String claves;
	String texto;
	Integer centro;
	Integer informa;
	String icd10cm;
	Integer refiere;
	Integer estado;
	byte[] pdf;
	Integer protegido;
	
	public boolean solicitado()
	{
		return estado == STATUS_REQUESTED;
	}

	public boolean noaprobado()
	{
		return estado == STATUS_INFORMED;
	}
	
	public boolean aprobado()
	{
		return estado == STATUS_APROVED;
	}

	public boolean terminado()
	{
		return estado == STATUS_FINISHED;
	}
}
