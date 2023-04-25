package com.cedicam.gm.bt.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class MontantConverter extends GenericConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

		return getAsString(context, component, value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		// les montants sont stock�s en centimes (2 d�cimales) et affich�es avec le s�parateur d�cimale
		String nb = "";
		
		DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(java.util.Locale.FRANCE));
		// TODO Format � sortir dans le fichier de localisation
		try {
			// on affiche les nombres avec le s�parateur d�cimale (stock�s en centimes)
			Number lNb = (Number) value;
			double ldNb = lNb.doubleValue() / 100;
 			// formatage pour l'affichage
			nb = df.format(ldNb);
		} catch (Exception ex) {
			nb = "";
		}
		
		return nb;
	}
}