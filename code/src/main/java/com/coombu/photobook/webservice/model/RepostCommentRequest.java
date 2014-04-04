package com.coombu.photobook.webservice.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RepostCommentRequest implements Serializable
{
	private static final long serialVersionUID = 1L;

	@XmlElement
	private long eventId;
	
	@XmlElement
	private long commentId;
	
	@XmlElement
	private long eventSecurityUserId;
	
	@XmlElement
	private long securityUserId;

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
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
	
}
