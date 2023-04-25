package com.cedicam.gm.bt.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.bt.core.service.TraceService;
import com.cedicam.gm.bt.sgc.ContexteCedicam;
import com.cedicam.gm.bt.sgt.EnteteComposant;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;

public class OperandeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext paramFacesContext, UIComponent paramUIComponent, String paramString) throws ConverterException {

		TraceService traceService = (TraceService) ApplicationContextHolder.getContext().getBean(
		"traceService");
		EnteteComposant enteteComposant = new EnteteComposant();
		enteteComposant.setIdComp("OperandeConverter");
		enteteComposant.setLibAppli("Centre Comptable Monétique");
		enteteComposant.setLibComp("OperandeConverter");
		enteteComposant.setNivArchi("");
		enteteComposant.setNomClasse(this.getClass().getName());
		enteteComposant.setTypComp(ConstantesTrace.TYPE_COMPOSANT_WS);
		enteteComposant.setVersAppli(ConstantesTrace.PROJECT_VERSION);

		traceService.enregistrerTraceDebug("OperandeConverter | getAsObject",
				"Entrée : " + paramString, enteteComposant, new ContexteCedicam());

		if (paramString != null) {
			if (paramString.equals(ConstantesTypo.OPERANDE_EQUAL)) {
				traceService.enregistrerTraceDebug("OperandeConverter | getAsObject",
						"Sortie : Boolean(true)", enteteComposant, new ContexteCedicam());
				return new Boolean(true);
			} else {
				if (paramString.equals(ConstantesTypo.OPERANDE_NOT_EQUAL)) {
					traceService.enregistrerTraceDebug("OperandeConverter | getAsObject",
							"Sortie : Boolean(false)", enteteComposant, new ContexteCedicam());
					return new Boolean(false);
				}
			}
		}
		traceService.enregistrerTraceDebug("OperandeConverter | getAsObject",
				"Sortie : null", enteteComposant, new ContexteCedicam());

		return null;
	}

	@Override
	public String getAsString(FacesContext paramFacesContext, UIComponent paramUIComponent, Object paramObject) throws ConverterException {
		TraceService traceService = (TraceService) ApplicationContextHolder.getContext().getBean(
		"traceService");
		EnteteComposant enteteComposant = new EnteteComposant();
		enteteComposant.setIdComp("OperandeConverter");
		enteteComposant.setLibAppli("Centre Comptable Monétique");
		enteteComposant.setLibComp("OperandeConverter");
		enteteComposant.setNivArchi("");
		enteteComposant.setNomClasse(this.getClass().getName());
		enteteComposant.setTypComp(ConstantesTrace.TYPE_COMPOSANT_WS);
		enteteComposant.setVersAppli(ConstantesTrace.PROJECT_VERSION);

		String objClasse = "noClass";
		try {
			objClasse = paramObject.getClass().getName();
		} catch (Exception ex) {

		}
		traceService.enregistrerTraceDebug("OperandeConverter | getAsString",
				"Entrée : " + objClasse + " | " + paramObject, enteteComposant, new ContexteCedicam());

		if (paramObject != null) {
			if (paramObject instanceof Boolean) {
				Boolean operande = (Boolean) paramObject;
				if (operande) {
					traceService.enregistrerTraceDebug("OperandeConverter | getAsString",
							"Sortie : " + ConstantesTypo.OPERANDE_EQUAL, enteteComposant, new ContexteCedicam());
					return ConstantesTypo.OPERANDE_EQUAL;
				} else {
					traceService.enregistrerTraceDebug("OperandeConverter | getAsString",
							"Sortie : " + ConstantesTypo.OPERANDE_NOT_EQUAL, enteteComposant, new ContexteCedicam());
					return ConstantesTypo.OPERANDE_NOT_EQUAL;
				}
			} else if (paramObject instanceof String && !paramObject.equals("")) {
				if ("true".equals(paramObject)) {
					traceService.enregistrerTraceDebug("OperandeConverter | getAsString",
							"Sortie : " + ConstantesTypo.OPERANDE_NOT_EQUAL, enteteComposant, new ContexteCedicam());
					return ConstantesTypo.OPERANDE_EQUAL;
				} else if ("false".equals(paramObject)) {
					traceService.enregistrerTraceDebug("OperandeConverter | getAsString",
							"Sortie : " + ConstantesTypo.OPERANDE_NOT_EQUAL, enteteComposant, new ContexteCedicam());
					return ConstantesTypo.OPERANDE_NOT_EQUAL;
				} else {
					traceService.enregistrerTraceDebug("OperandeConverter | getAsString",
							"Sortie : " + paramObject, enteteComposant, new ContexteCedicam());
					return "" + paramObject;
				}
			}
		}
		traceService.enregistrerTraceDebug("OperandeConverter | getAsString",
				"Sortie : --null--", enteteComposant, new ContexteCedicam());
		return null;
	}

}
