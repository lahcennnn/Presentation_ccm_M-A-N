package com.cedicam.gm.cm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.ExtendedDataModel;
import org.richfaces.model.selection.SimpleSelection;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.cm.data.dto.CriteresPourSuiviDto;
import com.cedicam.gm.cm.data.dto.LigneDetailDto;
import com.cedicam.gm.cm.data.dto.LigneRapprochementDto;
import com.cedicam.gm.cm.data.dto.LigneRegroupementDto;
import com.cedicam.gm.cm.data.dto.RapprochementDto;
import com.cedicam.gm.cm.data.dto.RegroupementDto;
import com.cedicam.gm.cm.data.dto.SuiviRapprochementDto;
import com.cedicam.gm.ui.core.DatatableManager;
import com.cedicam.gm.ui.core.DefaultDataProvider;
import com.cedicam.gm.ui.core.Exporter;
import com.cedicam.gm.ui.core.GenericBean;
import com.cedicam.gm.ui.core.SuiviSearchManager;

public class SuiviBean extends GenericBean {

	private boolean confirmation = false;
    DatatableManager<LigneRegroupementDto> regroupementEmetteurDtManager;
    DatatableManager<LigneRegroupementDto> regroupementDestinataireDtManager;
    DatatableManager<LigneRapprochementDto> rapprochementDtManager;
    RegroupementDto detail;
    LigneRegroupementDto selectedRegroupement;
    RapprochementDto detailRapprochement;
    private String idIncEPOC;
    private boolean autoriseRechercher = true;

	public RapprochementDto getDetailRapprochement() {
		return detailRapprochement;
	}

	SuiviSearchManager searchManager;
    BusinessServicesBean businessServicesBean;

    private static final String OUTCOME_SUIVI = "suivi";
    private static final String OUTCOME_REGROUPEMENTS = "regroupements";
    private static final String OUTCOME_DETAIL = "detail";
    private static final String OUTCOME_REFRESH = "refresh";


