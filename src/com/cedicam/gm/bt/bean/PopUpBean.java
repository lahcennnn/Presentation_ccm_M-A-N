/**
 * 
 */
package com.cedicam.gm.bt.bean;

import java.util.Map;

import javax.faces.event.ActionEvent;

import com.cedicam.gm.bt.spring.ApplicationContextHolder;

/**
 * @author CDENIS
 *
 */
public class PopUpBean {

	private Map<String, String> projectProperties;
	private boolean popUpActive = false;
	
	public PopUpBean() {
		projectProperties = ApplicationContextHolder.getProjectProperties();
	}
	
	public String getUrlPopUp() {
		Boolean env = Boolean.valueOf(projectProperties.get("gm.commun.accesUrlBouchon"));
		String urlPopUp;
		if (!env) {
			urlPopUp = "views/com/cedicam/gm/ch/ec001.bean";
		} else {
			urlPopUp = "views/com/cedicam/gm/ch/habilitation/habilitation.bean";
		}
		popUpActive = true;
		return urlPopUp;
	}
	
	public String getPopUpOuverte() {
		popUpActive = false;
		return "";
	}
	
	public String getErreurOuverturePopUp() {
		popUpActive = false;
		return "";
	}

	public boolean isPopUpActive() {
		return popUpActive;
	}

	public void setPopUpActive(boolean popUpActive) {
		this.popUpActive = popUpActive;
	}
	
}
