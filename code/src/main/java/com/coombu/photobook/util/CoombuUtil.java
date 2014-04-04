package com.coombu.photobook.util;

import javax.faces.event.ActionEvent;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.coombu.photobook.model.User;

public class CoombuUtil 
{
	
	public static Long getSecurityUserId()
	{
		Long id = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null)
		{	
			User user = null;
			Object principal = auth.getPrincipal();
			if (principal instanceof User) 
			{
				user = (User) principal;
				id = user.getSecurityUserId();
			}	
		}
		return id;
	}
	public static String getSecurityUserName()
	{
		String userName = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null)
		{	
			User user = null;
			Object principal = auth.getPrincipal();
			if (principal instanceof User) 
			{
				user = (User) principal;
				userName = user.getUsername();
			}	
		}
		return userName;
	}
	
	 // Getters -----------------------------------------------------------------------------------

    public static String getActionAttribute(ActionEvent event, String name) {
        return (String) event.getComponent().getAttributes().get(name);
    }
}
