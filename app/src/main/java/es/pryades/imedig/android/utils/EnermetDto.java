package es.pryades.imedig.android.utils;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author dismer.ronda 
* @since 1.0.0.0
*/

@EqualsAndHashCode(callSuper=true)
@Data
public class EnermetDto extends Query  implements Serializable
{
	private static final long serialVersionUID = 5525038683283246979L;
	
	Integer id;
}
