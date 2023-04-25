package com.cedicam.gm.bt.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class StatutConverterToolTip extends GenericConverter implements Converter {

	private Map<String, String> mapCodeLib = new HashMap<String, String>();
	private Map<String, String> mapLibCode = new HashMap<String, String>();
	public StatutConverterToolTip() {	}
	private static String PREFIX_STATUT_RESULT="gm.param.statut.";

	private void init() {
		if(mapCodeLib.size()==0){
			ResourceBundle bundle = ResourceBundle.getBundle("rubriques");
			for(String key: bundle.keySet()){
				if(key.startsWith(PREFIX_STATUT_RESULT)){
					String smallkey = key.substring(PREFIX_STATUT_RESULT.length());
					String value = bundle.getString(key);
					mapCodeLib.put(smallkey, value);
					mapLibCode.put(value, smallkey);
				}
			}
		}
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		init();
		return mapLibCode.get(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		String result = "";
		init();
		if (value != null && mapCodeLib.containsKey(value)) {
			result = mapCodeLib.get(String.valueOf(value));
		}
		return result;
	}

}