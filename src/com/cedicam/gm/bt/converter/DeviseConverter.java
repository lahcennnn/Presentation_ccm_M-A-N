package com.cedicam.gm.bt.converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.core.clientwebservice.ReponseWebServiceCodeDeviseTab;
import com.cedicam.gm.bt.core.clientwebservice.ReponseWebServiceDateApplication;
import com.cedicam.gm.bt.core.webservice.DateWebService;
import com.cedicam.gm.bt.data.dto.DateApplicationDto;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.ch.data.dto.FonctionDto;
import com.cedicam.gm.ri.core.webservice.CodeDeviseWebService;
import com.cedicam.gm.ri.data.dto.CodeDeviseDto;

public class DeviseConverter extends GenericConverter implements Converter {
	
	private Map<String, String> deviseMap = new HashMap<String, String>();
	private Map<String, String> deviseMapInv = new HashMap<String, String>();
	
	public DeviseConverter() {
		chargerDevisesValides();
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

		return deviseMapInv.get(String.valueOf(value));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		String result = "";
		
		if (value != null && deviseMap.containsKey(value)) {
			result = deviseMap.get(String.valueOf(value));
		}
		return result;
	}
	
	public void chargerDevisesValides() {
		
		HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");		
		Map<String, String> mapDeviseSession = (Map<String, String>) containerSession.get("deviseMap");
		
		//si une liste est renseignée dans le context alors l'autre aussi
		if (mapDeviseSession == null) {
			
			CodeDeviseWebService deviseWebService = (CodeDeviseWebService) ApplicationContextHolder.getContext().getBean("codeDeviseWebService");
			ReponseWebServiceCodeDeviseTab rwsDevises = deviseWebService.listerDevisesPourTypologie(getContexteCedicam());
			
			if (rwsDevises.getNuerf() != ConstantesNuerf.TRAITEMENT_COMPLETED
					&& rwsDevises.getNuerf() != ConstantesNuerf.AUCUN_ENREGISTREMENT) {
				throw new ConverterException("une exception a été capturée lors de la récupération des devises");
			} else {
				CodeDeviseDto[] tabDevises = rwsDevises.getDonnees();
				if (tabDevises.length > 0) {
					List<CodeDeviseDto> lsDevises = Arrays.asList(rwsDevises.getDonnees());
					for (CodeDeviseDto dev : lsDevises) {
						deviseMap.put("" + dev.getCdDeviseNum(), dev.getCdDeviseAlpha());
						deviseMapInv.put(dev.getCdDeviseAlpha(), "" + dev.getCdDeviseNum());
					}
				}
			}
			
			containerSession.put("deviseMap", deviseMap);
			containerSession.put("deviseMapInv", deviseMapInv);
			
		} else {
			Map<String, String> mapDeviseInvSession = (Map<String, String>) containerSession.get("deviseMapInv");
			
			deviseMap = new HashMap<String, String>(mapDeviseSession);
			deviseMapInv = new HashMap<String, String>(mapDeviseInvSession);
		}
	}
}