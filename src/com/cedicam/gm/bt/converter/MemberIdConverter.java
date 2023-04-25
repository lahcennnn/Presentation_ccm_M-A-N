package com.cedicam.gm.bt.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/** DateDebutConverter.
 */
public class MemberIdConverter implements Converter  {
	/** Taille du member id. */
	private static final int LENGTH = 11;
	
	/** Caractère de remplacement. */
	private static final char FILLER = '0';

	/** getAsObject.
	 * @param context Le contexte
	 * @param component Le composant
	 * @param value La valeur de la chaîne à convertir
	 * @return La chaîne de caractère sous forme d'objet
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Long nombre = null;
		if (null != value && !value.trim().isEmpty()) {
			nombre = Long.parseLong(value);
		}
		
		return nombre;
	}

	/** getAsString.
	 * @param context Le contexte
	 * @param component Le composant
	 * @param value L'object à convertir en String
	 * @return La String représentant l'objet
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return getAsString(value);
	}

	/** getAsString.
	 * dupliquée pour les besoins de l'export
	 * @param value L'object à convertir en String
	 * @return La String représentant l'objet
	 */
	public static String getAsString(Object value) {
		String result = "";
		
		String string = "" + value;
		
		if (null != value && !string.trim().isEmpty()) {
			result = new String(new char[LENGTH]).replaceAll("" + Character.MIN_VALUE, "" + FILLER);
			result += string;
			result = result.substring(result.length() - LENGTH);
		}
		
		return result;
	}
}
