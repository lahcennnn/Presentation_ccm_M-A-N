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

public class DateFinConverter implements Converter {

	/**
	 * @param str
	 * @return
	 */

	private static final TimeZone TIMEZONE_DEFAULT = TimeZone.getTimeZone("Europe/Paris");

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static Date stringToDate(String sDate, String sFormat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
		return sdf.parse(sDate);
	}

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

				value = "31/12/9999";
				dateConvertir = format.parse(value);
			} else {
				dateConvertir = format.parse(value);
			}
		} catch (ParseException e) {
			String[] objet = {value, "", component.getAttributes().get("label").toString()};
			throw new ConverterException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR, "javax.faces.converter.DateTimeConverter.DATETIME", objet, FacesContext.getCurrentInstance()));
		}
		return dateConvertir;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(null==value) return "";
		if (value instanceof Date) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String dateChaine = dateFormat.format(value);
			if ("31/12/9999".equals(dateChaine)) {
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
