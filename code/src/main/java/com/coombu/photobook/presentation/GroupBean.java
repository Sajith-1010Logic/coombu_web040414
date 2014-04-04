package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.Schedule;
import com.coombu.photobook.service.IUserService;
import com.coombu.photobook.util.Constants;
import com.coombu.photobook.util.Constants.EVENT_STATUS_TYPE;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;
import com.coombu.photobook.util.CoombuUtil;

@Named
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GroupBean  implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(GroupBean.class);
	
	private List<EventSecurityUser> userEvents;

	private String acceptTC;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private DashBean dashBean;

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public List<EventSecurityUser> getUserEvents() {
		return userEvents;
	}

	public void setUserEvents(List<EventSecurityUser> userEvents) {
		this.userEvents = userEvents;
	}
	
	public String getAcceptTC() {
		return acceptTC;
	}

	public void setAcceptTC(String acceptTC) {
		this.acceptTC = acceptTC;
	}

	public void loadData()
	{
		Long id = CoombuUtil.getSecurityUserId();
		userEvents = userService.getEventSecurityUser(id);
		
		log.debug("userEvents = {}", userEvents);
	}

	public long getEventSecurityUserId(long eventId)
	{
		long id = -1;
		for(EventSecurityUser userEvent: userEvents)
		{
			if ( userEvent.getEvent().getEventId() == eventId)
			{
				id = userEvent.getEventSecurityUserId();
				break;
			}
		}
		return id;
	}
	public EventSecurityUser getEventSecurityUser(long eventId)
	{
		EventSecurityUser user = null;
		for(EventSecurityUser userEvent: userEvents)
		{
			if ( userEvent.getEvent().getEventId() == eventId)
			{
				user = userEvent;
				break;
			}
		}
		return user;
	}
	
	public boolean isUserEventValid(EventSecurityUser esu)
	{
		boolean isValid = true;
		if ( esu.getEvent().getEventStatus() != EVENT_STATUS_TYPE.ACTIVE.id() &&
			 esu.getUserStatusTypeId()       != USER_STATUS_TYPE.ACTIVE.id() )
		{
			isValid = false;
		}
		return isValid;
	}
	public boolean isScheduleValid(EventSecurityUser esu)
	{
		boolean isValid = true;
		Date now = new Date();
		for(Schedule s : esu.getEvent().getSchedules())
		{
			Date startDate = s.getPictureAvailableStartDatetime();
			if(startDate != null && now.before(startDate))
			{
				isValid = false;
				break;
			}
			Date endDate   = s.getPictureAvailableEndDatetime();
			if ( endDate != null &&  now.after(endDate) )
			{
				isValid = false;
				break;
			}
		}
		
		return isValid;
	}
	public boolean isUploadScheduleValid(Long eventId)
	{
		EventSecurityUser esu = getEventSecurityUser(eventId);
		boolean isValid = true;
		Date now = new Date();
		for(Schedule s : esu.getEvent().getSchedules())
		{
			Date startDate = s.getUploadImageStartDatetime();
			if(startDate != null && now.before(startDate))
			{
				isValid = false;
				break;
			}
			Date endDate   = s.getUploadImageEndDatetime();
			if ( endDate != null &&  now.after(endDate) )
			{
				isValid = false;
				break;
			}
		}
		
		return isValid;
	}	
	public String activate(EventSecurityUser userEvent)
	{
		userEvent.setUserStatusTypeId(USER_STATUS_TYPE.ACTIVE.id());
		userService.updateEventSecurityUser(userEvent);
		return null;
	}

	public String inactivate(EventSecurityUser userEvent)
	{
		userEvent.setUserStatusTypeId(USER_STATUS_TYPE.INACTIVE.id());
		userService.updateEventSecurityUser(userEvent);
		FacesContext.getCurrentInstance().addMessage(null, new
				FacesMessage(FacesMessage.SEVERITY_INFO,"Your account has been inactivated.",
				 "Your account has been inactivated"));
		return null;
	}
	
	public String confirmRegister(EventSecurityUser userEvent)
	{
		if(!"true".equals(acceptTC))
		{
			FacesContext.getCurrentInstance().addMessage("accept", new
					FacesMessage(FacesMessage.SEVERITY_ERROR,"Please accept the terms and conditions.",
					 "Please accept the terms and conditions"));
			return null;
		}
		activate(userEvent);
		dashBean.loadData(userEvent.getEvent().getEventId(), Constants.SUBMENU_TYPE.LATEST);
		return "dash.xhtml";
	}
	
}
