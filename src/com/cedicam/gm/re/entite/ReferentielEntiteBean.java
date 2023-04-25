package com.cedicam.gm.re.entite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.model.selection.SimpleSelection;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.converter.EntiteGroupeConverter;
import com.cedicam.gm.bt.converter.MemberIdConverter;
import com.cedicam.gm.cm.data.dto.CritereEntiteDto;
import com.cedicam.gm.re.bean.EntiteServicesBean;
import com.cedicam.gm.re.data.dto.EntiteDto;
import com.cedicam.gm.re.dto.ExtendedEntiteDto;
import com.cedicam.gm.re.general.ReferencielGeneral;
import com.cedicam.gm.ui.core.DatatableManager;
import com.cedicam.gm.ui.core.EntiteDataProvider;
import com.cedicam.gm.ui.core.Exporter;
import com.cedicam.gm.ui.core.FiltreEntites;
import com.cedicam.gm.ui.core.GenericBean;

/**
 * @author Salim TAZI
 */
public class ReferentielEntiteBean extends GenericBean {
	/** Outcome entités : sélection. */
    private static final String OUTCOME_REFERENTIEL_ENTITES = "entites";
	/** Outcome entités : création. */
    private static final String OUTCOME_EDIT = "edition";
	/** Outcome entités : consultation. */
    private static final String OUTCOME_CONSULTATION = "consultation";
    /** Demande de confirmation. */
	private boolean confirmation = false;


	/** Le bean de gestion du référentiel entité. */
	private EntiteServicesBean entiteBean = null;
	/** Le manager de la liste d'entités. */
	private EntiteListSelectManager entiteListManager = null;
	/** Entité en cours de creation/modification le cas échéant. */
	private EntiteEditionBean entiteEdition;

	/** La liste des statuts de validite. */
	private List<SelectItem> listeValidites = new ArrayList<SelectItem>();

	/** Le statut de validité selectionné. */
	private Object selectedValidite = null;

	/** DatatableManager d'EntiteDto. */
    private DatatableManager<EntiteDto> entiteDtManager;

	/** Définition de la classe, comme un gros sale, au milieu du code. */
	public class SelectItemKeyComparator implements Comparator<SelectItem> {
		@Override
		public int compare(SelectItem o1, SelectItem o2) {
			return o1.getValue().toString().compareTo(o2.getValue().toString());
		}
	}
	/** Comparator pour trier la liste de statuts de validité. */
	private Comparator<SelectItem> comparator = new SelectItemKeyComparator();

	/** Constructeur. */
	public ReferentielEntiteBean() {
        super();
        entiteBean = (EntiteServicesBean) GenericBean.findBean("entiteservicesbean");
        entiteListManager = new EntiteListSelectManager(entiteBean);
        entiteDtManager = new DatatableManager<EntiteDto>(new EntiteDataProvider(), new EntiteDto());
        setEnteteComposant("GMREBN020", "Referentiel Entite Bean");
	}

