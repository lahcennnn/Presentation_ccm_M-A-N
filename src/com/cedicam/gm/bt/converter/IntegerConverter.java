package com.cedicam.gm.bt.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class IntegerConverter extends GenericConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		Integer nombre;
		try {
			nombre = Integer.parseInt((String) value);
		} catch (NumberFormatException nfe) {
			nombre = null;
		}
		return nombre;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		String nombre;
		
		if (null == value) {
			nombre = "";
		} else {
			nombre = "" + value;
		}
		
		return nombre;
	}
}