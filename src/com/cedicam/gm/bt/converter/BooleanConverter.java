package com.cedicam.gm.bt.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class BooleanConverter extends GenericConverter implements Converter {

	public BooleanConverter() {	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if("O".equals(value)) return Boolean.TRUE;
		if("N".equals(value)) return Boolean.FALSE;
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		if(Boolean.TRUE==value) return "O";
		if(Boolean.FALSE==value) return "N";
		return null;
	}

}