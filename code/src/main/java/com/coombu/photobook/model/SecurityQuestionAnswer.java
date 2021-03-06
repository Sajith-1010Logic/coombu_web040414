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
// Generated Nov 3, 2013 6:30:53 PM by Hibernate Tools 4.0.0

/**
 * SecurityQuestionAnswer generated by hbm2java
 */
@Entity
@Table(name = "security_question_answer")
public class SecurityQuestionAnswer extends AuditTrail implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "SECURITY_QUESTION_ANSWER_ID", unique = true, nullable = false)
	private int securityQuestionAnswerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SECURITY_USER_ID", nullable = false)
	private SecurityUser securityUser;
	
	@Column(name = "SECURITY_QUESTTION_", nullable = false, length = 45)
	private String securityQuestion;
	
	@Column(name = "SECURITY_ANSWER", nullable = false, length = 45)
	private String securityAnswer;

	public SecurityQuestionAnswer() {
	}

	public SecurityQuestionAnswer(int securityQuestionAnswerId,
			SecurityUser securityUser, String securityQuestion,
			String securityAnswer, Timestamp createTimestamp, long createdBy) {
		this.securityQuestionAnswerId = securityQuestionAnswerId;
		this.securityUser = securityUser;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
	}

	public SecurityQuestionAnswer(int securityQuestionAnswerId,
			SecurityUser securityUser, String securityQuestion,
			String securityAnswer, Timestamp createTimestamp, long createdBy,
			Timestamp updateTimestamp, Long updatedBy) {
		this.securityQuestionAnswerId = securityQuestionAnswerId;
		this.securityUser = securityUser;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.updateTimestamp = updateTimestamp;
		this.updatedBy = updatedBy;
	}

	public int getSecurityQuestionAnswerId() {
		return this.securityQuestionAnswerId;
	}

	public void setSecurityQuestionAnswerId(int securityQuestionAnswerId) {
		this.securityQuestionAnswerId = securityQuestionAnswerId;
	}

	public SecurityUser getSecurityUser() {
		return this.securityUser;
	}

	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	public String getSecurityQuestion() {
		return this.securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityAnswer() {
		return this.securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
}
