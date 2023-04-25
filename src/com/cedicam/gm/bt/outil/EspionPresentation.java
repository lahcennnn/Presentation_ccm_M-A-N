package com.cedicam.gm.bt.outil;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.core.clientwebservice.ReponseWebServiceParametresApplicatifsTab;
import com.cedicam.gm.bt.core.webservice.ParametreWebService;
import com.cedicam.gm.bt.data.dto.ParametresApplicatifsDto;
import com.cedicam.gm.bt.sgc.ContexteCedicam;


/**
    /**
     * <b>DESCRIPTION</b><BR>
     * Classe Espion.
     * Permet de vérifier le fonctionnement des couches présentation et métier et base de données de l'application
     * @see Servlet
     * <p>
     * <b>AUTEUR</b><BR>
     * <I>Valery BONNET</I>
     * </p>
     * <p>
     * <b>DATE CREATION</b><BR>
     * <I>16 novembre 2010</I>
     * </p>
     * <p>
     * <b>SOCIETE</b><BR>
     * <I>Capgemini</I>
     * </p>
     * <p>
     * <b>CONTACT</b><BR>
     * <I>valery.bonnet@capgemini.com</I>
     * </p>
     *
     * <p><b>Code service interne: BTS041M012</b></p>
     * <p><b>Code service externe: sans objet</b></p>
     *
     * <b>ALGORITHME</b>
     * <ul>
     * <li>Cette classe implémente une Servlet destinée à récupérer un paramètre applicatif en base.</li>
     * <li>Pour ce faire, elle sollicite le web service de recherche de paramètre ALPHANUMERIQUE, pour récupérer le code ICA (du Cedicam) </li>
     * <li>Sollicitation du service com.cedicam.gm.bt.core.ParametreService avec en entrée le code paramètre "PARAP003"</li>
     * </ul>
     *
     * <b>PARAMETRAGE</b>
     * <p>
     * Sans objet
     * </p>
     *
     * <b>EVENEMENTS EMIS</b>
     * Sans objet
     *
     * <b>INTERFACE</b><BR>
     * Sollicitable à L'URL suivante
     * <Url Application>/CCM-Presentation/EspionPresentation
     *
     * @return HttpResponse contenant une page présentant la valeur du paramètre en base de données (XXXXX - code Cedicam a priori)
     *
     * @exception à compléter
     */

public class EspionPresentation extends HttpServlet{

		private static final long serialVersionUID = 1L;
		private ParametreWebService parametreBeanWS;
		private static final String CODE_PARAM_A_RECUPERER = "PARAP004";

		/**
		 * @see Servlet#init(ServletConfig)
		 */
		public void init(ServletConfig config) throws ServletException {
		    super.init(config);
		    final WebApplicationContext context =
		        WebApplicationContextUtils.getWebApplicationContext(
		            config.getServletContext());
		    this.setParametreBeanWS((ParametreWebService) context.getBean("parametreWebService"));
		}

		/**
		 * @return the parametreBeanWS
		 */
		protected ParametreWebService getParametreBeanWS() {
			return parametreBeanWS;
		}

		/**
		 * @param parametreBeanWS the parametreBeanWS to set
		 */
		protected void setParametreBeanWS(ParametreWebService parametreBeanWS) {
			this.parametreBeanWS = parametreBeanWS;
		}

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doAction(request, response);
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doAction(request, response);
		}
		/**
		 * @see HttpServlet#doAction(HttpServletRequest request, HttpServletResponse response)
		 */
		@Transactional(readOnly = false)
		protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			try
			{
				ContexteCedicam contextecedicam = new ContexteCedicam();
				ReponseWebServiceParametresApplicatifsTab rwsParamWS = parametreBeanWS.rechercherParametre(contextecedicam,EspionPresentation.CODE_PARAM_A_RECUPERER);
				PrintWriter out = response.getWriter();
				String valeurParam = "";
				if ((rwsParamWS.getDonnees() != null)
						&& (rwsParamWS.getDonnees().length > 0)) {
					String[] tabParam = new String[]{" "};
					ParametresApplicatifsDto[] resParam  = rwsParamWS.getDonnees();

					valeurParam = "" + resParam[0].getValeurParametreNum();

				}

				response.setContentType("text/xml");
				out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
				out.println("<ReponseEspion>");
				out.println("<CodeRetour>" + rwsParamWS.getNuerf());
				out.println("</CodeRetour>");
				out.println("<Valeur>" + valeurParam);
				out.println("</Valeur>");
				out.println("</ReponseEspion>");
				out.close();
			}
			catch (Throwable e)
			{
				PrintWriter out = response.getWriter();
				response.setContentType("text/xml");
				out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
				out.println("<ReponseEspion>");
				out.println("<CodeRetour>");
				out.println(""+ConstantesNuerf.ERREUR_TECHNIQUE);
				out.println("</CodeRetour>");
				out.println("<Valeur>" +XMLUtilitaire.escape(e.getMessage()));
				out.println("</Valeur>");
				out.println("</ReponseEspion>");

			}



		}
/*
		public ParametreService getParametreBean() {
			return parametreBean;
		}

		public void setParametreBean(ParametreService parametreBean) {
			this.parametreBean = parametreBean;
		}*/
}
