package com.cedicam.gm.ui.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import javax.faces.model.SelectItem;

import com.cedicam.gm.bt.converter.CodeBanqueConverter;
import com.cedicam.gm.bt.converter.DateConverter;
import com.cedicam.gm.bt.converter.DateTimeConverter;
import com.cedicam.gm.bt.converter.EntiteGroupeConverter;
import com.cedicam.gm.bt.converter.IntegerConverter;
import com.cedicam.gm.bt.converter.MemberIdConverter;
import com.cedicam.gm.re.data.dto.EntiteDto;

/**
 * Classe permettant de filtrer la liste des entités.
 * @author Salim TAZI
 *
 */
public class FiltreEntites {		
	/** Constructeur. */
	public FiltreEntites() {		
		initConverters();
	}
	
	/** Liste des converters pour les labels. */
	private Map<String, Converter> labelConverters = new HashMap<String, Converter>();
	/** Liste des converters pour les values. */
	private Map<String, Converter> valueConverters = new HashMap<String, Converter>();
	
	/** Associe un converter à chaque propriété qui le nécessite. */
	private void initConverters() {		
		javax.faces.convert.DateTimeConverter englishDateTimeConverter = new javax.faces.convert.DateTimeConverter();
		englishDateTimeConverter.setPattern("yyyyMMddHHmmss");
		Converter frenchDateTimeConverter = new DateTimeConverter();
		Converter frenchDateConverter = new DateConverter();
		Converter integerConverter = new IntegerConverter();
		Converter entiteGroupeConverter = new EntiteGroupeConverter();
		Converter codeBanqueConverter = new CodeBanqueConverter();
		Converter memberIdConverter = new MemberIdConverter();

		// Définition des converters de labels
		labelConverters.put("codeBanque", codeBanqueConverter);
		labelConverters.put("cdBanqueEntACompt", codeBanqueConverter);
		labelConverters.put("circ1CdBqReglIntra", codeBanqueConverter);
		labelConverters.put("circ1CdBqReglInter", codeBanqueConverter);
	    labelConverters.put("circ1CdBqReglCore", codeBanqueConverter);
	    labelConverters.put("circ1CdBqReglCup", codeBanqueConverter);
	    labelConverters.put("circ1CdBqReglMci", codeBanqueConverter);
	    labelConverters.put("circ1CdBqReglVisa", codeBanqueConverter);
	    labelConverters.put("chefDeFile", codeBanqueConverter);
		labelConverters.put("codeIcaMci", memberIdConverter);
		labelConverters.put("dtDeMigrEntSurBoe", frenchDateTimeConverter);
		labelConverters.put("dtDeMigrEntSurBoa", frenchDateTimeConverter);
		labelConverters.put("dateCreation", frenchDateTimeConverter);
		labelConverters.put("dateMaj", frenchDateTimeConverter);
		labelConverters.put("dtDbEffet", frenchDateConverter);
		labelConverters.put("dtFinEffet", frenchDateConverter);
		labelConverters.put("version", integerConverter);
		labelConverters.put("clientExterneGroupeCa", entiteGroupeConverter);

		// Définition des converters de values
	    valueConverters.put("codeBanque", codeBanqueConverter);
	    valueConverters.put("cdBanqueEntACompt", codeBanqueConverter);
	    valueConverters.put("circ1CdBqReglIntra", codeBanqueConverter);
	    valueConverters.put("circ1CdBqReglInter", codeBanqueConverter);
	    valueConverters.put("circ1CdBqReglCore", codeBanqueConverter);
	    valueConverters.put("circ1CdBqReglCup", codeBanqueConverter);
	    valueConverters.put("circ1CdBqReglMci", codeBanqueConverter);
	    valueConverters.put("circ1CdBqReglVisa", codeBanqueConverter);
	    valueConverters.put("chefDeFile", codeBanqueConverter);
		valueConverters.put("codeIcaMci", memberIdConverter);
		valueConverters.put("dtDeMigrEntSurBoe", englishDateTimeConverter);
		valueConverters.put("dtDeMigrEntSurBoa", englishDateTimeConverter);
		valueConverters.put("dateCreation", englishDateTimeConverter);
		valueConverters.put("dateMaj", englishDateTimeConverter);
		valueConverters.put("dtDbEffet", englishDateTimeConverter);
		valueConverters.put("dtFinEffet", englishDateTimeConverter);
		valueConverters.put("version", integerConverter);
		valueConverters.put("clientExterneGroupeCa", entiteGroupeConverter);
	}
	
