package com.coombu.photobook.model.lookup;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.coombu.photobook.model.AuditTrail;
// Generated Nov 3, 2013 12:48:30 PM by Hibernate Tools 4.0.0

/**
 * UserStatus generated by hbm2java
 */
@Entity
@Table(name = "user_status_lkp", uniqueConstraints = {
		@UniqueConstraint(columnNames = "USER_STATUS_NAME"),
		@UniqueConstraint(columnNames = "USER_STATUS_DESCR") })
public class UserStatus extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)	
	@Column(name = "USER_STATUS_ID", unique = true, nullable = false)
	private short userStatusId;

	@Column(name = "USER_STATUS_NAME", unique = true, nullable = false, length = 15)
	private String userStatusName;
	
	@Column(name = "USER_STATUS_DESCR", unique = true, nullable = false, length = 45)
	private String userStatusDescription;


	public UserStatus() {
	}

	public UserStatus(short userStatusId, String userStatusName,
			String userStatusDescription, Date createTimestamp, long createdBy) {
		this.userStatusId = userStatusId;
		this.userStatusName = userStatusName;
		this.userStatusDescription = userStatusDescription;

	}

	public UserStatus(short userStatusId, String userStatusName,
			String userStatusDescription, Date createTimestamp, long createdBy,
			Date updateTimestamp, Long updatedBy) {
		this.userStatusId = userStatusId;
		this.userStatusName = userStatusName;
		this.userStatusDescription = userStatusDescription;


	}

	public short getUserStatusId() {
		return this.userStatusId;
	}

	public void setUserStatusId(short userStatusId) {
		this.userStatusId = userStatusId;
	}

	public String getUserStatusName() {
		return this.userStatusName;
	}

	public void setUserStatusName(String userStatusName) {
		this.userStatusName = userStatusName;
	}

	public String getUserStatusDescription() {
		return this.userStatusDescription;
	}

	public void setUserStatusDescription(String userStatusDescription) {
		this.userStatusDescription = userStatusDescription;
	}

	
}
