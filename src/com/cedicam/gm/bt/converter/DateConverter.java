package com.cedicam.gm.bt.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.validator.ValidatorException;

import org.richfaces.component.html.HtmlCalendar;

import com.cedicam.gm.ui.core.FormValidationUtil;
import com.cedicam.gm.ui.core.GenericBean;

public class DateConverter implements Converter {

	/**
	 * @param str
	 * @return
	 */

	private static final TimeZone TIMEZONE_DEFAULT = TimeZone.getTimeZone("Europe/Paris");

	private static final String dateSeparateur = "/";
	
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

	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		Date dateConvertir = null;
		boolean result = false;
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		HtmlCalendar scrCalendar = (HtmlCalendar) component;
		String nomChp = scrCalendar.getLabel();
		String prefixMsg = FormValidationUtil.ERROR_PREFIX_KEY;
		
		try {
			format.setTimeZone(TIMEZONE_DEFAULT);
			format.setLenient(false);
			
			if (!isBlank(value)) {
				Integer.parseInt(value.substring(6));
				Integer.parseInt(value.substring(3, 5));
				Integer.parseInt(value.substring(0, 2));
				String sepddMM = value.substring(2, 3);
				String sepMMyyyy = value.substring(5, 6);
				
				if (sepddMM.equals(dateSeparateur) && sepMMyyyy.equals(dateSeparateur)) {
					dateConvertir = format.parse(value);
					result = true;
				}
			} else {
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}

		if (!result) {
			ResourceBundle bundle = GenericBean.RUBRIQUE_PROPERTIES;
			String debutMsg = bundle.getString(prefixMsg);
			String descErrMsg = bundle.getString(FormValidationUtil.ERROR_DATE_FORMAT);
			String summary =  debutMsg + " : " + nomChp + " - " + descErrMsg;
			
			FacesMessage fMsg = new FacesMessage();
			fMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fMsg.setSummary(summary);
			fMsg.setDetail(descErrMsg);
			
			throw new ConverterException(fMsg);
		}

		return dateConvertir;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(null==value) return "";
		if (value instanceof Date) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormat.format(value);
		} else {
			if (value instanceof String) {
				return (String) value;
			} else {
				return "";
			}
		}

	}
}
