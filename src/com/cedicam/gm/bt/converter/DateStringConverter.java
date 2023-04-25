/**
 * 
 */
package com.cedicam.gm.bt.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

/**
 * @author CDENIS
 *
 */
public class DateStringConverter extends DateTimeConverter {

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) throws ConverterException {
		String str = null;
		if (arg2 != null) {
			str = "" + arg2;
		}
		return str;
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) throws ConverterException {
		Object o = null;
		if (arg2 != null && !arg2.isEmpty()) {
			o = arg2;
		}
		return o;
	}
	
}
