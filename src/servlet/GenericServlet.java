package servlet;

import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** * Servlet implementation class TestService */
public abstract class GenericServlet extends HttpServlet {
	//protected WebApplicationContext webApplicationContext;
	
	private static final long serialVersionUID = 5065858914742008037L;

	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    //webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());        

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		doAction(request, response);	
	}

	protected abstract void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	protected Object getBean(String beanName,HttpServletRequest request, HttpServletResponse response){
		FacesContext fc = getFacesContext(request, response);
		Object o = fc.getApplication().evaluateExpressionGet(fc, "#{" + beanName + "}", Object.class); 
		fc.release();
		return o;
	}
	
	protected FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
	    if (facesContext == null) {

	    	FacesContextFactory contextFactory  = (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
	        LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); 
	        Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

	        facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);

	        // Set using our inner class
	        InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);

	        // set a new viewRoot, otherwise context.getViewRoot returns null
	        UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
	        facesContext.setViewRoot(view);                
	    }
	    return facesContext;
	}
	
	public void removeFacesContext() {
	        InnerFacesContext.setFacesContextAsCurrentInstance(null);
	}
	
	protected Application getApplication(FacesContext facesContext) {
		return facesContext.getApplication();        
	}
	
	@SuppressWarnings("deprecation")
	protected Object getManagedBean(String beanName, FacesContext facesContext) {        
		return getApplication(facesContext).getVariableResolver().resolveVariable(facesContext, beanName);
	}
	
	// You need an inner class to be able to call FacesContext.setCurrentInstance
	// since it's a protected method
	private abstract static class InnerFacesContext extends FacesContext {
		protected static void setFacesContextAsCurrentInstance(FacesContext facesContext) {
			FacesContext.setCurrentInstance(facesContext);
		}
	}   
	
}
