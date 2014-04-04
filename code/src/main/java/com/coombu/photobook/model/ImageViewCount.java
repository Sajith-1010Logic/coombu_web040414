package com.coombu.photobook.model;

import java.io.Serializable;

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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@Entity
@Table(name = "image_view_count", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EVENT_SECURITY_USER_ID", "IMAGE_ID" }))
public class ImageViewCount  extends AuditTrail implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@XmlElement
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "IMAGE_VIEW_COUNT_ID", unique = true, nullable = false)
	private Long imageViewCountId;
	
	@XmlElement
	@Column(name = "VIEW_COUNT")
	private long viewCount;
	
	@XmlElement@Column(name="IMAGE_ID")
	private long imageId;
	
	@XmlElement
	@Column(name = "EVENT_SECURITY_USER_ID")
	private long eventSecurityUserId;
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_ID", nullable = false, insertable = false, updatable = false)
	private Image image;

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_SECURITY_USER_ID", nullable = false, insertable = false, updatable = false)
	private EventSecurityUser eventSecurityUser;

	/**
	 * @return the imageViewCountId
	 */
	public Long getImageViewCountId() {
		return imageViewCountId;
	}

	/**
	 * @param imageViewCountId the imageViewCountId to set
	 */
	public void setImageViewCountId(Long imageViewCountId) {
		this.imageViewCountId = imageViewCountId;
	}

	/**
	 * @return the viewCount
	 */
	public long getViewCount() {
		return viewCount;
	}

	/**
	 * @param viewCount the viewCount to set
	 */
	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * @return the imageId
	 */
	public long getImageId() {
		return imageId;
	}

	/**
	 * @param imageId the imageId to set
	 */
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return the eventSecurityUserId
	 */
	public long getEventSecurityUserId() {
		return eventSecurityUserId;
	}

	/**
	 * @param eventSecurityUserId the eventSecurityUserId to set
	 */
	public void setEventSecurityUserId(long eventSecurityUserId) {
		this.eventSecurityUserId = eventSecurityUserId;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the eventSecurityUser
	 */
	public EventSecurityUser getEventSecurityUser() {
		return eventSecurityUser;
	}

	/**
	 * @param eventSecurityUser the eventSecurityUser to set
	 */
	public void setEventSecurityUser(EventSecurityUser eventSecurityUser) {
		this.eventSecurityUser = eventSecurityUser;
	}
	

}
