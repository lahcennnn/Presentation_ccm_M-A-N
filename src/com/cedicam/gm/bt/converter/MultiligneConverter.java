package com.cedicam.gm.bt.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class MultiligneConverter extends GenericConverter implements Converter {

		@Override
		public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

			return getAsString(context, component, value);
		}

		@Override
		public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
			
			int tailleLigne = 100;
			
			int position = tailleLigne;
			String result = "" + value;
			
			String newligne=System.getProperty("line.separator");
			
			// Remplacer les espace par des non-breaking spaces
			result = result.replace(" ", ""+(char)160);
			
			// Enlever les sauts de lignes
			result = removeChar(result, '\r');
			result = removeChar(result, '\n');
			
			try {
				while (position < result.length()) {
					result = addCharAt(result, newligne, position);
					position += tailleLigne + newligne.length();
				}	
			} catch (Exception ex) {
				result = "";
			}

			result = removeChar(result, '\t');
			
			return result;
		}
		
		private String addCharAt(String aString, String aToAdd, int aPosition) {
			return aString.substring(0,aPosition) + aToAdd + aString.substring(aPosition);
		}
		
		public static String removeChar(String s, char c) {
			  StringBuffer r = new StringBuffer( s.length() );
			  r.setLength( s.length() );
			  int current = 0;
			  for (int i = 0; i < s.length(); i ++) {
			     char cur = s.charAt(i);
			     if (cur != c) r.setCharAt( current++, cur );
			  }
			  return r.toString();
		}
	}
