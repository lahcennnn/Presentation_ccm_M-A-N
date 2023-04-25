/**
 * 
 */
package com.cedicam.gm.ui.core;

import com.cedicam.gm.bt.data.dto.GenericDto;

/**
 * @author CDENIS
 *
 */
public class ElementFilAriane extends GenericDto {
	public String libelle = " ";
	public String outcome = null;
	
	public ElementFilAriane(String lib, String out) {
		libelle = lib;
		outcome = out;
	}
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	/* (non-Javadoc)
	 * @see com.cedicam.gm.bt.data.dto.GenericDto#getId()
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return libelle;
	}
}
