package com.cedicam.gm.bt.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.cedicam.gm.bt.service.ReponseService;
import com.cedicam.gm.bt.core.service.TypologieService;
import com.cedicam.gm.bt.data.model.Typologie;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;

public class StatutAcquisitionConverter extends GenericConverter implements Converter {

	private Map<String, String> statutMap = new HashMap<String, String>();
	private Map<String, String> statutMapInv = new HashMap<String, String>();

	public StatutAcquisitionConverter() {
		appelServiceListerTypologie();
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

		return statutMapInv.get(String.valueOf(value));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {

		return statutMap.get(String.valueOf(value));
	}

	/*** Récupération de la typologie du statut acquisition ***/
	public void appelServiceListerTypologie() {
		TypologieService typologieService = (TypologieService) ApplicationContextHolder.getContext().getBean("typologieService");

		ReponseService<List<Typologie>> reponse = typologieService.listerTypologie("StatutAcquisition", getContexteCedicam());
		List<Typologie> criteresStatut = reponse.getDonnees();

		for (Typologie typologie : criteresStatut) {
			statutMap.put(typologie.getCode(), typologie.getLibelle());
			statutMapInv.put(typologie.getLibelle(), typologie.getCode());
		}
	}
}
