package com.coombu.photobook.webservice.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LikeRequest implements Serializable
{
	private static final long serialVersionUID = 1L;

	@XmlElement
	private long eventId;
	
	@XmlElement
	private long imageId;
	
	@XmlElement
	private long eventSecurityUserId;
	
	@XmlElement
	private long securityUserId;
	
	@XmlElement
	private String  status;


	public long getEventId() {
		return eventId;
	}
	public LikeRequest(){}
	public LikeRequest(long eventId, long imageId, long eventSecurityId, long securityUserId, String status)
	{
		this.eventId = eventId;
		this.imageId = imageId;
		this.eventSecurityUserId = eventSecurityId;
		this.securityUserId = securityUserId;
		this.status = status;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
