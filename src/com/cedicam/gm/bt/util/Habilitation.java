/**
 * <p><b>MOTIVATION</b><BR>
 * <I>Sans objet</I>
 * <p><b>DESCRIPTION</b><BR>
 * <I>Sans objet</I>
 * <p><b>INDICATION D'UTILISATION</b><BR>
 * <I>Sans objet</I>
 * <p><b>DOCUMENTS DE REFERENCE</b><BR>
 * <I>Sans objet</I>
 *
 * <p><b>DATE CREATION</b><BR>
 * <I>9 sept. 2010</I>
 * <p><b>AUTEUR</b><BR>
 * <I>Valéry BONNET</I>
 * <p><b>SOCIETE</b><BR>
 * <I>Capgemini</I>
 * <p><b>CONTACT</b><BR>
 * <I>valery.bonnet@capgemini.com</I>
 * <b>Historique des revisions:</b>
 * <ul>
 *   <li>9 sept. 2010 creation<li>
 * </ul>
 */

package com.cedicam.gm.bt.util;

/**
 * @author Administrateur
 *
 */
public class Habilitation {

	Boolean isHabilite;
	/**
	 * 
	 */
	public Habilitation() {
		super();
	}
	

	
	/**
	 * @return the isHabilite
	 */
	public Boolean getIsHabilite() {
		return isHabilite;
	}



	/**
	 * @param isHabilite the isHabilite to set
	 */
	public void setIsHabilite(Boolean isHabilite) {
		this.isHabilite = isHabilite;
	}



	public String toString() {
		return "["
		+ "isHabilite : " + this.isHabilite + ", ";

	}
}