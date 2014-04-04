/**
 * 
 */
package com.coombu.photobook.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.coombu.photobook.service.IReferenceData;

/**
 * @author Fekade Aytaged
 *
 */
public class ApplicationInitializerListener implements ServletContextListener 
{
	private final static Logger log = LoggerFactory.getLogger(ApplicationInitializerListener.class);
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		log.info("Context Destroyed");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) 
	{
		IReferenceData referenceData = getReferenceData(event);
		loadReferenceData(referenceData);

	}
	
	private IReferenceData getReferenceData(ServletContextEvent event)
	{
		IReferenceData referenceData = null;
		try
		{
			ServletContext servletContext = event.getServletContext();
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			
			if (wac != null && wac.containsBean("referenceData"))
			{		
				referenceData = (IReferenceData)wac.getBean("referenceData");
			}
		}
		catch(Exception e)
		{
			log.error("Could not get get referenceData bean", e);			
		}
		return referenceData;
	}
	
	private void loadReferenceData(IReferenceData referenceData) 
	{
		log.info("Web Context Initialized. Trying to initialize the application from the database...");
		try
		{	
			referenceData.init();
			log.info("Application data initialized!");
		}
		catch(Exception e)
		{
			log.error("Could not initialize application data", e);			
		}

		
	}
}
