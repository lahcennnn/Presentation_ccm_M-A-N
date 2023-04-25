package com.cedicam.gm.cm.bean;

import java.util.Map;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.cm.core.clientwebservice.ReponseWebServiceEcartsRapprochementDto;
import com.cedicam.gm.cm.core.clientwebservice.ReponseWebServiceLigneRapprochement;
import com.cedicam.gm.cm.core.clientwebservice.ReponseWebServiceRapprochementDto;
import com.cedicam.gm.cm.core.clientwebservice.ReponseWebServiceRegroupementDto;
import com.cedicam.gm.cm.core.clientwebservice.ReponseWebServiceSuiviRapprochement;
import com.cedicam.gm.cm.core.webservice.RapprochementWebService;
import com.cedicam.gm.cm.core.webservice.RegroupementWebService;
import com.cedicam.gm.cm.data.dto.CriteresPourSuiviDto;
import com.cedicam.gm.cm.data.dto.EcartsRapprochementDto;
import com.cedicam.gm.cm.data.dto.LigneRapprochementDto;
import com.cedicam.gm.cm.data.dto.LigneRegroupementDto;
import com.cedicam.gm.cm.data.dto.RapprochementDto;
import com.cedicam.gm.cm.data.dto.RegroupementDto;
import com.cedicam.gm.cm.data.dto.SuiviRapprochementDto;
import com.cedicam.gm.ui.core.GenericBean;

public class BusinessServicesBean extends GenericBean {
	private  RapprochementWebService rapprochementWS = null;
	private RegroupementWebService regroupementWS = null;
	private Integer nbMaxLines = null;

	public RegroupementWebService getRegroupementWS() {		return regroupementWS;	}
	public void setRegroupementWS(RegroupementWebService regroupementWS) {		this.regroupementWS = regroupementWS;	}
	public RapprochementWebService getRapprochementWS() {		return rapprochementWS;	}
	public void setRapprochementWS(RapprochementWebService rapprochementWS) {		this.rapprochementWS = rapprochementWS;	}

	private void initWS(){
		if(null == rapprochementWS) {
			rapprochementWS = (RapprochementWebService) ApplicationContextHolder.getContext().getBean("rapprochementWebService");
		}
		if(null == regroupementWS) {
			regroupementWS = (RegroupementWebService) ApplicationContextHolder.getContext().getBean("regroupementWebService");
		}
		if (null == nbMaxLines) {
			Map<String,String> props = ApplicationContextHolder.getProjectProperties();
			String maxLines = props.get("gm.cm.list.nbMaxLines");
			if (null != maxLines) {
				nbMaxLines = new Integer(maxLines);
			}
		}
	}

