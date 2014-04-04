package com.coombu.photobook.presentation;

import java.io.Serializable;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.service.IMailService;
import com.coombu.photobook.service.IUserService;
import com.coombu.photobook.util.Constants;
import com.coombu.photobook.util.CoombuUtil;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;

@Named("signupBean")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)

public class SignupBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String  CONFIRM_HASH = "confirm_hash";
	private static Logger log = LoggerFactory.getLogger(SignupBean.class);
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IMailService mailService;
	
	private SecurityUser user;
	
	private Set<EventSecurityUser> eventUserSet;
	
	private String userId;
	
	private String password;
	
	private String confirmPassword;
	
	private String acceptTC;
	
	private String confirmHash;
	
	private String eventName;

	private @Value("${webserver.host}") String host;
	private @Value("${webserver.port}") String port;
	private @Value("${webserver.protocol}") String protocol;
	
	public String register()
	{
		try
		{
			user = userService.findUser(userId);
			if ( user == null)
			{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There are no invitations for you", "There are no invitations for you");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			}
				
			eventUserSet = user.getEventSecurityUsers();
			boolean isInvited = false;
			for(EventSecurityUser esu : eventUserSet)
			{
				if ( esu.getUserStatusTypeId() == USER_STATUS_TYPE.PENDING.id())
				{
					isInvited = true;
				}
			}
			if ( isInvited == false)
			{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There are no invitations for you", "There are no invitations for you");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;				
			}
			if (! userService.isPasswordValid(password, user.getPasswordHash()))
			{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your user name and password are incorrect. Please try again", "Your user name and password are incorrect. Please try again");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			}
			
		}
		catch(Exception e)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error was encountered processing your invitation. Please try again", "An error was encountered processing your invitation. Please try again");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		password = "";
		return "/terms.xhtml";
	}

	public String confirmRegister()
	{		
		boolean passValidation = true;
		if ( user == null)
		{
			userId = CoombuUtil.getSecurityUserName();
			user = userService.findUser(userId);
			eventUserSet = user.getEventSecurityUsers();
		}
		try
		{
			if (password != null && !password.equals(confirmPassword))
			{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password and Confirm password must match.", "Password and Confirm password must match.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				passValidation = false;					
			}
			if(!"true".equals(acceptTC))
			{
				passValidation = false;
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must accept the terms and conditions.", "you must accept the terms and conditions.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				passValidation = false;
			}
			if (!passValidation)
				return null;
			String suppliedPasswordHash = userService.hashPassword(password);
			user.setPasswordHash(suppliedPasswordHash);
			user.setUserStatusId((short)USER_STATUS_TYPE.ACTIVE.id());
			StringBuilder strBuf = new StringBuilder();
			for(EventSecurityUser esu : eventUserSet)
			{
				if ( esu.getUserStatusTypeId() == USER_STATUS_TYPE.PENDING.id())
				{
					esu.setUserStatusTypeId(USER_STATUS_TYPE.ACTIVE.id());
					strBuf.append(esu.getEvent().getEventName());
					strBuf.append(",");
				}
			}
			eventName = strBuf.toString();
			userService.updateSaveUser(user);
			StringBuilder url = new StringBuilder(protocol);
			url.append("://").append(host).append(":").append(port).append("/coombu/signup.xhtml");
			mailService.sendRegistrationMail(user, eventName.substring(0, eventName.length()-2), url.toString());
			clear();
		}
		catch(Exception e)
		{
			log.error("Exception  registering user: " , e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error was encountered processing your invitation. Please try again", "An error was encountered processing your invitation. Please try again");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		return "login.xhtml";
		
	}
	
	@SuppressWarnings("deprecation")
	public String forgotPassword()
	{
		String returnPage = null;
		SecurityUser user = userService.findUser(userId);
		if (user == null)
		{
			log.error("user tried to use forget password - user Id not found: {}", userId);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your user name was not found", "Your user name was not found");
			FacesContext.getCurrentInstance().addMessage(null, msg);	
		}
		else
		{
			SecureRandom random = new SecureRandom();
			byte[] bytes = new byte[20];
			random.nextBytes(bytes);
			String token = new Base64().encodeToString(bytes);			
			user.setTempPassword(token);
			Calendar cal = GregorianCalendar.getInstance();
			user.setTempPasswordUpdtTimestamp(new Timestamp(cal.getTimeInMillis()));
			userService.updateSaveUser(user);
			StringBuilder tokenUrl = new StringBuilder(protocol);
			tokenUrl.append("://").append(host).append(":").append(port)
			         .append("/coombu/resetpassword.xhtml?").append(CONFIRM_HASH).append("=")
			         .append(URLEncoder.encode(token));

			mailService.sendForgotPasswordToken(user, tokenUrl.toString());
			returnPage = "tokensent.xhtml";
			clear();
		}
				
		return returnPage;
	}
	

	public void expireSession()
	{
		// invalidate session
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		session.invalidate();

		// create new session
		((HttpServletRequest) ec.getRequest()).getSession(true);
	}
	
	public String resetPassword()
	{
		confirmHash = FacesContext.getCurrentInstance().
				getExternalContext().getRequestParameterMap().get("confirmHash");
		if ( confirmHash == null || confirmHash.isEmpty())
		{
			log.error("user used empty hash to rest password: ");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your request is not valid. Please copy the link from the reset password verification email and try again.", "Your request is not valid");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		SecurityUser user = userService.findUser(userId);
		if (user == null)
		{
			log.error("user tried to use reset password - user Id not found: {}", userId);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your user name was not found", "Your user name was not found");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		if (!confirmHash.equals(user.getTempPassword()))
		{
			log.error("user supplied hash {} does not match database stored hash {} for user {},", confirmHash, user.getTempPassword(), user.getUserName());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your request is not valid. Please copy the link from the reset password verification email and try again.", "Your request is not valid");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;			
		}
		long passwordLife = Constants.PASSWORD_EXPIRE_TIME * 60 *60 * 1000; 
		long requestTime = user.getTempPasswordUpdtTimestamp().getTime();		
		long now = System.currentTimeMillis();
		
		if (now - requestTime > passwordLife)
		{
			log.error("request for password reset has expired for user {} ", user.getUserName());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your request has expired. Please try again using the Forgot Password link.", "Your request has expired please try again");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;			
		}
		if (password != null && !password.equals(confirmPassword))
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password and Confirm password must match.", "Password and Confirm password must match.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;	
		}
		String suppliedPasswordHash = userService.hashPassword(password);
		user.setPasswordHash(suppliedPasswordHash);
		user.setTempPassword("");
		user.setTempPasswordUpdtTimestamp(null);
		userService.updateSaveUser(user);
		clear();
		return "resetsuccess.xhtml";
		
	}
	/**
	 * @return the securityUser
	 */
	public SecurityUser getUser() {
		return user;
	}

	/**
	 * @param securityUser the securityUser to set
	 */
	public void setSecurityUser(SecurityUser securityUser) {
		this.user = securityUser;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getAcceptTC() {
		return acceptTC;
	}
	public void setAcceptTC(String acceptTC) {
		this.acceptTC = acceptTC;
	}

	/**
	 * @return the confirmHash
	 */
	public String getConfirmHash() {
		return confirmHash;
	}

	/**
	 * @param confirmHash the confirmHash to set
	 */
	public void setConfirmHash(String confirmHash) {
		this.confirmHash = confirmHash;
	}

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the mailService
	 */
	public IMailService getMailService() {
		return mailService;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * @return the eventUserSet
	 */
	public Set<EventSecurityUser> getEventUserSet() {
		return eventUserSet;
	}

	/**
	 * @param eventUserSet the eventUserSet to set
	 */
	public void setEventUserSet(Set<EventSecurityUser> eventUserSet) {
		this.eventUserSet = eventUserSet;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(SecurityUser user) {
		this.user = user;
	}
	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void clear()
	{
		user = null;
		eventUserSet = null;
		userId = null;		
		password = null;
		confirmPassword = null;
		acceptTC = null;
		confirmHash = null;		
	}
}
