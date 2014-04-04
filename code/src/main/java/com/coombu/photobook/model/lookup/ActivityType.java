package com.coombu.photobook.model.lookup;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.coombu.photobook.model.AuditTrail;

@Entity
@Table(name = "activity_type_lkp", uniqueConstraints = @UniqueConstraint(columnNames = "ACTIVITY_TYPE_NAME"))
public class ActivityType extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ACTIVITY_TYPE_ID", unique = true, nullable = false)
	private short activityTypeId;

	@Column(name = "ACTIVITY_TYPE_NAME", unique = true, nullable = false)
	private String activityTypeName;

	@Column(name = "ACTIVITY_TYPE_DESCRIPTION")
	private String activityTypeDescription;

	public ActivityType() {
	}

	public ActivityType(short activityTypeId, String activityTypeName,
			String activityTypeDescription, Timestamp createTimestamp,
			long createdBy) {
		this.activityTypeId = activityTypeId;
		this.activityTypeName = activityTypeName;
		this.activityTypeDescription = activityTypeDescription;
		this.createdBy = createdBy;
		this.createTimestamp = createTimestamp;
	}

	public short getActivityTypeId() {
		return this.activityTypeId;
	}

	public void setActivityTypeId(short activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	public String getActivityTypeName() {
		return this.activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	public String getActivityTypeDescription() {
		return this.activityTypeDescription;
	}

	public void setActivityTypeDescription(String activityTypeDescription) {
		this.activityTypeDescription = activityTypeDescription;
	}
}
