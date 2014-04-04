package com.coombu.photobook.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.service.IImageService;
import com.coombu.photobook.service.IUserService;

@Named("membersBean")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MembersBean {

	@Autowired
	IUserService userService;

	@Autowired
	GroupBean groupBean;

	@Autowired
	DashBean dashBean;

	@Autowired
	IImageService imageService;

	private List<EventSecurityUser> eventUsers;

	public String allMembers() {
		eventUsers = userService.getEventSecurityUsersByEventId(
				dashBean.getCurrentEventId(), true, false, false, false, false);
		for (EventSecurityUser usr : eventUsers) {

			// setting the active image count of the uploaded photos
			usr.setImageCount(imageService.getImageCount(
					usr.getEventSecurityUserId(), dashBean.getCurrentEventId()));

			// setting the active comments count of the user
			usr.setCommentCount(imageService.getCommentCount(
					usr.getEventSecurityUserId(), dashBean.getCurrentEventId()));

			// setting the active images vote count of the user
			usr.setVoteCount(imageService.getLikeCountBasedOnUser(
					usr.getEventSecurityUserId(), dashBean.getCurrentEventId()));
		}

		return "members.xhtml";
	}

	public String searchMembers() {
		eventUsers = new ArrayList<EventSecurityUser>();
		return "members.xhtml";
	}

	/*
	 * public String photosSeen() { eventUsers =
	 * userService.userSeenPhoto(dashBean .getCurrentEventSecurityUser());
	 * return "members.xhtml"; }
	 * 
	 * public String loggedInUsers() { eventUsers =
	 * userService.loggedInUsers(dashBean.getCurrentEventId()); return
	 * "members.xhtml"; }
	 * 
	 * public String myFriends() { eventUsers = userService
	 * .friends(dashBean.getCurrentEventSecurityUser()); return "members.xhtml";
	 * }
	 */

	protected void handleError(String string) {
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.getApplication().getNavigationHandler()
				.handleNavigation(fc, null, "unauthorized");
	}

	public List<EventSecurityUser> getEventUsers() {
		return eventUsers;
	}

	public void setCurrentEventUsers(List<EventSecurityUser> eventUsers) {
		this.eventUsers = eventUsers;
	}
}
