package com.cedicam.gm.ui.core;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FormValidationUtil {
	public static final SimpleDateFormat SDF_JJMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat  SDF_HHMM = new SimpleDateFormat("HH:mm");
	
	public static final String dateSeparateur = "/";
	public static final String heureSeparateur = ":";

	public static String ERROR_PREFIX_KEY = "gm.error.titleprefix";
	public static String ERROR_EMPTY = "gm.error.datenull";
	public static String ERROR_DATE_FORMAT = "gm.error.date";
	public static String ERROR_HEURE_FORMAT = "gm.error.heure";
	public static String ERROR_NUMERIC_FORMAT = "gm.error.numeric";
	public static String ERROR_DEB_APRES_FIN = "gm.error.datedebutapresfin";

	public static void addFormValidationMessage(String fieldName, String errorDescription){
		ResourceBundle bundle = GenericBean.RUBRIQUE_PROPERTIES;
		String summary = bundle.getString(ERROR_PREFIX_KEY)+" : "+bundle.getString(fieldName)+" - " +bundle.getString(errorDescription);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, bundle.getString(errorDescription)));
	}

	public static boolean notEmpty(String s){
		if(s==null)return false;
		if(s.trim().length()>0) return true;
		return false;
	}

	/**
	 * empty.
	 * @param s La string à tester.
	 * @return Vrai si la change est vide.
	 */
	public static boolean empty(String s) {
		return !notEmpty(s);
	}

	/**
	 * isDate.
	 * @param s La chaine à tester.
	 * @return Vrai si la chaine correspond à une date existante.
	 */
	public static boolean isDate(String s) {
		boolean result = false;
		if (!empty(s) && s.length() == 10) {
			try {
				int yyyy = Integer.parseInt(s.substring(6));
				int mm = Integer.parseInt(s.substring(3, 5));
				int dd = Integer.parseInt(s.substring(0, 2));
				String sepddMM = s.substring(2, 3);
				String sepMMyyyy = s.substring(5, 6);
				
				if (sepddMM.equals(dateSeparateur) && sepMMyyyy.equals(dateSeparateur)) {

					if (mm > 0 && mm <= 12) {
						Calendar c = Calendar.getInstance();
						c.set(Calendar.YEAR, yyyy);
						c.set(Calendar.MONTH, mm - 1);
						
						if (dd > 0 && dd <= c.getActualMaximum(Calendar.DAY_OF_MONTH)) {
							result = true;
						}
					}					
					
				}
			} catch (NumberFormatException n) {
				// do nothing.
			}
		}
		return result;
	}

	/**
	 * isHHM.
	 * @param s La chaine à tester.
	 * @return Vrai si la chaine est une heure existante.
	 */
	public static boolean isHHMM(String s) {
		boolean result = false;
		if (!empty(s) && s.length() == 5) {
			try {
				int hh = Integer.parseInt(s.substring(0, 2));
				int mm = Integer.parseInt(s.substring(3, 5));
				
				String sepHHMM = s.substring(2, 3);
				
				if (sepHHMM.equals(heureSeparateur)) {
					if (hh >= 0 && hh <= 23 && mm <= 59 && mm >= 0) {
						result = true;
					}
				}
			} catch (NumberFormatException n) {
				// do nothing
			}
		}
		return result;
	}

	public static boolean isInteger(String s){
		if(empty(s))return false;
		try {
			Integer.valueOf(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isLong(String s){
		if(empty(s))return false;
		try {
			Long.valueOf(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	public static boolean debutApresFin(Date debut, Date fin){
		if(fin==null || debut==null)return false;
		return debut.after(fin);
	}

}
