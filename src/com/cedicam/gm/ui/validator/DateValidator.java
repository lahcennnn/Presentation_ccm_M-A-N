/**
 * 
 */
package com.cedicam.gm.ui.validator;

import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.richfaces.component.html.HtmlCalendar;

import com.cedicam.gm.ui.core.FormValidationUtil;
import com.cedicam.gm.ui.core.GenericBean;

/**
 * @author CDENIS
 *
 */
public class DateValidator implements Validator {

	/* (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		boolean success = true;
		
		HtmlCalendar scrCalendar = (HtmlCalendar) arg1;
		String nomChp = scrCalendar.getLabel();
		String errorDescription = "";
		String prefixMsg = FormValidationUtil.ERROR_PREFIX_KEY;
		
		if (arg2 != null) {
			String dateStr = "" + arg2;
			if (FormValidationUtil.empty(dateStr)) {
				errorDescription = FormValidationUtil.ERROR_EMPTY;
				success = false;
			} else if (!FormValidationUtil.isDate(dateStr)) {
				errorDescription = FormValidationUtil.ERROR_DATE_FORMAT;
				success = false;
			}
		}
		if (!success) {
			
			ResourceBundle bundle = GenericBean.RUBRIQUE_PROPERTIES;
			String debutMsg = bundle.getString(prefixMsg);
			String descErrMsg = bundle.getString(errorDescription);
			String summary =  debutMsg + " : " + nomChp + " - " + descErrMsg;
			
			FacesMessage fMsg = new FacesMessage();
			fMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fMsg.setSummary(summary);
			fMsg.setDetail(descErrMsg);
			
			throw new ValidatorException(fMsg);
		}
	}

}
