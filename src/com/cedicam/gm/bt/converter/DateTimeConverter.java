package com.cedicam.gm.bt.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.myfaces.shared_impl.util.MessageUtils;

/** DateDebutConverter.
 */
public class DateTimeConverter implements Converter  {

	/** Le TimeZone par défault. */
	private static final TimeZone TIMEZONE_DEFAULT = TimeZone.getTimeZone("Europe/Paris");
	
	/** Le pattern par défulat. */
	private static final String PATTERN_DEFAULT = "dd/MM/yyyy HH:mm";
	
	/** Le pattern pour transformer la chaîne en String et inversement. */
	private String pattern;

	/** default Constructor. */
	public DateTimeConverter() {
		pattern = PATTERN_DEFAULT;
	}
	
	/** parameter Constructor. 
	 * @param pattern Le pattern à utiliser
	 */
	public DateTimeConverter(String pattern) {
		this.pattern = pattern;
	}
	
	
	/** isNotBlank.
	 * @param str Une chaîne à tester
	 * @return True si la chaîne n'est pas vide, sinon False
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/** isBlank.
	 * @param str Une chaîne à tester
	 * @return True si la chaîne est vide, sinon False
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/** stringToDate.
	 * @param sDate Une String à convertir
	 * @param sFormat Le format de la String à convertir
	 * @return La date correspondante
	 * @throws Exception Si le couple String/Format n'est pads valide
	 */
	public static Date stringToDate(String sDate, String sFormat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
		return sdf.parse(sDate);
	}

	/** getAsObject.
	 * @param context Le contexte
	 * @param component Le composant
	 * @param value La valeur de la chaîne à convertir
	 * @return La chaîne de caractère sous forme d'objet
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Date dateConvertir;
		DateFormat format = new SimpleDateFormat(pattern);
		try {
			format.setTimeZone(TIMEZONE_DEFAULT);
			format.setLenient(false);

			if (isBlank(value)) {
				return null;
			} else {
				dateConvertir = format.parse(value);
			}
		} catch (ParseException e) {
			String[] objet = {value, "", component.getAttributes().get("label").toString()};
			throw new ConverterException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR, 
					"javax.faces.converter.DateTimeConverter.DATETIME", objet, FacesContext.getCurrentInstance()));
		}
		return dateConvertir;
	}

	/** getAsString.
	 * @param context Le contexte
	 * @param component Le composant
	 * @param value L'object à convertir en String
	 * @return La String représentant l'objet
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof Date) {
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			String dateChaine = dateFormat.format(value);
			return dateChaine;
		} else {
			return "";
		}
	}

	/** getPattern.
	 * @return Le pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/** setPattern.
	 * @param pattern Le pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
