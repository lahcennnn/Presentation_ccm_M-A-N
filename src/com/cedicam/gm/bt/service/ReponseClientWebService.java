/**
 * <p><b>MOTIVATION</b><BR>
 * <I>Sans objet</I>
 * <p><b>DESCRIPTION</b><BR>
 * <I>Classe de retour de webservice</I>
 * <p><b>INDICATION D'UTILISATION</b><BR>
 * <I>Sans objet</I>
 * <p><b>DOCUMENTS DE REFERENCE</b><BR>
 * <I>Sans objet</I>
 *
 * <p><b>DATE CREATION</b><BR>
 * <I>24 août 2010</I>
 * <p><b>AUTEUR</b><BR>
 * <I>Valéry BONNET</I>
 * <p><b>SOCIETE</b><BR>
 * <I>Capgemini</I>
 * <p><b>CONTACT</b><BR>
 * <I>valery.bonnet@capgemini.com</I>
 * <b>Historique des revisions:</b>
 * <ul>
 *   <li>24 août 2010 creation<li>
 * </ul>
 */
package com.cedicam.gm.bt.service;

import java.io.Serializable;

import com.cedicam.gm.bt.sgc.ContexteCedicam;

public class ReponseClientWebService<T> implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int nuerf;
	private String lierf;
	protected T donnees;
	private ContexteCedicam contexteCedicam;

	/**
	 * @return the returnObject
	 */
	public T getData() {
		return donnees;
	}

	/**
	 * @param returnObject the returnObject to set
	 */
	public void setData(T data) {
		this.donnees = data;
	}


	/**
	 * @return the nuerf
	 */
	public int getNuerf() {
		return nuerf;
	}

	/**
	 * @param nuerf the nuerf to set
	 */
	public void setNuerf(int nuerf) {
		this.nuerf = nuerf;
	}

	/**
	 * @return the lierf
	 */
	public String getLierf() {
		return lierf;
	}

	/**
	 * @param lierf the lierf to set
	 */
	public void setLierf(String lierf) {
		this.lierf = lierf;
	}

	/**
	 * @return le contexte Cedicam
	 */
	public ContexteCedicam getContexteCedicam() {
		return contexteCedicam;
	}

	/**
	 * @param contexteCedicam the contexteCedicam to set
	 */
	public void setContexteCedicam(ContexteCedicam contexteCedicam) {
		this.contexteCedicam = contexteCedicam;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer strDonnees = new StringBuffer();
		if (donnees != null) {
			if (donnees instanceof Object[]) {
				Object[] tabObj = (Object[]) donnees;

				for (int index=0; index < tabObj.length; index++) {
					strDonnees.append(tabObj[index].toString()).append(' ');
				}
			} else {
				strDonnees.append(donnees.toString());
			}
		}
		return "nuerf=" + nuerf + " lierf=" + lierf + " données=" + strDonnees.toString();
	}
}
