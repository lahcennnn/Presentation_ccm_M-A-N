package com.cedicam.gm.bt.converter;

import java.util.HashMap;

import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.core.service.EvenementService;
import com.cedicam.gm.bt.core.service.TraceService;
import com.cedicam.gm.bt.sgc.ContexteCedicam;
import com.cedicam.gm.bt.sgt.EnteteComposant;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;

public abstract class GenericConverter {


	protected TraceService traceService = null;

	protected EvenementService evenementService = null;

	protected EnteteComposant enteteComposant;

	private ContexteCedicam contexteCedicam;


	public GenericConverter() {
		traceService = (TraceService) ApplicationContextHolder.getTraceService();
		evenementService = (EvenementService) ApplicationContextHolder.getEvenementService();

		contexteCedicam = loadContexteCedicam();

	}

	protected EnteteComposant setEnteteComposant(String idComp, String libComp) {
		enteteComposant = new EnteteComposant();
		enteteComposant.setIdComp(idComp);
		enteteComposant.setLibAppli("Centre comptable monétique");
		enteteComposant.setLibComp(libComp);
		enteteComposant.setNivArchi((idComp != null && idComp.length() > 1) ? idComp.substring(0, 2) : "");
		enteteComposant.setNomClasse(this.getClass().getName());
		enteteComposant.setTypComp(ConstantesTrace.TYPE_COMPOSANT_FWK);
		enteteComposant.setVersAppli(ConstantesTrace.PROJECT_VERSION);
		return enteteComposant;
	}

	public EnteteComposant getEnteteComposant() {
		return enteteComposant;
	}

	public void setEnteteComposant(EnteteComposant enteteComposant) {
		this.enteteComposant = enteteComposant;
	}

	public ContexteCedicam getContexteCedicam() {
		return loadContexteCedicam();
	}

	public ContexteCedicam loadContexteCedicam() {
		HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
		return (ContexteCedicam) containerSession.get("contexteCedicam");
	}

	public void setContexteCedicam(ContexteCedicam contexteCedicam) {
		this.contexteCedicam = contexteCedicam;
		HashMap containerSession =
			(HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
		containerSession.put("contexteCedicam", contexteCedicam);
	}


	public TraceService getTraceService() {
		return traceService;
	}

	public void setTraceService(TraceService traceService) {
		this.traceService = traceService;
	}

	public EvenementService getEvenementService() {
		return evenementService;
	}

	public void setEvenementService(EvenementService evenementService) {
		this.evenementService = evenementService;
	}
}
