package com.cedicam.gm.cm.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.ch.bean.menus.St000Bean;
import com.cedicam.gm.cm.data.dto.CoupleBoCircuitDto;
import com.cedicam.gm.cm.data.dto.CriteresPourSuiviDto;
import com.cedicam.gm.cm.data.dto.EcartsRapprochementDto;
import com.cedicam.gm.ui.core.EcartSearchManager;
import com.cedicam.gm.ui.core.Exporter;
import com.cedicam.gm.ui.core.GenericBean;

public class EcartBean extends GenericBean {

	EcartSearchManager searchManager;
	BusinessServicesBean businessServicesBean;
	Integer matriceNombreEcarts[][] = new Integer[2][7];
	/** afficherSynthese. */
	private boolean afficheSynthese = false;
	private boolean autoriseRechercher = true;

	private static final String OUTCOME_ECARTS = "ecarts";
	
	public EcartBean() {
		searchManager = new EcartSearchManager();
		searchManager.reset(this.getDateApplication());
		//System.out.println("date application ecarts : " + this.getDateApplication());
		businessServicesBean = (BusinessServicesBean)  GenericBean.findBean("businessServicesBean");
		//this.setCodeEcran("ec100");
		setEnteteComposant("GMCMBN100", "Ecart Bean");
	}

	public String resetBloc(){
		return OUTCOME_ECARTS;
	}
	
	public void majBoCirc(ActionEvent e) {
		if (e != null) {
			Object bo = e.getComponent().getAttributes().get("bo");
			Object circ = e.getComponent().getAttributes().get("circ");
			
			CriteresPourSuiviDto mesCriteres = searchManager.getCriteres();
			mesCriteres.setBackOffice(bo.toString());
			mesCriteres.setReseau(circ.toString());
			mesCriteres.setStatutRapprochement(ConstantesTypo.STATUT_RAPPROCHEMENT_ECART);
			searchManager.setCriteres(mesCriteres);
		}
	}
	
	public String lanceSuivi() {
		
		String retour = null;
		St000Bean st000 = (St000Bean) GenericBean.findBean("st000");
		if (st000 != null && st000.getHabF0010()) {
			SuiviBean sv = (SuiviBean) GenericBean.findBean("suivibean");
			if (sv != null) {
				retour = sv.rechercherEcart(searchManager.getCriteres()); 
			}
		}
		return retour;
	}
	
