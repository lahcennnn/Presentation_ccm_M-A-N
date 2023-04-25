/**
 *
 */
package com.cedicam.gm.ui.core;

import com.cedicam.gm.bt.data.dto.GenericDto;
import com.cedicam.gm.cm.data.dto.EcartsRapprochementDto;
import com.cedicam.gm.re.bean.EntiteServicesBean;

/**
 * @author cdenis
 * Classe pour gérer les paramètres et la réponse du WS.
 */
public class EcartSearchManager extends SearchManager {

	private EcartsRapprochementDto ecartsRapprochementDto;
	private Boolean selectedStatus[] = new Boolean[]{false, false, false};

	public EcartSearchManager() {
		super();
	}

	public boolean updateCriteresWithRequestParameters() {
		boolean succes = true;
		succes = super.updateCriteresWithRequestParameters();
		return succes;
	}

	@Override
	public void loadResultatRecherche(GenericDto gdto) {
		EcartsRapprochementDto dto = (EcartsRapprochementDto) gdto;
		ecartsRapprochementDto = dto;
		if(dto==null)return;
//		if (null == criteres.getEntiteCompta() || (null != criteres.getEntiteCompta() && criteres.getEntiteCompta().isEmpty())) {
			reset(listEntiteCompta, EntiteServicesBean.toSelectItemList(dto.getListEntiteCompta()),TOUTES);
//		}
//		if (null == criteres.getEntiteEchange() || (null != criteres.getEntiteEchange() && criteres.getEntiteEchange().isEmpty())) {
			reset(listEntiteEchange, EntiteServicesBean.toSelectItemList(dto.getListEntiteEchange()),TOUTES);
//		}
	}

	@Override
	public boolean isCriteresSecondaires() {
		boolean retour = false;
		if (ecartsRapprochementDto != null) {
			if (ecartsRapprochementDto.getListEntiteCompta() != null
					&& ecartsRapprochementDto.getListEntiteEchange() != null) {
				if (ecartsRapprochementDto.getListEntiteCompta().length > 0
						|| ecartsRapprochementDto.getListEntiteEchange().length > 0) {
					retour = true;
				}
			}
		}
		return retour;
	}

	public EcartsRapprochementDto getEcartsRapprochementDto() {
		return ecartsRapprochementDto;
	}

	public void setEcartsRapprochementDto(
			EcartsRapprochementDto ecartsRapprochementDto) {
		this.ecartsRapprochementDto = ecartsRapprochementDto;
	}

	public Boolean[] getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(Boolean[] selectedStatus) {
		this.selectedStatus = selectedStatus;
	}


}
