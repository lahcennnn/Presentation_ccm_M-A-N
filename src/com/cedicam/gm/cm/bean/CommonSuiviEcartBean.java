package com.cedicam.gm.cm.bean;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.cm.data.dto.LigneRapprochementDto;
import com.cedicam.gm.cm.data.dto.LigneRegroupementDto;
import com.cedicam.gm.cm.data.dto.SuiviRapprochementDto;
import com.cedicam.gm.ui.core.DatatableManager;
import com.cedicam.gm.ui.core.DefaultDataProvider;
import com.cedicam.gm.ui.core.FrameManager;
import com.cedicam.gm.ui.core.GenericBean;
import com.cedicam.gm.ui.core.SearchManager;
import com.cedicam.gm.ui.core.SuiviSearchManager;

public class CommonSuiviEcartBean extends GenericBean {

	DatatableManager<LigneRegroupementDto> regroupementEmetteurDtManager;
	DatatableManager<LigneRegroupementDto> regroupementDestinataireDtManager;
	DatatableManager<LigneRapprochementDto> rapprochementDtManager;

	SearchManager searchManager;
	BusinessServicesBean businessServicesBean;

	private static String OUTCOME_SUIVI;
	private static String OUTCOME_REGROUPEMENTS;
	private static String OUTCOME_DETAIL;
	private static final String OUTCOME_REFRESH = "refresh";