	/** Comparateur de selectItems. */
	private Comparator<SelectItem> itemsComparator = new Comparator<SelectItem>() {
		@SuppressWarnings("unchecked")
		@Override
		public int compare(SelectItem o1, SelectItem o2) {
			int result;
			if (null == o1 && null == o2) {
				result = 0;
			} else if (null != o1 && null == o2) {
				result = 1; 
			} else if (null == o1 && null != o2) {
				result = -1;
			} else {
				try {
					result = ((Comparable) o1.getValue()).compareTo((Comparable) o2.getValue());
				} catch (Exception e) {
					result = 0;
				}
			}
			return result;
		}
	};

	/** Valeur permettant de positionner les filtres à : "Tous"/"Toutes". */
	public static final String ALL = "ALL";

	/** Remet la liste de filtres à zéro. */
	public void clear() {
		itemsLists.clear();
		filtreCourant.clear();
	}

	/** La liste de valeurs possible pour chaque champ. */
	private Map<String, Set<Object>> itemsLists = new HashMap<String, Set<Object>>();
	/**
	 * Pour un DTO donné, construit l'association nom de propriété / valeur.
	 * @param dto Le DTO
	 * @return La map d'association
	 */
	public Map<String, Object> getPropsValueMap(EntiteDto dto) {
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
	 * Rafrachit la liste de valeurs possibles.
	 * @param list La liste des entités affichées.
	 */
	private void refresh(List<EntiteDto> list) {
		itemsLists.clear();
		for (EntiteDto dto : list) {
			Map<String, Object> propValues = getPropsValueMap(dto);
			for (String key: propValues.keySet()) {
				addEntry(key, propValues.get(key));
			}
		}
	}

	/**
	 * Ajoute une entrée dans la liste des valeurs possibles.
	 * @param prop La propriété concernée.
	 * @param val La valeur a ajouter.
	 */
	private void addEntry(String prop, Object val) {
		Set<Object> vallist =  itemsLists.get(prop);
		if (null == vallist) {
			 vallist = new HashSet<Object>();
			 itemsLists.put(prop, vallist);
		}
		vallist.add(val);
	}

	/**
	 * Récupére la liste de selectItems pour la propriété passée en paramètre.
	 * @param prop La propriété.
	 * @return La liste de selectItems correspondant.
	 */
	private List<SelectItem> getSelectItemList(String prop) {
		// Contexte de l'application
		FacesContext contextApplication = FacesContext.getCurrentInstance();
		// Component pour l'appel aux converters
		UIComponent component = contextApplication.getViewRoot().getChildren().get(0);
		
		Set<Object> vallist =  itemsLists.get(prop);

		List<SelectItem> res = new ArrayList<SelectItem>();
		if (vallist == null) {
			return res;
		}
		for (Object s: vallist) {
			// Définition du label
			String label;
			if (labelConverters.containsKey(prop) && null != labelConverters.get(prop)) {
				Converter labelConverter = labelConverters.get(prop);
				label = labelConverter.getAsString(contextApplication, component, s);
			} else {
				label = "" + s;
			}
			
			// Définition de la value
			String value;
			if (valueConverters.containsKey(prop) && null != valueConverters.get(prop)) {
				Converter valueConverter = valueConverters.get(prop);
				value = valueConverter.getAsString(contextApplication, component, s);
			} else {
				value = "" + s;
			}
			
			res.add(new SelectItem(value, label));
		}

		// Tri
		Collections.sort(res, itemsComparator);
		
		return res;
	}

    /** Liste des filtres courants. */
	private Map<String, Object> filtreCourant = new HashMap<String, Object>();
	
	/** 
	 * Récupère la valeur du filtre courant pour la propriété passée en paramètre.
	 * @param prop La propriété dont le filtre est à récupérer.
	 * @return La valeur du filtre.
	 */
	public String getFilterValue(String prop) {
		// Contexte de l'application
		FacesContext contextApplication = FacesContext.getCurrentInstance();
		// Component pour l'appel aux converters
		UIComponent component = contextApplication.getViewRoot().getChildren().get(0);
		
		String value = ALL;
		if (filtreCourant.containsKey(prop)) {
			Object filterValue = filtreCourant.get(prop);
			
			if (valueConverters.containsKey(prop) && null != valueConverters.get(prop)) {			
				Converter currentConverter = valueConverters.get(prop);
				value = currentConverter.getAsString(contextApplication, component, filterValue);
			} else {
				value = "" + filterValue;
			}
		}
		
		return value;
	}
	
	/**
	 * Définit la valeur du filtre courant pour la propriété passée en paramètre.
	 * @param prop La propriété dont le filtre est a définir.
	 * @param value La valeur du filtre.
	 */
	private void setFilterValue(String prop, String value) {
		// Contexte de l'application
		FacesContext contextApplication = FacesContext.getCurrentInstance();
		// Component pour l'appel aux converters
		UIComponent component = contextApplication.getViewRoot().getChildren().get(0);
		
		filtreCourant.remove(prop);

		if (!ALL.equals(value)) {
			Object filterValue;
			if (valueConverters.containsKey(prop) && null != valueConverters.get(prop)) {
				Converter currentConverter = valueConverters.get(prop);
				filterValue = currentConverter.getAsObject(contextApplication, component, value);
			} else {
				filterValue = value;
			}
			filtreCourant.put(prop, filterValue);
		}
	}
	
	/**
	 * Filtre la liste en fonction des filtres définis.
	 * @param fullList La liste complète d'entités.
	 * @return La liste triée d'entités.
	 */
	public ArrayList<EntiteDto> filtrer(List<EntiteDto> fullList) {
		ArrayList<EntiteDto> filteredData = new ArrayList<EntiteDto>();
		for (EntiteDto dto: fullList) {
			Map<String, Object> props = getPropsValueMap(dto);

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

		refresh(filteredData);
		return filteredData;
	}
	
	/** Accesseurs. */
    public List<SelectItem> getCodeBanqueSelectItemList() {        return getSelectItemList("codeBanque");    }
    public List<SelectItem> getDtDbEffetSelectItemList() {        return getSelectItemList("dtDbEffet");    }
    public List<SelectItem> getVersionSelectItemList() {        return getSelectItemList("version");    }
    public List<SelectItem> getLibelleSelectItemList() {        return getSelectItemList("libelle");    }
    public List<SelectItem> getClientExterneGroupeCaSelectItemList() {        return getSelectItemList("clientExterneGroupeCa");    }
    public List<SelectItem> getDtDeMigrEntSurBoeSelectItemList() {        return getSelectItemList("dtDeMigrEntSurBoe");    }
    public List<SelectItem> getDtDeMigrEntSurBoaSelectItemList() {        return getSelectItemList("dtDeMigrEntSurBoa");    }
    public List<SelectItem> getCdBanqueEntAComptSelectItemList() {        return getSelectItemList("cdBanqueEntACompt");    }
    public List<SelectItem> getEnvImCrrEntEchSelectItemList() {        return getSelectItemList("envImCrrEntEch");    }
    public List<SelectItem> getEnvImCrrEntCompSelectItemList() {        return getSelectItemList("envImCrrEntComp");    }
    public List<SelectItem> getCodeIcaMciSelectItemList() {        return getSelectItemList("codeIcaMci");    }
    public List<SelectItem> getCirc1CdBqReglIntraSelectItemList() {        return getSelectItemList("circ1CdBqReglIntra");    }
    public List<SelectItem> getCirc1CdBqReglInterSelectItemList() {        return getSelectItemList("circ1CdBqReglInter");    }
    public List<SelectItem> getCirc1CdBqReglCoreSelectItemList() {        return getSelectItemList("circ1CdBqReglCore");    }
    public List<SelectItem> getCirc1CdBqReglCupSelectItemList() {        return getSelectItemList("circ1CdBqReglCup");    }
    public List<SelectItem> getCirc1CdBqReglMciSelectItemList() {        return getSelectItemList("circ1CdBqReglMci");    }
    public List<SelectItem> getCirc1CdBqReglVisaSelectItemList() {        return getSelectItemList("circ1CdBqReglVisa");    }
    public List<SelectItem> getDtFinEffetSelectItemList() {        return getSelectItemList("dtFinEffet");    }
    public List<SelectItem> getChefDeFileSelectItemList() {        return getSelectItemList("chefDeFile");    }
    public List<SelectItem> getDateCreationSelectItemList() {        return getSelectItemList("dateCreation");    }
    public List<SelectItem> getUserCreationSelectItemList() {        return getSelectItemList("userCreation");    }
    public List<SelectItem> getDateMajSelectItemList() {        return getSelectItemList("dateMaj");    }
    public List<SelectItem> getUserMajSelectItemList() {        return getSelectItemList("userMaj");    }

    public String getCodeBanqueValue() {        return getFilterValue("codeBanque");    }
    public String getDtDbEffetValue() {        return getFilterValue("dtDbEffet");    }
    public String getVersionValue() {        return getFilterValue("version");    }
    public String getLibelleValue() {        return getFilterValue("libelle");    }
    public String getClientExterneGroupeCaValue() {        return getFilterValue("clientExterneGroupeCa");    }
    public String getDtDeMigrEntSurBoeValue() {        return getFilterValue("dtDeMigrEntSurBoe");    }
    public String getDtDeMigrEntSurBoaValue() {        return getFilterValue("dtDeMigrEntSurBoa");    }
    public String getCdBanqueEntAComptValue() {        return getFilterValue("cdBanqueEntACompt");    }
    public String getEnvImCrrEntEchValue() {        return getFilterValue("envImCrrEntEch");    }
    public String getEnvImCrrEntCompValue() {        return getFilterValue("envImCrrEntComp");    }
    public String getCodeIcaMciValue() {        return getFilterValue("codeIcaMci");    }
    public String getCirc1CdBqReglIntraValue() {        return getFilterValue("circ1CdBqReglIntra");    }
    public String getCirc1CdBqReglInterValue() {        return getFilterValue("circ1CdBqReglInter");    }
    public String getCirc1CdBqReglCoreValue() {        return getFilterValue("circ1CdBqReglCore");    }
    public String getCirc1CdBqReglCupValue() {        return getFilterValue("circ1CdBqReglCup");    }
    public String getCirc1CdBqReglMciValue() {        return getFilterValue("circ1CdBqReglMci");    }
    public String getCirc1CdBqReglVisaValue() {        return getFilterValue("circ1CdBqReglVisa");    }
    public String getDtFinEffetValue() {        return getFilterValue("dtFinEffet");    }
    public String getChefDeFileValue() {        return getFilterValue("chefDeFile");    }
    public String getDateCreationValue() {        return getFilterValue("dateCreation");    }
    public String getUserCreationValue() {        return getFilterValue("userCreation");    }
    public String getDateMajValue() {        return getFilterValue("dateMaj");    }
    public String getUserMajValue() {        return getFilterValue("userMaj");    }

    public void setCodeBanqueValue(String value) {        setFilterValue("codeBanque", value);    }
    public void setDtDbEffetValue(String value) {        setFilterValue("dtDbEffet", value);    }
    public void setVersionValue(String value) {        setFilterValue("version", value);    }
    public void setLibelleValue(String value) {        setFilterValue("libelle", value);    }
    public void setClientExterneGroupeCaValue(String value) {        setFilterValue("clientExterneGroupeCa", value);    }
    public void setDtDeMigrEntSurBoeValue(String value) {        setFilterValue("dtDeMigrEntSurBoe", value);    }
    public void setDtDeMigrEntSurBoaValue(String value) {        setFilterValue("dtDeMigrEntSurBoa", value);    }
    public void setCdBanqueEntAComptValue(String value) {        setFilterValue("cdBanqueEntACompt", value);    }
    public void setEnvImCrrEntEchValue(String value) {        setFilterValue("envImCrrEntEch", value);    }
    public void setEnvImCrrEntCompValue(String value) {        setFilterValue("envImCrrEntComp", value);    }
    public void setCodeIcaMciValue(String value) {        setFilterValue("codeIcaMci", value);    }
    public void setCirc1CdBqReglIntraValue(String value) {        setFilterValue("circ1CdBqReglIntra", value);    }
    public void setCirc1CdBqReglInterValue(String value) {        setFilterValue("circ1CdBqReglInter", value);    }
    public void setCirc1CdBqReglCoreValue(String value) {        setFilterValue("circ1CdBqReglCore", value);    }
    public void setCirc1CdBqReglCupValue(String value) {        setFilterValue("circ1CdBqReglCup", value);    }
    public void setCirc1CdBqReglMciValue(String value) {        setFilterValue("circ1CdBqReglMci", value);    }
    public void setCirc1CdBqReglVisaValue(String value) {        setFilterValue("circ1CdBqReglVisa", value);    }
    public void setDtFinEffetValue(String value) {        setFilterValue("dtFinEffet", value);    }
    public void setChefDeFileValue(String value) {        setFilterValue("chefDeFile", value);    }
    public void setDateCreationValue(String value) {        setFilterValue("dateCreation", value);    }
    public void setUserCreationValue(String value) {        setFilterValue("userCreation", value);    }
    public void setDateMajValue(String value) {        setFilterValue("dateMaj", value);    }
    public void setUserMajValue(String value) {        setFilterValue("userMaj", value);    }

	/**
	 * @return the filtreCourant
	 */
	public Map<String, Object> getFiltreCourant() {
		return filtreCourant;
	}
}
