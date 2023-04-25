package com.cedicam.gm.bt.outil;

import java.util.HashMap;
import java.util.List;

import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.core.service.EvenementService;
import com.cedicam.gm.bt.core.service.TraceService;
import com.cedicam.gm.bt.exception.ErreurTechnique;
import com.cedicam.gm.bt.sgc.ContexteCedicam;
import com.cedicam.gm.bt.sgt.EnteteComposant;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.ch.data.dto.FonctionDto;

public class CacheHabilitation  {

	private TraceService traceService = null;
	private EvenementService evenementService = null;

	public CacheHabilitation() {
		super();

		traceService = (TraceService) ApplicationContextHolder.getTraceService();
		evenementService = (EvenementService) ApplicationContextHolder.getEvenementService();

	}

	protected EnteteComposant getEnteteComposant(String idComp, String libComp) {
		EnteteComposant enteteComposant = new EnteteComposant();
		enteteComposant.setIdComp(idComp);
		enteteComposant.setLibAppli("Centre Comptable Monétique");
		enteteComposant.setLibComp(libComp);
		enteteComposant.setNivArchi((idComp != null && idComp.length() > 1) ? idComp.substring(0, 2) : "");
		enteteComposant.setNomClasse(this.getClass().getName());
		enteteComposant.setTypComp(ConstantesTrace.TYPE_COMPOSANT_WS);
		enteteComposant.setVersAppli(ConstantesTrace.PROJECT_VERSION);
		return enteteComposant;

	}

	/**
	 *
	 * <b>ALGORITHME</b>
	 *
	 * <ul>
	 *
	 * <li>Vérification que l'objet hébergeant les données d'habilitation est
	 * présent en session (réponse du service CHSM01)</li>
	 *
	 * <li>Si absent, génération de l'événement 35 avec le message 164, fin de
	 * service avec réponse et "false", code retour 0000</li>
	 *
	 * <li>Si objet présent, vérification de la présence du code fonction parmi
	 * les fonctionnalités autorisées pour la personne connectée</li>
	 *
	 * <li>Si fonction présente, on récupère le libellé de la source en fonction
	 * du code récupéré, on renvoie <br/>
	 *
	 * - true <br/>
	 *
	 * - code source associée à la fonction <br/>
	 * - libellé source associé à la fonction <br/>
	 * - Code retour = 0000</li>
	 *
	 * <li>Si absent, <br/>
	 * - false <br/>
	 *
	 * - Code retour = 0000
	 *
	 */
	public boolean isHabilite(String codeFonction, ContexteCedicam contexteCedicam) throws ErreurTechnique {

		final String idComp = "BTW042M01";
		boolean bResult = false;

		EnteteComposant enteteComposant = getEnteteComposant(idComp, "verifer habilitation sur fonction");

		traceService.enregistrerTraceDebug("isHabilite", "Debut Traitement ishabilite() pour codeFonction:" + codeFonction,
				enteteComposant, contexteCedicam);

		//ReponseWebServiceFonctionTab reponseWebServiceHabilitation = new ReponseWebServiceFonctionTab();

		if (codeFonction == null || codeFonction.trim().length() == 0) {
			evenementService.emettreEvenement("isHabilite", ConstantesEvtTech.EVT_PARAM_NULL_ABSENT,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL,
					"MSG0050", new String[] { "fonctionID" }, enteteComposant, contexteCedicam);
			throw new ErreurTechnique("isHabilite() : code Profil vide");

		}

		List<FonctionDto> aFonctionList = getFonctionList();
		if (aFonctionList != null) {
			for (FonctionDto fonction : aFonctionList) {
				if (fonction != null) {
					if (codeFonction.equals(fonction.getIdentifiantFonction())) {

						bResult = true;
						break;
					}
				}
			}
		}

		traceService.enregistrerTraceDebug("isHabilite", "Traitement ishabilite() pour codeFonction " + codeFonction 
				 + " Résultat => " +bResult,
				enteteComposant, contexteCedicam);
		
/*		reponseWebServiceHabilitation.setData(habilitation);
		reponseWebServiceHabilitation.setNuerf(ConstantesNuerf.TRAITEMENT_COMPLETED);
		reponseWebServiceHabilitation.setLierf("OK");
		traceService.enregistrerTraceDebug("isHabilite", "Fin de traitement isHabilite : " + reponseWebServiceHabilitation,
				enteteComposant, contexteCedicam);
		return reponseWebServiceHabilitation;
*/
		return bResult;
		}

	public List<FonctionDto> getFonctionList() {
		HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
		return (List<FonctionDto>) containerSession.get("fonctionList");
	}

	public TraceService getTraceService() {
		return traceService;
	}

	public void setTraceService(TraceService traceService) {
		this.traceService = traceService;
	}

	public EvenementService getEvenementService() {
		return evenementService;
	}

	public void setEvenementService(EvenementService evenementService) {
		this.evenementService = evenementService;
	}

}
