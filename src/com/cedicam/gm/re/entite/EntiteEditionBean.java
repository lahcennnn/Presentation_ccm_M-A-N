package com.cedicam.gm.re.entite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;

import com.cedicam.gm.bt.bean.Btmu10;
import com.cedicam.gm.bt.converter.CodeBanqueConverter;
import com.cedicam.gm.bt.data.dto.DateApplicationDto;
import com.cedicam.gm.re.data.dto.EntiteDto;
import com.cedicam.gm.ui.core.FormValidationUtil;
import com.cedicam.gm.ui.core.SearchManager;

/**
 * @author Salim TAZI
 */
public class EntiteEditionBean {
	/** entite originale. */
	private EntiteDto entiteOriginale = null;
	/** mode création. */
	private boolean isCreation = true;

	/** date de début d'effet. */
	private String dDbEffet = null;
	
	private Date dtDbEffetOrigine = null;
	/** date de fin d'effet. */
	private String dFinEffet = null;
	/** date de migration sur Boe. */
	private String dMigBoe = null;
	/** heure de migration sur Boe. */
	private String hMigBoe = null;
	/** date de migration sur Boa. */
	private String dMigBoa = null;
	/** heure de migration sur Boa. */
	private String hMigBoa = null;
	
	/** permet de recevoir le code ICA MCI. */
	private String codeIcaMci = null;
	private String codeBanque = null;
	
	private DateApplicationDto dateApplication = null;
	private static final String MINUIT = "00:00";
	
	private Btmu10 btmu10;
	
	/** permet d'activer ou désactiver le bouton valider selon les différents contrôles de validation. */
	private boolean validationOk = true;

	public EntiteEditionBean(DateApplicationDto dateAppli) {
		btmu10 = new Btmu10();
		this.dateApplication = dateAppli;
	}
	
	public void creer(ActionEvent e) {
		creer();
	}
	
	/** creer. */
	public void creer() {
		initNewEntite();
	}

	/**
	 * modifier.
	 * @param entite L'entité à modifier.
	 */
	public void modifier(EntiteDto entite) {
		entiteOriginale = (EntiteDto) entite.clone();
		isCreation = false;

		if (null != entiteOriginale.getCodeBanque()) {
			codeBanque = "" + entiteOriginale.getCodeBanque();
		}
		
		if (null != entiteOriginale.getDtDbEffet()) {
			dtDbEffetOrigine = entiteOriginale.getDtDbEffet();
			dDbEffet = FormValidationUtil.SDF_JJMMYYYY.format(entiteOriginale.getDtDbEffet());
		}

		if (null != entiteOriginale.getDtFinEffet()) {
			dFinEffet = FormValidationUtil.SDF_JJMMYYYY.format(entiteOriginale.getDtFinEffet());
		}
		
		if (null != entiteOriginale.getCodeIcaMci()) {
			codeIcaMci = "" + entiteOriginale.getCodeIcaMci();
		}
		
		if (null == entiteOriginale.getDtDeMigrEntSurBoe()) {
			dMigBoe = "";
			hMigBoe = "";
		} else {
			dMigBoe = FormValidationUtil.SDF_JJMMYYYY.format(entiteOriginale.getDtDeMigrEntSurBoe());
			hMigBoe = FormValidationUtil.SDF_HHMM.format(entiteOriginale.getDtDeMigrEntSurBoe());
		}

		if (null == entiteOriginale.getDtDeMigrEntSurBoa()) {
			dMigBoa = "";
			hMigBoa = "";
		} else {
			dMigBoa = FormValidationUtil.SDF_JJMMYYYY.format(entiteOriginale.getDtDeMigrEntSurBoa());
			hMigBoa = FormValidationUtil.SDF_HHMM.format(entiteOriginale.getDtDeMigrEntSurBoa());
		}
	}

