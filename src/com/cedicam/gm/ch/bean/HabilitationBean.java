package com.cedicam.gm.ch.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.bt.util.RecupererMangedBean;
import com.cedicam.gm.ch.bean.menus.St000Bean;
import com.cedicam.gm.ch.core.clientwebservice.ReponseWebServiceFonctionTab;
import com.cedicam.gm.ch.core.webservice.HabilitationProfilFonctionWebService;
import com.cedicam.gm.ch.data.dto.FonctionDto;
import com.cedicam.gm.ui.core.GenericBean;


/**
 * @author Administrateur
 *
 */
public class HabilitationBean extends GenericBean {

	/** ID_COMP. */
	private static final String ID_COMP = "GMCHBNHAB";

	/** profils. */
	private String[] profils = new String[4];
	//private List<FonctionDto> fonctionList;
	/** user. */
	private String user;
	/** hab1. */
	private String hab1 = "";
	/** hab2. */
	private String hab2 = "";
	/** hab3. */
	private String hab3 = "";
	/** hab4. */
	private String hab4 = "";

	/**
	 * Consructeur.
	 */
	public HabilitationBean() {
		setEnteteComposant(HabilitationBean.ID_COMP, "Init Habilitation");

		getTraceService().enregistrerTraceDebug("MSG0170", HabilitationBean.ID_COMP,
				this.getEnteteComposant(), this.getContexteCedicam());

		this.setCodeEcran("st000");
	}

	/**
	 * @return The profils
	 */
	public final String[] getProfils() {
		return this.profils;
	}

	/**
	 * @param profils The profils to set
	 */
	public final void setProfils(String[] profils) {
		this.profils = profils;
	}

	/**
	 * @return The user
	 */
	public final String getUser() {
		return this.user;
	}

	/**
	 * @param user The user to set
	 */
	public final void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return The hab1
	 */
	public final String getHab1() {
		return this.hab1;
	}

	/**
	 * @param hab1 The hab1 to set
	 */
	public final void setHab1(String hab1) {
		this.hab1 = hab1;
	}

	/**
	 * @return The hab2
	 */
	public final String getHab2() {
		return this.hab2;
	}

	/**
	 * @param hab2 The hab2 to set
	 */
	public final void setHab2(String hab2) {
		this.hab2 = hab2;
	}

	/**
	 * @return The hab3
	 */
	public final String getHab3() {
		return this.hab3;
	}

	/**
	 * @param hab3 The hab3 to set
	 */
	public final void setHab3(String hab3) {
		this.hab3 = hab3;
	}

	/**
	 * @return The hab4
	 */
	public final String getHab4() {
		return this.hab4;
	}

	/**
	 * @param hab4 The hab4 to set
	 */
	public final void setHab4(String hab4) {
		this.hab4 = hab4;
	}

	/**
	 * getFonctionList.
	 * @return La liste
	 */
	@SuppressWarnings("unchecked")
	public List<FonctionDto> getFonctionList() {
		HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
		return (List<FonctionDto>) containerSession.get("fonctionList");
	}

	/**
	 * setFonctionList.
	 * @param fonctionList La liste
	 */
	@SuppressWarnings("unchecked")
	public void setFonctionList(List<FonctionDto> fonctionList) {
		HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
		containerSession.put("fonctionList", fonctionList);
	}

	/**
	 * valider.
	 * @return Un outcome
	 */
	public String valider() {
		if (hab1 == null) {
			profils[0] = "";
		} else {
			profils[0] = hab1;
		}
		if (hab2 == null) {
			profils[1] = "";
		} else {
			profils[1] = hab2;
		}
		if (hab3 == null) {
			profils[2] = "";
		} else {
			profils[2] = hab3;
		}
		if (hab4 == null) {
			profils[3] = "";
		} else {
			profils[3] = hab4;
		}
		try {
			// Nouvelle connexion: on retire le flag de déconnexion
			FacesContext context = FacesContext.getCurrentInstance();
			Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
			sessionMap.remove("isDeconnecte");

			HabilitationProfilFonctionWebService habProfilFctWebService = (HabilitationProfilFonctionWebService) ApplicationContextHolder
					.getContext().getBean("habilitationProfilFonctionWebService");
			
			ReponseWebServiceFonctionTab listerFonctions = habProfilFctWebService.listerFonctions(profils, getContexteCedicam());
			getTraceService().enregistrerTraceDebug("valider",
					"action valider nuerf  " + listerFonctions.getNuerf() + "action valider lierf " + listerFonctions.getLierf(),
					getEnteteComposant(), getContexteCedicam());
			switch (listerFonctions.getNuerf()) {
			case ConstantesNuerf.TRAITEMENT_COMPLETED:
				FonctionDto[] donnees = listerFonctions.getDonnees();
				List<FonctionDto> aFonctionList = Arrays.asList(donnees);
				setFonctionList(aFonctionList);
				HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
				containerSession.put("retourHabilitation", listerFonctions.getNuerf());
				St000Bean menuBean = (St000Bean) RecupererMangedBean.getInstanceSessionOf("st000");
				if (menuBean != null) {
					menuBean.appelServiceDroitFonction();
				}
				return "valider";
			case ConstantesNuerf.PARAMETRE_ABSENT:
				getEvenementService().emettreEvenement("habilitationProfilFonctionWebService", ConstantesEvtTech.EVT_PARAM_INVALIDE_INCOHERENT, ConstantesTrace.CATEGORIE_ERREUR,
						ConstantesTrace.GRAVITE_WARN, "MSG0050", getEnteteComposant(), getContexteCedicam());
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0050");
				break;
			case ConstantesNuerf.AUCUNE_FCT_HABILITEE:
				List<FonctionDto> aFonctionListVide = new ArrayList<FonctionDto>();
				setFonctionList(aFonctionListVide);
				// fonctionList = new ArrayList<FonctionDto>();
				
				//String msg = getBtmu10().getMessage("MSG3202", new String[] { ""+getContexteCedicam().getIdagt() });
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3202" );
	
				
				return "valider";

			default:
				break;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			getEvenementService().emettreEvenement("habilitationProfilFonctionWebService",
					ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR,
					ConstantesTrace.GRAVITE_FATAL, "MSG0097", e, getEnteteComposant(), getContexteCedicam());
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0143", "webService.habilitationProfilFonctionWebService");
		}
		return null;

	}

	public String clearSession() {
		
		try {
			System.out.println("entrée dans clearSession");
			
			FacesContext context = FacesContext.getCurrentInstance();
			
			
			Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
			
			sessionMap.remove("cacheHabilitation");
			
			HashMap containerSession = (HashMap) sessionMap.get("containerSession");
			
			containerSession.put("fonctionList", new ArrayList<FonctionDto>());
			
			sessionMap.remove("st000");
			
			// ajout du flag de deconnexion
			sessionMap.put("isDeconnecte", true);
			
			((HttpSession) context.getExternalContext().getSession(false)).invalidate();
			
			context.getExternalContext().redirect("/CCM/blank.html");
		
		} catch (IOException e) {
			//TODO à enlever suite à test CACP 513
			e.printStackTrace();
		}
		return "deco";
	}
}
