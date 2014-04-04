package com.coombu.photobook.presentation;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.WebAttributes;

@Named
@Scope("session")
public class LoginBean implements Serializable
{
	//managed properties for the login page, username/password/etc...
	 private static Logger log = LoggerFactory.getLogger(LoginBean.class);

	private static final long serialVersionUID = 1L;
	

	
	// This is the action method called when the user clicks the "login" button
    public String doLogin() throws IOException, ServletException
    {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
 
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                 .getRequestDispatcher("/j_spring_security_check");
 
        dispatcher.forward((HttpServletRequest) context.getRequest(),
                (ServletResponse) context.getResponse());
        
        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }
    
    public String doLogout()
    {
    	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    	RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_logout");

    	try {
    	        dispatcher.forward((ServletRequest) context.getRequest(),
    	               (ServletResponse) context.getResponse());
    	    } catch (ServletException e) {
    	        log.error("ServletException", e);
    	    } catch (IOException e) {
    	        log.error("IOException", e);
    	    }

    	FacesContext.getCurrentInstance().responseComplete();
    	// It's OK to return null here because Faces is just going to exit.

    	log.debug("End LoginBean.logout");       
  
    	return "logout.xhtml";
    }
    
    public void removeErrorAfterRender()
    {
    	for(Iterator i = FacesContext.getCurrentInstance().getMessages(null); i.hasNext(); ) 
    	{
			i.remove();
		}
    	
    }
    
    public void updateMessages(boolean update) throws Exception {
    	 log.debug("Start LoginBean updateMessages");
         ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();        
        	Exception ex = (Exception)ctx.getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
        	if (ex != null) 
        	{
         		log.error("Authentication Failed! ", ex);
         		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
         		FacesContext.getCurrentInstance().addMessage("", msg);
         		ctx.getSessionMap().remove(WebAttributes.AUTHENTICATION_EXCEPTION);
        	}
         log.debug("End LoginBean.updateMessages");
    }
}
