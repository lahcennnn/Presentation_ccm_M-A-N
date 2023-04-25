/**
 * 
 */
package com.cedicam.gm.bt.filtre;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.Filter;

import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.sgt.EnteteComposant;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;

/**
 * @author CDENIS
 *
 */
public class CustomRichfacesFilter extends Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		try {
			System.out.println("Entrée dans CustomRichfacesFilter avec uri cible : " + ((HttpServletRequest) request).getRequestURI());
			HttpServletResponse resp = (HttpServletResponse) response;
		    resp.addHeader("X-UA-Compatible", "IE=EmulateIE8");
		    resp.addHeader("Content-type", "text/css");
		    super.doFilter(request, resp, chain);
		} catch (IllegalArgumentException iae) {
			//permet de gérer l'incompatibilité RF 3.3.3 et IE9 - IE10
			//si c'est bien une erreur sur le ContentTypeList, on rend l'exception silencieuse, sinon on la propage
			if (iae.getMessage().contains("ContentTypeList")) {
				EnteteComposant ec = new EnteteComposant();
				ec.setIdComp("RFFilter");
				ec.setLibAppli("Centre comptable monétique");
				ec.setLibComp("RichFaces Filter");
				ec.setNomClasse(this.getClass().getName());
				ec.setTypComp(ConstantesTrace.TYPE_COMPOSANT_FWK);
				ec.setVersAppli(ConstantesTrace.PROJECT_VERSION);
				ApplicationContextHolder.getTraceService().enregistrerTraceDebug("CustomRichfaceFilter", 
						"Une exception a été capturée silencieusement lors de l'exécution du filtre Richfaces", ec, null);
			} else {
				throw iae;
			}
			
		}
	}
}
