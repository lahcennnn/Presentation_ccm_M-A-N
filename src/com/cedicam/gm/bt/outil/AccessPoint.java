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

public class AccessPoint extends HttpServlet {



	/**
	 *
	 */
	private static final long serialVersionUID = 366106836601921514L;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    final WebApplicationContext context =
	        WebApplicationContextUtils.getWebApplicationContext(
	            config.getServletContext());

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
			String valeurParam = "";

			String appliPath = request.getScheme() + "://" + request.getLocalName() + ":" + request.getServerPort() + request.getContextPath();


			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("<HTML>");
				out.println("<HEAD><TITLE>GM - Centre Comptable Monetique - Page accueil intermédiaire </TITLE>");
					out.println("<SCRIPT>");
					out.println("function lancerMenuWN()");
					out.println("{");
//					out.println("	alert('Entree');");
					out.println("	var appliPath = '" + appliPath + "/';");
//					out.println("	alert(appliPath);");
						//out.println("   var url = protocol + '//' + hostname +':' + port + appliPath + 'views/com/cedicam/gm/ch/menus/st000.bean'");
						out.println("   var url =  'views/com/cedicam/gm/ch/menus/st000.bean';");
//						out.println("   alert(appliPath + url);");
						//out.println("   var appli = window.open('#','mywindow','height=750, location=no, menubar=no, toolbar=no ,width=1200, directories=no, resizable=yes, scrollbars=yes, status=yes');" );
						//out.println("   var appli = window.open('#','mywindow','height=750, location=yes, menubar=yes, toolbar=yes ,width=1200, directories=no, resizable=yes, scrollbars=yes, status=yes');" );
						out.println("  try {");
						//out.println(" 	  appli.document.write(data);");
						out.println(" 	  var appli = window.open(appliPath + url,'appliWN', 'height=750, location=yes, menubar=yes, toolbar=yes ,width=1200, directories=no, resizable=yes, scrollbars=yes, status=yes');" );
						out.println("   } catch (e) {");
						out.println(" 	  alert('Erreur ouverture popup');");
						out.println("   }");

					out.println("}");
					out.println("</SCRIPT>");
				out.println("</HEAD>");
				out.println("<BODY onload='lancerMenuWN();'>");
//				out.println("<script>alert('test');</script>");
				out.println("</BODY>");
			out.println("</HTML>");
			out.close();
		}
		catch (Throwable e)
		{
			PrintWriter out = response.getWriter();
			response.setContentType("text/xml");
			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			out.println("<ReponseAccess>");
			out.println("<CodeRetour>");
			out.println(""+ConstantesNuerf.ERREUR_TECHNIQUE);
			out.println("</CodeRetour>");
			out.println("<Valeur>" +XMLUtilitaire.escape(e.getMessage()));
			out.println("</Valeur>");
			out.println("</ReponseAccess>");

		}

	}

}



