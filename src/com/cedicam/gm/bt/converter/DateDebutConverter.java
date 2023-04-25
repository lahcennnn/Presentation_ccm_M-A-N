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
public class DateDebutConverter implements Converter  {

	/** Le TimeZone par défault. */
	private static final TimeZone TIMEZONE_DEFAULT = TimeZone.getTimeZone("Europe/Paris");

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
		if (str == null) {
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
		if (context == null) {
			throw new NullPointerException("facesContext");
		}
		if (component == null) {
			throw new NullPointerException("uiComponent");
		}
		Date dateConvertir;
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {

			format.setTimeZone(TIMEZONE_DEFAULT);
			format.setLenient(false);

			if (isBlank(value)) {

				value = "01/01/1900";
				dateConvertir = format.parse(value);
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
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String dateChaine = dateFormat.format(value);
			if ("01/01/1900".equals(dateChaine)) {
				return "";
			} else {
				return dateChaine;
			}
		} else {
			if (value instanceof String) {
				return (String) value;
			} else {
				return "";
			}
		}
	}
}
