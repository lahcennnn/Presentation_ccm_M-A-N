/**
 * <p><b>MOTIVATION</b><BR>
 * <I>Sans objet</I>
 * <p><b>DESCRIPTION</b><BR>
 * <I>Sans objet</I>
 * <p><b>INDICATION D'UTILISATION</b><BR>
 * <I>Sans objet</I>
 * <p><b>DOCUMENTS DE REFERENCE</b><BR>
 * <I>Sans objet</I>
 *
 * <p><b>DATE CREATION</b><BR>
 * <I>23 août 2010</I>
 * <p><b>AUTEUR</b><BR>
 * <I>Valéry BONNET</I>
 * <p><b>SOCIETE</b><BR>
 * <I>CEDICAM</I>
 * <p><b>CONTACT</b><BR>
 * <I>valery.bonnet@capgemini.com</I>
 */

package com.cedicam.gm.bt.erreur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;

import com.cedicam.gm.bt.core.service.TraceService;
import com.cedicam.gm.bt.exception.LdapAccessException;
import com.cedicam.gm.bt.exception.LdapNoProfileException;
import com.cedicam.gm.bt.exception.LdapNoRightException;
import com.cedicam.gm.bt.exception.LdapUnknownUserException;
import com.cedicam.gm.bt.sgc.ContexteCedicam;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.ui.core.GenericBean;

/**
 * @author Administrateur
 *
 */
public class ErreurBean extends GenericBean {
	private static final String ID_COMP = "GMBTBNERREUR";

	private TraceService traceService;
	private ContexteCedicam contexteCedicam;
	
	private String codeErreurHttp = null;
	private String message = null;
	private String exception = null;
	
	public ErreurBean() {		
		contexteCedicam = loadContexteCedicam();
		traceService = (TraceService) ApplicationContextHolder.getTraceService();
		
		setEnteteComposant(ID_COMP, "ERREUR");
		getTraceService().enregistrerTraceDebug("MSG0170", ID_COMP, getEnteteComposant(), getContexteCedicam());
		FacesContext context = FacesContext.getCurrentInstance();
				
		// Suppression des faces messages pour éviter un affichage en doublon.
//		Iterator<FacesMessage> itMessages = context.getMessages();
//		while(itMessages.hasNext()) {
//			itMessages.next();
//			itMessages.remove();
//		}
		
		Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
		String exceptionClass = requestMap.get("excclass");
		if (exceptionClass != null) {
			defineMessages(exceptionClass);
		}
		
//		String erreurCode = requestMap.get("errorcode");
//		if (erreurCode != null) {
//			codeErreurHttp = erreurCode;
//		}
	}
	
	private void defineMessages(String exceptionClass) {
		String codeMessage;
		List<String> parametres = new ArrayList<String>();
		
		if (ViewExpiredException.class.getName().equals(exceptionClass)) {
			codeMessage = "MSG4500";
		} else if (LdapAccessException.class.getName().equals(exceptionClass)) {
			codeMessage = "MSG4502";
		} else if (LdapNoProfileException.class.getName().equals(exceptionClass)) {
			codeMessage = "MSG4503";
			parametres.add(contexteCedicam.getIdagt());
		} else if (LdapNoRightException.class.getName().equals(exceptionClass)) {
			codeMessage = "MSG4504";
			parametres.add(contexteCedicam.getIdagt());
		} else if (LdapUnknownUserException.class.getName().equals(exceptionClass)) {
			codeMessage = "MSG3199";
		} else {
			codeMessage = "MSG4501";
			exception = exceptionClass;
		}
		
		if (null != codeMessage) {
			message = "" + traceService.getMessage(codeMessage, parametres);
		}
	}

	public String getCodeErreurHttp() {
		return codeErreurHttp;
	}

	public void setCodeErreurHttp(String codeErreurHttp) {
		this.codeErreurHttp = codeErreurHttp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
}