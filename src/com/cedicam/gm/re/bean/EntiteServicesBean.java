package com.cedicam.gm.re.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.converter.CodeBanqueConverter;
import com.cedicam.gm.bt.core.clientwebservice.ReponseWebServiceChefDeFileTab;
import com.cedicam.gm.bt.core.clientwebservice.ReponseWebServiceEntite;
import com.cedicam.gm.bt.core.clientwebservice.ReponseWebServiceEntiteTab;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.cm.data.dto.CritereEntiteDto;
import com.cedicam.gm.re.core.webservice.EntiteWebService;
import com.cedicam.gm.re.data.dto.EntiteDto;
import com.cedicam.gm.ui.core.GenericBean;

/**
 * Bean pour gérer les entités.
 * @author Salim TAZI
 */
public class EntiteServicesBean extends GenericBean {
	/** Entite Webservice. */
	private EntiteWebService entiteWebService = null;
	/** Converter. */
	private Converter codeBanqueConverter;

	public EntiteServicesBean() {
		codeBanqueConverter = new CodeBanqueConverter();
	}
	
	/**
	 * Initialise le webservice.
	 */
	private void initWebService() {
		if (null == entiteWebService) {
			entiteWebService = (EntiteWebService) ApplicationContextHolder.getContext().getBean("entiteWebService");
		}
	}

