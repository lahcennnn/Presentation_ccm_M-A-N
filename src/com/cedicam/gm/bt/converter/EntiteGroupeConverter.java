package com.cedicam.gm.bt.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.cedicam.gm.bt.constantes.ConstantesTypo;

public class EntiteGroupeConverter extends GenericConverter implements Converter {

	public EntiteGroupeConverter() {	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if (ConstantesTypo.TOP_CLIENT_EXTERNE_TRUE.equals(value)) {
			return "N";
		} else if (ConstantesTypo.TOP_CLIENT_EXTERNE_FALSE.equals(value)) {
			return "O";
		} else {
			return value;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return (String) getAsObject(context, component, (String) value);
	}

	/**
	 * Méthode dupliquée pour être utilisée lors de l'export.
	 * 
	 * @param value
	 * @return
	 * @throws ConverterException
	 */
	public static String getAsString(String value) throws ConverterException {
		if (ConstantesTypo.TOP_CLIENT_EXTERNE_TRUE.equals(value)) {
			return "N";
		} else if (ConstantesTypo.TOP_CLIENT_EXTERNE_FALSE.equals(value)) {
			return "O";
		} else {
			return value;
		}
	}

}