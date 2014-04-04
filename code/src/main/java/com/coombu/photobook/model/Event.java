package com.coombu.photobook.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
// Generated Nov 3, 2013 6:30:53 PM by Hibernate Tools 4.0.0
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Event generated by hbm2java
 */
@XmlRootElement
@Entity
@Table(name = "event")
public class Event extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EVENT_ID", unique = true, nullable = false)
	private long eventId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SECURITY_USER_ID", nullable = false)
	private SecurityUser securityUser;

	@Column(name = "EVENT_STATUS", nullable = false)
	private int eventStatus;

	@Column(name = "EVENT_NAME", nullable = false, length = 45)
	private String eventName;

	@Column(name = "EVENT_TYPE_TABLE_ID", nullable = false)
	private short eventTypeTableId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	private Set<Schedule> schedules = new HashSet<>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	private Set<Book> books = new HashSet<>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	private Set<Image> images = new HashSet<>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	private Set<EventTemplate> eventTemplates = new HashSet<>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	private Set<EventSecurityUser> eventSecurityUsers = new HashSet<>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	private Set<ImageVote> imageVotes = new HashSet<>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<ImageTag> imageTags = new ArrayList<>(0);

	public Event() {
	}

	public Event(int eventId, SecurityUser securityUser, int eventStatus,
			String eventName, short eventTypeTableId, Timestamp createTimestamp,
			long createdBy) {
		this.eventId = eventId;
		this.securityUser = securityUser;
		this.eventStatus = eventStatus;
		this.eventName = eventName;
		this.eventTypeTableId = eventTypeTableId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
	}

	public Event(int eventId, SecurityUser securityUser, int eventStatus,
			String eventName, short eventTypeTableId, Timestamp createTimestamp,
			long createdBy, Timestamp updateTimestamp, Long updatedBy,
			Set<Schedule> schedules, Set<Book> books, Set<Image> images,
			Set<EventTemplate> eventTemplates,
			Set<EventSecurityUser> eventSecurityUsers) {
		this.eventId = eventId;
		this.securityUser = securityUser;
		this.eventStatus = eventStatus;
		this.eventName = eventName;
		this.eventTypeTableId = eventTypeTableId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.updateTimestamp = updateTimestamp;
		this.updatedBy = updatedBy;
		this.schedules = schedules;
		this.books = books;
		this.images = images;
		this.eventTemplates = eventTemplates;
		this.eventSecurityUsers = eventSecurityUsers;
	}

	@XmlElement
	public long getEventId() {
		return this.eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	@XmlTransient
	public SecurityUser getSecurityUser() {
		return this.securityUser;
	}

	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	@XmlElement
	public int getEventStatus() {
		return this.eventStatus;
	}

	public void setEventStatus(int eventStatus) {
		this.eventStatus = eventStatus;
	}

	@XmlElement
	public String getEventName() {
		return this.eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@XmlElement
	public short getEventTypeTableId() {
		return this.eventTypeTableId;
	}

	public void setEventTypeTableId(short eventTypeTableId) {
		this.eventTypeTableId = eventTypeTableId;
	}

	@XmlTransient
	public Set<Schedule> getSchedules() {
		return this.schedules;
	}

	public void setSchedules(Set<Schedule> schedules) {
		this.schedules = schedules;
	}

	@XmlTransient
	public Set<Book> getBooks() {
		return this.books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	@XmlTransient
	public Set<Image> getImages() {
		return this.images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	@XmlTransient
	public Set<EventTemplate> getEventTemplates() {
		return this.eventTemplates;
	}

	public void setEventTemplates(Set<EventTemplate> eventTemplates) {
		this.eventTemplates = eventTemplates;
	}

	@XmlTransient
	public Set<EventSecurityUser> getEventSecurityUsers() {
		return this.eventSecurityUsers;
	}

	public void setEventSecurityUsers(Set<EventSecurityUser> eventSecurityUsers) {
		this.eventSecurityUsers = eventSecurityUsers;
	}

	@XmlTransient
	public Set<ImageVote> getImageVotes() {
		return imageVotes;
	}

	public void setImageVotes(Set<ImageVote> imageVotes) {
		this.imageVotes = imageVotes;
	}

	public List<ImageTag> getImageTags() {
		return imageTags;
	}

	public void setImageTags(List<ImageTag> imageTags) {
		this.imageTags = imageTags;
	}

}
