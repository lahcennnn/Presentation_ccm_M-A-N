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
import com.cedicam.gm.bt.sgc.ContexteCedicam;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;

public class NiveauAgregationConverter extends GenericConverter implements Converter {

	private Map<String, String> deviseMap = new HashMap<String, String>();
	private Map<String, String> deviseMapInv = new HashMap<String, String>();

	public NiveauAgregationConverter() {
		appelServiceListerTypologie();
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

		return deviseMapInv.get(String.valueOf(value));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {

		return deviseMap.get(String.valueOf(value));
	}

	/*** Récupération de la typologie des devises ***/
	public void appelServiceListerTypologie() {
		ContexteCedicam contexteCedicam = getContexteCedicam();
		TypologieService typologieService = (TypologieService) ApplicationContextHolder.getContext().getBean("typologieService");

		ReponseService<List<Typologie>> reponse = typologieService.listerTypologie("NiveauAgregation", contexteCedicam);
		List<Typologie> criteresStatut = reponse.getDonnees();

		for (Typologie typologie : criteresStatut) {
			deviseMap.put(typologie.getCode(), typologie.getLibelle());
			deviseMapInv.put(typologie.getLibelle(), typologie.getCode());
		}
	}
}