package com.cedicam.gm.bt.util;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class RecupererMangedBean {

	public static Object getInstanceSessionOf(String bean) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		return sessionMap.get(bean);
	}

	public static void putInstanceSessionOf(String bean, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(bean, value);
	}
}
