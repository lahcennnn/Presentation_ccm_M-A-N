package com.cedicam.gm.ch.bean.menus;

import java.util.HashMap;
import java.util.List;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesFonctionHabilitation;
import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.outil.CacheHabilitation;
import com.cedicam.gm.bt.sgc.ContexteCedicam;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.ch.data.dto.FonctionDto;
import com.cedicam.gm.ui.core.GenericBean;

/**
 * @author Administrateur
 *
 */
public class St000Bean extends GenericBean {

	/** ID_COMP. */
	private static final String ID_COMP = "GMCHBNST0000";

	/** agent. */
	private String agent;
	/** habF0010. */
	private Boolean habF0010 = false;
	/** habF0020. */
	private Boolean habF0020 = false;
	/** hab00030. */
	private Boolean habF0030 = false;
	/** hab00040. */
	private Boolean habF0040 = false;
	/** habF0050. */
	private Boolean habF0050 = false;
	/** hab00060. */
	private Boolean habF0060 = false;
	/** hab00070. */
	private Boolean habF0070 = false;
	/** hab00080. */
	private Boolean habF0080 = false;
	/** hab00090. */
	private Boolean habF0090 = false;
	
	/** rubriquesProperties. */
	private java.util.Properties rubriquesProperties;
	/** applicationNom. */
	private String applicationNom;
	/** applicationNumeroVersion. */
	private String applicationNumeroVersion;
	/** applicationEnvironnementExecution. */
	private String applicationEnvironnementExecution;
	/** applicationContact. */
	private String applicationContact;
	/** applicationCheminAide. */
	private String applicationCheminAide;
	/** applicationFichierAide. */
	private String applicationFichierAide;

	/**
	 * Consructeur.
	 */
	public St000Bean() {
		
		setEnteteComposant(St000Bean.ID_COMP, "Menu général");
		getTraceService().enregistrerTraceDebug("St000Bean", "Création instance",  getEnteteComposant(), getContexteCedicam());

		int rsDroitFonctions = appelServiceDroitFonction();

		getTraceService().enregistrerTraceDebug("St000Bean", "Constructeur, retour appelServiceDroitFonction : " + rsDroitFonctions,
				getEnteteComposant(), getContexteCedicam());

		// Init du nom de l'agent
		if (getContexteCedicam() != null) {
			String nomAgent = getContexteCedicam().getIdagt();
			if (nomAgent != null && nomAgent.contains("@")) {
				String[] tokens = nomAgent.split("@");
				if (tokens != null && tokens.length > 0) {
					nomAgent = tokens[0];
				}
			}
			setAgent(nomAgent);
		}
	}

	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @return The applicationNom
	 */
	public final String getApplicationNom() {
		return this.applicationNom;
	}

	/**
	 * @param applicationNom
	 *            The applicationNom to set
	 */
	public final void setApplicationNom(String applicationNom) {
		this.applicationNom = applicationNom;
	}

	/**
	 * @return The applicationNumeroVersion
	 */
	public final String getApplicationNumeroVersion() {
		return this.applicationNumeroVersion;
	}

	/**
	 * @param applicationNumeroVersion
	 *            The applicationNumeroVersion to set
	 */
	public final void setApplicationNumeroVersion(String applicationNumeroVersion) {
		this.applicationNumeroVersion = applicationNumeroVersion;
	}

	/**
	 * @return The applicationEnvironnementExecution
	 */
	public final String getApplicationEnvironnementExecution() {
		return this.applicationEnvironnementExecution;
	}

	/**
	 * @param applicationEnvironnementExecution
	 *            The applicationEnvironnementExecution to set
	 */
	public final void setApplicationEnvironnementExecution(String applicationEnvironnementExecution) {
		this.applicationEnvironnementExecution = applicationEnvironnementExecution;
	}

	/**
	 * @return The applicationContact
	 */
	public final String getApplicationContact() {
		return this.applicationContact;
	}

	/**
	 * @param applicationContact
	 *            The applicationContact to set
	 */
	public final void setApplicationContact(String applicationContact) {
		this.applicationContact = applicationContact;
	}

	/**
	 * @return The applicationCheminAide
	 */
	public final String getApplicationCheminAide() {
		return this.applicationCheminAide;
	}

	/**
	 * @param applicationCheminAide
	 *            The applicationCheminAide to set
	 */
	public final void setApplicationCheminAide(String applicationCheminAide) {
		this.applicationCheminAide = applicationCheminAide;
	}

	/**
	 * @return The applicationFichierAide
	 */
	public final String getApplicationFichierAide() {
		return this.applicationFichierAide;
	}

	/**
	 * @param applicationFichierAide
	 *            The applicationFichierAide to set
	 */
	public final void setApplicationFichierAide(String applicationFichierAide) {
		this.applicationFichierAide = applicationFichierAide;
	}

	/**
	 * @return The rubriquesProperties
	 */
	public final java.util.Properties getRubriquesProperties() {
		return this.rubriquesProperties;
	}

