package com.cedicam.gm.ui.core;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.springframework.remoting.RemoteAccessException;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.bt.core.clientwebservice.ReponseWebServiceDateApplication;
import com.cedicam.gm.bt.core.service.EvenementService;
import com.cedicam.gm.bt.core.service.TraceService;
import com.cedicam.gm.bt.core.webservice.DateWebService;
import com.cedicam.gm.bt.data.dto.DateApplicationDto;
import com.cedicam.gm.bt.exception.ErreurTechnique;
import com.cedicam.gm.bt.sgc.ContexteCedicam;
import com.cedicam.gm.bt.sgt.EnteteComposant;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;

/**
 * @author Administrateur
 *
 */
public abstract class GenericBean {
	
	private Boolean afficheDateEtCode = null;
	private static final String PARAM_AFFICHE_DATE_CODE = "gm.commun.activation.codeetdate";
	
	public static Object findBean(String beanName) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    return context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
	}

	protected int checkIntParam(String paramName){
		String str =  getParam(paramName);
		if(str!=null && str.trim().length()>0){
			int res = Integer.parseInt(str);
			if(res>0)return res;
		}
		return 0;
	}

	protected String checkStringParam(String paramName){
		String str =  getParam(paramName);
		if(str!=null && str.trim().length()>0){
			return str.trim();
		}
		return null;
	}


	protected String getParam(String paramName){
		Map<String,String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		return map.get(paramName);
	}


	/** projectProperties. */
	private static Map<String,String> projectProperties;
	/** flaginit. */
	private static boolean flaginit = false;

	/** contexteCedicam. */
	private ContexteCedicam contexteCedicam;
	/** enteteComposant. */
	private EnteteComposant enteteComposant;

	/** evenementService. */
	private EvenementService evenementService;
	/** traceService. */
	private TraceService traceService;

	/** btmu10. */
	private Btmu10 btmu10 = new Btmu10();
	/** dateApplicationDto. */
	private DateApplicationDto dateApplicationDto = null;
	/** modification. */
	private boolean modification = false;

	/** codeSource pour écrans bloc VE. */
	private String codeSource;
	/** libellé pour écrans bloc VE. */
	private String libelleSource;

	/** code ecran. */
	private String codeEcran = "st000";

	/** Bundle des noms d'écran.*/
	public static ResourceBundle RUBRIQUE_PROPERTIES = ResourceBundle.getBundle("rubriques");
	/** Séparateur dans le fil d'Ariane.*/
	public static String SEPARATEUR_ARIANE = " > ";

	/**
	 * Consructeur.
	 */
	public GenericBean() {
		traceService = (TraceService) ApplicationContextHolder.getTraceService();
		evenementService = (EvenementService) ApplicationContextHolder.getEvenementService();

		contexteCedicam = loadContexteCedicam();
		if (!flaginit) {
			// Récupération des données du project.properties
			projectProperties = ApplicationContextHolder.getProjectProperties();
			try {
				String t = (String) projectProperties.get("wn.cm.list.nbMaxLines");
				flaginit = true;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		appelServiceRechercherDate();

		//setCodeSource(ConstantesTypo.SOURCE_MASTERCARD);
		setLibelleSource("MasterCard");
	}

	/**
	 * @return L'id invalide
	 */
	public String getInvalideId() {
		FacesContext currentInstance = FacesContext.getCurrentInstance();
		Iterator<String> clientIdsWithMessages = currentInstance.getClientIdsWithMessages();
		if (clientIdsWithMessages.hasNext()) {

			String next = clientIdsWithMessages.next();
			//System.out.println("invalide id : " + next);
			next = next.replace(":", "\\:");
			return "#" + next;
		}
		return null;
	}

	/**
	 *
	 */
	private void appelServiceRechercherDate() {
		try {
			DateWebService dateWebService = (DateWebService) ApplicationContextHolder.getContext().getBean("dateWebService");
			ReponseWebServiceDateApplication rechercherDate = dateWebService.rechercherDate(getContexteCedicam());

			switch (rechercherDate.getNuerf()) {
			case ConstantesNuerf.TRAITEMENT_COMPLETED:
				dateApplicationDto = rechercherDate.getDonnees();
				break;

			case ConstantesNuerf.PARAM_NOT_FOUND:
			case ConstantesNuerf.WRONG_FORMAT_RECUP_PARAM:
			default:
				getEvenementService().emettreEvenement("dateWebService.rechercherDate", ConstantesEvtTech.EVT_OBJET_ABSENT_BDD,
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG0096",
						setEnteteComposant("GMBTBNGEN", "GenericBean"), getContexteCedicam());

				btmu10.addMessage(Btmu10.MSG_ERREUR, "MSG0096");
				break;
			}
		} catch (ErreurTechnique e) {
			getEvenementService().emettreEvenement("rechercherDateService", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL, "MSG0143", "appelServiceRechercherDate",
					getEnteteComposant(), getContexteCedicam());

			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
		} catch (RemoteAccessException e) {
			// Cas où la couche métier n'est pas accessible: on est en code évènement "EVT_EXCEPTION_COUCHE_METIER_DETECTEE"
			getEvenementService().emettreEvenement("rechercherDateService", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL, "MSG0097", e, getEnteteComposant(),
					getContexteCedicam());

			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
		} catch (Exception e) {
			getEvenementService().emettreEvenement("rechercherDateService", ConstantesEvtTech.EVT_EXCEPTION_INATTENDUE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL, "MSG0097", e, getEnteteComposant(),
					getContexteCedicam());

			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
		}

	}

	/**
	 * @param idComp L'id du composant
	 * @param libComp Le libelle du composant
	 * @return L'entete du composant
	 */
	protected EnteteComposant setEnteteComposant(String idComp, String libComp) {
		enteteComposant = new EnteteComposant();
		enteteComposant.setIdComp(idComp);
		enteteComposant.setLibAppli("Centre comptable monétique");
		enteteComposant.setLibComp(libComp);
		enteteComposant.setNivArchi((idComp != null && idComp.length() > 1) ? idComp.substring(2, 4) : "");
		enteteComposant.setNomClasse(this.getClass().getName());
		enteteComposant.setTypComp(ConstantesTrace.TYPE_COMPOSANT_WS);
		enteteComposant.setVersAppli(ConstantesTrace.PROJECT_VERSION);
		return enteteComposant;
	}

	/**
	 * @return L'entete du composant
	 */
	public EnteteComposant getEnteteComposant() {
		return enteteComposant;
	}

	/**
	 * @param enteteComposant L'entete du composant
	 */
	public void setEnteteComposant(EnteteComposant enteteComposant) {
		this.enteteComposant = enteteComposant;
	}

	/**
	 * @return Le contexte
	 */
	public ContexteCedicam getContexteCedicam() {
		return contexteCedicam;
	}

	/**
	 * @return Le contexte
	 */
	public ContexteCedicam loadContexteCedicam() {
		HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
		return (ContexteCedicam) containerSession.get("contexteCedicam");
	}

	/**
	 * @param contexteCedicam Le contexte
	 */
	@SuppressWarnings("unchecked")
	public void setContexteCedicam(ContexteCedicam contexteCedicam) {
		this.contexteCedicam = contexteCedicam;
		HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
		containerSession.put("contexteCedicam", contexteCedicam);
	}

	/**
	 * @return Le messager
	 */
	public Btmu10 getBtmu10() {
		return btmu10;
	}

	/**
	 * @param btmu10 Le messager
	 */
	public void setBtmu10(Btmu10 btmu10) {
		this.btmu10 = btmu10;
	}

	/**
	 * @return Le service
	 */
	public TraceService getTraceService() {
		return traceService;
	}

	/**
	 * @param traceService Le service
	 */
	public void setTraceService(TraceService traceService) {
		this.traceService = traceService;
	}

	/**
	 * @return Le service
	 */
	public EvenementService getEvenementService() {
		return evenementService;
	}

	/**
	 * @param evenementService Le service
	 */
	public void setEvenementService(EvenementService evenementService) {
		this.evenementService = evenementService;
	}

	/**
	 * @param dateApplicationDto La date emballée
	 */
	public void setDateApplicationDto(DateApplicationDto dateApplicationDto) {
		this.dateApplicationDto = dateApplicationDto;
	}

	/**
	 * @return La date emballée
	 */
	public DateApplicationDto getDateApplicationDto() {
		return dateApplicationDto;
	}

	/**
	 * @return La date emballée
	 */
	public Date getDateApplication() {
		appelServiceRechercherDate();
		
		if (dateApplicationDto != null) {
			return dateApplicationDto.getDateApplication();
		} else {
			return null;
		}
	}

	/**
	 * @param type Le type
	 * @param codeMessage Le code de message
	 */
	public void redirect(String type, String codeMessage) {
		try {
			// getBtmu10().addMessage(type, codeMessage);
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			externalContext.redirect("/CCM-Presentation/views/com/cedicam/gm/bt/erreur/redirect.bean?type=" + type + "+codeMessage="
					+ codeMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Le separateur
	 */
	public String getSeparateur() {
		return ConstantesTypo.SEPARATEUR_AFFICHAGE_ECRAN;

	}

	/**
	 * @return the codeSource
	 */
	public String getCodeSource() {
		return codeSource;
	}

	/**
	 * @param codeSource the codeSource to set
	 */
	public void setCodeSource(String codeSource) {
		this.codeSource = codeSource;
	}

	/**
	 * @param modification Si modification
	 */
	public void setModification(boolean modification) {
		this.modification = modification;
	}

	/** setModificationToTrue.
	 * @param a Un ActionEvent envoyé par la page
	 */
	public void setModificationToTrue(ActionEvent a) {
		//System.out.println("setModificationToTrue");
		modification = true;
	}

	/** isModification.
	 * @return Si modification
	 */
	public boolean isModification() {
		return modification;
	}

	/** getLibelleSource.
	 * @return the libelleSource
	 */
	public String getLibelleSource() {
		return libelleSource;
	}

	/** setLibelleSource.
	 * @param libelleSource
	 *            the libelleSource to set
	 */
	public void setLibelleSource(String libelleSource) {
		this.libelleSource = libelleSource;
	}

	/** getCodeEcran.
	 * @return the codeEcran
	 */
	public String getCodeEcran() {
		return codeEcran;
	}

	/**
	 * @param event La modification
	 * @throws AbortProcessingException L'arrêt d'action
	 */
	public final void chgSaisie(ValueChangeEvent event) throws AbortProcessingException {
		setModification(true);
	}

	/** enregistrerTaceDebug.
	 * @param nomMethod Le nom de la méthode.
	 */
	public void enregistrerTraceDebug(String nomMethod) {
		getTraceService().enregistrerTraceDebug(nomMethod, null, getEnteteComposant(), getContexteCedicam());
	}

	public void setCodeEcran(String codeEcran) {
		this.codeEcran = codeEcran;
	}

	public Boolean getAfficheDateEtCode() {
		if (null == afficheDateEtCode) {
			Map<String,String> props = ApplicationContextHolder.getProjectProperties();
			String bool = props.get(PARAM_AFFICHE_DATE_CODE);
			this.afficheDateEtCode = Boolean.valueOf(bool);
		}
		return afficheDateEtCode;
	}

	public void setAfficheDateEtCode(Boolean afficheDateEtCode) {
		this.afficheDateEtCode = afficheDateEtCode;
	}

	/**
	 * Méthode retournant le message dans le fichier properties,
	 * en fonction de la clé passée en paramètre.
	 * 
	 * @param cle
	 * @return le message associé
	 */
	public static String recupererMessage(String cle) {
		try {
			ResourceBundle bundle = GenericBean.RUBRIQUE_PROPERTIES;
			return bundle.getString(cle);
		} catch (Exception e) {
			return "";
		}
	}
}
