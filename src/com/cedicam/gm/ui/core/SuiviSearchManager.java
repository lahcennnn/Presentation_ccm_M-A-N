/**
 *
 */
package com.cedicam.gm.ui.core;

import java.util.Date;

import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.bt.data.dto.GenericDto;
import com.cedicam.gm.cm.data.dto.CriteresPourSuiviDto;
import com.cedicam.gm.cm.data.dto.SuiviRapprochementDto;
import com.cedicam.gm.re.bean.EntiteServicesBean;

/**
 * @author cdenis
 *
 */
public class SuiviSearchManager extends SearchManager {

	private SuiviRapprochementDto resultatCourant;
	private boolean criterePrimaireChanged = false;
	public boolean isCriteresSecondaires() {

		return !criterePrimaireChanged && resultatCourant!=null && resultatCourant.getListeRapprochements()!=null && resultatCourant.getListeRapprochements().length>0;
	}


	public void changerCriterePrimaire(){
		resultatCourant = null;
		this.resetSecondaires();
	}

	public void loadResultatRecherche(GenericDto gdto){
		SuiviRapprochementDto dto = (SuiviRapprochementDto)gdto;
		criterePrimaireChanged = false;
		resultatCourant = dto;
		if(dto==null)return;
		reset(listEntiteEchange, EntiteServicesBean.toSelectItemList(dto.getListeEntiteEchange()),TOUTES);
		reset(listEntiteEchangeEmettrice, EntiteServicesBean.toSelectItemList(dto.getListeEntiteEchangeEmettrice()),TOUTES);
		reset(listEntiteEchangeDestinataire, EntiteServicesBean.toSelectItemList(dto.getListeEntiteEchangeDestinataire()),TOUTES);
		reset(listEntiteCompta, EntiteServicesBean.toSelectItemList(dto.getListeEntiteAComptabiliser()),TOUTES);
		reset(listEntiteComptaEmettrice, EntiteServicesBean.toSelectItemList(dto.getListeEntiteAComptabiliserEmettrice()),TOUTES);
		reset(listEntiteComptaDestinataire, EntiteServicesBean.toSelectItemList(dto.getListeEntiteAComptabiliserDestinataire()),TOUTES);
		reset(listReferenceComptable, toSelectItemList(dto.getListeReferenceComptable()),TOUTES);
		reset(listCodeOperationNfcca, toSelectItemList(dto.getListeCodeOperation()),TOUS);
		reset(listFichierReferenceRemise, toSelectItemList(dto.getListeIdFichier()),TOUS);
	}

	public boolean isReseauInter() {
		return ConstantesTypo.CIRCUIT_1_BQ_REGL_INTER.equals(getCriteres().getReseau());
	}

	public boolean isReseauTous() {
		return CriteresPourSuiviDto.TOUS.equals(getCriteres().getReseau()) || null == getCriteres().getReseau();
	}

	public boolean isReseauAutre() {
		return !isReseauTous() && !isReseauInter();
	}

	public boolean isSensTous() {
		return CriteresPourSuiviDto.TOUS.equals(getCriteres().getSens()) || null == getCriteres().getSens();
	}

	public void reset(Date dateApplication){
		resultatCourant = null;
		criterePrimaireChanged = false;
		super.reset(dateApplication);
	}

}
