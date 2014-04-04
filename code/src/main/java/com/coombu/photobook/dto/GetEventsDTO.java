package com.coombu.photobook.dto;

import java.io.Serializable;

public final class GetEventsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private long eventSecurityUserId;
	
	
	private Integer roleId;

	
	private long eventId;
	
	
	private int eventStatus;

	
	private String eventName;
	
	private String roleName;
	
	


	public long getEventSecurityUserId() {
		return eventSecurityUserId;
	}


	public void setEventSecurityUserId(long eventSecurityUserId) {
		this.eventSecurityUserId = eventSecurityUserId;
	}


	


	public Integer getRoleId() {
		return roleId;
	}


	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}





	public long getEventId() {
		return eventId;
	}


	public void setEventId(long eventId) {
		this.eventId = eventId;
	}


	public int getEventStatus() {
		return eventStatus;
	}


	public void setEventStatus(int eventStatus) {
		this.eventStatus = eventStatus;
	}


	public String getEventName() {
		return eventName;
	}


	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	


	

}
