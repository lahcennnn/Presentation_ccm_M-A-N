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
		// les montants sont stockés en centimes (2 décimales) et affichées avec le séparateur décimale
		String nb = "";
		
		DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(java.util.Locale.FRANCE));
		// TODO Format à sortir dans le fichier de localisation
		try {
			// on affiche les nombres avec le séparateur décimale (stockés en centimes)
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