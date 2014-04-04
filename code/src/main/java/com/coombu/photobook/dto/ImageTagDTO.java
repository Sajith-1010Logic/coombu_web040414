package com.coombu.photobook.dto;

import java.io.Serializable;



public class ImageTagDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3543436303031483113L;


	private long eventId;
	
	
	private long imageId;
	
	
	private long eventSecurityUserId;
	
	
	private long securityUserId;
	
	private String securityUserName;

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public long getEventSecurityUserId() {
		return eventSecurityUserId;
	}

	public void setEventSecurityUserId(long eventSecurityUserId) {
		this.eventSecurityUserId = eventSecurityUserId;
	}

	public long getSecurityUserId() {
		return securityUserId;
	}

	public void setSecurityUserId(long securityUserId) {
		this.securityUserId = securityUserId;
	}

	public String getSecurityUserName() {
		return securityUserName;
	}

	public void setSecurityUserName(String securityUserName) {
		this.securityUserName = securityUserName;
	}
	
	

}