	/**
	 * Point d'entrée : 1ère méthode appelée lors du clic sur le menu haut.
	 * 
	 * @return Retourne le bloc
	 */
	public String resetBloc() {
		try {
			entiteDtManager.reset();
	
	        initRecherche();
	        updateDatatable();
	
			return OUTCOME_REFERENTIEL_ENTITES;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.reset()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	/**
	 * Effectue le rechargement du tableau de résultat en conservant le contexte :
	 * - filtres
	 * - page courante
	 * 
	 * Appelé lors du clic sur le bouton retour lorsqu'on est sur une entité
	 * 
	 */
	public String refreshBloc() {
		return refreshBloc('R', null);
	}
	
	/**
	 * Effectue le rechargement du tableau de résultat en conservant le contexte :
	 * - filtres
	 * - page courante
	 * 
	 * Appelé par les méthodes de modification d'entité du bean
	 * 
	 * @param char mode : 'C' Création 'U' Update 'D' Delete 'R' Refresh
	 * @param EntiteDto : entité créée, modifiée ou supprimée
	 * 
	 */
	private String refreshBloc(final char aMode, final EntiteDto aEntiteModifiee) {
		try {
	        EntiteDataProvider dataProvider = (EntiteDataProvider) entiteDtManager.getDataProvider();
			// sauvegarde des filtres
	        FiltreEntites lFiltresCourant = new FiltreEntites();
	        lFiltresCourant.setCdBanqueEntAComptValue(dataProvider.getFilter().getCdBanqueEntAComptValue());
	        lFiltresCourant.setChefDeFileValue(dataProvider.getFilter().getChefDeFileValue());
	        lFiltresCourant.setCirc1CdBqReglCoreValue(dataProvider.getFilter().getCirc1CdBqReglCoreValue());
	        lFiltresCourant.setCirc1CdBqReglCupValue(dataProvider.getFilter().getCirc1CdBqReglCupValue());
	        lFiltresCourant.setCirc1CdBqReglInterValue(dataProvider.getFilter().getCirc1CdBqReglInterValue());
	        lFiltresCourant.setCirc1CdBqReglIntraValue(dataProvider.getFilter().getCirc1CdBqReglIntraValue());
	        lFiltresCourant.setCirc1CdBqReglMciValue(dataProvider.getFilter().getCirc1CdBqReglMciValue());
	        lFiltresCourant.setCirc1CdBqReglVisaValue(dataProvider.getFilter().getCirc1CdBqReglVisaValue());
	        lFiltresCourant.setClientExterneGroupeCaValue(dataProvider.getFilter().getClientExterneGroupeCaValue());
	        lFiltresCourant.setCodeBanqueValue(dataProvider.getFilter().getCodeBanqueValue());
	        lFiltresCourant.setCodeIcaMciValue(dataProvider.getFilter().getCodeIcaMciValue());
	        lFiltresCourant.setDtDbEffetValue(dataProvider.getFilter().getDtDbEffetValue());
	        lFiltresCourant.setDtFinEffetValue(dataProvider.getFilter().getDtFinEffetValue());
	        lFiltresCourant.setDtDeMigrEntSurBoaValue(dataProvider.getFilter().getDtDeMigrEntSurBoaValue());
	        lFiltresCourant.setDtDeMigrEntSurBoeValue(dataProvider.getFilter().getDtDeMigrEntSurBoeValue());
	        // sauvegarde de la pagination
	        final int lNumPageCourant = dataProvider.getScroller().getCurrentPage();
	        // sauvegarde occurence en cours
	        EntiteDto lEntiteDtoConcernee = entiteDtManager.getSelectedOccurrence();
	        if (aEntiteModifiee != null) {
	        	lEntiteDtoConcernee = aEntiteModifiee;
	        }
			// traitements
	        updateDatatable();
	
	        // restauration du contexte initial, si possible
	        dataProvider = (EntiteDataProvider) entiteDtManager.getDataProvider();
	        try {
	        	// restauration des filtres
	            dataProvider.getFilter().setCdBanqueEntAComptValue(lFiltresCourant.getCdBanqueEntAComptValue());
	            dataProvider.getFilter().setChefDeFileValue(lFiltresCourant.getChefDeFileValue());
	            dataProvider.getFilter().setCirc1CdBqReglCoreValue(lFiltresCourant.getCirc1CdBqReglCoreValue());
	            dataProvider.getFilter().setCirc1CdBqReglCupValue(lFiltresCourant.getCirc1CdBqReglCupValue());
	            dataProvider.getFilter().setCirc1CdBqReglInterValue(lFiltresCourant.getCirc1CdBqReglInterValue());
	            dataProvider.getFilter().setCirc1CdBqReglIntraValue(lFiltresCourant.getCirc1CdBqReglIntraValue());
	            dataProvider.getFilter().setCirc1CdBqReglMciValue(lFiltresCourant.getCirc1CdBqReglMciValue());
	            dataProvider.getFilter().setCirc1CdBqReglVisaValue(lFiltresCourant.getCirc1CdBqReglVisaValue());
	            dataProvider.getFilter().setClientExterneGroupeCaValue(lFiltresCourant.getClientExterneGroupeCaValue());
	            dataProvider.getFilter().setCodeBanqueValue(lFiltresCourant.getCodeBanqueValue());
	            dataProvider.getFilter().setCodeIcaMciValue(lFiltresCourant.getCodeIcaMciValue());
	            dataProvider.getFilter().setDtDbEffetValue(lFiltresCourant.getDtDbEffetValue());
	            dataProvider.getFilter().setDtFinEffetValue(lFiltresCourant.getDtFinEffetValue());
	            dataProvider.getFilter().setDtDeMigrEntSurBoaValue(lFiltresCourant.getDtDeMigrEntSurBoaValue());
	            dataProvider.getFilter().setDtDeMigrEntSurBoeValue(lFiltresCourant.getDtDeMigrEntSurBoeValue());
	            dataProvider.filtrer();
	            // restauration de la pagination
	            if (lNumPageCourant <= dataProvider.getScroller().getPageCount()) {
	            	dataProvider.getScroller().setCurrentPage(lNumPageCourant);
	            }
	            // restauration occurence en cours, sauf si suppression car n'existe plus
	            dataProvider = (EntiteDataProvider) entiteDtManager.getDataProvider();
	            if (aMode != 'D') {
	            	// mise à jour de la ligne
	            	// inutile car tout a été rechargé
	//                entiteDtManager.updateRow(lEntiteDto);
	//                if (aEntiteModifiee != null) {
	//                    entiteDtManager.updateRow(aEntiteModifiee);
	//                }
	                // sélection de l'occurence
	                //entiteDtManager.setSelectedObject(lEntiteDto);
	            	int lNbElementSurPage = dataProvider.getScroller().getPageSize();
	            	if (lNumPageCourant == dataProvider.getScroller().getPageCount()) {
	            		// cas de la dernière page incomplète
	            		lNbElementSurPage = dataProvider.getScroller().getNumberOfElements() - ((lNumPageCourant-1) * dataProvider.getScroller().getPageSize());
	            	}
	                List<EntiteDto> lEntitesDto = entiteDtManager.getDatatableModel().getDataProvider().getItemsByRange(0, lNbElementSurPage);
	                int i = 0;
	                for (EntiteDto entiteDto : lEntitesDto) {
						if (entiteDto.equals(lEntiteDtoConcernee)) {
							entiteDtManager.getDatatableModel().setRowKey(entiteDtManager.getDatatableModel().getDataProvider().getKey(entiteDto));
							entiteDtManager.getDatatableModel().setRowIndex(i);
							SimpleSelection lSelection = new SimpleSelection();
							lSelection.addKey(entiteDtManager.getDatatableModel().getDataProvider().getKey(entiteDto));
							entiteDtManager.setSelection(lSelection);
							break;
						}
						i++;
					}
	            }
	        } catch (Exception e) {
				// ce ne doit pas etre bloquant pour le chargement
			}
	
			return OUTCOME_REFERENTIEL_ENTITES;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.refreshBloc()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	/**
	 * Initialise la recherche :
	 * - prépare la liste déroulante des validités possibles
	 * - sélectionne la première dans la liste
	 */
	public void initRecherche() {
		ReferencielGeneral ref = ReferencielGeneral.getInstance();
		listeValidites = new ArrayList<SelectItem>(ReferencielGeneral.toSelectList(ref.getStatutsValidite()));
		Collections.sort(listeValidites, comparator);
		selectedValidite = listeValidites.get(0).getValue();
	}

	/**
	 * @param selectedValidite Le statut de validité selectionné.
	 * @return La liste d'entités correspondante.
	 */
	private List<EntiteDto> listerEntites(Object selectedValidite) {
		List<EntiteDto> listeEntites = entiteBean.rechercherEntites((String) selectedValidite);
		
		// On ne doit pas avoir le cas liste == null si tout se passe bien
		// si c'est le cas, l'erreur est traitée dans le bean
//		if (null == listeEntites) {
//			listeEntites = new ArrayList<EntiteDto>();
//		}

		return listeEntites;
	}

	/**
	 * Récupère les données et met à jour la datatable
	 * en se basant sur la validité sélectionnée dans la liste du haut.
	 * 
	 * Appelé depuis une autre méthode du bean si action sur l'entité. 
	 */
	private void updateDatatable() {
		List<EntiteDto> listeEntite = listerEntites(selectedValidite);
		EntiteDto[] arrayEntite = listeEntite.toArray(new EntiteDto[0]);

		entiteDtManager.reset();
		entiteDtManager.updateData(arrayEntite, arrayEntite.length);
	}

	/**
	 * Rafraîchit la datatable.
	 * @return l'outcome d'acces au referentiel.
	 */
	public String refresh() {
		return OUTCOME_REFERENTIEL_ENTITES;
	}
	
	/**
	 * modification de la validite.
	 * @param e L'action event.
	 */
	public void selectValidite(ActionEvent ae) {
		try {
			updateDatatable();
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.selectValidite()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
		}
	}

	/**
	 * Rafraichit l'entité sélectionnée.
	 */
	private void refreshSelected() {
		Integer codeBanque = entiteDtManager.getSelectedOccurrence().getCodeBanque();
		Date dateDebut = entiteDtManager.getSelectedOccurrence().getDtDbEffet();
		EntiteDto entite = entiteBean.rechercherEntite(codeBanque, dateDebut);
		if (null == entite) {
			entiteTrouvee = false;
		} else {
			entiteTrouvee = true;
			entiteDtManager.updateRow(entite);
		}
	}

	private boolean entiteTrouvee = false;
	
	/**
	 * Permet de basculer vers l'écran de consultation.
	 * @return L'outcome consultation.
	 */
	public String goConsulter() {
		try {
			refreshSelected();
			if (entiteTrouvee) {
				return OUTCOME_CONSULTATION;
			} else {
				return null;
			}
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.goConsulter()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	/**
	 * Permet de basculer vers l'écran de création.
	 * @return L'outcome création.
	 */
	public String goCreer() {
		try {
			entiteEdition = new EntiteEditionBean(getDateApplicationDto());
			entiteEdition.creer();
			List<EntiteDto> listeRef = entiteBean.rechercherEntites(ConstantesTypo.STATUT_VALIDITE_TOUS);
			List<CritereEntiteDto> chefsDeFile = entiteBean.listerChefsDefile();

			List<String> sChefsDeFile = new ArrayList<String>();
			for (CritereEntiteDto chefDeFile : chefsDeFile) {
				sChefsDeFile.add("" + chefDeFile.getEntiteEchange());
			}
			entiteListManager.refreshCreation(listeRef, sChefsDeFile);
			return OUTCOME_EDIT;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.goCreer()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}
	
	public void goCreer(ActionEvent e) {
		goCreer();
	}
	
	public void initialiser(ActionEvent e) {
		entiteEdition = null;
	}
	

	/**
	 * Permet de basculer vers l'écran de modification.
	 * @return L'outcome création.
	 */
	public String goModifier() {
		try {
			refreshSelected();
			entiteEdition = new EntiteEditionBean(getDateApplicationDto());
			entiteEdition.modifier(entiteDtManager.getSelectedOccurrence());
			
			List<EntiteDto> listeRef = entiteBean.rechercherEntites(ConstantesTypo.STATUT_VALIDITE_TOUS);
			List<CritereEntiteDto> chefsDeFile = entiteBean.listerChefsDefile();
			
			List<String> sChefsDeFile = new ArrayList<String>();
			for (CritereEntiteDto chefDeFile : chefsDeFile) {
				sChefsDeFile.add("" + chefDeFile.getEntiteEchange());
			}
			
			String [] paramsMessage = entiteEdition.verifierReferencementEntite(listeRef, entiteDtManager.getSelectedOccurrence());
			
			if (null != paramsMessage) {
				getBtmu10().addMessage(Btmu10.MSG_WARNING, "MSG3231", paramsMessage);
			}
			
			entiteListManager.refreshModification(listeRef, sChefsDeFile, entiteDtManager.getSelectedOccurrence());
					
			return OUTCOME_EDIT;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.goModifier()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	/**
	 * @return La liste des statuts de validité.
	 */
	public List<SelectItem> getListeValidites() {
		return listeValidites;
	}

	/**
	 * @param listeValidites La liste des statuts de validité.
	 */
	public void setListeValidites(List<SelectItem> listeValidites) {
		this.listeValidites = listeValidites;
	}

	/**
	 * @return La validité sélectionnée.
	 */
	public Object getSelectedValidite() {
		return selectedValidite;
	}

	/**
	 * @param selectedValidite La validité sélectionnée.
	 */
	public void setSelectedValidite(Object selectedValidite) {
		this.selectedValidite = selectedValidite;
	}

	/**
	 * @return Le manager de la datatable.
	 */
	public DatatableManager<EntiteDto> getEntiteDtManager() {
		return entiteDtManager;
	}

	/**
	 * Trie la liste sur la propriété renvoyée par l'action event.
	 * @param event L'action event.
	 * @return l'outcome d'acces au referentiel.
	 */
    public String trier(ActionEvent event) {
    	try {
	        String propertyName =  (String) event.getComponent().getAttributes().get("sortPropertyName");
	        entiteDtManager.getDataProvider().doSort(propertyName);
	        
	        return OUTCOME_REFERENTIEL_ENTITES;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.trier()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }

    /**
     * Filtre la liste en fonction des filtres renseignés.
     * @param event L'action event.
     * @return l'outcome d'acces au referentiel.
     */
   public String filtrer(ActionEvent event) {
	   try {
	        EntiteDataProvider dataProvider = (EntiteDataProvider) entiteDtManager.getDataProvider();
	        dataProvider.filtrer();
	        
	        return OUTCOME_REFERENTIEL_ENTITES;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.filtrer()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }

   /**
    * Affiche le popup de confirmation.
    * @return l'outcome nul.
    */
    public String afficheConfirmation() {
    	this.confirmation = true;
    	return null;
    }

    public void afficheConfirmationEdition(ActionEvent e) {
    	afficheConfirmationEdition();
    }
    
    /**
     * Affiche le popup de confirmation d'édition.
     * @return l'outcome d'édition.
     */
    public String afficheConfirmationEdition() {
    	try {
	    	this.confirmation = entiteEdition.validerFormulaireEditionEntite();
	    	if (confirmation) {
	    		return OUTCOME_EDIT;
	    	} else {
	    		return null;
	    	}
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.afficheConfirmationEdition()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
    }

    /**
     * Vérifie s'il y a besoin de confirmation.
     * @return s'il y a besoin de confirmation.
     */
	public boolean isConfirmation() {
		return confirmation;
	}

	/** 
	 * Définit s'il y a besoin de confirmation.
	 * @param confirmation d'il y a besoin de confirmation.
	 */
	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}

	/**
	 * Définit la demande de confirmation a faux.
	 * @return l'outcome null.
	 */
	public String continuerSaisie() {
		this.confirmation = false;
		return null;
	}
	
	/**
	 * Définit la demande de confirmation a faux.
	 * @param e Action event
	 */
	public void continuerSaisie(ActionEvent e) {
		continuerSaisie();
	}

	/**
	 * enregistrerEdition.
	 * @return l'outcome d'acces au referentiel si on est en création, l'outcome modification si on est en modification.
	 */
	public String enregistrerEdition() {
		try {
			this.confirmation = false;
			String retour = null;
			if (entiteEdition.isCreation()) {
				EntiteDto entiteCree = entiteBean.creerEntite(entiteEdition.getEntiteOriginale());
				if (entiteCree != null) {
					refreshBloc('C', entiteCree);
					//updateDatatable();
					retour = OUTCOME_REFERENTIEL_ENTITES;
				}
			} else {
				EntiteDto modifiee = entiteEdition.getEntiteOriginale();
				ExtendedEntiteDto originale = new ExtendedEntiteDto(entiteDtManager.getSelectedOccurrence());
				
				if (!originale.allPropertiesEqual(modifiee)) {
					EntiteDto entiteModifiee = entiteBean.modifierEntite(modifiee, 
							entiteEdition.getDtDbEffetOrigine());
					if (entiteModifiee != null) {
						updateDatatable();
						retour = OUTCOME_REFERENTIEL_ENTITES;
					}
				} else {
					getBtmu10().addMessage(Btmu10.MSG_WARNING, "MSG3230");
				}
			}
			return retour;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.enregistrerEdition()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	/**
	 * supprimer.
	 * @return l'outcome d'acces au referentiel.
	 */
	public String supprimer() {
		try {
			EntiteDto dto = entiteDtManager.getSelectedObject();
			EntiteDto res = entiteBean.supprimerEntite(dto);
			if (null != res) {
				refreshBloc('D', res);
				//updateDatatable();
			}
			this.confirmation = false;
			return OUTCOME_REFERENTIEL_ENTITES;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.supprimer()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

	/**
	 * getEntiteListManager.
	 * @return l'entiteListManager.
	 */
	public EntiteListSelectManager getEntiteListManager() {
		return entiteListManager;
	}
	
	/**
	 * setEntiteListManager.
	 * @param entiteListManager l'entiteListManager.
	 */
	public void setEntiteListManager(EntiteListSelectManager entiteListManager) {
		this.entiteListManager = entiteListManager;
	}

	/**
	 * getEntiteEdition.
	 * @return le bean d'édition d'entités.
	 */
	public EntiteEditionBean getEntiteEdition() {
		return entiteEdition;
	}

	/**
	 * Point d'entrée de la génération.
	 * Les paramètres sont récupérés dans le contexte d'appel :
	 * - format de génération: CSV ou XLS
	 * - type de rapport à générer: RECH_ENTITE, 
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
	 * @param type de rapport à générer: RECH_ENTITE, 
	 */
	public void genererRapport(final String format, final String typeRapport){
		// Générations CSV
		if (Exporter.FORMAT_CSV.equals(format)) {
			if ("RECH_ENTITE".equals(typeRapport)) {
			Exporter.genererRapportCSV(genererDonneesExportRechercheEntite());
			}
		}
	   }
	
	/**
	 * Méthode de construction des données pour l'export CSV.
	 * Refabrique les données visibles sur l'écran EC020.
	 * 
	 * @return ArrayList<ArrayList<String>>
	 */
    protected ArrayList<ArrayList<String>> genererDonneesExportRechercheEntite() {
    	final String SEPARATEUR_COLONNE = "";
    	ArrayList<ArrayList<String>> lignes = new ArrayList<ArrayList<String>>();
    	
    	ArrayList<String> ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el002"));
    	lignes.add(ligne);
    	lignes.add(new ArrayList<String>());
    	
    	// Critères de recherche
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el002"));
    	lignes.add(ligne);

    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el002")); // entite
    	ligne.add(Exporter.recupererLabelDansListe(""+getSelectedValidite(),getListeValidites()));
    	lignes.add(ligne);

    	lignes.add(new ArrayList<String>());
    	// Tableau résultat
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec050.el001"));
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(SEPARATEUR_COLONNE);
    	ligne.add(GenericBean.recupererMessage("wn.commun.nombre.de.resultats"));
    	ligne.add(""+getEntiteDtManager().getDataProvider().getTotalResultSize());
    	lignes.add(ligne);

    	// Tableau résultat - Filtres
    	EntiteDataProvider dataProvider = (EntiteDataProvider) entiteDtManager.getDataProvider();
    	FiltreEntites filter = dataProvider.getFilter();
    	ligne = new ArrayList<String>();
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCodeBanqueValue(), filter.getCodeBanqueSelectItemList())); // entite echg
    	ligne.add(Exporter.recupererLabelDansListe(filter.getClientExterneGroupeCaValue(), filter.getClientExterneGroupeCaSelectItemList())); // entite grp
    	ligne.add(Exporter.recupererLabelDansListe(filter.getChefDeFileValue(), filter.getChefDeFileSelectItemList())); // chef de file
    	ligne.add(Exporter.recupererLabelDansListe(filter.getDtDeMigrEntSurBoaValue(), filter.getDtDeMigrEntSurBoaSelectItemList())); // date migr BOA
    	ligne.add(Exporter.recupererLabelDansListe(filter.getDtDeMigrEntSurBoeValue(), filter.getDtDeMigrEntSurBoeSelectItemList())); // date migr BOE
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCdBanqueEntAComptValue(), filter.getCdBanqueEntAComptSelectItemList())); // entite a compta
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCodeIcaMciValue(), filter.getCodeIcaMciSelectItemList())); // member id MCI
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCirc1CdBqReglInterValue(), filter.getCirc1CdBqReglInterSelectItemList())); // reglt inter
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCirc1CdBqReglIntraValue(), filter.getCirc1CdBqReglIntraSelectItemList())); // reglt intra
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCirc1CdBqReglCoreValue(), filter.getCirc1CdBqReglCoreSelectItemList())); // reglt core
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCirc1CdBqReglCupValue(), filter.getCirc1CdBqReglCupSelectItemList())); // reglt cup
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCirc1CdBqReglMciValue(), filter.getCirc1CdBqReglMciSelectItemList())); // reglt mci
    	ligne.add(Exporter.recupererLabelDansListe(filter.getCirc1CdBqReglVisaValue(), filter.getCirc1CdBqReglVisaSelectItemList())); // reglt visa
    	ligne.add(Exporter.recupererLabelDansListe(filter.getDtDbEffetValue(), filter.getDtDbEffetSelectItemList())); // date deb effet
    	ligne.add(Exporter.recupererLabelDansListe(filter.getDtFinEffetValue(), filter.getDtFinEffetSelectItemList())); // date fin effet

