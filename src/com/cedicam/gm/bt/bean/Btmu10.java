package com.cedicam.gm.bt.bean;

import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.cedicam.gm.bt.msg.Messages;

/**
 * @author Administrateur
 * 
 */
public class Btmu10 {

	/** MSG_QUESTION. */
	public static final String MSG_QUESTION = "Confirmation";
	/** MSG_ERREUR. */
	public static final String MSG_ERREUR = "Erreur";
	/** MSG_INFO. */
	public static final String MSG_INFO = "Information";
	/** MSG_AVERTISSEMENT. */
	public static final String MSG_WARNING = "Avertissement";

	/** serialVersionUID. */
	private static final long serialVersionUID = -6479897299239746841L;
	/** BEAN_NAME. */
	private static final String BEAN_NAME = Btmu10.class.getName();

	/** messageHeader. */
	private String messageHeader;
	/** messageImage. */
	private String messageImage;
	/** msgUtilisateur. */
	private Messages msgUtilisateur;

	/**
	 * Constructeur.
	 */
	public Btmu10() {
	}

	/**
	 * Correspondance entre le type message et le niveau de séverité de jsf
	 * SEVERITY_WARN est retrounée en cas d'échec de correspondance.
	 * 
	 * @param type Le type
	 * @return Severity
	 */
	public static Severity getCorrespondreTypeMessage(String type) {
		Severity fmSeverity = null;

		if (type.equals(MSG_ERREUR)) {
			fmSeverity = FacesMessage.SEVERITY_ERROR;
		} else if (type.equals(MSG_INFO)) {
			fmSeverity = FacesMessage.SEVERITY_INFO;
		} else if (type.equals(MSG_QUESTION)) {
			fmSeverity = FacesMessage.SEVERITY_INFO;
		} else {
			fmSeverity = FacesMessage.SEVERITY_WARN;
		}
		return fmSeverity;
	}

	/**
	 * Rendre un message jsf pour la partie présentation.
	 * 
	 * @param type Le type
	 * @param codeMessage Le codeMessage
	 * @param libelle Le libelle
	 * @return
	 */
	private void rendreMessage(String type, String codeMessage, String libelle) {
		String msgDetail = type + codeMessage;

		// Correspondre type message et Severity faces
		Severity fmSeverity = getCorrespondreTypeMessage(type);

		// Créer le message et restituer à l'écran par file d'attente
		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage facesMessage = new FacesMessage(fmSeverity, type + ", " + codeMessage + ", " + libelle, msgDetail);
		
		boolean dejaEmis = false;
		Iterator<FacesMessage> itMessages = facesContext.getMessages();
		while (itMessages.hasNext()) {
			FacesMessage message = itMessages.next();
			if (message.getSummary().equals(facesMessage.getSummary())) {
				dejaEmis = true;
				break;
			}
		}
		
		if (!dejaEmis) {
			facesContext.addMessage(codeMessage, facesMessage);
		}
	}

	/**
	 * Rendre un message jsf pour la partie présentation. Constituer le libellé
	 * avec plusieurs paramètre de substitution
	 * 
	 * @param type Le type
	 * @param codeMessage Le codeMessage
	 * @param libRemplacement Le libRemplacement
	 * @return
	 */
	public void addMessage(String type, String codeMessage, String[] libRemplacement) {
		String libelle = getMessage(codeMessage, libRemplacement);
		if (libelle == null) {
			libelle = "Le libellé du code " + codeMessage + " n'existe pas dans l'application. Liste des paramètres: ";
			StringBuffer message = new StringBuffer(libelle);
			for (int index = 0; index < libRemplacement.length; index++) {
				message.append(libRemplacement[index] + " ");
			}
			libelle = message.toString();
		}
		rendreMessage(type, codeMessage, libelle);
	}

	/**
	 * Rendre un message jsf pour la partie présentation. Constituer le libellé
	 * avec un unique paramètre de substitution
	 * 
	 * @param type Le type
	 * @param codeMessage Le codeMessage
	 * @param libRemplacement Le libRemplacement
	 * @return
	 */
	public void addMessage(String type, String codeMessage, String libRemplacement) {
		String libelle = getMessage(codeMessage, libRemplacement);
		if (libelle == null) {
			libelle = "Le libellé du code " + codeMessage + " n'existe pas dans l'application. Liste des paramètres: " + libRemplacement;
		}
		rendreMessage(type, codeMessage, libelle);
	}

	/**
	 * Rendre un message jsf pour la partie présentation. Récupère le libellé
	 * 
	 * @param type Le type
	 * @param codeMessage Le codeMessage
	 * @return
	 */
	public void addMessage(String type, String codeMessage) {
				String libelle = getMessage(codeMessage);
		if (libelle == null) {
			libelle = "Le libellé du code " + codeMessage + " n'existe pas dans l'application.";
		}
		rendreMessage(type, codeMessage, libelle);
	}

	/**
	 * Récupère le libellé valorisé du code message avec plusieurs paramètres de
	 * remplacement.
	 * 
	 * @param codeMessage Le codeMessage
	 * @param libRemplacement Le libRemplacement
	 * @return Le message
	 */
	public String getMessage(String codeMessage, String[] libRemplacement) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		msgUtilisateur = Messages.getInstance(facesContext.getViewRoot().getLocale());
		return msgUtilisateur.getMessage(codeMessage, libRemplacement);
	}

	/**
	 * Récupère le libellé valorisé du code message avec un unique paramètre de
	 * remplacement.
	 * 
	 * @param codeMessage Le codeMessage
	 * @param libRemplacement Le libRemplacement
	 * @return Le message
	 */
	public String getMessage(String codeMessage, String libRemplacement) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		msgUtilisateur = Messages.getInstance(facesContext.getViewRoot().getLocale());
		return msgUtilisateur.getMessage(codeMessage, libRemplacement);
	}

	/**
	 * Récupère le libellé du code message.
	 * 
	 * @param codeMessage Le codeMessage
	 * @return Le message
	 */
	public String getMessage(String codeMessage) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			UIViewRoot viewRoot = facesContext.getViewRoot();
			if (viewRoot != null) {
				msgUtilisateur = Messages.getInstance(viewRoot.getLocale());
			}
		}
		return msgUtilisateur.getMessage(codeMessage);
	}

	/**
	 * @return Is not blank
	 */
	public Boolean getRenderMessage() {
		return new Boolean(isNotBlank(getMessageHeader()));
	}

	/**
	 * @return Bean name
	 */
	public String getBeanName() {
		return BEAN_NAME;
	}

	/**
	 * @return Le message
	 */
	public String getMessageHeader() {
		return messageHeader;
	}

	/**
	 * @return Le message
	 */
	public String getMessageImage() {
		return messageImage;
	}

	/**
	 * @param str L'objet String
	 * @return Is not blank
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * @param str L'objet String
	 * @return Is blank
	 */
	public static final boolean isBlank(final String str) {
		boolean isBlank = true;
		if (str != null) {
			isBlank = str.trim().isEmpty();
		}
		return isBlank;
	}
}