    public String trier(ActionEvent event){
    	try {
	        String propertyName =  (String)event.getComponent().getAttributes().get("sortPropertyName");
	        String dt =  (String)event.getComponent().getAttributes().get("dt");
	        if("rap".equals(dt))rapprochementDtManager.getDataProvider().doSort(propertyName);
	        else if("regem".equals(dt))regroupementEmetteurDtManager.getDataProvider().doSort(propertyName);
	        else if("regdest".equals(dt))regroupementDestinataireDtManager.getDataProvider().doSort(propertyName);
	        return OUTCOME_REFRESH;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.trier()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }

    public SuiviBean(){
        super();

        regroupementEmetteurDtManager = new DatatableManager<LigneRegroupementDto>(new DefaultDataProvider<LigneRegroupementDto>(), new LigneRegroupementDto());
        regroupementDestinataireDtManager = new DatatableManager<LigneRegroupementDto>(new DefaultDataProvider<LigneRegroupementDto>(), new LigneRegroupementDto());
        rapprochementDtManager = new DatatableManager<LigneRapprochementDto>(new DefaultDataProvider<LigneRapprochementDto>(), new LigneRapprochementDto());
        regroupementEmetteurDtManager.getDtManagersToClearSelection().add(regroupementDestinataireDtManager);
        regroupementDestinataireDtManager.getDtManagersToClearSelection().add(regroupementEmetteurDtManager);
        searchManager = new SuiviSearchManager();
        searchManager.reset(this.getDateApplication());
        businessServicesBean = (BusinessServicesBean)  GenericBean.findBean("businessServicesBean");
        this.setCodeEcran("ec040");
        setEnteteComposant("GMCMBN040", "Suivi Bean");
    }

    public String refresh() {
    	return OUTCOME_REFRESH;
    }

    public String resetBloc(){
    	try {
	        regroupementEmetteurDtManager.reset();
	        regroupementDestinataireDtManager.reset();
	        rapprochementDtManager.reset();
	        autoriseRechercher = true;
	        searchManager.reset(this.getDateApplication());

	        return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.resetBloc()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }
    
    public void resetResult(ActionEvent e) {
    	rapprochementDtManager.reset();
    }
    
    public void resetEPOC(ActionEvent e) {
    	this.idIncEPOC = null;
    }

    /*********************************************************************************************************************************
     * **********************************************  ACTIONS  ************************************************************
     *********************************************************************************************************************************/
    public String changePrimary() {
    	try {
	    	searchManager.changerCriterePrimaire();
	    	//commenté car la validation des champs se fait désormais sur validation du formulaire et non petit à petit
			//autoriseRechercher = searchManager.updatePrimaryCriteresWithRequestParameters();
	    	String statut = searchManager.getCriteres().getStatutRapprochement();
	    	if (ConstantesTypo.STATUT_RAPPROCHEMENT_RAPPROCHE_TOTAL.equals(statut)
	    			|| ConstantesTypo.STATUT_RAPPROCHEMENT_SANS_ECART.equals(statut) 
	    			|| ConstantesTypo.STATUT_RAPPROCHEMENT_ECART.equals(statut)) {
	    		searchManager.getCriteres().setForce(null);
	    	}
	    	rapprochementDtManager.reset();
	    	regroupementEmetteurDtManager.reset();
	    	regroupementDestinataireDtManager.reset();
	    	selectedRegroupement = null;
	    	detail = null;
	    	return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.changePrimary()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}

    }

    public void changePrimary(ActionEvent e) {
    	changePrimary();
    }

    /**
     * Cette méthode réinitialise le formulaire.
     * Elle
     * 	- vide les données et le tableau résultat et le résultat de la recherche
     *  - réinitialiser les critères de recherche primaires et secondaires
     *  - réinitialiser les listes de recherche
     *  - réinitialisée l'affichage des frames.
     * @return
     */
    public void initialiser(ActionEvent e){
        resetBloc();
    }

    /**
     * Cette méthode réinitialise avec les valeurs par défaut les critères secondaire du formulaire de recherche
     * @return
     */
    public String retablir(){
        searchManager.updateCriteresWithRequestParameters();
        searchManager.resetSecondaires();
        return runSearch();
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
	        boolean testParams = searchManager.updateCriteresWithRequestParameters();
	        if(!testParams) return OUTCOME_SUIVI;
	        return runSearch();
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.rechercher()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }

    private String runSearch(){
    	try {
	    	this.rapprochementDtManager.setSelection(new SimpleSelection());
	
	    	SuiviRapprochementDto dto = businessServicesBean.rechercherRapprochements(searchManager.getSearchCriteres());
	    	
	        Integer nbMaxLines = businessServicesBean.getNbMaxLines();
	        rapprochementDtManager.reset();
	        if(dto.getNombreRapprochements() <= 0){
	            getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3083");
	            searchManager.resetSecondairesNonValorises();
		    } else {
		    	if (null != nbMaxLines && dto.getNombreRapprochements() > nbMaxLines.longValue()) {
		    		getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3081", new String [] {nbMaxLines.toString(),""+dto.getNombreRapprochements()});   
		        } else if (dto.getListeRapprochements() != null && dto.getListeRapprochements().length > 0) {
		        	getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3082", "" + dto.getListeRapprochements().length);
		        }
		    	searchManager.loadResultatRecherche(dto);
		    	rapprochementDtManager.updateData(dto.getListeRapprochements(), (int)dto.getNombreRapprochements());
		        regroupementEmetteurDtManager.reset();
		        regroupementDestinataireDtManager.reset();
		    }
	        return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.runSearch()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }

    public String rechercherEcart(CriteresPourSuiviDto criteresEcart){
 		this.searchManager.setCriteres((CriteresPourSuiviDto)criteresEcart.clone());
    	return runSearch();
    }



    /**
     * Prendre la ligne sélectionnée
     * Controler le format du parametre epoc
     * Lancer le WS d'enregistrement
     * si OK, message d'info vert et modifier le contenu de l'occurence pour mettre à jour le DT sans rappeler la recherche
     * si KO, message d'erreur rouge
     * on reste sur la liste
     */
    public String enregistrerEpoc() {
    	try {
	        LigneRapprochementDto ligne = rapprochementDtManager.getSelectedObject();
	        if (idIncEPOC != null && !idIncEPOC.trim().isEmpty()) {
		        String lastIdEpoc = ligne.getIdIncEPOC();
	        	ligne.setIdIncEPOC(idIncEPOC);
		        LigneRapprochementDto res = businessServicesBean.attribuerEPOC(ligne, ligne.getIdIncEPOC());
		        if (res != null) {
		        	rapprochementDtManager.updateRow(res);
		        	rapprochementDtManager.setSelectedObject(res);
		        	getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3100", idIncEPOC);
		        } else {
		        	ligne.setIdIncEPOC(lastIdEpoc);
		        }
		        idIncEPOC = null;
	        } else {
	        	getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3084", "Identifiant d'incident EPOC");
	        }
	        return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.enregistrerEpoc()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }


    /**
     * Prendre la ligne sélectionnée
     * Controler le format du parametre epoc
     * Lancer le WS d'enregistrement
     * si OK, message d'info vert et modifier le contenu de l'occurence pour mettre à jour le DT sans rappeler la recherche
     * si KO, message d'erreur rouge
     * on reste sur la liste
     */
    public String forcerRapprochement(){
    	try {
	        LigneRapprochementDto ligne = rapprochementDtManager.getSelectedObject();
	        LigneRapprochementDto res = businessServicesBean.forcerClotureRapprochement(ligne);
	        if (res != null) {
	        	rapprochementDtManager.updateRow(res);
	        	getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3101");
	        	runSearch();
	        }
	        this.confirmation = false;
	        return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.forcerRapprochement()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }


    /**
     * Prendre la ligne sélectionnée
     * Remplir les liste de regrouppements
     * rediriger vers le détail de rapprochements
     */
    public String detaillerRapprochement(){
    	try {
	    	regroupementDestinataireDtManager.reset();
	    	regroupementEmetteurDtManager.reset();
	
	    	LigneRapprochementDto ligne = rapprochementDtManager.getSelectedObject();
	    	detailRapprochement =  businessServicesBean.rechercherRapprochement(ligne);
	    	if(detailRapprochement!=null){
	    		if(detailRapprochement.getRegroupementsDestinataires()!=null)regroupementDestinataireDtManager.updateData(detailRapprochement.getRegroupementsDestinataires(),detailRapprochement.getRegroupementsDestinataires().length);
	    		if(detailRapprochement.getRegroupementsEmetteurs()!=null)regroupementEmetteurDtManager.updateData(detailRapprochement.getRegroupementsEmetteurs(), detailRapprochement.getRegroupementsEmetteurs().length);
	    		return OUTCOME_REGROUPEMENTS;
	    	}
	    	return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.detaillerRapprochement()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }


    public ExtendedDataModel getSelectedRapprochementDtModel(){
    	return DatatableManager.getDataModel(detailRapprochement);
    }

    public ExtendedDataModel getSelectedRegroupementDtModel(){
    	return DatatableManager.getDataModel(selectedRegroupement);
    }

    /**
     * Prendre la ligne sélectionnée
     * Remplir les liste de regrouppements
     * rediriger vers le détail de rapprochements
     */
    public void detaillerRegroupement(ActionEvent event){
    	String reg = (String)event.getComponent().getAttributes().get("reg");
    	Long regId = (Long)event.getComponent().getAttributes().get("regId");
    	DatatableManager<LigneRegroupementDto> mng = null;
    	if("em".equals(reg)) mng = regroupementEmetteurDtManager;
    	else  mng = regroupementDestinataireDtManager;

    	for(LigneRegroupementDto dto: mng.getDataProvider().getFullData()){
    		if(dto.getIdRegroupement().equals(regId)){
    			selectedRegroupement = dto;
    			break;
    		}
    	}


    }

    public String detaillerRegroupement() {
    	try {
	    	detail =  businessServicesBean.rechercherRegroupement(selectedRegroupement);
	    	if(detail!=null){
	    		return OUTCOME_DETAIL;
	    	}
	    	return OUTCOME_REGROUPEMENTS;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.detaillerRegroupement()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }

    public String fermerDetailRegroupement() {
    	return OUTCOME_REGROUPEMENTS;
    }
    /*********************************************************************************************************************************
     * **********************************************  GETTERS & SETTERS  ************************************************************
     *********************************************************************************************************************************/

    public DatatableManager<LigneRegroupementDto> getRegroupementEmetteurDtManager() {		return regroupementEmetteurDtManager;	}
    public DatatableManager<LigneRegroupementDto> getRegroupementDestinataireDtManager() {		return regroupementDestinataireDtManager;	}
    public DatatableManager<LigneRapprochementDto> getRapprochementDtManager() {		return rapprochementDtManager;	}
    public SuiviSearchManager getSearchManager() {		return searchManager;	}
    public RegroupementDto getDetail() {		return detail;	}

    public String afficheConfirmation() {
    	this.confirmation = true;
    	return OUTCOME_SUIVI;
    }

	public boolean isConfirmation() {
		return confirmation;
	}

	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}

	public String continuerSaisie() {
		this.confirmation = false;
		return null;
	}

	public String getIdIncEPOC() {
		return idIncEPOC;
	}

	public void setIdIncEPOC(String idIncEPOC) {
		this.idIncEPOC = idIncEPOC;
	}

	public boolean isAutoriseRechercher() {
		return autoriseRechercher;
	}

	public void setAutoriseRechercher(boolean autoriseRechercher) {
		this.autoriseRechercher = autoriseRechercher;
	}
	
	/**
	 * Point d'entrée de la génération.
	 * Les paramètres sont récupérés dans le contexte d'appel :
	 * - format de génération: CSV ou XLS
	 * - type de rapport à générer: RECH_RAPPRO, DETAIL_RAPPRO, DETAIL_REGROUP 
	 */
	public void genererRapport(){
		Map<String,String> params = 
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		genererRapport(params.get("format"), params.get("typeRapport"));
	}

	/**
	 * Point d'entrée de la génération.
	 * Les paramètres sont :
	 * 
	 * @param format de génération: CSV ou XLS
	 * @param type de rapport à générer: RECH_RAPPRO, DETAIL_RAPPRO, DETAIL_REGROUP
	 */
	public void genererRapport(final String format, final String typeRapport){
		// Générations CSV
		if (Exporter.FORMAT_CSV.equals(format)) {
			if ("RECH_RAPPRO".equals(typeRapport)) {
				Exporter.genererRapportCSV(genererDonneesExportRechercheRapprochement());		   
			} else if ("DETAIL_RAPPRO".equals(typeRapport)) {
				Exporter.genererRapportCSV(genererDonneesExportDetailRapprochement());
			} else if ("DETAIL_REGROUP".equals(typeRapport)) {
			Exporter.genererRapportCSV(genererDonneesExportDetailRegroupement());
			}
		}
	   }

	/**
	 * Méthode de construction des données pour l'export CSV.
	 * Refabrique les données visibles sur les écrans EC040 et EC050.
	 * 
	 * @return ArrayList<ArrayList<String>>
	 */
    protected ArrayList<ArrayList<String>> genererDonneesExportRechercheRapprochement() {
    	final String SEPARATEUR_COLONNE = "";
    	ArrayList<ArrayList<String>> lignes = new ArrayList<ArrayList<String>>();
    	
    	ArrayList<String> ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el000"));
    	lignes.add(ligne);
    	lignes.add(new ArrayList<String>());
    	
    	// Critères de recherche
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el001"));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el002-a")); // Date heure echg
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateDebutEchange()));
    	ligne.add(getSearchManager().getHeureEchangeDebut());
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el006")); // réseau
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getReseau(),getSearchManager().getListReseau()));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el002-b")); // au
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateFinEchange()));
    	ligne.add(getSearchManager().getHeureEchangeFin());
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el007")); // sens
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getSens(),getSearchManager().getListSens()));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el003-a")); // date conv reference
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateConvRef()));
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el003-b")); // au
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateFinConvRef()));
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el008")); // back office
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getBackOffice(),getSearchManager().getListBO()));
    	lignes.add(ligne);

       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el004-a")); // date règlt du
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateDebutReglement()));
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el004-b")); // au
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateFinReglement()));
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el009")); // statut rapprochement
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getStatutRapprochement(),getSearchManager().getListStatut()));
    	lignes.add(ligne);

       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el005")); // chef de file
    	ligne.add(Exporter.recupererLabelDansListe((getSearchManager().getCriteres().getChefDeFile()==null)?"":""+getSearchManager().getCriteres().getChefDeFile(),getSearchManager().getListChefDeFile()));
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el010")); // devise reglt
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getDevRegCompt(),getSearchManager().getListDevise()));
    	lignes.add(ligne);

       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el022")); // indicateur forcage
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getForceStr(),getSearchManager().getListForcage()));
    	lignes.add(ligne);

    	// Critères secondaires
    	// si réseau INTER
    	if (getSearchManager().isReseauInter()) {
           	ligne = new ArrayList<String>();
        	ligne.add(GenericBean.recupererMessage("gm.ec040.el011")); // entite echg emet
        	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getEntiteEchangeE(),getSearchManager().getListEntiteEchangeEmettrice()));
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(GenericBean.recupererMessage("gm.ec040.el012")); // entite echg dest
        	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getEntiteEchangeD(),getSearchManager().getListEntiteEchangeDestinataire()));
        	lignes.add(ligne);

           	ligne = new ArrayList<String>();
        	ligne.add(GenericBean.recupererMessage("gm.ec040.el013")); // entite compta emet
        	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getEntiteComptaE(),getSearchManager().getListEntiteComptaEmettrice()));
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(GenericBean.recupererMessage("gm.ec040.el014")); // entite compta dest
        	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getEntiteComptaD(),getSearchManager().getListEntiteComptaDestinataire()));
        	lignes.add(ligne);
    	}
    	// si réseau AUTRE
    	if (getSearchManager().isReseauAutre()) {
           	ligne = new ArrayList<String>();
        	ligne.add(GenericBean.recupererMessage("gm.ec040.el015")); // entite echg
        	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getEntiteEchange(),getSearchManager().getListEntiteEchange()));
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(SEPARATEUR_COLONNE);
        	ligne.add(GenericBean.recupererMessage("gm.ec040.el016")); // entite compta
        	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getEntiteCompta(),getSearchManager().getListEntiteCompta()));
        	lignes.add(ligne);
    	}
    	// commun
       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el017")); // id fichier echange
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getIdFichierRefRemise(),getSearchManager().getListFichierReferenceRemise()));
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el018")); // code opNFCCA
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getCodeOpeNfcca(),getSearchManager().getListCodeOperationNfcca()));
    	lignes.add(ligne);
    	
       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec040.el019")); // ref comptable
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getReferenceComptable(),getSearchManager().getListReferenceComptable()));
    	lignes.add(ligne);
    	
    	lignes.add(new ArrayList<String>());
    	// Tableau résultat - Entêtes
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el001"));
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("wn.commun.nombre.de.resultats"));
    	ligne.add(""+getRapprochementDtManager().getDataProvider().getTotalResultSize());
    	lignes.add(ligne);

       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("")); // code op
    	ligne.add(GenericBean.recupererMessage("")); // devise
    	ligne.add(GenericBean.recupererMessage("")); // entite emet
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006")); // Regrp Emission \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("")); // entite dest
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008")); // Regrp reception\ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009")); // Ecart \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010")); // Reglt \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("")); // statut
    	ligne.add(GenericBean.recupererMessage("")); // epoc
    	ligne.add(GenericBean.recupererMessage("")); // force
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el002")); // code op
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el004")); // devise
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el005")); // entite emet
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-a")); // Regrp Emission \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el007")); // entite dest
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-a")); // Regrp reception\ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-a")); // Ecart \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010-a")); // Reglt \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010-b")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el011")); // statut
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el012")); // epoc
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el013")); // force
    	lignes.add(ligne);

    	// Tableau résultat - Contenu
    	List<LigneRapprochementDto> lignesDto = getRapprochementDtManager().getDataManager().getFullData();
    	for (LigneRapprochementDto ligneRapprochementDto : lignesDto) {
        	ligne = new ArrayList<String>();
        	ligne.add(ligneRapprochementDto.getCodeOperationNfcca()); // code op
        	ligne.add(Exporter.recupererLabelDansListe(ligneRapprochementDto.getDevise(),getSearchManager().getListDevise())); // devise
        	ligne.add((ligneRapprochementDto.getEntiteEmettrice()==null)?"":ligneRapprochementDto.getEntiteEmettrice()); // entite emet
        	ligne.add(""+ligneRapprochementDto.getNombreOpeEmission()); // Regrp Emission \ Nb
        	ligne.add(ligneRapprochementDto.getSigneEmission()); // signe
        	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantEmission())); // montant
        	ligne.add((ligneRapprochementDto.getEntiteDestinataire()==null)?"":ligneRapprochementDto.getEntiteDestinataire()); // entite dest
        	ligne.add(""+ligneRapprochementDto.getNombreOpeReception()); // Regrp reception\ Nb
        	ligne.add(ligneRapprochementDto.getSigneReception()); // signe
        	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantReception())); // montant
        	ligne.add(""+ligneRapprochementDto.getNombreEcart()); // Ecart \ Nb
        	ligne.add(ligneRapprochementDto.getSigneEcart()); // signe
        	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantEcart())); // montant
        	ligne.add(""+ligneRapprochementDto.getNombreReglReal()); // Reglt \ Nb
        	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantReglReal())); // montant
        	ligne.add(GenericBean.recupererMessage("gm.result.statut."+ligneRapprochementDto.getIdStatut())); // statut
        	ligne.add((ligneRapprochementDto.getIdIncEPOC()==null)?"":ligneRapprochementDto.getIdIncEPOC()); // epoc
        	ligne.add(ligneRapprochementDto.isForce()?"O":"N"); // force
        	lignes.add(ligne);
		}
    	
    	// Tableau résultat - Total
       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el014")); // total
    	String devise = getRapprochementDtManager().getLigneTotal().getDevise();
    	if (devise != null && devise != "") {
    		devise = Exporter.recupererLabelDansListe(devise,getSearchManager().getListDevise());
    	}
    	ligne.add(devise);
    	ligne.add(getRapprochementDtManager().getLigneTotal().getEntiteEmettrice());
    	if (getRapprochementDtManager().getLigneTotal().getNombreOpeEmission() >= 0) {
	    	ligne.add(""+getRapprochementDtManager().getLigneTotal().getNombreOpeEmission());
	    	ligne.add(getRapprochementDtManager().getLigneTotal().getSigneEmission());
	    	ligne.add(Exporter.formaterNombre(getRapprochementDtManager().getLigneTotal().getMontantEmission()));
    	} else {
        	ligne.add("");
        	ligne.add("");
        	ligne.add("");
    	}
    	ligne.add(getRapprochementDtManager().getLigneTotal().getEntiteDestinataire());
    	if (getRapprochementDtManager().getLigneTotal().getNombreOpeReception() >= 0) {
    		ligne.add(""+getRapprochementDtManager().getLigneTotal().getNombreOpeReception());
    		ligne.add(getRapprochementDtManager().getLigneTotal().getSigneReception());
    		ligne.add(Exporter.formaterNombre(getRapprochementDtManager().getLigneTotal().getMontantReception()));
    	} else {
        	ligne.add("");
        	ligne.add("");
        	ligne.add("");
    	}
    	if (getRapprochementDtManager().getLigneTotal().getNombreEcart() >= 0) {
	    	ligne.add(""+getRapprochementDtManager().getLigneTotal().getNombreEcart());
	    	ligne.add(getRapprochementDtManager().getLigneTotal().getSigneEcart());
	    	ligne.add(Exporter.formaterNombre(getRapprochementDtManager().getLigneTotal().getMontantEcart()));
    	} else {
        	ligne.add("");
        	ligne.add("");
        	ligne.add("");
    	}
    	if (getRapprochementDtManager().getLigneTotal().getNombreReglReal() >= 0) {
    		ligne.add(""+getRapprochementDtManager().getLigneTotal().getNombreReglReal());
    	// ASS_034: pas de montant car non signé 
    	//	ligne.add(Exporter.formaterNombre(getRapprochementDtManager().getLigneTotal().getMontantReglReal()));
        	ligne.add("");
    	} else {
        	ligne.add("");
        	ligne.add("");
    	}
    	ligne.add("");
    	ligne.add("");
    	ligne.add("");
    	lignes.add(ligne);


    	return lignes;
    }

	/**
	 * Méthode de construction des données pour l'export CSV.
	 * Refabrique les données visibles sur l'écrans EC060.
	 * 
	 * @return ArrayList<ArrayList<String>>
	 */
    protected ArrayList<ArrayList<String>> genererDonneesExportDetailRapprochement() {
    	final String SEPARATEUR_COLONNE = "";
    	ArrayList<ArrayList<String>> lignes = new ArrayList<ArrayList<String>>();

    	ArrayList<String> ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec060.el000"));
    	lignes.add(ligne);
    	lignes.add(new ArrayList<String>());

    	// rappel du rapprochement selectionne
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec060.el001"));
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec060.el004"));
    	ligne.add(Exporter.formaterDate(getDetailRapprochement().getTimeStampMaj()));
    	lignes.add(ligne);
    	
       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("")); // code op
    	ligne.add(GenericBean.recupererMessage("")); // devise
    	ligne.add(GenericBean.recupererMessage("")); // entite emet
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006")); // Regrp Emission \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("")); // entite dest
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008")); // Regrp reception\ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009")); // Ecart \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010")); // Reglt \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("")); // statut
    	ligne.add(GenericBean.recupererMessage("")); // epoc
    	ligne.add(GenericBean.recupererMessage("")); // force
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el002")); // code op
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el004")); // devise
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el005")); // entite emet
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-a")); // Regrp Emission \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el007")); // entite dest
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-a")); // Regrp reception\ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-a")); // Ecart \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010-a")); // Reglt \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010-b")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el011")); // statut
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el012")); // epoc
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el013")); // force
    	lignes.add(ligne);

    	LigneRapprochementDto ligneRapprochementDto = getRapprochementDtManager().getSelectedOccurrence();
    	ligne = new ArrayList<String>();
    	ligne.add(ligneRapprochementDto.getCodeOperationNfcca()); // code op
    	ligne.add(Exporter.recupererLabelDansListe(ligneRapprochementDto.getDevise(),getSearchManager().getListDevise())); // devise
    	ligne.add((ligneRapprochementDto.getEntiteEmettrice()==null)?"":ligneRapprochementDto.getEntiteEmettrice()); // entite emet
    	ligne.add(""+ligneRapprochementDto.getNombreOpeEmission()); // Regrp Emission \ Nb
    	ligne.add(ligneRapprochementDto.getSigneEmission()); // signe
    	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantEmission())); // montant
    	ligne.add((ligneRapprochementDto.getEntiteDestinataire()==null)?"":ligneRapprochementDto.getEntiteDestinataire()); // entite dest
    	ligne.add(""+ligneRapprochementDto.getNombreOpeReception()); // Regrp reception\ Nb
    	ligne.add(ligneRapprochementDto.getSigneReception()); // signe
    	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantReception())); // montant
    	ligne.add(""+ligneRapprochementDto.getNombreEcart()); // Ecart \ Nb
    	ligne.add(ligneRapprochementDto.getSigneEcart()); // signe
    	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantEcart())); // montant
    	ligne.add(""+ligneRapprochementDto.getNombreReglReal()); // Reglt \ Nb
    	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantReglReal())); // montant
    	ligne.add(GenericBean.recupererMessage("gm.result.statut."+ligneRapprochementDto.getIdStatut())); // statut
    	ligne.add((ligneRapprochementDto.getIdIncEPOC()==null)?"":ligneRapprochementDto.getIdIncEPOC()); // epoc
    	ligne.add(ligneRapprochementDto.isForce()?"O":"N"); // force
    	lignes.add(ligne);

    	// regroupements émetteurs
    	if (getRegroupementEmetteurDtManager().getDataProvider().getTotalResultSize() > 0) {
	    	lignes.add(new ArrayList<String>());
	    	ligne = new ArrayList<String>();
	    	ligne.add(GenericBean.recupererMessage("gm.ec060.el002"));
	    	lignes.add(ligne);
	    	// entete
	    	ligne = new ArrayList<String>();
	    	ligne.add(GenericBean.recupererMessage("")); // type CRE
	    	ligne.add(GenericBean.recupererMessage("")); // ref comptable
	    	ligne.add(GenericBean.recupererMessage("")); // zone rappro
	    	ligne.add(GenericBean.recupererMessage("")); // id fic remise
	    	ligne.add(GenericBean.recupererMessage("")); // nb op
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006")); // reglement\ devise
	    	ligne.add(GenericBean.recupererMessage("")); // signe
	    	ligne.add(GenericBean.recupererMessage("")); // montant
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007")); // transaction\ devise
	    	ligne.add(GenericBean.recupererMessage("")); // signe
	    	ligne.add(GenericBean.recupererMessage("")); // montant
	    	ligne.add(GenericBean.recupererMessage("")); // top compta
	    	ligne.add(GenericBean.recupererMessage("")); // date reglt
	    	ligne.add(GenericBean.recupererMessage("")); // processing code
	    	ligne.add(GenericBean.recupererMessage("")); // type impaye
	    	lignes.add(ligne);
	
	    	ligne = new ArrayList<String>();
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el001")); // type CRE
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el002")); // ref comptable
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el003")); // zone rappro
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el004")); // id fic remise
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el005")); // nb op
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-a")); // reglement\ devise
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-b")); // signe
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-c")); // montant
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-a")); // transaction\ devise
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-b")); // signe
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-c")); // montant
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el008")); // top compta
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el009")); // date reglt
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el010")); // processing code
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el011")); // type impaye
	    	lignes.add(ligne);
	 
	    	// contenu
	    	List<LigneRegroupementDto> lignesRegroupementEmetteurDto = getRegroupementEmetteurDtManager().getDataProvider().getFullData();
	    	for (LigneRegroupementDto ligneRegroupementDto : lignesRegroupementEmetteurDto) {
		    	ligne = new ArrayList<String>();
		    	ligne.add(ligneRegroupementDto.getTypeCre()); // type CRE
		    	ligne.add((ligneRegroupementDto.getRefComptable()==null)?"":ligneRegroupementDto.getRefComptable()); // ref comptable
		    	ligne.add((ligneRegroupementDto.getZoneRapprochement()==null)?"":ligneRegroupementDto.getZoneRapprochement()); // zone rappro
		    	ligne.add(ligneRegroupementDto.getRefFicOpeEcrin()); // id fic remise
		    	ligne.add(""+ligneRegroupementDto.getNbOperations()); // nb op
		    	String deviseRglmt = ligneRegroupementDto.getDeviseRglmt();
		    	if (deviseRglmt != null && deviseRglmt != "") {
		    		deviseRglmt = Exporter.recupererLabelDansListe(deviseRglmt,getSearchManager().getListDevise());
		    	} else {
		    		deviseRglmt = "";
		    	}
		    	ligne.add(deviseRglmt);
		    	ligne.add((ligneRegroupementDto.getSigneRglmt()==null)?"":ligneRegroupementDto.getSigneRglmt()); // signe
		    	ligne.add(Exporter.formaterNombre(ligneRegroupementDto.getSommeMontantRglmt())); // montant
		    	String deviseTrans = ligneRegroupementDto.getDeviseTrans();
		    	if (deviseTrans != null && deviseTrans != "") {
		    		deviseTrans = Exporter.recupererLabelDansListe(deviseTrans,getSearchManager().getListDevise());
		    	} else {
		    		deviseTrans = "";
		    	}
		    	ligne.add(deviseTrans);
		    	ligne.add((ligneRegroupementDto.getSigneTrans()==null)?"":ligneRegroupementDto.getSigneTrans()); // signe
		    	ligne.add(Exporter.formaterNombre(ligneRegroupementDto.getSommeMontantTrans())); // montant
		    	ligne.add(ligneRegroupementDto.getTopCompta()); // top compta
		    	ligne.add(Exporter.formaterDate(ligneRegroupementDto.getDateRglmt())); // date reglt
		    	ligne.add((ligneRegroupementDto.getProcessingCode()==null)?"":ligneRegroupementDto.getProcessingCode()); // processing code
		    	ligne.add((ligneRegroupementDto.getTypeImpaye()==null)?"":ligneRegroupementDto.getTypeImpaye()); // type impaye
		    	lignes.add(ligne);
			}
    	}

    	// regroupements destinatires
    	if (getRegroupementDestinataireDtManager().getDataProvider().getTotalResultSize() > 0) {
	    	lignes.add(new ArrayList<String>());
	    	ligne = new ArrayList<String>();
	    	ligne.add(GenericBean.recupererMessage("gm.ec060.el003"));
	    	lignes.add(ligne);
	
	    	// entete
	    	ligne = new ArrayList<String>();
	    	ligne.add(GenericBean.recupererMessage("")); // type CRE
	    	ligne.add(GenericBean.recupererMessage("")); // ref comptable
	    	ligne.add(GenericBean.recupererMessage("")); // zone rappro
	    	ligne.add(GenericBean.recupererMessage("")); // id fic remise
	    	ligne.add(GenericBean.recupererMessage("")); // nb op
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006")); // reglement\ devise
	    	ligne.add(GenericBean.recupererMessage("")); // signe
	    	ligne.add(GenericBean.recupererMessage("")); // montant
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007")); // transaction\ devise
	    	ligne.add(GenericBean.recupererMessage("")); // signe
	    	ligne.add(GenericBean.recupererMessage("")); // montant
	    	ligne.add(GenericBean.recupererMessage("")); // top compta
	    	ligne.add(GenericBean.recupererMessage("")); // date reglt
	    	ligne.add(GenericBean.recupererMessage("")); // processing code
	    	ligne.add(GenericBean.recupererMessage("")); // type impaye
	    	lignes.add(ligne);
	
	    	ligne = new ArrayList<String>();
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el001")); // type CRE
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el002")); // ref comptable
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el003")); // zone rappro
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el004")); // id fic remise
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el005")); // nb op
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-a")); // reglement\ devise
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-b")); // signe
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-c")); // montant
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-a")); // transaction\ devise
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-b")); // signe
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-c")); // montant
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el008")); // top compta
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el009")); // date reglt
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el010")); // processing code
	    	ligne.add(GenericBean.recupererMessage("gm.ec062.el011")); // type impaye
	    	lignes.add(ligne);
	
	    	// contenu
	    	List<LigneRegroupementDto> lignesRegroupementDestinataireDto = getRegroupementDestinataireDtManager().getDataProvider().getFullData();
	    	for (LigneRegroupementDto ligneRegroupementDto : lignesRegroupementDestinataireDto) {
		    	ligne = new ArrayList<String>();
		    	ligne.add(ligneRegroupementDto.getTypeCre()); // type CRE
		    	ligne.add((ligneRegroupementDto.getRefComptable()==null)?"":ligneRegroupementDto.getRefComptable()); // ref comptable
		    	ligne.add((ligneRegroupementDto.getZoneRapprochement()==null)?"":ligneRegroupementDto.getZoneRapprochement()); // zone rappro
		    	ligne.add(ligneRegroupementDto.getRefFicOpeEcrin()); // id fic remise
		    	ligne.add(""+ligneRegroupementDto.getNbOperations()); // nb op
		    	String deviseRglmt = ligneRegroupementDto.getDeviseRglmt();
		    	if (deviseRglmt != null && deviseRglmt != "") {
		    		deviseRglmt = Exporter.recupererLabelDansListe(deviseRglmt,getSearchManager().getListDevise());
		    	} else {
		    		deviseRglmt = "";
		    	}
		    	ligne.add(deviseRglmt);
		    	ligne.add((ligneRegroupementDto.getSigneRglmt()==null)?"":ligneRegroupementDto.getSigneRglmt()); // signe
		    	ligne.add(Exporter.formaterNombre(ligneRegroupementDto.getSommeMontantRglmt())); // montant
		    	String deviseTrans = ligneRegroupementDto.getDeviseTrans();
		    	if (deviseTrans != null && deviseTrans != "") {
		    		deviseTrans = Exporter.recupererLabelDansListe(deviseTrans,getSearchManager().getListDevise());
		    	} else {
		    		deviseTrans = "";
		    	}
		    	ligne.add(deviseTrans);
		    	ligne.add((ligneRegroupementDto.getSigneTrans()==null)?"":ligneRegroupementDto.getSigneTrans()); // signe
		    	ligne.add(Exporter.formaterNombre(ligneRegroupementDto.getSommeMontantTrans())); // montant
		    	ligne.add(ligneRegroupementDto.getTopCompta()); // top compta
		    	ligne.add(Exporter.formaterDate(ligneRegroupementDto.getDateRglmt())); // date reglt
		    	ligne.add((ligneRegroupementDto.getProcessingCode()==null)?"":ligneRegroupementDto.getProcessingCode()); // processing code
		    	ligne.add((ligneRegroupementDto.getTypeImpaye()==null)?"":ligneRegroupementDto.getTypeImpaye()); // type impaye
		    	lignes.add(ligne);
			}
    	}

    	return lignes;
    }

	/**
	 * Méthode de construction des données pour l'export CSV.
	 * Refabrique les données visibles sur l'écrans EC070.
	 * 
	 * @return ArrayList<ArrayList<String>>
	 */
    protected ArrayList<ArrayList<String>> genererDonneesExportDetailRegroupement() {
    	final String SEPARATEUR_COLONNE = "";
    	ArrayList<ArrayList<String>> lignes = new ArrayList<ArrayList<String>>();

    	ArrayList<String> ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec070.el000"));
    	lignes.add(ligne);
    	lignes.add(new ArrayList<String>());

    	// rappel du rapprochement selectionne
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec070.el001"));
    	//ligne.add(SEPARATEUR_COLONNE);
    	//ligne.add(GenericBean.recupererMessage("gm.ec060.el004"));
    	//ligne.add(Exporter.formaterDate(getDetailRapprochement().getTimeStampMaj()));
    	lignes.add(ligne);
    	
       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("")); // code op
    	ligne.add(GenericBean.recupererMessage("")); // devise
    	ligne.add(GenericBean.recupererMessage("")); // entite emet
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006")); // Regrp Emission \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("")); // entite dest
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008")); // Regrp reception\ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009")); // Ecart \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010")); // Reglt \ Nb
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("")); // statut
    	ligne.add(GenericBean.recupererMessage("")); // epoc
    	ligne.add(GenericBean.recupererMessage("")); // force
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el002")); // code op
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el004")); // devise
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el005")); // entite emet
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-a")); // Regrp Emission \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el006-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el007")); // entite dest
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-a")); // Regrp reception\ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el008-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-a")); // Ecart \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el009-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010-a")); // Reglt \ Nb
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el010-b")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el011")); // statut
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el012")); // epoc
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el013")); // force
    	lignes.add(ligne);

    	LigneRapprochementDto ligneRapprochementDto = getRapprochementDtManager().getSelectedOccurrence();
    	ligne = new ArrayList<String>();
    	ligne.add(ligneRapprochementDto.getCodeOperationNfcca()); // code op
    	ligne.add(Exporter.recupererLabelDansListe(ligneRapprochementDto.getDevise(),getSearchManager().getListDevise())); // devise
    	ligne.add((ligneRapprochementDto.getEntiteEmettrice()==null)?"":ligneRapprochementDto.getEntiteEmettrice()); // entite emet
    	ligne.add(""+ligneRapprochementDto.getNombreOpeEmission()); // Regrp Emission \ Nb
    	ligne.add(ligneRapprochementDto.getSigneEmission()); // signe
    	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantEmission())); // montant
    	ligne.add((ligneRapprochementDto.getEntiteDestinataire()==null)?"":ligneRapprochementDto.getEntiteDestinataire()); // entite dest
    	ligne.add(""+ligneRapprochementDto.getNombreOpeReception()); // Regrp reception\ Nb
    	ligne.add(ligneRapprochementDto.getSigneReception()); // signe
    	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantReception())); // montant
    	ligne.add(""+ligneRapprochementDto.getNombreEcart()); // Ecart \ Nb
    	ligne.add(ligneRapprochementDto.getSigneEcart()); // signe
    	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantEcart())); // montant
    	ligne.add(""+ligneRapprochementDto.getNombreReglReal()); // Reglt \ Nb
    	ligne.add(Exporter.formaterNombre(ligneRapprochementDto.getMontantReglReal())); // montant
    	ligne.add(GenericBean.recupererMessage("gm.result.statut."+ligneRapprochementDto.getIdStatut())); // statut
    	ligne.add((ligneRapprochementDto.getIdIncEPOC()==null)?"":ligneRapprochementDto.getIdIncEPOC()); // epoc
    	ligne.add(ligneRapprochementDto.isForce()?"O":"N"); // force
    	lignes.add(ligne);

    	// rappel du regroupement sélectionné
    	lignes.add(new ArrayList<String>());
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec070.el002"));
    	lignes.add(ligne);
    	// entete
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("")); // type CRE
    	ligne.add(GenericBean.recupererMessage("")); // ref comptable
    	ligne.add(GenericBean.recupererMessage("")); // zone rappro
    	ligne.add(GenericBean.recupererMessage("")); // id fic remise
    	ligne.add(GenericBean.recupererMessage("")); // nb op
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006")); // reglement\ devise
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007")); // transaction\ devise
    	ligne.add(GenericBean.recupererMessage("")); // signe
    	ligne.add(GenericBean.recupererMessage("")); // montant
    	ligne.add(GenericBean.recupererMessage("")); // top compta
    	ligne.add(GenericBean.recupererMessage("")); // date reglt
    	ligne.add(GenericBean.recupererMessage("")); // processing code
    	ligne.add(GenericBean.recupererMessage("")); // type impaye
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el001")); // type CRE
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el002")); // ref comptable
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el003")); // zone rappro
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el004")); // id fic remise
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el005")); // nb op
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-a")); // reglement\ devise
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el006-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-a")); // transaction\ devise
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-b")); // signe
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el007-c")); // montant
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el008")); // top compta
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el009")); // date reglt
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el010")); // processing code
    	ligne.add(GenericBean.recupererMessage("gm.ec062.el011")); // type impaye
    	lignes.add(ligne);
 
    	// contenu
    	LigneRegroupementDto ligneRegroupementDto = selectedRegroupement; // getDetail();
    	ligne = new ArrayList<String>();
    	ligne.add(ligneRegroupementDto.getTypeCre()); // type CRE
    	ligne.add((ligneRegroupementDto.getRefComptable()==null)?"":ligneRegroupementDto.getRefComptable()); // ref comptable
    	ligne.add((ligneRegroupementDto.getZoneRapprochement()==null)?"":ligneRegroupementDto.getZoneRapprochement()); // zone rappro
    	ligne.add((ligneRegroupementDto.getRefFicOpeEcrin()==null)?"":ligneRegroupementDto.getRefFicOpeEcrin()); // id fic remise
    	ligne.add(""+ligneRegroupementDto.getNbOperations()); // nb op
    	String deviseRglmt = ligneRegroupementDto.getDeviseRglmt();
    	if (deviseRglmt != null && deviseRglmt != "") {
    		deviseRglmt = Exporter.recupererLabelDansListe(deviseRglmt,getSearchManager().getListDevise());
    	} else {
    		deviseRglmt = "";
    	}
    	ligne.add(deviseRglmt);
    	ligne.add((ligneRegroupementDto.getSigneRglmt()==null)?"":ligneRegroupementDto.getSigneRglmt()); // signe
    	ligne.add(Exporter.formaterNombre(ligneRegroupementDto.getSommeMontantRglmt())); // montant
    	String deviseTrans = ligneRegroupementDto.getDeviseTrans();
    	if (deviseTrans != null && deviseTrans != "") {
    		deviseTrans = Exporter.recupererLabelDansListe(deviseTrans,getSearchManager().getListDevise());
    	} else {
    		deviseTrans = "";
    	}
    	ligne.add(deviseTrans);
    	ligne.add((ligneRegroupementDto.getSigneTrans()==null)?"":ligneRegroupementDto.getSigneTrans()); // signe
    	ligne.add(Exporter.formaterNombre(ligneRegroupementDto.getSommeMontantTrans())); // montant
    	ligne.add(ligneRegroupementDto.getTopCompta()); // top compta
    	ligne.add(Exporter.formaterDate(ligneRegroupementDto.getDateRglmt())); // date reglt
    	ligne.add((ligneRegroupementDto.getProcessingCode()==null)?"":ligneRegroupementDto.getProcessingCode()); // processing code
    	ligne.add((ligneRegroupementDto.getTypeImpaye()==null)?"":ligneRegroupementDto.getTypeImpaye()); // type impaye
    	lignes.add(ligne);

    	// resultat liste CRE details
    	lignes.add(new ArrayList<String>());
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec070.el003"));
    	lignes.add(ligne);

    	List<LigneDetailDto> lignesDetailDto = getDetail().getDetailList();
    	for (LigneDetailDto ligneDetailDto : lignesDetailDto) {
	    	if (ligneDetailDto.getEntete() != null && ligneDetailDto.getEntete().length() > 0) {
		    	ligne = new ArrayList<String>();
	    		ligne.add(ligneDetailDto.getEntete()); // entete
		    	lignes.add(ligne);
	    	}
	    	if (ligneDetailDto.getLigne() != null && ligneDetailDto.getLigne().length() > 0) {
		    	ligne = new ArrayList<String>();
		    	ligne.add(ligneDetailDto.getLigne()); // ligne
		    	lignes.add(ligne);
	    	}
		}

    	return lignes;
    }

    /**
     * Fonction retournant la date de génération (pour affichage en entête des impressions).
     * 
     * @return Date
     */
    public Date getDateGeneration() {
    	return new Date();
    }

    /**
     * Fonction appelée lors du clic sur le bouton Imprimer.
     * Fixe le mode impression dans la requête.
     * 
     * @return chemin de navigation
     */
    public String imprimer() {
    	try {
			// ajout du mode "print"
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
					.put("modeAffichage", "print");
	
			// redirection
			Map<String, String> params = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
	
			String typeRapport = params.get("typeRapport");
			if ("RECH_RAPPRO".equals(typeRapport)) {
				return OUTCOME_SUIVI;
			} else if ("DETAIL_RAPPRO".equals(typeRapport)) {
				return OUTCOME_REGROUPEMENTS;
			} else if ("DETAIL_REGROUP".equals(typeRapport)) {
				return OUTCOME_DETAIL;
			}
			return OUTCOME_SUIVI;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("SuiviBean.imprimer()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}
    
}
