package com.cedicam.gm.bt.erreur;

import java.io.IOException;

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

public class ExceptionFilter implements Filter{
	private static final String URL_PREFIX = "url=";
	private  String getStackTrace(Exception e){
		StackTraceElement[] stackTrace = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		for(StackTraceElement el: stackTrace){
			sb.append(el.getClassName());
			sb.append(".");
			sb.append(el.getMethodName());
			sb.append(" - ");
			sb.append(el.getFileName());
			sb.append(" ");
			sb.append(el.getLineNumber());
			sb.append("\n");
		}
		return sb.toString();
	}

	 private boolean testCauses(Throwable exception, String exceptionName){
	    	Throwable e = exception;
	    	for(int i=0;i<50;i++){
	    		if(e==null)return false;
	    		if(e.getClass().getName().equals(exceptionName)) return true;
	    		e = e.getCause();
	    	}
	    	return false;
	    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String query = req.getQueryString();
        System.out.println("DEBUG - "+query);

        HttpSession session = req.getSession(false);
        if (session == null) {
        	System.out.println("DEBUG - SESSION INVALIDE ");
        	FacesContext.getCurrentInstance().getExternalContext().redirect("/views/com/cedicam/gm/bt/erreur/erreur.bean?errorcode=exception&excclass=javax.faces.application.ViewExpiredException");
        }


        if ( query.contains(URL_PREFIX) ) {
            String url = query.replace(URL_PREFIX, "");
            if ( !url.startsWith(req.getContextPath()) ) {
                url = req.getContextPath() + url;
            }
            //Class exceptionClass = (Class) req.getAttribute("javax.servlet.error.exception_type");
            System.out.println("DEBUG - javax.servlet.error.exception_type: "+req.getAttribute("javax.servlet.error.exception_type"));
            Exception exception = (Exception) req.getAttribute("javax.servlet.error.exception");
            System.out.println("DEBUG - EXCEPTION "+getStackTrace(exception));
            if(exception!=null){
            	 if(testCauses(exception, "javax.faces.application.ViewExpiredException")){
            		 url += "&excclass=javax.faces.application.ViewExpiredException";
            	 }else url += "&excclass="  +exception.getClass().getName();
            }
            System.out.println("DEBUG - URL Redirigée: "+url);
            res.sendRedirect(url);
        }



        /*if (!req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) { // Skip JSF resources (CSS/JS/Images/etc)
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            res.setDateHeader("Expires", 0); // Proxies.
        }
*/
        chain.doFilter(request, response);
    }

	@Override
	public void destroy() {	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}