package com.cedicam.gm.re.entite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.bt.util.FormatUtils;
import com.cedicam.gm.cm.data.dto.CritereEntiteDto;
import com.cedicam.gm.re.bean.EntiteServicesBean;
import com.cedicam.gm.re.data.dto.EntiteDto;

public class EntiteListSelectManager {
	/** Taille du code banque. */
	private static final int CODE_BANQUE_LENGTH = 5;
	/** Filler pour compléter à 5 caractères. */
	private static final char CODE_BANQUE_FILLER = '0';
	
	private EntiteServicesBean service = null;

	private List<SelectItem> listEntiteReglement= null;
	private List<SelectItem> listChefDeFile= null;
	private List<SelectItem> listEntiteCompta= null;
	
	public EntiteListSelectManager(EntiteServicesBean serv) {
		super();
		service = serv;
	}
	
	public void refreshCreation() {
		List<EntiteDto> listeEntites = service.rechercherEntites(ConstantesTypo.STATUT_VALIDITE_TOUS);
		List<CritereEntiteDto> chefsDeFile = service.listerChefsDefile();
		
		List<String> sChefsDeFile = new ArrayList<String>();
		for (CritereEntiteDto chefDeFile : chefsDeFile) {
			sChefsDeFile.add("" + chefDeFile.getEntiteEchange());
		}
		refreshCreation(listeEntites, sChefsDeFile);
	}

	public void refreshCreation(List<EntiteDto> listeEntites, List<String> chefsDeFile) {
		ResourceBundle bundle = ResourceBundle.getBundle("rubriques");
		List<SelectItem> data = new ArrayList<SelectItem>();
		List<SelectItem> chefFileData = new ArrayList<SelectItem>();
		
		SortedMap<String, String> map = new TreeMap<String, String>();
		
		for (EntiteDto entite : listeEntites) {
			String codeBanque = (entite.getCodeBanque() == null) ? null : "" + entite.getCodeBanque();
			String entiteFormatee = FormatUtils.format(CODE_BANQUE_LENGTH, codeBanque, CODE_BANQUE_FILLER);
			
			map.put(codeBanque, entiteFormatee + " - " + entite.getLibelle());
		}

		int insertPosition = 0;
		for (String key : map.keySet()) {
			SelectItem item = new SelectItem(key, map.get(key));
			
			data.add(item);
			
			if (chefsDeFile.contains(key)) {
				chefFileData.add(insertPosition++, item);
			} else {
				chefFileData.add(item);
			}
		}

		// cas de creation CFF et ER
		listEntiteReglement= new ArrayList<SelectItem>(data);
		listChefDeFile= new ArrayList<SelectItem>(chefFileData);
		listEntiteCompta= new ArrayList<SelectItem>(data);
		
		//Ajout elle meme
		listEntiteReglement.add(0,new SelectItem(-1, bundle.getString("wn.commun.select.entite.echange")));
		listEntiteCompta.add(0, new SelectItem(null, bundle.getString("wn.commun.select.entite.echange")));
		listChefDeFile.add(0, new SelectItem(-1, bundle.getString("wn.commun.select.entite.echange")));
		
		// Ajout du vide
		listEntiteReglement.add(0, new SelectItem(null, bundle.getString("wn.commun.select.vide")));
	}

	public void refreshModification(List<EntiteDto> listeEntites, List<String> chefsDeFile, EntiteDto entiteCourante) {
		ResourceBundle bundle = ResourceBundle.getBundle("rubriques");
		List<SelectItem> data = new ArrayList<SelectItem>();
		List<SelectItem> chefFileData = new ArrayList<SelectItem>();
		
		SortedMap<String, String> map = new TreeMap<String, String>();
		
		for (EntiteDto entite : listeEntites) {
			String codeBanque = (entite.getCodeBanque() == null) ? null : "" + entite.getCodeBanque();
			String entiteFormatee = FormatUtils.format(CODE_BANQUE_LENGTH, codeBanque, CODE_BANQUE_FILLER); 
					
			map.put(entite.getCodeBanque().toString(), entiteFormatee  + " - " + entite.getLibelle());
		}
		
		int insertPosition = 0;
		for (String key : map.keySet()) {
			SelectItem item = new SelectItem(key, map.get(key));
			
			data.add(item);
			
			if (chefsDeFile.contains(key)) {
				chefFileData.add(insertPosition++, item);
			} else {
				chefFileData.add(item);
			}
		}		
		
		// cas de modification chef de file
		listChefDeFile = new ArrayList<SelectItem>(chefFileData);
		
		// cas de modification entité de reglement ou entite compta
		listEntiteReglement = new ArrayList<SelectItem>(data);
		listEntiteCompta = new ArrayList<SelectItem>(data);
		
		for (SelectItem item: data) {
			// Suppression de l'entité courante dans les listes d'entités de reglement et d'entite compta + ajout 'entité échange'
			if (item.getValue().equals(entiteCourante.getCodeBanque().toString())) {
				listEntiteCompta.remove(item);
				listEntiteCompta.add(0, new SelectItem(null, bundle.getString("wn.commun.select.entite.echange")));
				listEntiteReglement.remove(item);
				listEntiteReglement.add(0, new SelectItem(item.getValue(), bundle.getString("wn.commun.select.entite.echange")));
				listChefDeFile.remove(item);
				listChefDeFile.add(0, new SelectItem(item.getValue(), bundle.getString("wn.commun.select.entite.echange")));
			}
		}
		// Ajout du vide
		listEntiteReglement.add(0, new SelectItem(null, bundle.getString("wn.commun.select.vide")));
	}


	public static final String VALIDITE_EN_COURS = "0";
	public static final String VALIDITE_ECHUES = "1";
	public static final String VALIDITE_A_VENIR = "2";

	public List<SelectItem> getListEntiteReglement() {
		return listEntiteReglement;
	}
	
	public List<SelectItem> getListEntiteCompta() {
		return listEntiteCompta;
	}
	
	public List<SelectItem> getListChefDeFile() {
		return listChefDeFile;
	}

}
