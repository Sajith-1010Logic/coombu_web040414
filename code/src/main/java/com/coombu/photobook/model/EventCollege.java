package com.coombu.photobook.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
// Generated Nov 3, 2013 6:30:53 PM by Hibernate Tools 4.0.0

/**
 * EventCollege generated by hbm2java
 */
@Entity
@Table(name = "event_college")
public class EventCollege extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "EVENT_ID", unique = true, nullable = false)
	private int eventId;

	@Column(name = "COLLEGE_LOGO")
	private byte[] collegeLogo;

	@Column(name = "COLLEGE_YEAR")
	private Short collegeYear;


	public EventCollege() {
	}

	public EventCollege(int eventId, Timestamp createTimestamp, long createdBy) {
		this.eventId = eventId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
	}

	public EventCollege(int eventId, byte[] collegeLogo, Short collegeYear,
			Timestamp createTimestamp, long createdBy, Timestamp updateTimestamp,
			Long updatedBy) {
		this.eventId = eventId;
		this.collegeLogo = collegeLogo;
		this.collegeYear = collegeYear;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.updateTimestamp = updateTimestamp;
		this.updatedBy = updatedBy;
	}

	public int getEventId() {
		return this.eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public byte[] getCollegeLogo() {
		return this.collegeLogo;
	}

	public void setCollegeLogo(byte[] collegeLogo) {
		this.collegeLogo = collegeLogo;
	}

	public Short getCollegeYear() {
		return this.collegeYear;
	}

	public void setCollegeYear(Short collegeYear) {
		this.collegeYear = collegeYear;
	}


}
