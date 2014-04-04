package com.coombu.photobook.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
// Generated Nov 3, 2013 6:30:53 PM by Hibernate Tools 4.0.0

/**
 * Template generated by hbm2java
 */
@Entity
@Table(name = "template", uniqueConstraints = {
		@UniqueConstraint(columnNames = "TEMPLATE_NAME"),
		@UniqueConstraint(columnNames = { "TEMPLATE_FILE_NAME",
				"TEMPLATE_FILE_PATH" }) })
public class Template extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "TEMPLATE_ID", unique = true, nullable = false)
	private short templateId;

	@Column(name = "TEMPLATE_NAME", unique = true, nullable = false, length = 45)
	private String templateName;
	
	@Column(name = "TEMPLATE_STATUS_ID", nullable = false)
	private byte templateStatusId;
	
	@Column(name = "TEMPLATE_FILE_NAME", nullable = false, length = 128)
	private String templateFileName;
	
	@Column(name = "TEMPLATE_FILE_PATH", nullable = false)
	private String templateFilePath;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
	private Set<EventTemplate> eventTemplates = new HashSet<>(0);

	public Template() {
	}

	public Template(short templateId, String templateName,
			byte templateStatusId, String templateFileName,
			String templateFilePath, Timestamp createTimestamp, long createdBy) {
		this.templateId = templateId;
		this.templateName = templateName;
		this.templateStatusId = templateStatusId;
		this.templateFileName = templateFileName;
		this.templateFilePath = templateFilePath;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
	}

	public Template(short templateId, String templateName,
			byte templateStatusId, String templateFileName,
			String templateFilePath, Timestamp createTimestamp, long createdBy,
			Timestamp updateTimestamp, Long updatedBy,
			Set<EventTemplate> eventTemplates) {
		this.templateId = templateId;
		this.templateName = templateName;
		this.templateStatusId = templateStatusId;
		this.templateFileName = templateFileName;
		this.templateFilePath = templateFilePath;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.updateTimestamp = updateTimestamp;
		this.updatedBy = updatedBy;
		this.eventTemplates = eventTemplates;
	}

	public short getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public byte getTemplateStatusId() {
		return this.templateStatusId;
	}

	public void setTemplateStatusId(byte templateStatusId) {
		this.templateStatusId = templateStatusId;
	}

	public String getTemplateFileName() {
		return this.templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public String getTemplateFilePath() {
		return this.templateFilePath;
	}

	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}

	public Set<EventTemplate> getEventTemplates() {
		return this.eventTemplates;
	}

	public void setEventTemplates(Set<EventTemplate> eventTemplates) {
		this.eventTemplates = eventTemplates;
	}

}
