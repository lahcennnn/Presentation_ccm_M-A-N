package com.cedicam.gm.bt.filtre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.core.service.EvenementService;
import com.cedicam.gm.bt.core.service.TraceService;
import com.cedicam.gm.bt.exception.ErreurTechnique;
import com.cedicam.gm.bt.exception.LdapAccessException;
import com.cedicam.gm.bt.exception.LdapNoProfileException;
import com.cedicam.gm.bt.exception.LdapNoRightException;
import com.cedicam.gm.bt.exception.LdapUnknownUserException;
import com.cedicam.gm.bt.outil.Chronometre;
import com.cedicam.gm.bt.sgc.ContexteCedicam;
import com.cedicam.gm.bt.sgt.EnteteComposant;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.ch.core.clientwebservice.ReponseWebServiceFonctionTab;
import com.cedicam.gm.ch.core.webservice.HabilitationProfilFonctionWebService;
import com.cedicam.gm.ch.data.dto.FonctionDto;


/**
 * Servlet Filter implementation class HabilitationFilter
 */
public class HabilitationFilter implements Filter {

	 private static String PREFIX_HEADER = "subject=emailAddress%3d";
	 private static String ID_COMP = "BTFLHAB";

    /**
     * Default constructor.
     */
    public HabilitationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	protected EnteteComposant getEnteteComposant(String idComp, String libComp) {
		EnteteComposant enteteComposant = new EnteteComposant();
		enteteComposant.setIdComp(idComp);
		enteteComposant.setLibAppli("Centre Comptable Monétique");
		enteteComposant.setLibComp(libComp);
		enteteComposant.setNivArchi((idComp != null && idComp.length() > 1) ? idComp.substring(0, 2) : "");
		enteteComposant.setNomClasse(this.getClass().getName());
		enteteComposant.setTypComp(ConstantesTrace.TYPE_COMPOSANT_FWK);
		enteteComposant.setVersAppli(ConstantesTrace.PROJECT_VERSION);
		return enteteComposant;
	}


