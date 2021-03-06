package com.coombu.photobook.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

// Generated Nov 3, 2013 6:30:53 PM by Hibernate Tools 4.0.0

/**
 * SecurityUser generated by hbm2java
 */
@Entity
@Table(name = "security_user", uniqueConstraints = @UniqueConstraint(columnNames = "USER_NAME"))
public class SecurityUser extends AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SECURITY_USER_ID", unique = true, nullable = false)
	private long securityUserId;

	@Column(name = "USER_NAME", unique = true, nullable = false, length = 25)
	private String userName;

	@Column(name = "PASSWORD_HASH", nullable = false, length = 256)
	private String passwordHash;

	@Column(name = "TEMP_PASSWORD", length = 45)
	private String tempPassword;

	@Column(name = "TEMP_PASSWORD_UPDT_TMSTMP", length = 45)
	private Timestamp tempPasswordUpdtTimestamp;

	@Column(name = "SALT", nullable = false, length = 45)
	private String salt;

	@Column(name = "PASSEWORD_RESET_COUNTER")
	private Integer passewordResetCounter;

	@Column(name = "USER_STATUS_ID", nullable = false)
	private short userStatusId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "securityUser")
	private Set<SecurityQuestionAnswer> securityQuestionAnswers = new HashSet<>(
			0);

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "securityUser", cascade = CascadeType.ALL)
	private UserProfile userProfile;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "securityUser", cascade = CascadeType.ALL)
	private Set<SecurityUserRole> securityUserRoles = new HashSet<>(0);

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "securityUser", cascade = CascadeType.ALL)
	private Set<EventSecurityUser> eventSecurityUsers = new HashSet<>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "securityUser")
	private Set<PasswordHistory> passwordHistories = new HashSet<>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "securityUser", cascade = CascadeType.ALL)
	private Set<Event> events = new HashSet<>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "securityUser")
	private Set<Event> loginHistories = new HashSet<>(0);

	@Transient
	private String password;
	@Transient
	private String confirmPassword;
	@Transient
	private String currentPassword;
	@Transient
	private String resetAnswer;
	@Transient
	private String userResponse;

	public SecurityUser() {
	}

	public SecurityUser(int securityUserId, String userName,
			String passwordHash, String salt, short userStatusId,
			Timestamp createTimestamp, long createdBy) {
		this.securityUserId = securityUserId;
		this.userName = userName;
		this.passwordHash = passwordHash;
		this.salt = salt;
		this.userStatusId = userStatusId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
	}

	public SecurityUser(int securityUserId, String userName,
			String passwordHash, String tempPassword,
			Timestamp tempPasswordUpdtTimestamp, String salt,
			Integer passewordResetCounter, short userStatusId,
			Timestamp createTimestamp, long createdBy,
			Timestamp updateTimestamp, Long updatedBy,
			Set<SecurityQuestionAnswer> securityQuestionAnswers,
			UserProfile userProfile, Set<SecurityUserRole> securityUserRoles,
			Set<EventSecurityUser> eventSecurityUsers,
			Set<PasswordHistory> passwordHistories, Set<Event> events) {
		this.securityUserId = securityUserId;
		this.userName = userName;
		this.passwordHash = passwordHash;
		this.tempPassword = tempPassword;
		this.tempPasswordUpdtTimestamp = tempPasswordUpdtTimestamp;
		this.salt = salt;
		this.passewordResetCounter = passewordResetCounter;
		this.userStatusId = userStatusId;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.updateTimestamp = updateTimestamp;
		this.updatedBy = updatedBy;
		this.securityQuestionAnswers = securityQuestionAnswers;
		this.userProfile = userProfile;
		this.securityUserRoles = securityUserRoles;
		this.eventSecurityUsers = eventSecurityUsers;
		this.passwordHistories = passwordHistories;
		this.events = events;
	}

	public long getSecurityUserId() {
		return this.securityUserId;
	}

	public void setSecurityUserId(long securityUserId) {
		this.securityUserId = securityUserId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswordHash() {
		return this.passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getTempPassword() {
		return this.tempPassword;
	}

	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}

	public Timestamp getTempPasswordUpdtTimestamp() {
		return this.tempPasswordUpdtTimestamp;
	}

	public void setTempPasswordUpdtTimestamp(Timestamp tempPasswordUpdtTimestamp) {
		this.tempPasswordUpdtTimestamp = tempPasswordUpdtTimestamp;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getPassewordResetCounter() {
		return this.passewordResetCounter;
	}

	public void setPassewordResetCounter(Integer passewordResetCounter) {
		this.passewordResetCounter = passewordResetCounter;
	}

	public short getUserStatusId() {
		return this.userStatusId;
	}

	public void setUserStatusId(short userStatusId) {
		this.userStatusId = userStatusId;
	}

	public Set<SecurityQuestionAnswer> getSecurityQuestionAnswers() {
		return this.securityQuestionAnswers;
	}

	public void setSecurityQuestionAnswers(
			Set<SecurityQuestionAnswer> securityQuestionAnswers) {
		this.securityQuestionAnswers = securityQuestionAnswers;
	}

	public UserProfile getUserProfile() {
		return this.userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public Set<SecurityUserRole> getSecurityUserRoles() {
		return this.securityUserRoles;
	}

	public void setSecurityUserRoles(Set<SecurityUserRole> securityUserRoles) {
		this.securityUserRoles = securityUserRoles;
	}

	public Set<EventSecurityUser> getEventSecurityUsers() {
		return this.eventSecurityUsers;
	}

	public void setEventSecurityUsers(Set<EventSecurityUser> eventSecurityUsers) {
		this.eventSecurityUsers = eventSecurityUsers;
	}

	public Set<PasswordHistory> getPasswordHistories() {
		return this.passwordHistories;
	}

	public void setPasswordHistories(Set<PasswordHistory> passwordHistories) {
		this.passwordHistories = passwordHistories;
	}

	public Set<Event> getEvents() {
		return this.events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setResetAnswer(String resetAnswer) {
		this.resetAnswer = resetAnswer;
	}

	public String getResetAnswer() {
		return resetAnswer;
	}

	public void setUserResponse(String UserResponse) {
		this.userResponse = UserResponse;
	}

	public String getUserResponse() {
		return userResponse;
	}

	/**
	 * @return the loginHistories
	 */
	public Set<Event> getLoginHistories() {
		return loginHistories;
	}

	/**
	 * @param loginHistories
	 *            the loginHistories to set
	 */
	public void setLoginHistories(Set<Event> loginHistories) {
		this.loginHistories = loginHistories;
	}

}