	/** Initialise l'entite originale avec une nouvelle entité. */
	private void initNewEntite() {
		entiteOriginale = new EntiteDto();
		isCreation = true;
		
		//init date début avec dateApplication +1jour
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(dateApplication.getDateApplication());
		c.add(GregorianCalendar.DAY_OF_YEAR, 1);
		
		dDbEffet = FormValidationUtil.SDF_JJMMYYYY.format(c.getTime());
		
		dFinEffet = "31/12/9999";
		
		dMigBoa = FormValidationUtil.SDF_JJMMYYYY.format(dateApplication.getDateApplication());
		dMigBoe = FormValidationUtil.SDF_JJMMYYYY.format(dateApplication.getDateApplication());
		hMigBoa = MINUIT;
		hMigBoe = MINUIT;
		codeIcaMci = "";
		codeBanque = "";
	}
	
	/**
	 * getEntiteOriginale.
	 * @return L'entite originale.
	 */
	public EntiteDto getEntiteOriginale() {
		return entiteOriginale;
	}
	
	/**
	 * setEntiteOriginale.
	 * @param entiteOriginale L'entité originale.
	 */
	public void setEntiteOriginale(EntiteDto entiteOriginale) {
		this.entiteOriginale = entiteOriginale;
	}

	/**
	 * isCreation.
	 * @return Vrai si on est en mode création.
	 */
	public boolean isCreation() {
		return isCreation;
	}

	/**
	 *	gethMigBoe.
	 * @return L'heure de migration de l'entité sur le BOE.
	 */
	public String gethMigBoe() {
		return hMigBoe;
	}
	
	/**
	 * sethMigBoe.
	 * @param hMigBoe L'heure de migration de l'entité sur le BOE.
	 */
	public void sethMigBoe(String hMigBoe) {
		this.hMigBoe = hMigBoe;
	}

	/**
	 * gethMigBoa.
	 * @return L'heure de migration de l'entité sur le BOA.
	 */
	public String gethMigBoa() {
		return hMigBoa;
	}
	
	/**
	 * sethMigBoa.
	 * @param hMigBoa L'heure de migration de l'entité sur le BOA.
	 */
	public void sethMigBoa(String hMigBoa) {
		this.hMigBoa = hMigBoa;
	}
	
	public void validerSaisie(ActionEvent e) {
		validerFormulaireEditionEntite();
	}
	