	/**
	 * Retourne la liste des entités dont la validité correspond au critère passé en paramètre.
	 * @param validite Le critère de validité recherché.
	 * @return La liste d'entités correspondant au résultat de la recherche, null si une erreur est survenue.
	 */
	public List<EntiteDto> rechercherEntites(String validite) {
		List<EntiteDto> result = new ArrayList<EntiteDto>();

		initWebService();
		
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMREBNEC020", "Sélection Entité");
		
		Date dateApplication = getDateApplication();
		try {
			ReponseWebServiceEntiteTab res = entiteWebService.listerEntite(validite, dateApplication, getContexteCedicam());

			String type = null;
			String msg = null;

			switch (res.getNuerf()) {
				case ConstantesNuerf.TRAITEMENT_COMPLETED:
					EntiteDto[] entiteTab = res.getDonnees();
					result = Arrays.asList(entiteTab);
					break;

				case ConstantesNuerf.AUCUN_ENREGISTREMENT:
					type = Btmu10.MSG_INFO;
					msg = "MSG3215";
					result = new ArrayList<EntiteDto>();
					getTraceService().enregistrerTrace("rechercherEntites", ConstantesEvtTech.EVT_AUCUN_ENREGISTREMENT,
							ConstantesTrace.CATEGORIE_SUIVI, ConstantesTrace.GRAVITE_INFO,
							"MSG2311", null, getEnteteComposant(), getContexteCedicam());
					break;

				case ConstantesNuerf.STATUT_EN_PARAMETRE_INCORRECT:
				case ConstantesNuerf.PARAMETRE_ABSENT:
				default:
					type = Btmu10.MSG_ERREUR;
					msg = "MSG3500";
					getTraceService().enregistrerTrace("rechercherEntites", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
							ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG1094",
							new String[] {"" + res.getNuerf(), "EntiteWebService.listerEntites", res.getLierf(),
							"statut validité : " + validite + ", date : " + dateApplication}, getEnteteComposant(), getContexteCedicam());
					break;
			}

			if(msg != null) {
				getBtmu10().addMessage(type, msg);
			}

		} catch (Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
				
			getTraceService().enregistrerTrace("rechercherEntites", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN,
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	/**
	 * Recherche une entité à partir de son identifiant.
	 * @param entiteEchange Le code banque de l'entité d'échange.
	 * @param dateDebut La date de début d'effet de l'entité.
	 * @return L'entité trouvée en base.
	 */
	public EntiteDto rechercherEntite(Integer entiteEchange, Date dateDebut) {
		EntiteDto result = null;

		initWebService();

		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMREBNEC025", "Consultation Entité");
				
		try {
			ReponseWebServiceEntite res = entiteWebService.rechercherEntite(entiteEchange, dateDebut, getContexteCedicam());

			String msg = null;

			switch (res.getNuerf()) {
				case ConstantesNuerf.TRAITEMENT_COMPLETED:
					result = res.getDonnees();
					break;

				case ConstantesNuerf.NOT_FOUND:
					msg = "MSG3206";
					getTraceService().enregistrerTrace("rechercherRapprochement", ConstantesEvtTech.EVT_AUCUN_ENREGISTREMENT, 
							ConstantesTrace.CATEGORIE_SUIVI, ConstantesTrace.GRAVITE_INFO, 
							"MSG0188", new String[]{"Entite", "" + entiteEchange}, 
							getEnteteComposant(), getContexteCedicam());
					break;
					
				case ConstantesNuerf.PARAMETRE_ABSENT:
				default:
					msg = "MSG3500";
					getTraceService().enregistrerTrace("rechercherEntite", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
							ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG1094",
							new String[] {"" + res.getNuerf(), "EntiteWebService.rechercherEntite", res.getLierf(),
							"entite d'échange : " + entiteEchange + ", date début : " + dateDebut},
							getEnteteComposant(), getContexteCedicam());
					break;
			}

			if(msg != null) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, msg);
			}

		} catch (Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");

			getTraceService().enregistrerTrace("rechercherEntite", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN,
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	/**
	 * Modifie l'entité passée en paramètre.
	 * @param aCreer L'entité à créer
	 * @return L'entité créée, null si la création a échoué.
	 */
	public EntiteDto creerEntite(EntiteDto aCreer) {
		EntiteDto result = null;
		initWebService();
		
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMREBNEC035", "Création Entité");
		
		try {
			ReponseWebServiceEntite res = entiteWebService.creerEntite(aCreer, getContexteCedicam());
			if(res.getNuerf()==ConstantesNuerf.TRAITEMENT_COMPLETED){
				result = res.getDonnees();
				getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3218", "" + codeBanqueConverter.getAsString(null, null, result.getCodeBanque()));
			} else if (res.getNuerf() == ConstantesNuerf.PARAMETRE_NON_VALIDE) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3224");
			}else if(res.getNuerf()==ConstantesNuerf.CREATION_IMPOSSIBLE_DOUBLON){
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3221");
			}else {
				getTraceService().enregistrerTrace("creerEntite", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG1094",
						new String[] {"" + res.getNuerf(), "EntiteWebService.creerEntite", res.getLierf(),
						"entite d'échange : " + aCreer.getCodeBanque() + ", date début : " + aCreer.getDtDbEffet()},
						getEnteteComposant(), getContexteCedicam());
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3501", "");
			}

		} catch (Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3501");
			getTraceService().enregistrerTrace("creerEntite", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN,
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	/**
	 * Modifie l'entité passée en paramètre.
	 * @param aModifier L'entité à modifier
	 * @return L'entité modifiée, null si la modification a échoué.
	 */
	public EntiteDto modifierEntite(EntiteDto aModifier, Date dtDebutOrigine) {
		EntiteDto result = null;
		initWebService();
		
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMREBNEC030", "Modification Entité");
				
		try {
			ReponseWebServiceEntite res = entiteWebService.modifierEntite(aModifier, dtDebutOrigine, getContexteCedicam());
			if (res.getNuerf()==ConstantesNuerf.TRAITEMENT_COMPLETED){
				result = res.getDonnees();
				getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3220", "" + codeBanqueConverter.getAsString(null, null, aModifier.getCodeBanque()));
			} else if (res.getNuerf() == ConstantesNuerf.PARAMETRE_NON_VALIDE) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3224");
			} else if (res.getNuerf() == ConstantesNuerf.NEWEST_IN_BASE) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3207");
			} else if (res.getNuerf()==ConstantesNuerf.MISE_A_JOUR_IMPOSSIBLE) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3222", "" + codeBanqueConverter.getAsString(null, null, aModifier.getCodeBanque()));
			} else {
				getTraceService().enregistrerTrace("modifierEntite", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG1094",
						new String[] {"" + res.getNuerf(), "EntiteWebService.modifierEntite", res.getLierf(),
						"entite d'échange : " + aModifier.getCodeBanque() + ", date début : " + aModifier.getDtDbEffet()},
						getEnteteComposant(), getContexteCedicam());
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3501", "");
			}
		} catch (Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3501");
			getTraceService().enregistrerTrace("modifierEntite", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN,
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	/**
	 * Modifie l'entité passée en paramètre.
	 * @param aSupprimer L'entité à modifier
	 * @return L'entité supprimée, null si la suppression a échoué.
	 */
	public EntiteDto supprimerEntite(EntiteDto aSupprimer) {
		EntiteDto result = null;
		initWebService();
		
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMCMBNEC020", "Sélection Entité");
				
		try {
			ReponseWebServiceEntite res = entiteWebService.supprimerEntite(aSupprimer, getContexteCedicam());
			if (ConstantesNuerf.TRAITEMENT_COMPLETED == res.getNuerf()) {
				result = res.getDonnees();
				getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3212", "" + codeBanqueConverter.getAsString(null, null, result.getCodeBanque()));
			} else if (ConstantesNuerf.SUPPRESSION_LOGIQUE == res.getNuerf()) {
				result = res.getDonnees();
				getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3213", "" + codeBanqueConverter.getAsString(null, null, result.getCodeBanque()));
			} else if (ConstantesNuerf.NOT_FOUND == res.getNuerf()) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3090");
			} else {
				getTraceService().enregistrerTrace("rechercherEntite", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG1094",
						new String[] {"" + res.getNuerf(), "EntiteWebService.supprimerEntite", res.getLierf(),
						"entite d'échange : " + aSupprimer.getCodeBanque() + ", date début : " + aSupprimer.getDtDbEffet()},
						getEnteteComposant(), getContexteCedicam());
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3501", "");
			}
		} catch (Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3501");
			getTraceService().enregistrerTrace("supprimerEntite", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN,
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	/**
	 * @return La liste des chefs de file présent dans le référentiel entités.
	 */
	public List<CritereEntiteDto> listerChefsDefile() {
		List<CritereEntiteDto> listeChefsDeFile = new ArrayList<CritereEntiteDto>();
		try {
			initWebService();
			ReponseWebServiceChefDeFileTab res = entiteWebService.listerChefDeFile(getContexteCedicam());
			if (ConstantesNuerf.TRAITEMENT_COMPLETED == res.getNuerf() && res.getDonnees() != null) {
				listeChefsDeFile = Arrays.asList(res.getDonnees());
			}
		} catch (Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("listerChefsDefile", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN,
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return listeChefsDeFile;
	}
	/**
	 * @param liste Une liste de CritèreEntiteDto à transformer en SelectItems
	 * @return Une liste de SelectItem représentant la liste passée en paramètre
	 */
	public static List<SelectItem> toSelectItemList(Collection<CritereEntiteDto> liste) {
		ArrayList<SelectItem> res = new ArrayList<SelectItem>();
		for (CritereEntiteDto dto : liste) {
			res.add(new SelectItem(dto.getId(), dto.getEntiteEchange() + " - " + dto.getLibelle()));
		}
		return res;
	}

	/**
	 * @param liste Un tableau de CritèreEntiteDto à transformer en SelectItems
	 * @return Une liste de SelectItem représentant la liste passée en paramètre
	 */
	public static List<SelectItem> toSelectItemList(CritereEntiteDto[] liste) {
		ArrayList<SelectItem> res = new ArrayList<SelectItem>();
		if (null == liste || 0 == liste.length) {
			return res;
		}
		for (CritereEntiteDto dto : liste) {
			res.add(new SelectItem(dto.getId(), dto.getEntiteEchange() + " - " + dto.getLibelle()));
		}
		return res;
	}
}
