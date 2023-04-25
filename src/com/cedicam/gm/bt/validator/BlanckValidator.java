package com.cedicam.gm.bt.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.shared_impl.util.MessageUtils;

public class BlanckValidator implements Validator {

	@Override
	public void validate(FacesContext paramFacesContext, UIComponent paramUIComponent, Object paramObject) throws ValidatorException {
		try {
			//System.out.println("validation param" + paramObject);
			String string = paramObject.toString();
			//System.out.println("validation string " + string);
			if (isBlanck(string)) {
				String[] objet = {paramUIComponent.getAttributes().get("label").toString()};
				throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR, "com.cedicam.gm.bt.validator.BLANCK",
						objet, FacesContext.getCurrentInstance()));
			}
		} catch (NullPointerException e) {
			String[] objet = {paramUIComponent.getAttributes().get("label").toString()};
			throw new ValidatorException(MessageUtils.getMessage(FacesContext.getCurrentInstance(), "com.cedicam.gm.bt.validator.NULL",
					objet));
		}
	}

	private boolean isBlanck(String string) {
		if (string.isEmpty()) {
			return true;
		} else {
			char[] charArray = string.toCharArray();
			for (char c : charArray) {
				if (c != ' ')
					return false;
			}
			return true;
		}
	}
}