	public String trier(ActionEvent event){
		try {
			String propertyName =  (String)event.getComponent().getAttributes().get("sortPropertyName");
			String dt =  (String)event.getComponent().getAttributes().get("dt");
			if("rap".equals(dt))rapprochementDtManager.getDataProvider().doSort(propertyName);
			else if("regem".equals(dt))rapprochementDtManager.getDataProvider().doSort(propertyName);
			else if("regdest".equals(dt))rapprochementDtManager.getDataProvider().doSort(propertyName);
			return OUTCOME_REFRESH;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("CommonSuiviEcartBean.trier()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	public CommonSuiviEcartBean(){
		super();

		regroupementEmetteurDtManager = new DatatableManager<LigneRegroupementDto>(new DefaultDataProvider<LigneRegroupementDto>(), new LigneRegroupementDto());
		regroupementDestinataireDtManager = new DatatableManager<LigneRegroupementDto>(new DefaultDataProvider<LigneRegroupementDto>(), new LigneRegroupementDto());
		rapprochementDtManager = new DatatableManager<LigneRapprochementDto>(new DefaultDataProvider<LigneRapprochementDto>(), new LigneRapprochementDto());

		searchManager = new SuiviSearchManager();
		searchManager.reset(this.getDateApplication());
		businessServicesBean = (BusinessServicesBean)  GenericBean.findBean("businessServicesBean");
		rechercheRapprochementsFrameManager = new FrameManager();
		this.resetPanels();
	}

	public String refresh() {
		return OUTCOME_REFRESH;
		}

	/*********************************************************************************************************************************
	 * **********************************************  GESTION DE LA VUE  ************************************************************
	 *********************************************************************************************************************************/
	FrameManager rechercheRapprochementsFrameManager;
	public static final int RECHERCHE_RAPPROCHEMENT_FRAME_RECH = 5;
	public static final int RECHERCHE_RAPPROCHEMENT_FRAME_LIST = 10;

	public String expand(int frameId){
		rechercheRapprochementsFrameManager.expand(frameId);
		return refresh();
	}

	public String collapse(int frameId){
		rechercheRapprochementsFrameManager.collapse(frameId);
		return refresh();
	}


	private String bigger(int frameId){
		if(!rechercheRapprochementsFrameManager.getPanel(frameId).isExpanded()) rechercheRapprochementsFrameManager.expand(frameId);
		else rechercheRapprochementsFrameManager.setBigger(frameId);
		return refresh();
	}

	private String smaller(int frameId){
		if(!rechercheRapprochementsFrameManager.getPanel(frameId).isExpanded()) rechercheRapprochementsFrameManager.expand(frameId);
		else rechercheRapprochementsFrameManager.setSmaller(frameId);
		return refresh();
	}

	public String expandRecherche(){ 	return expand(RECHERCHE_RAPPROCHEMENT_FRAME_RECH);	}
	public String collapseRecherche(){	return collapse(RECHERCHE_RAPPROCHEMENT_FRAME_RECH); 	}
	public String biggerRecherche(){return bigger(RECHERCHE_RAPPROCHEMENT_FRAME_RECH);}
	public String smallerRecherche(){return smaller(RECHERCHE_RAPPROCHEMENT_FRAME_RECH);}
	public int getRechercheHeight(){		return rechercheRapprochementsFrameManager.getRenderedHeight(RECHERCHE_RAPPROCHEMENT_FRAME_RECH);	}
	public boolean isRechercheExpanded(){		return rechercheRapprochementsFrameManager.isExpanded(RECHERCHE_RAPPROCHEMENT_FRAME_RECH);	}

	public String expandRapprochements(){ return expand(RECHERCHE_RAPPROCHEMENT_FRAME_LIST); 	}
	public String collapseRapprochements(){	return collapse(RECHERCHE_RAPPROCHEMENT_FRAME_LIST); 	}
	public String biggerRapprochements(){return bigger(RECHERCHE_RAPPROCHEMENT_FRAME_LIST);}
	public String smallerRapprochements(){return smaller(RECHERCHE_RAPPROCHEMENT_FRAME_LIST);}
	public int getRapprochementsHeight(){		return rechercheRapprochementsFrameManager.getRenderedHeight(RECHERCHE_RAPPROCHEMENT_FRAME_LIST);	}
	public boolean isRapprochementsExpanded(){		return rechercheRapprochementsFrameManager.isExpanded(RECHERCHE_RAPPROCHEMENT_FRAME_LIST);	}



	public void resetPanels(){
		rechercheRapprochementsFrameManager.panels.clear();
		rechercheRapprochementsFrameManager.addPanel(RECHERCHE_RAPPROCHEMENT_FRAME_RECH);
		rechercheRapprochementsFrameManager.addPanel(RECHERCHE_RAPPROCHEMENT_FRAME_LIST);
	}

	public boolean getHasRapprochements(){return rechercheRapprochementsFrameManager.getPanel(RECHERCHE_RAPPROCHEMENT_FRAME_LIST)!=null;}

	public String resetBloc(){
		this.resetPanels();

		regroupementEmetteurDtManager.reset();
		regroupementDestinataireDtManager.reset();
		rapprochementDtManager.reset();

		searchManager.reset(this.getDateApplication());

		return "suivi";
	}

	/*********************************************************************************************************************************
	 * **********************************************  ACTIONS  ************************************************************
	 *********************************************************************************************************************************/

	/**
	 * Cette méthode réinitialise le formulaire.
	 * Elle
	 * 	- vide les données et le tableau résultat et le résultat de la recherche
	 *  - réinitialiser les critères de recherche primaires et secondaires
	 *  - réinitialiser les listes de recherche
	 *  - réinitialisée l'affichage des frames.
	 * @return
	 */
	public String initialiser(){
		try {
			resetBloc();
			return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("CommonSuiviEcartBean.initialiser()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	/**
	 * Cette méthode réinitialise avec les valeurs par défaut les critères secondaire du formulaire de recherche
	 * @return
	 */
	public String retablir(){
		try {
			searchManager.resetSecondaires();
			return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("CommonSuiviEcartBean.retablir()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	/**
	 * la recherche lance
	 * 	- un controle des critères de recherche et la mise en forme de certains critères (combinaison date/heure)
	 *  - l'exécution du WS de recherche et avec le résulta
	 *  	- remplit les données du tableau
	 *  	- met à jour les listes de critères secondaires.
	 * @return
	 */
	public String rechercher(){
		try {
			Map<String, String> paramsMap= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			for(String s: paramsMap.keySet()){
			
			}


			boolean testParams = searchManager.updateCriteresWithRequestParameters();
			if(!testParams){
				return OUTCOME_SUIVI;
			}

			SuiviRapprochementDto dto = businessServicesBean.rechercherRapprochements(searchManager.getSearchCriteres());
			searchManager.loadResultatRecherche(dto);
			rapprochementDtManager.updateData(dto.getListeRapprochements(), (int)dto.getNombreRapprochements());
//			System.out.println("NB RES TOTAL: "+dto.getNombreRapprochements());
//			if(dto.getListeRapprochements()!=null)System.out.println("NB RES: "+dto.getListeRapprochements().length);
			//else System.out.println("PAS DE RAPPROCHEMENT");
			regroupementEmetteurDtManager.reset();
			regroupementDestinataireDtManager.reset();

			if(dto.getNombreRapprochements()==0){
				String noresult = GenericBean.RUBRIQUE_PROPERTIES.getString("gm.msg.aucunresultat");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,noresult,noresult));
			}

			expand(RECHERCHE_RAPPROCHEMENT_FRAME_LIST);
			return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("CommonSuiviEcartBean.rechercher()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}


	public String selectionnerRapprochement(){
		return OUTCOME_REGROUPEMENTS;
	}

	/*********************************************************************************************************************************
	 * **********************************************  GETTERS & SETTERS  ************************************************************
	 *********************************************************************************************************************************/

	public DatatableManager<LigneRegroupementDto> getRegroupementEmetteurDtManager() {		return regroupementEmetteurDtManager;	}
	public DatatableManager<LigneRegroupementDto> getRegroupementDestinataireDtManager() {		return regroupementDestinataireDtManager;	}
	public DatatableManager<LigneRapprochementDto> getRapprochementDtManager() {		return rapprochementDtManager;	}
	public SearchManager getSearchManager() {		return searchManager;	}
	public FrameManager getRechercheRapprochementsFrameManager() {		return rechercheRapprochementsFrameManager;	}

}
