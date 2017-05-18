package es.pryades.imedig.android.utils;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author dismer.ronda 
* @since 1.0.0.0
*/

@EqualsAndHashCode(callSuper=true)
@Data
public class InformeQuery extends Informe 
{
	Long desde;
	Long hasta;
	List<String> list_claves;
	List<Integer> estados;
}
