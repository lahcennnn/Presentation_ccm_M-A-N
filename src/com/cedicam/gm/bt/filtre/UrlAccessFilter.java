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

		System.out.println("Entr�e dans urlAccessFilter avec uri cible : " + uri
				+ " et session " + session != null?"RENSEIGN�E":"NULLE");
		
		//r�cup�rer la valeur contenue par PopUpBean : permet de savoir si on se trouve en mode Pop-Up
		PopUpBean popUpBean = null;
		String modeAffichage = null;
		if (session != null) {
			popUpBean = (PopUpBean) session.getAttribute("popUpBean");
			modeAffichage = (String) session.getAttribute("modeAffichage");;
		}
		
		//si on imprime o� qu'on veut l'aide on ne fait pas de redirection
		if (modeAffichage == null || !modeAffichage.equals("print")) {
			
			//Ce test pr�-redirection g�re : la premi�re entr�e dans l'application (=> ouverture mode pop up) 
			//ainsi que l'ouverture de l'application dans une nouvelle fen�tre (=> ouverture mode pop up)
			//Description : Le bean popUpBean est cr��e apr�s le premier passage dans index.jspx, il est donc null au premier appel du filtre
			//Si popUpBean est null et referer non renseign� alors l'utilisateur n'est pas connect�
			// OU Si popUpBean est cr�� mais que nous ne sommes pas pass� par index.jspx (isPopUpActive => false) 
			//		alors l'utilisateur essaie d'ouvrir CCM dans une nouvelle fen�tre
			//on le redirige vers l'ouverture du mode pop up
			if (!uri.equals("/CCM/") && (popUpBean == null || !popUpBean.isPopUpActive()) && referrer == null) {
				System.out.println("redirection");
				((HttpServletResponse) response).sendRedirect("/CCM/");
	
			//Si la session est nulle alors que le referer est renseign� alors la session a expir�
			//on exclut la page d'erreur pour �viter une boucle de redirection
			} else if (!uri.equals("/CCM/views/com/cedicam/gm/bt/erreur/erreur.bean") && (null == session && referrer != null)) {
				
				((HttpServletResponse) response).sendRedirect("/CCM/views/com/cedicam/gm/bt/erreur/erreur.bean?excclass=" + ViewExpiredException.class.getName());
				
			} //else if (popUpBean != null && popUpBean.isPopUpActive() && (uri.equals("/CCM/views/com/cedicam/gm/ch/habilitation/habilitation.bean") || uri.equals("/CCM/views/com/cedicam/gm/ch/ec001.bean"))) {
//				//d�sactivation du booleen permettant de savoir si l'appli a �t� ouverte en mode pop up
//				// permet de d�tecter si l'utilisateur tente d'ouvrir une nouvelle fen�tre de CCM
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
