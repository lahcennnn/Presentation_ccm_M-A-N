package com.cedicam.gm.bt.erreur;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.shared_impl.util.ExceptionUtils;


public class RedirectServlet extends HttpServlet {

	private static final long serialVersionUID = 8794767790419929833L;

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
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String query = req.getQueryString();
	    if (query != null && query.contains(URL_PREFIX)) {
	        String url = query.replace(URL_PREFIX, "");
	        if (!url.startsWith(req.getContextPath())) {
	            url = req.getContextPath() + url;
	        }
	        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");

	        if (throwable != null) {
	            Throwable rootCause = throwable.getCause();
	            if (rootCause != null) {
	                throwable = rootCause;
	            }

	            String excclassParam = "excclass=" + throwable.getClass().getName();
	            String separator = (url.contains("?")) ? "&" : "?";
	            url += separator + excclassParam;
	        }

	        resp.sendRedirect(url);
	    }
	}

	
	
	
	
 /*   @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getQueryString();
        if (null != query) {
	        if (query.contains(URL_PREFIX)) {        	
	        	String url = query.replace(URL_PREFIX, "");
	            if ( !url.startsWith(req.getContextPath()) ) {
	                url = req.getContextPath() + url;
	            }
	            Exception exception = (Exception) req.getAttribute("javax.servlet.error.exception");
	            
	            if (exception != null) {
		            List listExc = ExceptionUtils.getExceptions(exception);
		            Throwable throwable = (Throwable) listExc.get(listExc.size() - 1);
		            
		           // Throwable cause = org.hibernate.exception.ExceptionUtils.getRootCause(throwable);
		            Throwable cause = ExceptionConverterImpl.convert(throwable);


		            if (null != cause) {
		            	throwable = cause;
		            }
		            
		            if (exception!=null) {
		            	String getSeparator = "?";
		            	if (url.contains("?")) {
		            		getSeparator = "&";
		            	}
		            	url += getSeparator + "excclass="  + throwable.getClass().getName();
		            }
	            }
	            
	            resp.sendRedirect(url);
	        }
        }
    }*/

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}