	public SuiviRapprochementDto rechercherRapprochements(CriteresPourSuiviDto criteres){
		initWS();
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMCMBNEC040", "Recherche Rapprochements");
		
		ReponseWebServiceSuiviRapprochement res=null ;
		SuiviRapprochementDto result = null;
		try {
			res = rapprochementWS.listerPourSuivi(criteres, nbMaxLines, getContexteCedicam());
		
			String msg = null;
			
			switch (res.getNuerf()) {
			case ConstantesNuerf.TRAITEMENT_COMPLETED:
				result = res.getDonnees();
				break;
				
			case ConstantesNuerf.AUCUN_ENREGISTREMENT:
				result = new SuiviRapprochementDto();
				result.setNombreRapprochements(0);
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_AUCUN_ENREGISTREMENT, 
						ConstantesTrace.CATEGORIE_SUIVI, ConstantesTrace.GRAVITE_INFO, 
						"MSG3083", null, getEnteteComposant(), getContexteCedicam());
				break;
				
			case ConstantesNuerf.NEWEST_IN_BASE:
				msg = "MSG3089";
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
						msg, null, getEnteteComposant(), getContexteCedicam());
				break;
				
			default:
				msg = "MSG3500";
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
						"MSG1094", new String[]{"" + res.getNuerf(),"RapprochementWebService.listerPourSuivi", res.getLierf(), criteres.toString()}, getEnteteComposant(), getContexteCedicam());
				break;
			}
			
			if(msg != null) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, msg);
				
			}

		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			
			getTraceService().enregistrerTrace("listerRapprochements", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	public LigneRapprochementDto attribuerEPOC(LigneRapprochementDto ligneRapprochementDto, String idIncidentEpoc) {
		initWS();
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMCMBNEC050", "Liste Rapprochement");
		
		ReponseWebServiceLigneRapprochement res=null ;
		LigneRapprochementDto result = null;
		try{
			res = rapprochementWS.attribuerEPOC(ligneRapprochementDto, idIncidentEpoc, getContexteCedicam());
		
			String msg = null;
			switch (res.getNuerf()) {
			case ConstantesNuerf.TRAITEMENT_COMPLETED:
				result = res.getDonnees();
				break;
				
			case ConstantesNuerf.NEWEST_IN_BASE:
				msg = "MSG3089";
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
						msg, null, getEnteteComposant(), getContexteCedicam());
				break;
				
			default:
				msg = "MSG3105";
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
						"MSG1094", new String[]{"" + res.getNuerf(),"RapprochementWebService.attribuerEPOC", res.getLierf(), "LigneRapprochement : " + ligneRapprochementDto + ", numéro EPOC : " + idIncidentEpoc}, 
						getEnteteComposant(), getContexteCedicam());
				break;
			}
			
			if(msg!=null) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, msg);
				
			}
			
		}catch(Exception e){
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3105");
			getTraceService().enregistrerTrace("attribuerIncidentEpoc", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	public LigneRapprochementDto forcerClotureRapprochement(LigneRapprochementDto ligneRapprochementDto) {
		initWS();
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMCMBNEC050", "Liste Rapprochement");
		
		ReponseWebServiceLigneRapprochement res=null ;
		LigneRapprochementDto result = null;
		try{
			ligneRapprochementDto.setIdUtilisateurForcage(getContexteCedicam().getIdagt());
			res = rapprochementWS.forcerClotureRapprochement(ligneRapprochementDto, getContexteCedicam());
			
			
			String msg = null;
			switch (res.getNuerf()) {
			case ConstantesNuerf.TRAITEMENT_COMPLETED:
				result = res.getDonnees();
				break;
				
			case ConstantesNuerf.NEWEST_IN_BASE:
			case ConstantesNuerf.STATUT_INCORRECT:
				msg = "MSG3089";
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
						msg, null, getEnteteComposant(), getContexteCedicam());
				break;
				
			default:
				msg = "MSG3106";
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
						"MSG1094", new String[]{"" + res.getNuerf(),"RapprochementWebService.forcerClotureRapprochement", res.getLierf(), "LigneRapprochement : " + ligneRapprochementDto}, 
						getEnteteComposant(), getContexteCedicam());
				break;
			}
			
			if(msg!=null) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, msg);
				
			}
			
		}catch(Exception e){
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3106");
			getTraceService().enregistrerTrace("forcerCloture", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	public RapprochementDto rechercherRapprochement(LigneRapprochementDto ligneRapprochementDto) {
		initWS();
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMCMBNEC060", "Détail Rapprochement");
		
		ReponseWebServiceRapprochementDto res=null ;
		RapprochementDto result = null;
		try {
			res = rapprochementWS.rechercherRapprochement(ligneRapprochementDto, getContexteCedicam());
			String msg = null;
			String libRemplacement = null;
			switch (res.getNuerf()) {
				case ConstantesNuerf.TRAITEMENT_COMPLETED:
					result = res.getDonnees();
					break;
					
				case ConstantesNuerf.NOT_FOUND:
					msg = "MSG3079";
					libRemplacement = "" + ligneRapprochementDto.getIdRapprochement();
					getTraceService().enregistrerTrace("rechercherRapprochement", ConstantesEvtTech.EVT_AUCUN_ENREGISTREMENT, 
							ConstantesTrace.CATEGORIE_SUIVI, ConstantesTrace.GRAVITE_INFO, 
							"MSG0188", new String[]{"Rapprochement", "" + ligneRapprochementDto.getIdRapprochement()}, 
							getEnteteComposant(), getContexteCedicam());
					break;
					
				default:
					msg = "MSG3500";
					getTraceService().enregistrerTrace("rechercherRapprochement", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
							ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
							"MSG1094", new String[]{"" + res.getNuerf(),"RapprochementWebService.rechercherRapprochement", res.getLierf(), "LigneRapprochement : " + ligneRapprochementDto}, 
							getEnteteComposant(), getContexteCedicam());
					break;
			}
			
			if(msg != null) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, msg, libRemplacement);
			}
				
		} catch (Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
				
			getTraceService().enregistrerTrace("rechercherRapprochement", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}

	public EcartsRapprochementDto syntheseEcarts(CriteresPourSuiviDto criteresPourSuiviDto){
		initWS();
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMCMBNEC100", "Recherche Ecarts");
		
		EcartsRapprochementDto retour = null;
		ReponseWebServiceEcartsRapprochementDto res = rapprochementWS.syntheseEcarts(criteresPourSuiviDto, getContexteCedicam());
		
		switch (res.getNuerf()) {
			case ConstantesNuerf.TRAITEMENT_COMPLETED:
				retour = res.getDonnees();
				break;
			
			case ConstantesNuerf.AUCUN_ENREGISTREMENT:
				getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3083");
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_AUCUN_ENREGISTREMENT, 
						ConstantesTrace.CATEGORIE_SUIVI, ConstantesTrace.GRAVITE_INFO, 
						"MSG3083", null, getEnteteComposant(), getContexteCedicam());
				break;
			
			default:
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0143", new String[]{"syntheseEcarts", criteresPourSuiviDto.toString()});
				getTraceService().enregistrerTrace("rechercherRapprochements", ConstantesEvtTech.EVT_AUCUN_ENREGISTREMENT, 
						ConstantesTrace.CATEGORIE_SUIVI, ConstantesTrace.GRAVITE_INFO, 
						"MSG0143", new String[]{"syntheseEcarts", criteresPourSuiviDto.toString()}, 
						getEnteteComposant(), getContexteCedicam());
				break;
		}
		
		return retour;
	}

	public RegroupementDto rechercherRegroupement(LigneRegroupementDto lgn) {
		initWS();
	
		//Vu que les beans d'appels WS sont utilisés par plusieurs écrans on définit l'entête composant
		//sur appel de méthode et non dans le constructeur
		setEnteteComposant("GMCMBNEC070", "Détail Regroupement");
				
		ReponseWebServiceRegroupementDto res=null ;
		RegroupementDto result = null;
		
		try{
			res = regroupementWS.rechercherRegroupement(lgn, getContexteCedicam());
			String msg = null;
			String libRemplacement = null;
			switch (res.getNuerf()) {
				case ConstantesNuerf.TRAITEMENT_COMPLETED:
					result = res.getDonnees();
					break;
					
				case ConstantesNuerf.NOT_FOUND:
					msg = "MSG3077";
					libRemplacement = "" + lgn.getIdRegroupement();
					getTraceService().enregistrerTrace("rechercherRegroupement", ConstantesEvtTech.EVT_AUCUN_ENREGISTREMENT, 
							ConstantesTrace.CATEGORIE_SUIVI, ConstantesTrace.GRAVITE_INFO, 
							"MSG0188", new String[]{"Regroupement", "" + lgn.getIdRegroupement()}, 
							getEnteteComposant(), getContexteCedicam());
					break;
					
				default:
					msg = "MSG3500";
					getTraceService().enregistrerTrace("rechercherRegroupement", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
							ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
							"MSG1094", new String[]{"" + res.getNuerf(),"RegroupementWebService.rechercherRegroupement", res.getLierf(), "LigneRegroupement : " + lgn}, 
							getEnteteComposant(), getContexteCedicam());
					break;
			}
			
			if(msg != null) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, msg, libRemplacement);
				
			}
					
		}catch(Exception e){
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			
			getTraceService().enregistrerTrace("rechercherRegroupement", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
		return result;
	}
	
	public Integer getNbMaxLines() {
		return nbMaxLines;
	}
	public void setNbMaxLines(Integer nbMaxLines) {
		this.nbMaxLines = nbMaxLines;
	}
}