	/**
	 * setRubriquesProperties.
	 *
	 * @param rubriquesProperties
	 *            Les rubriques
	 */
	public void setRubriquesProperties(java.util.Properties rubriquesProperties) {
		this.rubriquesProperties = rubriquesProperties;

		this.applicationNom = rubriquesProperties.getProperty("defaut.app.label");
		this.applicationNumeroVersion = rubriquesProperties.getProperty("defaut.app.vers");
		this.applicationEnvironnementExecution = rubriquesProperties.getProperty("defaut.app.execution");
		this.applicationContact = rubriquesProperties.getProperty("defaut.app.contact");
	}

	/**
	 * appelServiceDroitFonction.
	 */
	public int appelServiceDroitFonction() {
		
		HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
		Object retourHabObj = containerSession.get("retourHabilitation");
		if (retourHabObj != null) {
			int rSessionHab = Integer.parseInt(retourHabObj.toString());
			ContexteCedicam contexteTmp = (ContexteCedicam) containerSession.get("contexteCedicam");
			if (contexteTmp == null) {
				contexteTmp = new ContexteCedicam();
				contexteTmp.setIdagt("UNKNOWN");
				setContexteCedicam(contexteTmp);
				containerSession.put("contexteCedicam", contexteTmp);
			}
			String nomUser = contexteTmp.getIdagt();
			
			getTraceService().enregistrerTraceDebug("St000Bean", "appelServiceDroitFonction - retour FiltreHabilitation : " + rSessionHab,
					getEnteteComposant(), getContexteCedicam());
			
			if (contexteTmp != null) {
				nomUser = contexteTmp.getIdagt();
			}
			
			if ( rSessionHab != ConstantesNuerf.TRAITEMENT_COMPLETED) {
				switch (rSessionHab) {
					case 202: //todo constante à remplacer après deploy socle.jar
						// Pb ldap
						getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3200", nomUser);
											
						break;
							
					case 13://Pas de profil Global todo constate à remplacer après deploy socle.jar
					case 14://Pas de profil CCM
						// Pas de profil => génération événement
						getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3201", nomUser);
						break;
										
					case ConstantesNuerf.AUCUNE_FCT_HABILITEE:
						// Pas de fonction => génération événement
						getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3202",nomUser);
						break;
						
					case ConstantesNuerf.ERREUR_TECHNIQUE :
						default: 
						// planton technique
						getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
						return ConstantesNuerf.ERREUR_TECHNIQUE;
					
				}
			}
			
			CacheHabilitation oCacheHabilitation = (CacheHabilitation) ApplicationContextHolder.getContext().getBean("cacheHabilitation");
	
			// Récupération des habilitations
			this.setHabF0010(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.CONSULTATION_SUIVI_ECHANGE, getContexteCedicam()));
			
			this.setHabF0020(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.SAISIE_EPOC, getContexteCedicam()));
			
			this.setHabF0030(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.FORCAGE_RAPPROCHEMENT, getContexteCedicam()));
			
			this.setHabF0040(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.CONSULTATION_SUIVI_ECART, getContexteCedicam()));
			
			this.setHabF0050(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.CONSULTATION_REFERENTIEL_LISTE, getContexteCedicam()));
			
			this.setHabF0060(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.GESTION_REFERENTIEL, getContexteCedicam()));
			
			this.setHabF0070(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.CONSULTATION_REFERENTIEL_DETAIL, getContexteCedicam()));
			
			this.setHabF0080(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.ACCESSIBILITE_AIDE, getContexteCedicam()));
			
			this.setHabF0090(oCacheHabilitation.isHabilite(
					ConstantesFonctionHabilitation.ACCESSIBILITE_DECONNEXION, getContexteCedicam()));
		
			return ConstantesNuerf.TRAITEMENT_COMPLETED;
		}
		return ConstantesNuerf.ERREUR_RECUPERATION_PARAMETRE;

	}

	public Boolean getHabF0010() {
		return habF0010;
	}

	public void setHabF0010(Boolean habF0010) {
		this.habF0010 = habF0010;
	}

	public Boolean getHabF0020() {
		return habF0020;
	}

	public void setHabF0020(Boolean habF0020) {
		this.habF0020 = habF0020;
	}
	
	public Boolean getHabF0030() {
		return habF0030;
	}

	public void setHabF0030(Boolean habF0030) {
		this.habF0030 = habF0030;
	}
	
	public Boolean getHabF0040() {
		return habF0040;
	}

	public void setHabF0040(Boolean habF0040) {
		this.habF0040 = habF0040;
	}
	
	public Boolean getHabF0050() {
		return habF0050;
	}

	public void setHabF0050(Boolean habF0050) {
		this.habF0050 = habF0050;
	}
	
	public void setHabF0060(Boolean habF0060) {
		this.habF0060 = habF0060;
	}

	public Boolean getHabF0060() {
		return habF0060;
	}
	
	public void setHabF0070(Boolean habF0070) {
		this.habF0070 = habF0070;
	}
	
	public Boolean getHabF0070() {
		return habF0070;
	}
	
	public void setHabF0080(Boolean habF0080) {
		this.habF0080 = habF0080;
	}
	
	public Boolean getHabF0080() {
		return habF0080;
	}
	
	public void setHabF0090(Boolean habF0090) {
		this.habF0090 = habF0090;
	}
	
	public Boolean getHabF0090() {
		return habF0090;
	}
}
