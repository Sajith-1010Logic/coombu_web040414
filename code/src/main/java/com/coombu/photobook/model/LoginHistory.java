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
 * LoginHistory generated by hbm2java
 */
@Entity
@Table(name = "login_history")
public class LoginHistory extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "LOGIN_HISTORY_ID", unique = true, nullable = false)
	private Long loginHistoryId;
	
	@Column(name = "SECURITY_USER_ID", nullable = false)
	private Long securityUserId;
	
	@Column(name = "LOGIN_TMPSTMP", nullable = false, length = 19)
	private Timestamp loginTimestamp;
	
	@Column(name = "LOGOUT_TMSTMP", length = 19)
	private Timestamp logoutTimestamp;
	
	@Column(name = "SESSION_ID", nullable = false, length = 128)
	private String sessionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SECURITY_USER_ID", nullable = false, insertable = false, updatable = false)
	private SecurityUser securityUser;

	public LoginHistory() {
	}

	public LoginHistory(Long loginHistoryId, Long securityUserId,
			Timestamp loginTimestamp, String sessionId, Timestamp createTimestamp,
			long createdBy) {
		this.loginHistoryId = loginHistoryId;
		this.securityUserId = securityUserId;
		this.loginTimestamp = loginTimestamp;
		this.sessionId = sessionId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
	}

	public LoginHistory(Long loginHistoryId, Long securityUserId,
			Timestamp loginTimestamp, Timestamp logoutTimestamp, String sessionId,
			Timestamp createTimestamp, long createdBy, Timestamp updateTimestamp,
			Long updatedBy) {
		this.loginHistoryId = loginHistoryId;
		this.securityUserId = securityUserId;
		this.loginTimestamp = loginTimestamp;
		this.logoutTimestamp = logoutTimestamp;
		this.sessionId = sessionId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.updateTimestamp = updateTimestamp;
		this.updatedBy = updatedBy;
	}

	public Long getLoginHistoryId() {
		return this.loginHistoryId;
	}

	public void setLoginHistoryId(Long loginHistoryId) {
		this.loginHistoryId = loginHistoryId;
	}

	public Long getSecurityUserId() {
		return this.securityUserId;
	}

	public void setSecurityUserId(Long securityUserId) {
		this.securityUserId = securityUserId;
	}

	public Timestamp getLoginTimestamp() {
		return this.loginTimestamp;
	}

	public void setLoginTimestamp(Timestamp loginTimestamp) {
		this.loginTimestamp = loginTimestamp;
	}

	public Timestamp getLogoutTimestamp() {
		return this.logoutTimestamp;
	}

	public void setLogoutTimestamp(Timestamp logoutTimestamp) {
		this.logoutTimestamp = logoutTimestamp;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the securityUser
	 */
	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	/**
	 * @param securityUser the securityUser to set
	 */
	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("loginHistoryId = ").append(loginHistoryId);
		buf.append("\nsecurityUserId =").append( securityUserId);
		buf.append("\nloginTimestamp =").append( loginTimestamp);
		buf.append("\nlogoutTimestamp =").append( logoutTimestamp);
		buf.append("\nsessionId =").append( sessionId);
		buf.append("\ncreateTimestamp =").append( createTimestamp);
		buf.append("\ncreatedBy =").append( createdBy);
		buf.append("\nupdateTimestamp =").append( updateTimestamp);
		buf.append("\nupdatedBy =").append( updatedBy);
		return buf.toString();
	}
}
