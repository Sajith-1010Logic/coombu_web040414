package com.coombu.photobook.security.web.authentication;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import com.coombu.photobook.model.User;
import com.coombu.photobook.service.IUserService;

public class CoombuSessionTimeoutListener implements
		ApplicationListener<HttpSessionDestroyedEvent> 
{
	private final static Logger log = LoggerFactory.getLogger(CoombuSessionTimeoutListener.class);
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}
	
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) 
	{
		List<SecurityContext> scList = event.getSecurityContexts();
		for(SecurityContext context: scList)
		{
			Authentication auth = context.getAuthentication();
			if(auth == null)
				continue;
			User user = null;
	         Object principal = auth.getPrincipal();
	         if (principal instanceof User) 
	         {
	             user = (User) principal;
	             userService.updateLogoutHistory(user);
	             log.info("session timedout userId: {} - session Id {}", user.getUsername(), event.getSession().getId());
	         }			
		}
	}
}
