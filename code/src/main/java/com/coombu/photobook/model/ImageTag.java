package com.coombu.photobook.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
// Generated Nov 3, 2013 6:30:53 PM by Hibernate Tools 4.0.0

/**
 * ImageTag generated by hbm2java
 */
@Entity
@Table(name = "image_tag", uniqueConstraints = @UniqueConstraint(columnNames = {
		"IMAGE_ID", "EVENT_SECURITY_USER_ID" }))
public class ImageTag extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "IMAGE_TAG_ID", unique = true, nullable = false)
	private int imageTagId;
	
	@Column(name = "EVENT_SECURITY_USER_ID", insertable = false, updatable = false)
	private long eventSecurityUserId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_ID", nullable = false)
	private Image image;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_ID", nullable = false)
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_SECURITY_USER_ID", nullable = false)
	private EventSecurityUser eventSecurityUser;


	public ImageTag() {
	}

	public ImageTag(int imageTagId, Image image,
			EventSecurityUser eventSecurityUser, Timestamp createTimestamp,
			long createdBy) {
		this.imageTagId = imageTagId;
		this.image = image;
		this.eventSecurityUser = eventSecurityUser;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
	}

	public ImageTag(int imageTagId, Image image,
			EventSecurityUser eventSecurityUser, Timestamp createTimestamp,
			long createdBy, Timestamp updateTimestamp, Long updatedBy) {
		this.imageTagId = imageTagId;
		this.image = image;
		this.eventSecurityUser = eventSecurityUser;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.updateTimestamp = updateTimestamp;
		this.updatedBy = updatedBy;
	}

	public int getImageTagId() {
		return this.imageTagId;
	}

	public void setImageTagId(int imageTagId) {
		this.imageTagId = imageTagId;
	}

	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public EventSecurityUser getEventSecurityUser() {
		return this.eventSecurityUser;
	}

	public void setEventSecurityUser(EventSecurityUser eventSecurityUser) {
		this.eventSecurityUser = eventSecurityUser;
	}

	public long getEventSecurityUserId() {
		return eventSecurityUserId;
	}

	public void setEventSecurityUserId(long eventSecurityUserId) {
		this.eventSecurityUserId = eventSecurityUserId;
	}


}
