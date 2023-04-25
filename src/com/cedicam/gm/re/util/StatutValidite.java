package com.cedicam.gm.re.util;

/**
 * @author SALTAZI
 */
public class StatutValidite {
	/** Le code du statut. */
	private String code;
	/** Le libelle du statut. */
	private String libelle;
	
	/** Constructeur.
	 * @param code Le code du statut.
	 * @param libelle Le libell� du statut.
	 */
	public StatutValidite(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}

	/**
	 * @return Le code du statut de validit�.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return Le libell� du statut de validit�.
	 */
	public String getLibelle() {
		return libelle;
	}
}
