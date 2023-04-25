package com.cedicam.gm.bt.converter;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class NombreConverter extends GenericConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

		return getAsString(context, component, value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		
		String nb = "";
		
		DecimalFormat df = new DecimalFormat("#,##0.###");
		// TODO Format à sortir dans le fichier de localisation
		try {
			nb = df.format(value);
		} catch (Exception ex) {
			nb = "";
		}
		
		return nb;
	}
}