	/**
     * Cette méthode réinitialise avec les valeurs par défaut les critères secondaire du formulaire de recherche
     * @return
     */
    public String retablir(){
    	try {
	        searchManager.resetSecondaires();
	        searchManager.getCriteres().setEntiteCompta(null);
	        searchManager.getCriteres().setEntiteEchange(null);
	        searchManager.updatePrimaryCriteresWithRequestParameters();
	        lancerRecherche();
	        return OUTCOME_ECARTS;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("EcartBean.retablir()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }
    
    public String rechercher() {
    	try {
	    	boolean success = searchManager.updateCriteresWithRequestParameters();
	    	if (success) {
	    		lancerRecherche();
	    		return OUTCOME_ECARTS;
	    	}
	    	return null;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("EcartBean.rechercher()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }
    
    private void lancerRecherche() {
    	EcartsRapprochementDto ecartDto = businessServicesBean.syntheseEcarts(searchManager.getCriteres());
    	matriceNombreEcarts = new Integer[2][7];
    	if (ecartDto != null && ecartDto.getListeCoupleBoCircuitDto() != null) {
    		searchManager.loadResultatRecherche(ecartDto);
    		initMatriceEcarts(ecartDto.getListeCoupleBoCircuitDto());
    		this.afficheSynthese = true;
    	} else {
    		this.afficheSynthese = false;
    	}
/*    	for (int i = 0; i < 2; i++) {
    		for (int j = 0; j < 6; j++) {
    			System.out.println("[" + i + "][" + j + "] = " + matriceNombreEcarts[i][j]);
    		}
    	}*/
    	
    }
    
    public String initialiser() {
    	searchManager.resetSecondaires();
    	searchManager.initPrimaryCriteres(getDateApplication());
    	autoriseRechercher = true;
    	this.afficheSynthese = false;
    	return resetBloc();
    }
    
    public void changeCritPrimaires(ValueChangeEvent e) {
    	chgPrimaires();
    }
    
    public void changeCritPrimaires(ActionEvent e) {
    	chgPrimaires();
    }
    
    private void chgPrimaires() {
//    	commenté car la validation des champs se fait désormais sur validation du formulaire et non petit à petit
//    	if (!searchManager.updatePrimaryCriteresWithRequestParameters()) {
//    		autoriseRechercher  = false;
//    	} else {
//    		autoriseRechercher = true;
//    	}
	    this.afficheSynthese = false;
    }
    
    private void initMatriceEcarts(CoupleBoCircuitDto[] tableau) {
    	for (int i = 0; i < tableau.length; i++) {
    		if (null != tableau[i]) {
	    		String bo = tableau[i].getBo();
	    		int circuit = tableau[i].getIdCircuit();
	    		
	    		if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOA)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_1_BQ_REGL_INTER)) {
	    			matriceNombreEcarts[0][0] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOA)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_2_BQ_REGL_INTRA)) {
	    			matriceNombreEcarts[0][1] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOA)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_3_BQ_REGL_CORE)) {
	    			matriceNombreEcarts[0][2] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOA)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_4_BQ_REGL_CUP)) {
	    			matriceNombreEcarts[0][3] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOA)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_5_BQ_REGL_MCI)) {
	    			matriceNombreEcarts[0][4] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOA)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_6_BQ_REGL_VISA)) {
	    			matriceNombreEcarts[0][5] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOA)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_0_INDETERMINE)) {
	    			matriceNombreEcarts[0][6] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOE)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_1_BQ_REGL_INTER)) {
	    			matriceNombreEcarts[1][0] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOE)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_2_BQ_REGL_INTRA)) {
	    			matriceNombreEcarts[1][1] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOE)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_3_BQ_REGL_CORE)) {
	    			matriceNombreEcarts[1][2] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOE)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_4_BQ_REGL_CUP)) {
	    			matriceNombreEcarts[1][3] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOE)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_5_BQ_REGL_MCI)) {
	    			matriceNombreEcarts[1][4] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOE)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_6_BQ_REGL_VISA)) {
	    			matriceNombreEcarts[1][5] = tableau[i].getNbEcarts();
	    		} else if (bo.equals(ConstantesTypo.APPLICATION_EMETTRICE_BOE)
	    				&& circuit == Integer.valueOf(ConstantesTypo.CIRCUIT_0_INDETERMINE)) {
	    			matriceNombreEcarts[1][6] = tableau[i].getNbEcarts();
	    		}
    		}
    	}
    }
    
    public String reset() {
    	try {
	    	searchManager.reset(getDateApplication());
	    	this.afficheSynthese = false;
	    	return OUTCOME_ECARTS;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("EcartBean.reset()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }

	/**
	 * Point d'entrée de la génération.
	 * Les paramètres sont récupérés dans le contexte d'appel :
	 * - format de génération: CSV ou XLS
	 * - type de rapport à générer: RECH_ECART 
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
	 * @param type de rapport à générer: RECH_ECART
	 */
	public void genererRapport(final String format, final String typeRapport){
		// Générations CSV
		if (Exporter.FORMAT_CSV.equals(format)) {
			if ("RECH_ECART".equals(typeRapport)) {
			Exporter.genererRapportCSV(genererDonneesExportRechercheEcart());
			}
		}
	   }

	/**
	 * Méthode de construction des données pour l'export CSV.
	 * Refabrique les données visibles sur les écrans EC090, EC100 et EC110.
	 * 
	 * @return ArrayList<ArrayList<String>>
	 */
    protected ArrayList<ArrayList<String>> genererDonneesExportRechercheEcart() {
    	final String SEPARATEUR_COLONNE = "";
    	ArrayList<ArrayList<String>> lignes = new ArrayList<ArrayList<String>>();
    	
    	ArrayList<String> ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el000"));
    	lignes.add(ligne);
    	lignes.add(new ArrayList<String>());
    	
    	// critères
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el001"));
    	lignes.add(ligne);
   	
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el002-a")); // Date heure echg
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateDebutEchange()));
    	ligne.add(getSearchManager().getHeureEchangeDebut());
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el004-a")); // date conv reference
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateConvRef()));
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el004-b")); // au
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateFinConvRef()));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el002-b")); // au
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateFinEchange()));
    	ligne.add(getSearchManager().getHeureEchangeFin());
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el005-a")); // date règlt du
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateDebutReglement()));
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el005-b")); // au
    	ligne.add(Exporter.formaterDate(getSearchManager().getCriteres().getDateFinReglement()));
    	lignes.add(ligne);

       	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el006")); // chef de file
    	ligne.add(Exporter.recupererLabelDansListe((getSearchManager().getCriteres().getChefDeFile()==null)?"":""+getSearchManager().getCriteres().getChefDeFile(),getSearchManager().getListChefDeFile()));
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("gm.ec100.el003")); // devise reglt
    	ligne.add(Exporter.recupererLabelDansListe(getSearchManager().getCriteres().getDevRegCompt(),getSearchManager().getListDevise()));
    	lignes.add(ligne);

    	lignes.add(new ArrayList<String>());
    	// résultat
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el001"));
    	lignes.add(ligne);
    	
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage(""));
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el002-a"));
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el002-b"));
    	lignes.add(ligne);

    	Integer[][] matrice = getMatriceNombreEcarts();
    	// dans la suite, on utilise :
    	// gm.param.statut.6 pour Sans écart
    	// gm.param.statut.7 pour Ecart
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el003-a")); // inter
    	ligne.add((matrice[0][0] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	ligne.add((matrice[1][0] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el003-b")); // intra
    	ligne.add((matrice[0][1] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	ligne.add((matrice[1][1] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el003-c")); // core
    	ligne.add((matrice[0][2] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	ligne.add((matrice[1][2] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el003-d")); // cup
    	ligne.add((matrice[0][3] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	ligne.add((matrice[1][3] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el003-e")); // mci
    	ligne.add((matrice[0][4] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	ligne.add((matrice[1][4] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el003-f")); // visa
    	ligne.add((matrice[0][5] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	ligne.add((matrice[1][5] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec110.el003-g")); // indetermine
    	ligne.add((matrice[0][6] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	ligne.add((matrice[1][6] == null)?GenericBean.recupererMessage("gm.param.statut.6"):GenericBean.recupererMessage("gm.param.statut.7"));
    	lignes.add(ligne);

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
			if ("RECH_ECART".equals(typeRapport)) {
				return OUTCOME_ECARTS;
			}
			return OUTCOME_ECARTS;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("EcartBean.imprimer()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

    public EcartSearchManager getSearchManager() {
		return searchManager;
	}

	public void setSearchManager(EcartSearchManager searchManager) {
		this.searchManager = searchManager;
	}

	public BusinessServicesBean getBusinessServicesBean() {
		return businessServicesBean;
	}

	public void setBusinessServicesBean(BusinessServicesBean businessServicesBean) {
		this.businessServicesBean = businessServicesBean;
	}

	public boolean isAfficheSynthese() {
		return afficheSynthese;
	}

	public void setAfficheSynthese(boolean afficheSynthese) {
		this.afficheSynthese = afficheSynthese;
	}

	public Integer[][] getMatriceNombreEcarts() {
		return matriceNombreEcarts;
	}

	public void setMatriceNombreEcarts(Integer[][] matriceNombreEcarts) {
		this.matriceNombreEcarts = matriceNombreEcarts;
	}

	public boolean isAutoriseRechercher() {
		return autoriseRechercher;
	}

	public void setAutoriseRechercher(boolean autoriseRechercher) {
		this.autoriseRechercher = autoriseRechercher;
	}
}
