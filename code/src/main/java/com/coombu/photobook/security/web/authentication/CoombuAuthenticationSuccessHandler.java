package com.coombu.photobook.security.web.authentication;

import java.io.IOException;
import java.util.Collection;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.User;
import com.coombu.photobook.service.IUserService;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;

public class CoombuAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler 
{


		
		private final static Logger log = LoggerFactory.getLogger(CoombuAuthenticationSuccessHandler.class);
		
		@Autowired
		@Qualifier("userService")
		private IUserService userService;
	
		String userUrl;
		String adminUrl;
		String signupUrl;
		/**
		 * @param userService the userService to set
		 */
		public void setIUserService(IUserService userService) {
			this.userService = userService;
		}
		/**
		 * @return the userService
		 */
		public IUserService getIUserService() {
			return userService;
		}
		public String getUserUrl() {
			return userUrl;
		}
		public void setUserUrl(String userUrl) {
			this.userUrl = userUrl;
		}
		public String getAdminUrl() {
			return adminUrl;
		}
		public void setAdminUrl(String adminUrl) {
			this.adminUrl = adminUrl;
		}
		@Override
		 public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		            Authentication authentication) throws ServletException, IOException 
		{
			User user = null;
	        Object principal = authentication.getPrincipal();
	        if (principal instanceof User) 
	        {
	            user = (User) principal;
	            userService.updateLoginHistory(user, 1, request.getSession().getId());
	            log.info("user successfully logged in: " + user.getUsername());
	        }		
	        handle(request, response, authentication);
	        clearAuthenticationAttributes(request);
		}
		
		 protected void handle(HttpServletRequest request, 
			      HttpServletResponse response, Authentication authentication) throws IOException {
			        String targetUrl = determineTargetUrl(authentication);
			 
			        if (response.isCommitted()) {
			            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			            return;
			        }
			 
			        getRedirectStrategy().sendRedirect(request, response, targetUrl);
			    }
		 
		/** Builds the target URL according to the logic defined in the main class Javadoc. */
		
	    protected String determineTargetUrl(Authentication authentication) {
	        boolean isUser = false;
	        boolean isAdmin = false;
	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        for (GrantedAuthority grantedAuthority : authorities) {
	            if (grantedAuthority.getAuthority().equals("ROLE_STUDENT") ||
	            	grantedAuthority.getAuthority().equals("ROLE_REP")
	            	) 
	            {
	                isUser = true;
	                break;
	            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
	                isAdmin = true;
	                break;
	            }
	        }
	        User user = (User)authentication.getPrincipal();
	        if (user.getUserStatusTypeId() == USER_STATUS_TYPE.PENDING.id()) 
	        {	        	
	        	 //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	        	return signupUrl;
	        }
	        else if (isUser )
	        {
	            return this.userUrl;
	        }
	        else if (isAdmin)
	        {
	            return adminUrl;
	        } else {
	            throw new IllegalStateException("User Role Not Known - Redirection target URL not set");
	        }
	    }
		public String getSignupUrl() {
			return signupUrl;
		}
		public void setSignupUrl(String signupUrl) {
			this.signupUrl = signupUrl;
		}
		
	 
		
	}