    	lignes.add(ligne);
    	// Tableau résultat - Entêtes
    	ligne = new ArrayList<String>();
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el004")); // entite echg
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el005")); // entite grp
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el006")); // chef de file
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el007")); // date migr BOA
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el008")); // date migr BOE
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el009")); // entite a compta
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el010")); // member id MCI
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el011")); // reglt inter
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el012")); // reglt intra
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el013")); // reglt core
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el014")); // reglt cup
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el015")); // reglt mci
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el016")); // reglt visa
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el017")); // date deb effet
    	ligne.add(GenericBean.recupererMessage("gm.ec020.el018")); // date fin effet
    	lignes.add(ligne);

    	// Tableau résultat - Contenu
    	//List<EntiteDto> entitesDto = getEntiteDtManager().getDataManager().getData();
    	// correction 07/10/2013: il faut exporter toutes les données en fonction des filtres mais pour toutes les pages
    	// on refait donc la méthode de filtre de FiltreEntites.java ici
    	List<EntiteDto> entitesDto = filtrer(getEntiteDtManager().getDataManager().getFullData());
    	for (EntiteDto entiteDto : entitesDto) {
        	ligne = new ArrayList<String>();
        	ligne.add(""+entiteDto.getCodeBanque()); // entite echg
        	ligne.add(EntiteGroupeConverter.getAsString(entiteDto.getClientExterneGroupeCa())); // entite grp
        	ligne.add((entiteDto.getChefDeFile()==null)?"":""+entiteDto.getChefDeFile()); // chef de file
        	ligne.add(Exporter.formaterDateHeure(entiteDto.getDtDeMigrEntSurBoa())); // date migr BOA
        	ligne.add(Exporter.formaterDateHeure(entiteDto.getDtDeMigrEntSurBoe())); // date migr BOE
        	ligne.add((entiteDto.getCdBanqueEntACompt()==null)?"":""+entiteDto.getCdBanqueEntACompt()); // entite a compta
        	ligne.add((entiteDto.getCodeIcaMci()==null)?"":""+MemberIdConverter.getAsString(entiteDto.getCodeIcaMci())); // member id MCI ***
        	ligne.add((entiteDto.getCirc1CdBqReglInter()==null)?"":""+entiteDto.getCirc1CdBqReglInter()); // reglt inter
        	ligne.add((entiteDto.getCirc1CdBqReglIntra()==null)?"":""+entiteDto.getCirc1CdBqReglIntra()); // reglt intra
        	ligne.add((entiteDto.getCirc1CdBqReglCore()==null)?"":""+entiteDto.getCirc1CdBqReglCore()); // reglt core
        	ligne.add((entiteDto.getCirc1CdBqReglCup()==null)?"":""+entiteDto.getCirc1CdBqReglCup()); // reglt cup
        	ligne.add((entiteDto.getCirc1CdBqReglMci()==null)?"":""+entiteDto.getCirc1CdBqReglMci()); // reglt mci
        	ligne.add((entiteDto.getCirc1CdBqReglVisa()==null)?"":""+entiteDto.getCirc1CdBqReglVisa()); // reglt visa
        	ligne.add(Exporter.formaterDate(entiteDto.getDtDbEffet())); // date deb effet
        	ligne.add(Exporter.formaterDate(entiteDto.getDtFinEffet())); // date fin effet
        	lignes.add(ligne);
		}

