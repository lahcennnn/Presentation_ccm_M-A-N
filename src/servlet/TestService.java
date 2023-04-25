package servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cedicam.gm.bt.core.clientwebservice.ReponseWebServiceDateApplication;
import com.cedicam.gm.bt.core.webservice.DateWebService;
import com.cedicam.gm.bt.sgc.ContexteCedicam;

/**
 * Servlet implementation class TestService
 */
public class TestService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	 DateWebService myBean;

   /**
    * @see HttpServlet#HttpServlet()
    */
   public TestService() {
       super();
       // TODO Auto-generated constructor stub
   }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    final WebApplicationContext context =
	        WebApplicationContextUtils.getWebApplicationContext(
	            config.getServletContext());
	    //myBean = (FamilleService) context.getBean("familleService");
	    myBean = (DateWebService) context.getBean("dateWebService");
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
		// TODO Auto-generated method stub
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ReponseWebServiceDateApplication reponse = myBean.rechercherDate(new ContexteCedicam());
		response.getWriter().println("Reponse du web service rechercherDate : nuerf=" + reponse.getNuerf() + " Date="+reponse.getDonnees().toString());
		// TODO Auto-generated method stub
	}

}