	/**
	 * validerFormulaireEditionEntite.
	 * @return Vrai si la validation réussit, faux autrement.
	 */
	public boolean validerFormulaireEditionEntite() {
		boolean success = true;

		Date dbEffet = SearchManager.stringToDate(dDbEffet);
		Date fnEffet = SearchManager.stringToDate(dFinEffet);
		
		Date dateAppli = dateApplication.getDateApplication();
		// En création ou modification (à venir), la date de début d'effet doit être postérieure à la date application.
		if (isCreation || entiteOriginale.getDtDbEffet().after(dateAppli)) {
			if (!dbEffet.after(dateAppli)) {
				btmu10.addMessage(Btmu10.MSG_ERREUR, "MSG3224");
				success = false;
			}
		}
		
		// En création ou en modification, la date de fin d'effet doit être postérieure à la date application.
		if (!fnEffet.after(dateAppli)) {
			btmu10.addMessage(Btmu10.MSG_ERREUR, "MSG3225");
			success = false;
		}

		// La date de fin d'effet doit être postérieure à la date de début d'effet.
		if (FormValidationUtil.debutApresFin(dbEffet, fnEffet)) {
			FormValidationUtil.addFormValidationMessage("gm.error.field.dateEffet", FormValidationUtil.ERROR_DEB_APRES_FIN);
			success = false;
		}
		
		//les champs suivants sont obligatoires, donc ne peuvent être null à cet endroit
		entiteOriginale.setDtDbEffet(dbEffet);
		entiteOriginale.setDtFinEffet(fnEffet);
		entiteOriginale.setCodeBanque(Integer.parseInt(codeBanque));
		entiteOriginale.setCodeIcaMci(Long.parseLong(codeIcaMci));

		//les champs suivants ne sont pas obligatoires, ils peuvent être nulls à cet endroit.
		
		// date migration boe et boa avec heure de migration (facultatif)
		if (null != dMigBoa) {
			String dtDeMigrEntSurBoa = dMigBoa;
			String hDeMigrEntSurBoa = hMigBoa;

			entiteOriginale.setDtDeMigrEntSurBoa(SearchManager.stringToDate(dtDeMigrEntSurBoa));
		
			hMigBoa = hDeMigrEntSurBoa;
			Calendar c = Calendar.getInstance();
			c.setTime(entiteOriginale.getDtDeMigrEntSurBoa());
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hMigBoa.substring(0, 2)));
			c.set(Calendar.MINUTE, Integer.parseInt(hMigBoa.substring(3, 5)));
			entiteOriginale.setDtDeMigrEntSurBoa(c.getTime());
			
		}
		
		if (null != dMigBoe) {
			String dtDeMigrEntSurBoe = dMigBoe;
			String hDeMigrEntSurBoe = hMigBoe;
	
			entiteOriginale.setDtDeMigrEntSurBoe(SearchManager.stringToDate(dtDeMigrEntSurBoe));
		
			hMigBoe = hDeMigrEntSurBoe;
			Calendar c = Calendar.getInstance();
			c.setTime(entiteOriginale.getDtDeMigrEntSurBoe());
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hMigBoe.substring(0, 2)));
			c.set(Calendar.MINUTE, Integer.parseInt(hMigBoe.substring(3, 5)));
			entiteOriginale.setDtDeMigrEntSurBoe(c.getTime());
		}

		// les entites ne sont pas à controler car issues de listes déroulantes
		// si creation et entite reglement ou chef de file, remplacer Entite échange par leur propre code banque
		if (entiteOriginale.getChefDeFile() != null) {
			if (null != entiteOriginale.getChefDeFile() && entiteOriginale.getChefDeFile().equals(-1)) {
				entiteOriginale.setChefDeFile(entiteOriginale.getCodeBanque());
			}
		}

		if (entiteOriginale.getCirc1CdBqReglCore() != null) {
			if (entiteOriginale.getCirc1CdBqReglCore().equals(-1)) {
				entiteOriginale.setCirc1CdBqReglCore(entiteOriginale.getCodeBanque());
			}
		}

		if (entiteOriginale.getCirc1CdBqReglCup() != null) {
			if (entiteOriginale.getCirc1CdBqReglCup().equals(-1)) {
				entiteOriginale.setCirc1CdBqReglCup(entiteOriginale.getCodeBanque());
			}
		}

		if (entiteOriginale.getCirc1CdBqReglInter() != null) {
			if (entiteOriginale.getCirc1CdBqReglInter().equals(-1)) {
				entiteOriginale.setCirc1CdBqReglInter(entiteOriginale.getCodeBanque());
			}
		}

		if (entiteOriginale.getCirc1CdBqReglIntra() != null) {
			if (entiteOriginale.getCirc1CdBqReglIntra().equals(-1)) {
				entiteOriginale.setCirc1CdBqReglIntra(entiteOriginale.getCodeBanque());
			}
		}

		if (entiteOriginale.getCirc1CdBqReglMci() != null) {
			if (entiteOriginale.getCirc1CdBqReglMci().equals(-1)) {
				entiteOriginale.setCirc1CdBqReglMci(entiteOriginale.getCodeBanque());
			}
		}

		if (entiteOriginale.getCirc1CdBqReglVisa() != null) {
			if (entiteOriginale.getCirc1CdBqReglVisa().equals(-1)) {
				entiteOriginale.setCirc1CdBqReglVisa(entiteOriginale.getCodeBanque());
			}
		}
		
		validationOk = success;
		
		return success;
    }
	
	/**
	 * Retourne les paramètre du message à générer s'il y a une ou des entités non référencées.
	 * @param referentiel Le contenu de la table du référentiel.
	 * @param entite L'entité à vérifier.
	 * @return Un tableau de 2 chaînes représentant respectivement la liste de valeurs et la liste de champs en erreur.
	 */
	public String[] verifierReferencementEntite(List<EntiteDto> referentiel, EntiteDto entite) {
		String[] result = null;
		
		// Récupération du rubriques.properties
		ResourceBundle bundle = ResourceBundle.getBundle("rubriques");
		
		// Préparation de la liste des codes banque
		List<Integer> listeCodesBanque = new ArrayList<Integer>();
		listeCodesBanque.add(null);
		for (EntiteDto e : referentiel) {
			listeCodesBanque.add(e.getCodeBanque());
		}
		
		// Prépration de la liste de champs à contrôler
		Map<String, Integer> mapControle = new HashMap<String, Integer>();
		mapControle.put("gm.ec030.el012", entite.getChefDeFile());
		mapControle.put("gm.ec030.el013", entite.getCdBanqueEntACompt());
		mapControle.put("gm.ec030.el015", entite.getCirc1CdBqReglInter());
		mapControle.put("gm.ec030.el016", entite.getCirc1CdBqReglIntra());
		mapControle.put("gm.ec030.el017", entite.getCirc1CdBqReglCore());
		mapControle.put("gm.ec030.el018", entite.getCirc1CdBqReglCup());
		mapControle.put("gm.ec030.el019", entite.getCirc1CdBqReglMci());
		mapControle.put("gm.ec030.el020", entite.getCirc1CdBqReglVisa());
		
		
		// Vérification de l'existence des données de l'entité courante et affichage d'un message si non.
		List<Integer> valeurs = new ArrayList<Integer>();
		List<String> champs = new ArrayList<String>();
				
		for (Entry<String, Integer> entry : mapControle.entrySet()) {
			Integer valeur = entry.getValue();
			if (!listeCodesBanque.contains(valeur)) {
				String libelle = bundle.getString(entry.getKey());
				champs.add(libelle);
				
				if (!valeurs.contains(valeur)) {
					valeurs.add(valeur);
				}
			}
		}
		
		if (!valeurs.isEmpty()) {
			result = new String[2];
			
			Converter codeBanqueConverter = new CodeBanqueConverter();

			Collections.sort(valeurs);
			
			String messageValeurs = "";
			String messageChamps = "";
			
			Iterator<Integer> iterValeurs = valeurs.iterator();
			while (iterValeurs.hasNext()) {
				Integer current = iterValeurs.next();
				String convertedCurrent = codeBanqueConverter.getAsString(null, null, current);
				messageValeurs += convertedCurrent;
				
				if (iterValeurs.hasNext()) {
					messageValeurs += ", ";
				}
			}
			
			Iterator<String> iterChamps = champs.iterator();
			while (iterChamps.hasNext()) {
				messageChamps += iterChamps.next();
				
				if (iterChamps.hasNext()) {
					messageChamps += ", ";
				}
			}
			
			result[0] = messageValeurs;
			result[1] = messageChamps;
		}
		
		return result;
	}

	public String getdDbEffet() {
		return dDbEffet;
	}

	public void setdDbEffet(String dDbEffet) {
		this.dDbEffet = dDbEffet;
	}

	public String getdFinEffet() {
		return dFinEffet;
	}

	public void setdFinEffet(String dFinEffet) {
		this.dFinEffet = dFinEffet;
	}

	public String getdMigBoe() {
		return dMigBoe;
	}

	public void setdMigBoe(String dMigBoe) {
		this.dMigBoe = dMigBoe;
	}

	public String getdMigBoa() {
		return dMigBoa;
	}

	public void setdMigBoa(String dMigBoa) {
		this.dMigBoa = dMigBoa;
	}

	public Date getDtDbEffetOrigine() {
		return dtDbEffetOrigine;
	}

	public void setDtDbEffetOrigine(Date dtDbEffetOrigine) {
		this.dtDbEffetOrigine = dtDbEffetOrigine;
	}

	public String getCodeIcaMci() {
		return codeIcaMci;
	}

	public void setCodeIcaMci(String codeIcaMci) {
		this.codeIcaMci = codeIcaMci;
	}

	public String getCodeBanque() {
		return codeBanque;
	}

	public void setCodeBanque(String codeBanque) {
		this.codeBanque = codeBanque;
	}

	public boolean isValidationOk() {
		return validationOk;
	}

	public void setValidationOk(boolean validationOk) {
		this.validationOk = validationOk;
	}	
}
