package com.cedicam.gm.bt.outil;

import java.util.Map;

import javax.faces.context.FacesContext;

public class MiscUtils {
	public static String getRequestParams(){
		Map<String, String> paramsMap= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		StringBuffer sb = new StringBuffer();
		for(String s: paramsMap.keySet()){
	        sb.append(s);
	        sb.append(" ::: ");
	        sb.append(paramsMap.get(s));
	    }
		return sb.toString();
	}
}
