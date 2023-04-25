package com.cedicam.gm.re.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

/**
 * @author Fabien JALABERT.
 */
public class ReferencielGeneral {
	/** L'instance du bean de gestion du r�f�rentiel. */
	private static ReferencielGeneral instance = new ReferencielGeneral();

	/** R�f�rentiel des sens. */
	private Map<String, String> sens = new HashMap<String, String>();
	/** R�f�rentiel des r�seaux. */
	private Map<String, String> reseaux = new HashMap<String, String>();
	/** R�f�rentiel des statuts de rapprochement. */
	private Map<String, String> statutsRapprochement = new HashMap<String, String>();
	/** R�f�rentiel des devises. */
	private Map<String, String> devises = new HashMap<String, String>();
	/** R�f�rentiel des indicateurs de for�age. */
	private Map<String, String> forcage = new HashMap<String, String>();
	/** R�f�rentiel des BO. */
	private Map<String, String> backOffices = new HashMap<String, String>();
	/** R�f�rentiel des statuts de validit�. */
	private Map<String, String> statutsValidite = new HashMap<String, String>();
	
	/** Pr�fixe des valeurs du backoffice. */
	private static final String PREFIX_BACKOFFICE = "gm.param.backoffice.";
	/** Pr�fixe des valeurs du r�seau. */
	private static final String PREFIX_RESEAU = "gm.param.reseau.";
	/** Pr�fixe des valeurs du sens. */
	private static final String PREFIX_SENS = "gm.param.sens.";
	/** Pr�fixe des valeurs du for�age. */
	private static final String PREFIX_FORCAGE = "gm.param.forcage.";
	/** Pr�fixe des valeurs de la devise. */
	private static final String PREFIX_DEVISE = "gm.param.devise.";
	/** Pr�fixe des valeurs du statut de rapprochement. */
	private static final String PREFIX_STATUT = "gm.param.statut.";
	/** Pr�fixe des valeurs du statut de validit�. */
	private static final String PREFIX_VALIDITE = "gm.param.validite.";
	
	/**
	 * Permet de r�cup�rer une instance du r�f�rentiel g�n�ral.
	 * @return Une instance du r�f�rentiel g�n�ral.
	 */
	public static ReferencielGeneral getInstance() {
		if (instance.backOffices.size() == 0) {
			instance.init();
		}
		return instance;
	}

	/**
	 * Convertit une map en liste de selectItems.
	 * @param map La map � convertir.
	 * @return La liste de SelectItems g�n�r�e.
	 */
	public static List<SelectItem> toSelectList(Map<String, String> map) {
		List<SelectItem> result = null;
		if (map != null) {
			result = new ArrayList<SelectItem>(map.size());
			for (Entry<String, String> e : map.entrySet()) {
				SelectItem current = new SelectItem(e.getKey(), e.getValue());
				result.add(current);
			}
		}
		return result;
	}

	/**
	 * Remplit une map � partir des valeurs d'un bundle.
	 * @param bundle Le bundle contenant les valeurs recherch�es.
	 * @param map La map � remplir.
	 * @param prefix Le pr�fixe recherch�.
	 */
	private static void fillMap(ResourceBundle bundle, Map<String, String> map, String prefix) {
		map.clear();
		for (String key : bundle.keySet()) {
			if (key.startsWith(prefix)) {
				String smallkey = key.substring(prefix.length());
				String value = bundle.getString(key);
				map.put(smallkey, value);
			}
		}
	}

	/** 
	 * Initialise les r�f�rentiels � partir du fichier properties. 
	 */
	private void init() {
		ResourceBundle bundle = ResourceBundle.getBundle("rubriques");
		fillMap(bundle, backOffices, PREFIX_BACKOFFICE);
		fillMap(bundle, reseaux, PREFIX_RESEAU);
		fillMap(bundle, sens, PREFIX_SENS);
		fillMap(bundle, devises, PREFIX_DEVISE);
		fillMap(bundle, forcage, PREFIX_FORCAGE);
		fillMap(bundle, statutsRapprochement, PREFIX_STATUT);
		fillMap(bundle, statutsValidite, PREFIX_VALIDITE);
	}

	/**
	 * @return Le r�f�rentiel backoffices.
	 */
	public Map<String, String> getBackOffices() {
		return backOffices;
	}

	/**
	 * @return Le r�f�rentiel sens.
	 */
	public Map<String, String> getSens() {
		return sens;
	}

	/**
	 * @return Le r�f�rentiel reseaux.
	 */
	public Map<String, String> getReseaux() {
		return reseaux;
	}

	/**
	 * @return Le r�f�rentiel statuts rapprochement.
	 */
	public Map<String, String> getStatutsRapprochement() {
		return statutsRapprochement;
	}

	/**
	 * @return Le r�f�rentiel devises.
	 */
	public Map<String, String> getDevises() {
		return devises;
	}

	/**
	 * @return Le r�f�rentiel forcage.
	 */
	public Map<String, String> getForcage() {
		return forcage;
	}

	/**
	 * @return Le r�f�rentiel statuts validit�.
	 */
	public Map<String, String> getStatutsValidite() {
		return statutsValidite;
	}

}
