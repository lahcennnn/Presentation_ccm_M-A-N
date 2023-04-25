package com.cedicam.gm.bt.converter;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.myfaces.shared_impl.util.MessageUtils;

/**
 * @author Administrateur
 *
 */
public class FractionConverter extends GenericConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) 
	throws ConverterException {
		BigDecimal montant = null;
		if (string != null && !string.isEmpty()) {
			try {
				String newString;
				if (string.contains(",")) {
					newString = string.replace(",", ".");
				} else {
					newString = string;
				}
				montant = new BigDecimal(newString);
			} catch (Exception e) {
				String[] objet = {string, "",
						component.getAttributes().get("label").toString()};
				throw new ConverterException(MessageUtils.getMessage(
						FacesMessage.SEVERITY_ERROR,
						"javax.faces.converter.NumberConverter.NUMBER", objet,
						FacesContext.getCurrentInstance()));
			}
		}
		return montant;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
/*
 		if (object == null) {
			return "";
		} else {
			return String.valueOf(object);
		}
*/
		String string = null;
		if (object != null) {
			string = String.valueOf(object);
		}
		return string;
	}

}
