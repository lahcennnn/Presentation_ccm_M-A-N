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
	/** L'instance du bean de gestion du référentiel. */
	private static ReferencielGeneral instance = new ReferencielGeneral();

	/** Référentiel des sens. */
	private Map<String, String> sens = new HashMap<String, String>();
	/** Référentiel des réseaux. */
	private Map<String, String> reseaux = new HashMap<String, String>();
	/** Référentiel des statuts de rapprochement. */
	private Map<String, String> statutsRapprochement = new HashMap<String, String>();
	/** Référentiel des devises. */
	private Map<String, String> devises = new HashMap<String, String>();
	/** Référentiel des indicateurs de forçage. */
	private Map<String, String> forcage = new HashMap<String, String>();
	/** Référentiel des BO. */
	private Map<String, String> backOffices = new HashMap<String, String>();
	/** Référentiel des statuts de validité. */
	private Map<String, String> statutsValidite = new HashMap<String, String>();
	
	/** Préfixe des valeurs du backoffice. */
	private static final String PREFIX_BACKOFFICE = "gm.param.backoffice.";
	/** Préfixe des valeurs du réseau. */
	private static final String PREFIX_RESEAU = "gm.param.reseau.";
	/** Préfixe des valeurs du sens. */
	private static final String PREFIX_SENS = "gm.param.sens.";
	/** Préfixe des valeurs du forçage. */
	private static final String PREFIX_FORCAGE = "gm.param.forcage.";
	/** Préfixe des valeurs de la devise. */
	private static final String PREFIX_DEVISE = "gm.param.devise.";
	/** Préfixe des valeurs du statut de rapprochement. */
	private static final String PREFIX_STATUT = "gm.param.statut.";
	/** Préfixe des valeurs du statut de validité. */
	private static final String PREFIX_VALIDITE = "gm.param.validite.";
	
	/**
	 * Permet de récupérer une instance du référentiel général.
	 * @return Une instance du référentiel général.
	 */
	public static ReferencielGeneral getInstance() {
		if (instance.backOffices.size() == 0) {
			instance.init();
		}
		return instance;
	}

	/**
	 * Convertit une map en liste de selectItems.
	 * @param map La map à convertir.
	 * @return La liste de SelectItems générée.
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
	 * Remplit une map à partir des valeurs d'un bundle.
	 * @param bundle Le bundle contenant les valeurs recherchées.
	 * @param map La map à remplir.
	 * @param prefix Le préfixe recherché.
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
	 * Initialise les référentiels à partir du fichier properties. 
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
	 * @return Le référentiel backoffices.
	 */
	public Map<String, String> getBackOffices() {
		return backOffices;
	}

	/**
	 * @return Le référentiel sens.
	 */
	public Map<String, String> getSens() {
		return sens;
	}

	/**
	 * @return Le référentiel reseaux.
	 */
	public Map<String, String> getReseaux() {
		return reseaux;
	}

	/**
	 * @return Le référentiel statuts rapprochement.
	 */
	public Map<String, String> getStatutsRapprochement() {
		return statutsRapprochement;
	}

	/**
	 * @return Le référentiel devises.
	 */
	public Map<String, String> getDevises() {
		return devises;
	}

	/**
	 * @return Le référentiel forcage.
	 */
	public Map<String, String> getForcage() {
		return forcage;
	}

	/**
	 * @return Le référentiel statuts validité.
	 */
	public Map<String, String> getStatutsValidite() {
		return statutsValidite;
	}

}
