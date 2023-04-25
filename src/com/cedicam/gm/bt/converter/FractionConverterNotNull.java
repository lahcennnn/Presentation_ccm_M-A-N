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
public class FractionConverterNotNull extends GenericConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) throws ConverterException {
		try {
			String newString;
			if (string.contains(",")) {
				newString = string.replace(",", ".");

			} else {
				newString = string;
			}
			BigDecimal bigDecimal = new BigDecimal(newString);
			if (BigDecimal.ZERO.equals(bigDecimal)) {
				String[] objet = {string, "", component.getAttributes().get("label").toString()};
				throw new ConverterException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,
						"com.cedicam.gm.bt.converter.FractionConverter.NotNull", objet, FacesContext.getCurrentInstance()));
			} else {
				return bigDecimal;
			}
		} catch (ConverterException e) {
			throw e;
		} catch (Exception e) {
			String[] objet = {string, "", component.getAttributes().get("label").toString()};
			throw new ConverterException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,
					"javax.faces.converter.NumberConverter.NUMBER", objet, FacesContext.getCurrentInstance()));
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		// TODO Auto-generated method stub
		if (object == null) {
			return "";
		} else {
			return String.valueOf(object);
		}
	}

}