    	return lignes;
    }

	/**
	 * Filtre la liste en fonction des filtres définis.
	 * RECUPERE de FiltreEntites.java
	 * 
	 * Correction 07/10/2013: il faut exporter toutes les données en fonction des filtres mais pour toutes les pages
	 * on refait donc la méthode de filtre de FiltreEntites.java ici
	 * 
	 * @param fullList La liste complète d'entités.
	 * @return La liste triée d'entités.
	 */
	private ArrayList<EntiteDto> filtrer(List<EntiteDto> fullList) {
		ArrayList<EntiteDto> filteredData = new ArrayList<EntiteDto>();
		for (EntiteDto dto: fullList) {
			Map<String, Object> props = getPropsValueMap(dto);

			// ajout SCA
	    	EntiteDataProvider dataProvider = (EntiteDataProvider) entiteDtManager.getDataProvider();
	    	FiltreEntites filter = dataProvider.getFilter();
	    	Map<String, Object> filtreCourant = filter.getFiltreCourant();
	    	// fin ajout SCA
	    	
			boolean keep = true;
			for (String key: filtreCourant.keySet()) {
				Object constraint = filtreCourant.get(key);
				Object rowvalue = props.get(key);
				if ((null == constraint && null != rowvalue) || (null != constraint && !constraint.equals(rowvalue))) {
					keep = false;
					break;
				}
			}

			if (keep) {
				filteredData.add(dto);
			}
		}

		// SCA refresh(filteredData);
		return filteredData;
	}

	/**
	 * Pour un DTO donné, construit l'association nom de propriété / valeur.
	 * RECUPERE de FiltreEntites.java
	 * 
	 * @param dto Le DTO
	 * @return La map d'association
	 */
	private Map<String, Object> getPropsValueMap(EntiteDto dto) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("cdBanqueEntACompt", dto.getCdBanqueEntACompt());
        map.put("chefDeFile", dto.getChefDeFile());
        map.put("circ1CdBqReglCore", dto.getCirc1CdBqReglCore());
        map.put("circ1CdBqReglCup", dto.getCirc1CdBqReglCup());
        map.put("circ1CdBqReglInter", dto.getCirc1CdBqReglInter());
        map.put("circ1CdBqReglIntra", dto.getCirc1CdBqReglIntra());
        map.put("circ1CdBqReglMci", dto.getCirc1CdBqReglMci());
        map.put("circ1CdBqReglVisa", dto.getCirc1CdBqReglVisa());
        map.put("clientExterneGroupeCa", dto.getClientExterneGroupeCa());
        map.put("codeBanque", dto.getCodeBanque());
        map.put("codeIcaMci", dto.getCodeIcaMci());
        map.put("dateCreation", dto.getDateCreation());
        map.put("dateMaj", dto.getDateMaj());
        map.put("dtDbEffet", dto.getDtDbEffet());
        map.put("dtDeMigrEntSurBoa", dto.getDtDeMigrEntSurBoa());
        map.put("dtDeMigrEntSurBoe", dto.getDtDeMigrEntSurBoe());
        map.put("dtFinEffet", dto.getDtFinEffet());
        map.put("envImCrrEntComp", dto.getEnvImCrrEntComp());
        map.put("envImCrrEntEch", dto.getEnvImCrrEntEch());
        map.put("libelle", dto.getLibelle());
        map.put("userCreation", dto.getUserCreation());
        map.put("userMaj", dto.getUserMaj());
        map.put("version", dto.getVersion());

        return map;
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
			if ("RECH_ENTITE".equals(typeRapport)) {
				return OUTCOME_REFERENTIEL_ENTITES;
			}
			return OUTCOME_REFERENTIEL_ENTITES;
		} catch(Exception e) {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG3500");
			getTraceService().enregistrerTrace("ReferentielEntiteBean.imprimer()", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, 
					ConstantesTrace.CATEGORIE_DEBUG, ConstantesTrace.GRAVITE_WARN, 
					"MSG0089", null, e, getEnteteComposant(), getContexteCedicam());
			return null;
		}
	}

    /**
     * Définit l'entiteEdition.
     * @param entiteEdition Une entiteEdition
     */
	public void setEntiteEdition(EntiteEditionBean entiteEdition) {
		this.entiteEdition = entiteEdition;
	}

}
