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
 * Privilege generated by hbm2java
 */
@Entity
@Table(name = "privilege_lkp", uniqueConstraints = {
		@UniqueConstraint(columnNames = "PRIVILEGE_DESCR"),
		@UniqueConstraint(columnNames = "PRIVILEGE_NAME") })
public class Privilege extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "PRIVILEGE_ID", unique = true, nullable = false)
	private short privilegeId;

	@Column(name = "PRIVILEGE_NAME", unique = true, nullable = false, length = 15)
	private String privilegeName;
	
	@Column(name = "PRIVILEGE_DESCR", unique = true, nullable = false, length = 45)
	private String privilegeDescription;


	public Privilege() {
	}

	public Privilege(short privilegeId, String privilegeName,
			String privilegeDescription, Date createTimestamp, long createdBy) {
		this.privilegeId = privilegeId;
		this.privilegeName = privilegeName;
		this.privilegeDescription = privilegeDescription;

	}

	public Privilege(short privilegeId, String privilegeName,
			String privilegeDescription, Date createTimestamp, long createdBy,
			Date updateTimestamp, Long updatedBy) {
		this.privilegeId = privilegeId;
		this.privilegeName = privilegeName;
		this.privilegeDescription = privilegeDescription;


	}

	public short getPrivilegeId() {
		return this.privilegeId;
	}

	public void setPrivilegeId(short privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getPrivilegeName() {
		return this.privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public String getPrivilegeDescription() {
		return this.privilegeDescription;
	}

	public void setPrivilegeDescription(String privilegeDescription) {
		this.privilegeDescription = privilegeDescription;
	}

	
}