	protected ContexteCedicam getContexteCedicam() {
		ContexteCedicam contexteCedicam = new ContexteCedicam();
		return contexteCedicam;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String uri = ((HttpServletRequest) request).getRequestURI();
		Map<String, String> projectProperties = ApplicationContextHolder.getProjectProperties();
		Boolean accesParBouchon = Boolean.valueOf(projectProperties.get("gm.commun.accesUrlBouchon"));
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		System.out.println("Entrée dans habilitationFilter avec uri cible : " + uri
				+ " et session " + session != null?"RENSEIGNéE":"NULLE");
		
		if (uri.equals("/CCM/views/com/cedicam/gm/ch/ec001.bean") && !accesParBouchon && session != null) {
			int nbrFonction = 0;
			int rws = 0;
			String user = "";
			String nomMethode = "doFilter";
			
			RuntimeException exception = null;
	
			TraceService traceService = (TraceService) ApplicationContextHolder.getTraceService();
			EvenementService evenementService = (EvenementService) ApplicationContextHolder.getEvenementService();
	
			EnteteComposant enteteComposant = getEnteteComposant(ID_COMP, "Filtre Habilitation");
			ContexteCedicam contexteCedicam = getContexteCedicam();
	
			Chronometre chronometre = new Chronometre(nomMethode, "Filtre d'habilitation", contexteCedicam, enteteComposant);
			chronometre.start(true);
	
			// trace debut traitement
			traceService.enregistrerTraceDebug(nomMethode,
					"debut du traitement pour Lister fonction de HabilitationProfilFonction ", enteteComposant, contexteCedicam);
	
			try {
				traceService.enregistrerTraceDebug(nomMethode, "La request : " + request.getLocalAddr(), enteteComposant, contexteCedicam);
				traceService.enregistrerTraceDebug(nomMethode, "On va chercher containerSession", enteteComposant, contexteCedicam);
				//Object managedBean = RecupererMangedBean.getInstanceSessionOf("habilitation");
				HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
	
	
				List<FonctionDto> listFonctionSession = (List<FonctionDto>) containerSession.get("fonctionList");
				if (listFonctionSession != null) {
					traceService.enregistrerTraceDebug(nomMethode, "listFonctionSession trouvée", enteteComposant, contexteCedicam);
				}
				else {
					traceService.enregistrerTraceDebug(nomMethode, "listFonctionSession est null", enteteComposant, contexteCedicam);
					HttpServletRequest httpRequest = (HttpServletRequest) request;
	
					traceService.enregistrerTraceDebug(nomMethode, "Création d'un nouveau Bean Habilitation", enteteComposant, contexteCedicam);
					listFonctionSession = new ArrayList<FonctionDto>(); //HabilitationBean habilitations = new HabilitationBean();
	
					// Pour debug, trace les header HTTP de la requête
					traceService.enregistrerTraceDebug(nomMethode, "*** Requête HTTP, headers : ", enteteComposant, contexteCedicam);
					Enumeration headers = httpRequest.getHeaderNames();
					while(headers.hasMoreElements()) {
						String titreHeader = "" + headers.nextElement();
						String contenuHeader = httpRequest.getHeader(titreHeader);
						traceService.enregistrerTraceDebug(nomMethode, "  - " + titreHeader + " --> " + contenuHeader , enteteComposant, contexteCedicam);
					}
	
					String clientCert = httpRequest.getHeader("client-cert");
					traceService.enregistrerTraceDebug("doFilter", "Récupération du client-cert : " + clientCert, enteteComposant, contexteCedicam);
					
					if (null == clientCert || clientCert.trim().isEmpty() || clientCert.indexOf(PREFIX_HEADER) < 0) {
						user = null;
						traceService.enregistrerTraceDebug(nomMethode, "Récupération du user impossible : header http '" + clientCert + "' erroné.", enteteComposant, contexteCedicam);
					} else {
						int indexUserDebut = clientCert.indexOf(PREFIX_HEADER)	+  PREFIX_HEADER.length();
						int indexUserFin = clientCert.indexOf("&", indexUserDebut);
						if (indexUserFin < indexUserDebut) {
							indexUserFin = clientCert.length();
						}
	
						user = clientCert.substring(indexUserDebut, indexUserFin);
						user = user.trim();
	
						// Il est possible que dans le header reçu l'arobase soit remplacée par
						// son code HTML. ==> On rétablit.
						user = user.replaceAll("%40", "@");
						traceService.enregistrerTraceDebug(nomMethode, "Récupération du user : " + user, enteteComposant, contexteCedicam);
	
	
						// on défini le user de connexion dans le contexte
						contexteCedicam.setIdagt(user);
					}
	
					traceService.enregistrerTraceDebug(nomMethode, "Le mail du user : " + user, enteteComposant, contexteCedicam);
					HabilitationProfilFonctionWebService habProfilFctWebService =
						(HabilitationProfilFonctionWebService) ApplicationContextHolder.getContext().getBean("habilitationProfilFonctionWebService");
	
					ReponseWebServiceFonctionTab listerFonctions = habProfilFctWebService.listerFonctionsUser(user, contexteCedicam);
	
					rws = listerFonctions.getNuerf();
					
					traceService.enregistrerTraceDebug(nomMethode,"Code retour WS récupération fonction pour user:  " 
							+ rws, enteteComposant, contexteCedicam);
					
					switch (rws) {
					case ConstantesNuerf.TRAITEMENT_COMPLETED:
						if (listerFonctions != null) {
	
							FonctionDto[] donnees = listerFonctions.getDonnees();
							if (donnees != null) {
								nbrFonction = donnees.length;
								listFonctionSession = Arrays.asList(donnees);
							}
						}
						traceService.enregistrerTraceDebug(nomMethode, "Mise en session des " + nbrFonction
								+ " fonctions habilitées du user : " + user, enteteComposant, contexteCedicam);
	
						break;
					case ConstantesNuerf.PARAMETRE_ABSENT:
						// L'utilisateur passé en paramètre n'a pas renseigné => problème de client-cert
						evenementService.emettreEvenement(nomMethode, 
								ConstantesEvtTech.EVT_ERREUR_CLIENT_CERT, ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL,
								"MSG3199", enteteComposant, contexteCedicam);	
						exception = new LdapUnknownUserException();
						break;
					case ConstantesNuerf.ERREUR_ACCES_LDAP:
						// Problème d'accès au LDAP
						evenementService.emettreEvenement(nomMethode, 
								ConstantesEvtTech.EVT_LDAP_INACCESSIBLE, ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL,
								"MSG3200", enteteComposant, contexteCedicam);	
						exception = new LdapAccessException();
						break;
					case ConstantesNuerf.AUCUN_PROFIL:
						// L'utilisateur n'a pas de profil
						evenementService.emettreEvenement(nomMethode, 
								ConstantesEvtTech.EVT_AUCUN_PROFIL, ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL,
								"MSG3201", new String[] {user}, enteteComposant, contexteCedicam);
						exception = new LdapNoProfileException();
						break;				
					case ConstantesNuerf.AUCUNE_FCT_HABILITEE:
					case ConstantesNuerf.AUCUN_PROFIL_CCM:
					default:
						// L'utilisateur n'a pas de fonction habilitée
						evenementService.emettreEvenement(nomMethode, 
								ConstantesEvtTech.EVT_AUCUNE_FONCTION_HABILITE, ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL,
								"MSG3202", new String[] {user}, enteteComposant, contexteCedicam);
						exception = new LdapNoRightException();
						break;
					}
					
					//on supprime le bean st000 car la valorisation des habilitations qu'il contient est effectuée à la création
					((HttpServletRequest) request).getSession(false).removeAttribute("st000");
					
					/// mise en session : liste fonction, contexte, retour habilitation
					if (!listFonctionSession.isEmpty()) {
						containerSession.put("fonctionList", listFonctionSession);
					}
					containerSession.put("contexteCedicam", contexteCedicam);
					containerSession.put("retourHabilitation", rws);
				}
				traceService.enregistrerTraceDebug(nomMethode,
						"Fin du traitement pour Lister fonction de HabilitationProfilFonction ", enteteComposant, contexteCedicam);
			} catch (ErreurTechnique et) {
				evenementService.emettreEvenement("habilitationProfilFonctionWebService", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, ConstantesTrace.CATEGORIE_ERREUR,
						ConstantesTrace.GRAVITE_FATAL, "MSG0097", et, enteteComposant, contexteCedicam);
				rws = ConstantesNuerf.ERREUR_TECHNIQUE;
			} catch (Throwable e) {
				evenementService.emettreEvenement("habilitationProfilFonctionWebService", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE, ConstantesTrace.CATEGORIE_ERREUR,
					ConstantesTrace.GRAVITE_FATAL, "MSG0097", e, enteteComposant, contexteCedicam);
				rws = ConstantesNuerf.ERREUR_TECHNIQUE;
				exception = new RuntimeException(e);
			} finally {
//				HashMap containerSession = (HashMap) ApplicationContextHolder.getContext().getBean("containerSession");
//				
//				// Dans tous les cas on enregistre le CR du WS afin pister les plantons techniques
//				if (ConstantesNuerf.TRAITEMENT_COMPLETED != rws) {
//					containerSession.put("fonctionList", new ArrayList<FonctionDto>());
//				}
//				containerSession.put("retourHabilitation", rws);
				
				chronometre.stop(true);
			}
			
			if (null != exception) {
				throw exception;
			} else {
				// pass the request along the filter chain
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
