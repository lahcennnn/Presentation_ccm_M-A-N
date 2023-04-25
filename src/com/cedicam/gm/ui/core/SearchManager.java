package com.cedicam.gm.ui.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.bt.data.dto.GenericDto;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.cm.data.dto.CriteresPourSuiviDto;
import com.cedicam.gm.re.bean.EntiteServicesBean;
import com.cedicam.gm.re.general.ReferencielGeneral;

public abstract class SearchManager {
	protected CriteresPourSuiviDto criteres;
	public static final String CODE_INIT_DT_DEBUT = "gm.commun.initHeureEchangeDebut";
	public static final String CODE_INIT_DT_FIN = "gm.commun.initHeureEchangeFin";

	public void setCriteres(CriteresPourSuiviDto criteres) {		this.criteres = criteres;	}
	public CriteresPourSuiviDto getCriteres() {		return criteres;	}

	private String focus="";
	public String getFocus() {
		return focus;
	}
	public void setFocus(String focus) {
		this.focus = focus;
	}
	public abstract boolean isCriteresSecondaires();
	public abstract void loadResultatRecherche(GenericDto dto);


	protected Date getDate(Date d, String hhmm) {

		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		if(hhmm==null || "".equals(hhmm.trim())){
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
		}
		else {
			Calendar c2 = Calendar.getInstance();
			try {
				c2.setTime(FormValidationUtil.SDF_HHMM.parse(hhmm));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
			c.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
		}
		return c.getTime();
	}

	public static String FORM_NAME = "formrecherche:";
	public static Date stringToDate(String str){
		if(FormValidationUtil.empty(str)) return null;
		try {
			return FormValidationUtil.SDF_JJMMYYYY.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}
	public static Date stringToDateSoir(String str){
		Date date  = stringToDate(str);
		if(date==null) return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}


	public void initPrimaryCriteres(Date dateApplication){
		//Date tmpDate = dateApplication;
		//if(null==tmpDate) tmpDate = new Date();
		Map<String, String> props = ApplicationContextHolder.getProjectProperties();

		heureEchangeDebut = props.get(CODE_INIT_DT_DEBUT);
		heureEchangeFin = props.get(CODE_INIT_DT_FIN);

		criteres.setDateDebutEchange(dateApplication);
		criteres.setDateFinEchange(dateApplication);
		criteres.setDateDebutConvRef(null);
		criteres.setDateFinConvRef(null);
		criteres.setDateDebutReglement(null);
		criteres.setDateFinReglement(null);

		criteres.setStatutRapprochement(CriteresPourSuiviDto.TOUS);
		criteres.setDevRegCompt(CriteresPourSuiviDto.TOUS);
		criteres.setReseau(CriteresPourSuiviDto.TOUS);
		criteres.setSens(CriteresPourSuiviDto.TOUS);
		criteres.setBackOffice(CriteresPourSuiviDto.TOUS);

		criteres.setChefDeFile(null);
		criteres.setForce(null);

		initSecondaryCriteres();
	}

	public void initSecondaryCriteres(){


		criteres.setEntiteEchange(CriteresPourSuiviDto.TOUS);
		criteres.setEntiteEchangeE(CriteresPourSuiviDto.TOUS);
		criteres.setEntiteEchangeD(CriteresPourSuiviDto.TOUS);
		criteres.setEntiteCompta(CriteresPourSuiviDto.TOUS);
		criteres.setEntiteComptaE(CriteresPourSuiviDto.TOUS);
		criteres.setEntiteComptaD(CriteresPourSuiviDto.TOUS);

		criteres.setIdFichierRefRemise(CriteresPourSuiviDto.TOUS);
		criteres.setCodeOpeNfcca(CriteresPourSuiviDto.TOUS);
		criteres.setReferenceComptable(CriteresPourSuiviDto.TOUS);

	}



	public void reset(Date dateApplication){
		criteres = new CriteresPourSuiviDto();
		initPrimaryCriteres(dateApplication);
		ReferencielGeneral ref = ReferencielGeneral.getInstance();
		reset(listReseau, (ReferencielGeneral.toSelectList(ref.getReseaux())),TOUS);
		reset(listDevise, (ReferencielGeneral.toSelectList(ref.getDevises())),TOUTES);
		reset(listBO, (ReferencielGeneral.toSelectList(ref.getBackOffices())),TOUS);
		reset(listSens, (ReferencielGeneral.toSelectList(ref.getSens())),TOUS);
		reset(listForcage, (ReferencielGeneral.toSelectList(ref.getForcage())),TOUS);
		reset(listStatut, (ReferencielGeneral.toSelectList(ref.getStatutsRapprochement())),TOUS);


		EntiteServicesBean refEntites = (EntiteServicesBean) GenericBean.findBean("entiteservicesbean");
		reset(listChefDeFile, EntiteServicesBean.toSelectItemList(refEntites.listerChefsDefile()),TOUS);
	}

	protected static String TOUS = "- Tous -";
	protected static String TOUTES = "- Toutes -";
	protected void reset(List<SelectItem> targetList, Collection<SelectItem> srcList, String tousToutes){
		targetList.clear();
		if(srcList!=null)targetList.addAll(srcList);
		Collections.sort(targetList, comparator);
		targetList.add(0, new SelectItem(CriteresPourSuiviDto.TOUS, tousToutes));
	}

	/**********************************************************************************************************/
	/************************************** TRIS DES LISTES DE SELECT ITEM ********************************************/
	/**********************************************************************************************************/
	public class SelectItemLabelComparator implements Comparator<SelectItem>{
		@Override
		public int compare(SelectItem o1, SelectItem o2) {
			return o1.getLabel().compareTo(o2.getLabel());
		}
	}
	private Comparator<SelectItem> comparator = new SelectItemLabelComparator();



	/**********************************************************************************************************/
	/************************************** CRITERES PRIMAIRES ********************************************/
	/**********************************************************************************************************/
	protected String heureEchangeDebut="";
	protected String heureEchangeFin="";

	protected  List<SelectItem> listChefDeFile = new ArrayList<SelectItem>();

	protected List<SelectItem> listForcage = new ArrayList<SelectItem>();
	protected List<SelectItem> listReseau = new ArrayList<SelectItem>();
	protected List<SelectItem> listSens = new ArrayList<SelectItem>();
	protected List<SelectItem> listBO = new ArrayList<SelectItem>();
	protected List<SelectItem> listStatut = new ArrayList<SelectItem>();
	protected List<SelectItem> listDevise = new ArrayList<SelectItem>();

	public List<SelectItem> getListChefDeFile() {return listChefDeFile;}


	public String getHeureEchangeDebut() {		return heureEchangeDebut;}
	public void setHeureEchangeDebut(String heureEchangeDebut) {		this.heureEchangeDebut = heureEchangeDebut;	}
	public String getHeureEchangeFin() {		return heureEchangeFin;	}
	public void setHeureEchangeFin(String heureEchangeFin) {		this.heureEchangeFin = heureEchangeFin;	}


	public List<SelectItem> getListForcage() {
		return listForcage;
	}
	public List<SelectItem> getListReseau() {		return listReseau;	}
	public List<SelectItem> getListSens() {		return listSens;	}
	public List<SelectItem> getListBO() {		return listBO;	}
	public List<SelectItem> getListStatut() {		return listStatut;	}
	public List<SelectItem> getListDevise() {		return listDevise;	}


	/**********************************************************************************************************/
	/************************************** CRITERES SECONDAIRES********************************************/
	/**********************************************************************************************************/
	protected  List<SelectItem> listEntiteEchange = new ArrayList<SelectItem>();
	protected  List<SelectItem> listEntiteEchangeEmettrice = new ArrayList<SelectItem>();
	protected  List<SelectItem> listEntiteEchangeDestinataire = new ArrayList<SelectItem>();
	protected  List<SelectItem> listEntiteCompta = new ArrayList<SelectItem>();
	protected  List<SelectItem> listEntiteComptaEmettrice = new ArrayList<SelectItem>();
	protected  List<SelectItem> listEntiteComptaDestinataire = new ArrayList<SelectItem>();
	protected  List<SelectItem> listReferenceComptable = new ArrayList<SelectItem>();
	protected  List<SelectItem> listCodeOperationNfcca = new ArrayList<SelectItem>();
	protected  List<SelectItem> listFichierReferenceRemise = new ArrayList<SelectItem>();

	public List<SelectItem> getListEntiteEchange() {return listEntiteEchange;}
	public List<SelectItem> getListEntiteEchangeEmettrice() {return listEntiteEchangeEmettrice;}
	public List<SelectItem> getListEntiteEchangeDestinataire() {return listEntiteEchangeDestinataire;}
	public List<SelectItem> getListEntiteCompta() {return listEntiteCompta;}
	public List<SelectItem> getListEntiteComptaEmettrice() {return listEntiteComptaEmettrice;}
	public List<SelectItem> getListEntiteComptaDestinataire() {return listEntiteComptaDestinataire;}
	public List<SelectItem> getListReferenceComptable() {return listReferenceComptable;}
	public List<SelectItem> getListCodeOperationNfcca() {return listCodeOperationNfcca;}
	public List<SelectItem> getListFichierReferenceRemise() {return listFichierReferenceRemise;}



	public void resetSecondaires(){
		reset(listEntiteEchange,null,TOUTES);
		reset(listEntiteEchangeEmettrice,null,TOUTES);
		reset(listEntiteEchangeDestinataire,null,TOUTES);
		reset(listEntiteCompta,null,TOUTES);
		reset(listEntiteComptaEmettrice,null,TOUTES);
		reset(listEntiteComptaDestinataire,null,TOUTES);
		reset(listReferenceComptable,null,TOUTES);
		reset(listCodeOperationNfcca,null,TOUS);
		reset(listFichierReferenceRemise,null,TOUTES);

		criteres.setEntiteEchange(null);
		criteres.setEntiteEchangeD(null);
		criteres.setEntiteEchangeE(null);

		criteres.setEntiteCompta(null);
		criteres.setEntiteComptaD(null);
		criteres.setEntiteComptaE(null);

		criteres.setReferenceComptable(null);
		criteres.setCodeOpeNfcca(null);
		criteres.setIdFichierRefRemise(null);

	}

	public void resetSecondairesNonValorises() {

		if (criteres.getEntiteEchange() == null) {
			reset(listEntiteEchange,null,TOUTES);
		}
		if (criteres.getEntiteEchangeE() == null) {
			reset(listEntiteEchangeEmettrice,null,TOUTES);
		}
		if (criteres.getEntiteEchangeD() == null) {
			reset(listEntiteEchangeDestinataire,null,TOUTES);
		}
		if (criteres.getEntiteCompta() == null) {
			reset(listEntiteCompta,null,TOUTES);
		}
		if (criteres.getEntiteComptaE() == null) {
			reset(listEntiteComptaEmettrice,null,TOUTES);
		}
		if (criteres.getEntiteComptaD() == null) {
			reset(listEntiteComptaDestinataire,null,TOUTES);
		}
		if (criteres.getReferenceComptable() == null) {
			reset(listReferenceComptable,null,TOUTES);
		}
		if (criteres.getCodeOpeNfcca() == null) {
			reset(listCodeOperationNfcca,null,TOUS);
		}
		if (criteres.getIdFichierRefRemise()== null) {
			reset(listFichierReferenceRemise,null,TOUTES);
		}
	}

	protected static List<SelectItem> toSelectItemList(Collection<String> src){
		List<SelectItem> res = new ArrayList<SelectItem>();
		if(null==src || 0==src.size()) return res;
		for(String s: src) res.add(new SelectItem(s,s));
		return res;
	}
	protected static List<SelectItem> toSelectItemList(String[] src){
		List<SelectItem> res = new ArrayList<SelectItem>();
		if(null==src || 0==src.length) return res;
		for(String s: src) res.add(new SelectItem(s,s));
		return res;
	}

	public boolean updatePrimaryCriteresWithRequestParameters(){
		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		boolean success = true;
		focus = "";

		boolean successEchange = true;
		String dateDebutEchange = paramMap.get(FORM_NAME+"debutEchInputDate");
		if(FormValidationUtil.empty(dateDebutEchange)) {
			FormValidationUtil.addFormValidationMessage("gm.error.field.datedebutechange", FormValidationUtil.ERROR_EMPTY);
			criteres.setDateDebutEchange(null);
			successEchange=false;
			focus = FORM_NAME+"debutEchInputDate";
		} else if(!FormValidationUtil.isDate(dateDebutEchange)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.datedebutechange", FormValidationUtil.ERROR_DATE_FORMAT);
			successEchange=false;
			focus = FORM_NAME+"debutEchInputDate";
		}
		else criteres.setDateDebutEchange(stringToDate(dateDebutEchange));

		String dateFinEchange = paramMap.get(FORM_NAME+"finEchInputDate");
		if(FormValidationUtil.empty(dateFinEchange)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.datefinechange", FormValidationUtil.ERROR_EMPTY);
			criteres.setDateFinEchange(null);
			successEchange=false;
			focus = FORM_NAME+"finEchInputDate";
		}else if(!FormValidationUtil.isDate(dateFinEchange)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.datefinechange", FormValidationUtil.ERROR_DATE_FORMAT);
			successEchange=false;
			focus = FORM_NAME+"finEchInputDate";
		}else criteres.setDateFinEchange(stringToDate(dateFinEchange));



		heureEchangeDebut = paramMap.get(FORM_NAME+"heureDebut");
		if(FormValidationUtil.empty(heureEchangeDebut)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.heuredebutechange", FormValidationUtil.ERROR_EMPTY);
			successEchange=false;
			focus = FORM_NAME+"heureDebut";
		}else if(!FormValidationUtil.isHHMM(heureEchangeDebut)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.heuredebutechange", FormValidationUtil.ERROR_HEURE_FORMAT);
			successEchange=false;
			focus = FORM_NAME+"heureDebut";
		}else if(criteres.getDateDebutEchange()!=null) criteres.setDateDebutEchange(getDate(criteres.getDateDebutEchange(),heureEchangeDebut ));

		heureEchangeFin = paramMap.get(FORM_NAME+"heureFin");
		if(FormValidationUtil.empty(heureEchangeFin)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.heurefinechange", FormValidationUtil.ERROR_EMPTY);
			successEchange=false;
			focus = FORM_NAME+"heureFin";
		}else if(!FormValidationUtil.isHHMM(heureEchangeFin)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.heurefinechange", FormValidationUtil.ERROR_HEURE_FORMAT);
			successEchange=false;
			focus = FORM_NAME+"heureFin";
		}else if(criteres.getDateFinEchange()!=null) criteres.setDateFinEchange(getDate(criteres.getDateFinEchange(), heureEchangeFin));


		if (successEchange) {
			if(FormValidationUtil.debutApresFin(criteres.getDateDebutEchange(),criteres.getDateFinEchange())){
				FormValidationUtil.addFormValidationMessage("gm.error.field.dateechange", FormValidationUtil.ERROR_DEB_APRES_FIN);
				success=false;
				focus = FORM_NAME+"debutEchInputDate";
			}
		}

		boolean successConv = true;

		String dateDebutConv = paramMap.get(FORM_NAME+"debutDateConvInputDate");
		if(FormValidationUtil.notEmpty(dateDebutConv) && !FormValidationUtil.isDate(dateDebutConv)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.datedebutconvref", FormValidationUtil.ERROR_DATE_FORMAT);
			successConv=false;
			focus = FORM_NAME+"debutDateConvInputDate";
		}else criteres.setDateDebutConvRef(stringToDate(dateDebutConv));

		String dateFinConv = paramMap.get(FORM_NAME+"finDateConvInputDate");
		if(FormValidationUtil.notEmpty(dateFinConv) && !FormValidationUtil.isDate(dateFinConv)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.datefinconvref", FormValidationUtil.ERROR_DATE_FORMAT);
			successConv=false;
			focus = FORM_NAME+"finDateConvInputDate";
		}else criteres.setDateFinConvRef(stringToDateSoir(dateFinConv));

		if (successConv) {
			if(FormValidationUtil.debutApresFin(criteres.getDateDebutConvRef(),criteres.getDateFinConvRef())){
				FormValidationUtil.addFormValidationMessage("gm.error.field.dateconvref", FormValidationUtil.ERROR_DEB_APRES_FIN);
				success=false;
				focus = FORM_NAME+"debutDateConvInputDate";
			}
		}

		boolean successRegl = true;
		String dateDebutRegl = paramMap.get(FORM_NAME+"debutDateRegInputDate");
		if(FormValidationUtil.notEmpty(dateDebutRegl) && !FormValidationUtil.isDate(dateDebutRegl)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.datedebutreglement", FormValidationUtil.ERROR_DATE_FORMAT);
			successRegl=false;
			focus = FORM_NAME+"debutDateRegInputDate";
		}else criteres.setDateDebutReglement(stringToDate(dateDebutRegl));

		String dateFinRegl = paramMap.get(FORM_NAME+"finDateRegInputDate");
		if(FormValidationUtil.notEmpty(dateFinRegl) && !FormValidationUtil.isDate(dateFinRegl)){
			FormValidationUtil.addFormValidationMessage("gm.error.field.datefinreglement", FormValidationUtil.ERROR_DATE_FORMAT);
			successRegl=false;
			focus = FORM_NAME+"finDateRegInputDate";
		}else criteres.setDateFinReglement(stringToDateSoir(dateFinRegl));



		if (successRegl) {
			if(FormValidationUtil.debutApresFin(criteres.getDateDebutReglement(),criteres.getDateFinReglement())){
				FormValidationUtil.addFormValidationMessage("gm.error.field.datereglement", FormValidationUtil.ERROR_DEB_APRES_FIN);
				success=false;
				focus = FORM_NAME+"debutDateRegInputDate";
			}
		}

		success = success && successConv && successEchange && successRegl;

		String chef = paramMap.get(FORM_NAME+"selectChef");
		if(chef!=null && !"".equals(chef.trim()))criteres.setChefDeFile(Integer.valueOf(chef));
		else criteres.setChefDeFile(null);

		String devise = paramMap.get(FORM_NAME+"selectDevise");
		criteres.setDevRegCompt(null);
		if (devise != null) {
			if (!devise.isEmpty()) {
				criteres.setDevRegCompt(devise);
			}
		}

		String reseau = paramMap.get(FORM_NAME+"selectReseau");
		criteres.setReseau(null);
		if (reseau != null) {
			if (!reseau.isEmpty()) {
				criteres.setReseau(reseau);
			}
		}

		String sens = paramMap.get(FORM_NAME+"selectSens");
		criteres.setSens(null);
		if (sens != null) {
			if (!sens.isEmpty()) {
				criteres.setSens(sens);
			}
		}

		String bo = paramMap.get(FORM_NAME+"selectBO");
		criteres.setBackOffice(null);
		if (bo != null) {
			if (!bo.isEmpty()) {
				criteres.setBackOffice(bo);
			}
		}


		String force = paramMap.get(FORM_NAME+"selectIndicateurForcage");
		if("O".equals(force))criteres.setForce(Boolean.TRUE);
		else if("N".equals(force))criteres.setForce(Boolean.FALSE);
		else criteres.setForce(null);

		String statut = paramMap.get(FORM_NAME+"selectStatut");
		criteres.setStatutRapprochement(null);
		if (statut != null) {
			if (!statut.isEmpty()) {
				criteres.setStatutRapprochement(statut);
			}
		}


		if(ConstantesTypo.STATUT_RAPPROCHEMENT_SANS_ECART.equals(statut) || ConstantesTypo.STATUT_RAPPROCHEMENT_ECART.equals(statut)){
			criteres.setForce(null);
		}

		return success;
	}

	public boolean updateCriteresWithRequestParameters(){
		Map<String,String> paramMap =  FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		boolean success = true;

		success = updatePrimaryCriteresWithRequestParameters();

		String refCpt = paramMap.get(FORM_NAME + "selectRefCpt");
		criteres.setReferenceComptable(null);
		if (refCpt != null) {
			if (!refCpt.isEmpty()) {
				criteres.setReferenceComptable(refCpt);
			}
		}
		String selIdFic = paramMap.get(FORM_NAME+"selectIdFic");
		criteres.setIdFichierRefRemise(null);
		if (selIdFic != null) {
			if (!selIdFic.isEmpty()) {
				criteres.setIdFichierRefRemise(selIdFic);
			}
		}

		String selCodeOpe = paramMap.get(FORM_NAME+"selectCodeOp");
		criteres.setCodeOpeNfcca(null);
		if (selCodeOpe != null) {
			if (!selCodeOpe.isEmpty()) {
				criteres.setCodeOpeNfcca(selCodeOpe);
			}
		}

		String entCompta = paramMap.get(FORM_NAME+"selectEntCpt");
		criteres.setEntiteCompta(null);
		if (entCompta != null) {
			if(!entCompta.isEmpty()) {
				criteres.setEntiteCompta(entCompta);
			}
		}
		String entComptaE = paramMap.get(FORM_NAME+"selectEntCptE");
		criteres.setEntiteComptaE(null);
		if (entComptaE != null) {
			if(!entComptaE.isEmpty()) {
				criteres.setEntiteComptaE(entComptaE);
			}
		}
		String entComptaD = paramMap.get(FORM_NAME+"selectEntCptD");
		criteres.setEntiteComptaD(null);
		if (entComptaD != null) {
			if(!entComptaD.isEmpty()) {
				criteres.setEntiteComptaD(entComptaD);
			}
		}
		String entEch = paramMap.get(FORM_NAME+"selectEntEch");
		criteres.setEntiteEchange(null);
		if (entEch != null) {
			if(!entEch.isEmpty()) {
				criteres.setEntiteEchange(entEch);
			}
		}
		String entEchE = paramMap.get(FORM_NAME+"selectEntEchE");
		criteres.setEntiteEchangeE(null);
		if (entEchE != null) {
			if(!entEchE.isEmpty()) {
				criteres.setEntiteEchangeE(entEchE);
			}
		}
		String entEchD = paramMap.get(FORM_NAME+"selectEntEchD");
		criteres.setEntiteEchangeD(null);
		if (entEchD != null) {
			if(!entEchD.isEmpty()) {
				criteres.setEntiteEchangeD(entEchD);
			}
		}

		String indicateurForcage = paramMap.get(FORM_NAME+"selectIndicateurForcage");
		criteres.setForce(null);
		if (indicateurForcage != null) {
			if(!indicateurForcage.isEmpty()) {
				criteres.setForce("O".equals(indicateurForcage) ? Boolean.TRUE:Boolean.FALSE);
			}
		}

		return success;
	}

	public CriteresPourSuiviDto getSearchCriteres(){
//		System.out.println("************   Avant controle critères:    ********************\n"+ criteres);
		CriteresPourSuiviDto tmpcriteres;
		try {
			tmpcriteres = criteres.getPreparedClone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}



		tmpcriteres.setDateDebutEchange(getDate(tmpcriteres.getDateDebutEchange(),heureEchangeDebut ));
		tmpcriteres.setDateFinEchange(getDate(tmpcriteres.getDateFinEchange(), heureEchangeFin));

//		System.out.println("************   Apres controle critères:    ********************\n"+ tmpcriteres);
		return tmpcriteres;

	}
}
