/**
 * 
 */
package com.cedicam.gm.bt.filtre;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cedicam.gm.bt.bean.PopUpBean;

/**
 * @author CDENIS
 *
 */
public class UrlAccessFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		String uri = ((HttpServletRequest) request).getRequestURI();
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		String referrer = ((HttpServletRequest) request).getHeader("referer");

		System.out.println("Entrée dans urlAccessFilter avec uri cible : " + uri
				+ " et session " + session != null?"RENSEIGNéE":"NULLE");
		
		//récupérer la valeur contenue par PopUpBean : permet de savoir si on se trouve en mode Pop-Up
		PopUpBean popUpBean = null;
		String modeAffichage = null;
		if (session != null) {
			popUpBean = (PopUpBean) session.getAttribute("popUpBean");
			modeAffichage = (String) session.getAttribute("modeAffichage");;
		}
		
		//si on imprime où qu'on veut l'aide on ne fait pas de redirection
		if (modeAffichage == null || !modeAffichage.equals("print")) {
			
			//Ce test pré-redirection gère : la première entrée dans l'application (=> ouverture mode pop up) 
			//ainsi que l'ouverture de l'application dans une nouvelle fenêtre (=> ouverture mode pop up)
			//Description : Le bean popUpBean est créée après le premier passage dans index.jspx, il est donc null au premier appel du filtre
			//Si popUpBean est null et referer non renseigné alors l'utilisateur n'est pas connecté
			// OU Si popUpBean est créé mais que nous ne sommes pas passé par index.jspx (isPopUpActive => false) 
			//		alors l'utilisateur essaie d'ouvrir CCM dans une nouvelle fenêtre
			//on le redirige vers l'ouverture du mode pop up
			if (!uri.equals("/CCM/") && (popUpBean == null || !popUpBean.isPopUpActive()) && referrer == null) {
				System.out.println("redirection");
				((HttpServletResponse) response).sendRedirect("/CCM/");
	
			//Si la session est nulle alors que le referer est renseigné alors la session a expiré
			//on exclut la page d'erreur pour éviter une boucle de redirection
			} else if (!uri.equals("/CCM/views/com/cedicam/gm/bt/erreur/erreur.bean") && (null == session && referrer != null)) {
				
				((HttpServletResponse) response).sendRedirect("/CCM/views/com/cedicam/gm/bt/erreur/erreur.bean?excclass=" + ViewExpiredException.class.getName());
				
			} //else if (popUpBean != null && popUpBean.isPopUpActive() && (uri.equals("/CCM/views/com/cedicam/gm/ch/habilitation/habilitation.bean") || uri.equals("/CCM/views/com/cedicam/gm/ch/ec001.bean"))) {
//				//désactivation du booleen permettant de savoir si l'appli a été ouverte en mode pop up
//				// permet de détecter si l'utilisateur tente d'ouvrir une nouvelle fenêtre de CCM
//				
//				popUpBean.setPopUpActive(false);
//			}
			
		}
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
