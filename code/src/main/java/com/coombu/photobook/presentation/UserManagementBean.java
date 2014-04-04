package com.coombu.photobook.presentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.SecurityUserRole;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.model.lookup.Role;
import com.coombu.photobook.service.IMailService;
import com.coombu.photobook.service.IReferenceData;
import com.coombu.photobook.service.IUserService;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;

@Named("userManagementBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserManagementBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(UserManagementBean.class);

	private @Value("${webserver.host}")
	String host;

	private @Value("${webserver.port}")
	String port;

	private @Value("${webserver.protocol}")
	String protocol;

	@Autowired
	IUserService userService;

	@Autowired
	private IReferenceData referenceData;

	@Autowired
	DashBean dashBean;

	@Autowired
	IMailService emailService;

	private String inputCsvFile;

	private EventSecurityUser securityUser;

	private String firstname;

	private String lastname;

	private String emailaddress;

	private long userRoleId;

	private String userName;

	private List<Role> roleList;

	private List<EventSecurityUser> usersList;

	private List<String> acknowledgeOfUpload;

	public List<String> getAcknowledgeOfUpload() {
		return acknowledgeOfUpload;
	}

	public void setAcknowledgeOfUpload(List<String> acknowledgeOfUpload) {
		this.acknowledgeOfUpload = acknowledgeOfUpload;
	}

	public String getInputCsvFile() {
		return inputCsvFile;
	}

	public void setInputCsvFile(String inputCsvFile) {
		this.inputCsvFile = inputCsvFile;
	}

	public void setUsersList(List<EventSecurityUser> usersList) {
		this.usersList = usersList;
	}

	public List<EventSecurityUser> getUsersList() {
		return usersList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public IReferenceData getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(IReferenceData referenceData) {
		this.referenceData = referenceData;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
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
	 * @param port
	 *            the port to set
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
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the dashBean
	 */
	public DashBean getDashBean() {
		return dashBean;
	}

	/**
	 * @param dashBean
	 *            the dashBean to set
	 */
	public void setDashBean(DashBean dashBean) {
		this.dashBean = dashBean;
	}

	/**
	 * @return the emailService
	 */
	public IMailService getEmailService() {
		return emailService;
	}

	/**
	 * @param emailService
	 *            the emailService to set
	 */
	public void setEmailService(IMailService emailService) {
		this.emailService = emailService;
	}

	@PostConstruct
	public void init() {
		roleList = referenceData.getRoleList();

		Iterator<Role> iterator = roleList.iterator();

		while (iterator.hasNext()) {
			if (iterator.next().getRoleName().equals("ROLE_ADMIN")) {
				iterator.remove();
				break;
			}
		}

		usersList = userService.getEventSecurityUsersByEventId(
				dashBean.getCurrentEventId(), true, true, false, true, true);

		log.debug("List of users:" + usersList.size());

		log.debug("User management init is called!");
	}

	public String loadData() {

		return "usermanagement.xhtml";
	}

	public String loadScheduling() {

		return "schedulemanagement.xhtml";
	}

	/*
	 * public void searchUser(String userId) {
	 * setSecurityUser(userService.findUser(userId));
	 * 
	 * }
	 */
	@Transactional
	public void createNewUser(ActionEvent event) {
		createUser();
	}

	public boolean createUser() {

		if (userName == null || userName.length() <= 0) {
			userName = emailaddress;
		}

		Map<String, Object> options = userService
				.checkUserExistenceByEmailId(dashBean.getCurrentEvent()
						.getEventId(), emailaddress, userName);
		FacesMessage msg = null;
		String tempPass = null;
		// if the map options returns true => there is no value exists in
		// database. if it is false => value aleady exists.

		RequestContext context = RequestContext.getCurrentInstance();
		if (((boolean) options.get("email"))
				&& ((boolean) options.get("userName"))) {

			log.debug("User Name: " + userName);
			log.debug("First Name:" + firstname);
			log.debug("Last Name:" + lastname);
			log.debug("Email :" + emailaddress);
			log.debug("User role:" + userRoleId);

			SecurityUser user = new SecurityUser();
			user.setUserName(userName);
			tempPass = userService.generateTempPassword();
			log.debug("temp pass is: " + tempPass);
			user.setCreatedBy(dashBean.getCurrentEventSecurityUser()
					.getSecurityUser().getSecurityUserId());
			user.setUserStatusId((short) USER_STATUS_TYPE.PENDING.id());
			String hash = userService.hashPassword(tempPass);
			user.setPasswordHash(hash);
			user.setSalt("");

			EventSecurityUser eventSecUser = new EventSecurityUser();
			eventSecUser.setRoleId((short) userRoleId);
			eventSecUser.setEvent(dashBean.getCurrentEvent());
			eventSecUser.setSecurityUser(user);
			eventSecUser.setUserStatusTypeId(USER_STATUS_TYPE.PENDING.id());

			Set<EventSecurityUser> eventSecUserSet = new HashSet<EventSecurityUser>();
			eventSecUserSet.add(eventSecUser);

			user.setEventSecurityUsers(eventSecUserSet);

			UserProfile profile = new UserProfile();
			profile.setCreatedBy(dashBean.getCurrentEventSecurityUser()
					.getSecurityUser().getSecurityUserId());
			profile.setFirstName(firstname);
			profile.setLastName(lastname);
			profile.setSecurityUser(user);
			profile.setEmailAddress(emailaddress);

			user.setUserProfile(profile);

			SecurityUserRole role = new SecurityUserRole();
			role.setSecurityUser(user);
			role.setCreatedBy(dashBean.getCurrentEventSecurityUser()
					.getSecurityUser().getSecurityUserId());
			role.setRoleId((short) userRoleId);

			Set<SecurityUserRole> roles = new HashSet<SecurityUserRole>();
			roles.add(role);

			user.setSecurityUserRoles(roles);

			if (userService.updateSaveUser(user)) {
				log.debug("User is created!");
				String emailContent = "Hiya "
						+ firstname
						+ " "
						+ lastname
						+ ",\n Hooray, you have been invited by "
						+ dashBean.getCurrentEventSecurityUser()
								.getSecurityUser().getUserName()
						+ " to be a part of your group, "
						+ dashBean.getCurrentEvent().getEventName()
						+ ".\n"
						+ "\n\n"
						+ "Coombu is a photo social application with web and mobile capabilities. You can upload your photos, and comment on and like the photos uploaded by the members of the group. If you think that any of the photo is offensive or inappropriate, you can flag and remove from the group dashboard."
						+ "\n\n"
						+ "Please click on the following link to sign up. You will be asked to set a new password the first time you login."
						+ "\n\n"
						+ "<a href=\""
						+ protocol
						+ "://"
						+ host
						+ ":"
						+ port
						+ "/coombu/signup.xhtml\">Login</a>"
						+ "\n\n"
						+ "User Name: "
						+ userName
						+ " or "
						+ emailaddress
						+ "\n"
						+ "\n\n"
						+ "Password: "
						+ tempPass
						+ "\n"
						+ "\n\n"
						+ "If you have any questions, please feel free to send an email to support@coombu.com"
						+ "\n\n" + "Have fun!" + "\n\n" + "Cheers," + "Coombu"
						+ "\n";

				// sending email
				emailService.sendEmail("coombu@caseopinion.com",
						"Registration", emailContent, emailaddress);

				context.addCallbackParam("cstatus", true);

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Record Saved!", ""));

				// updating the list
				usersList = userService.getEventSecurityUsersByEventId(
						dashBean.getCurrentEventId(), true, true, false, true,
						true);

				return true;

			} else {
				log.debug("User is not created!");
			}

			return false;

		} else if (((boolean) options.get("email") && !(boolean) options
				.get("userName"))) {
			log.debug("User Name already Exists!");
			context.addCallbackParam("cstatus", false);
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "User Name "
					+ userName + " is already exists!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			firstname = null;
			lastname = null;
			userName = null;
			emailaddress = null;
			log.debug("user name values are cleared!");

			return false;

		} else if (!(boolean) options.get("email")) {
			context.addCallbackParam("cstatus", true);
			log.debug("User already Exists! someone was trying to add him on another group!");
			SecurityUser user = userService.getSecurityUser(
					dashBean.getCurrentEventId(), emailaddress, userName);

			if (user == null) {

				log.debug("User is already exists in this group!");

				if (options.get("status").toString().equals("1")) {
					msg = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"Username you entered is currently part of your group and is active. User can login to the application using his username and password. If you need to reset the user password, please use the forget  password link in the home page.",
							"");
					log.debug("status id is:" + options.get("1"));

				} else if (options.get("status").toString().equals("2")) {

					msg = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"Username you entered is currently part of your group and is inactive. User can login to the application using his last used valid username and password to get back to where they left.",
							"");
					log.debug("status id is:" + options.get("2"));
				} else if (options.get("status").toString().equals("3")) {
					msg = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"User existed previously in your group - Do you want to add the same user to the group. If yes,  change the user state to active and take the user to the same state as before(similar to reset state/activate the inactivated state). If no, display a message saying ‘User cannot be added with this user id. Please change the user name and try again.",
							"");
					log.debug("status id is:" + options.get("3"));
				} else if (options.get("status").toString().equals("4")) {
					msg = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"Username you entered is currently part of your group and is pending login. User can login to the application using username and password sent to the email and use application for the first time.",
							"");
					log.debug("status id is:" + options.get("4"));
				} else if (options.get("status").toString().equals("5")) {
					msg = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"Username you entered is currently part of your group and is blocked by the student representative. Please click on the unblock action to allow application access to the user.",
							"");
					log.debug("status id is:" + options.get("5"));
				}

				if (msg != null) {
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
				context.addCallbackParam("cstatus", false);
				return false;

			} else {

				EventSecurityUser eventSecUser = new EventSecurityUser();
				eventSecUser.setRoleId((short) userRoleId);
				eventSecUser.setEvent(dashBean.getCurrentEvent());
				eventSecUser.setSecurityUser(user);
				eventSecUser.setCreatedBy(dashBean
						.getCurrentEventSecurityUser().getSecurityUser()
						.getSecurityUserId());
				eventSecUser.setUserStatusTypeId(USER_STATUS_TYPE.PENDING.id());
				user.getEventSecurityUsers().add(eventSecUser);
				if (userService.updateSaveUser(user)) {
					msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"User " + userName + " is created!");
					log.debug("User is added to the another group!");
					FacesContext.getCurrentInstance().addMessage(null, msg);

					context.addCallbackParam("cstatus", true);

					String emailContent = "Hiya "
							+ firstname
							+ " "
							+ lastname
							+ ",\n"
							+ "\n\n"
							+ "Yippe, you are getting popular! "
							+ dashBean.getCurrentEventSecurityUser()
									.getSecurityUser().getUserName()
							+ " has invited to join another group, "
							+ dashBean.getCurrentEvent().getEventName()
							+ ".\n"
							+ "\n\n"
							+ "As you might have already known, Coombu is a photo social application with web and mobile capabilities. You can upload your photos, and comment on and like the photos uploaded by the members of the group. If you think that any of the photo is offensive or inappropriate, you can flag and remove from the group dashboard."
							+ "\n\n"
							+ "Please click on the following link, You can accept or deny the invitation to join  "
							+ dashBean.getCurrentEvent().getEventName()
							+ ".\n"
							+ "\n\n"
							+ "<a href=\""
							+ protocol
							+ "://"
							+ host
							+ ":"
							+ port
							+ "/coombu/resetPassword.xhtml\">Login</a>"
							+ "\n\n"
							+ "if you have any questions, please feel free to send an email to support@coombu.com\n"
							+ "\n\n" + "Have fun!\n" + "Cheers,\n" + "Coombu";

					// sending email
					emailService.sendEmail("coombu@caseopinion.com",
							"Registration", emailContent, emailaddress);

					// updating the list
					usersList = userService.getEventSecurityUsersByEventId(
							dashBean.getCurrentEventId(), true, true, false,
							true, true);

					return true;
				} else {
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"Error in creation for the user " + userName + " !");
					log.debug("User is not added to the another group!");
					FacesContext.getCurrentInstance().addMessage(null, msg);

					return false;
				}

			}

		}
		return false;
	}

	public void updateUser(ActionEvent event) {
		log.debug("entered usm saveUser");
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage msg = null;
		boolean savedStatus = false;
		log.debug(String.valueOf(getSecurityUser().getSecurityUser()
				.getSecurityUserId()));
		log.debug(String.valueOf(getSecurityUser().getSecurityUser()
				.getUserProfile().getUserProfileId()));
		log.debug(getSecurityUser().getSecurityUser().getUserProfile()
				.getFirstName());
		log.debug(getSecurityUser().getSecurityUser().getUserProfile()
				.getLastName());
		if (getSecurityUser().getSecurityUser().getUserProfile().getFirstName() != null) {

			log.debug("Total roles for this user: "
					+ getSecurityUser().getSecurityUser()
							.getEventSecurityUsers().size());
			long tempRole = 0;

			for (EventSecurityUser evs : getSecurityUser().getSecurityUser()
					.getEventSecurityUsers()) {
				if (evs.getEvent().getEventId() == dashBean.getCurrentEventId()) {
					tempRole = evs.getRoleId();
					evs.setRoleId((short) userRoleId);
				}
			}

			if (tempRole == 2 && userRoleId == 1) {
				log.debug("this user current role is STUDENT_REP");

				if (userService
						.checkUserExistsBasedOnRole(dashBean
								.getCurrentEventId(), tempRole,
								getSecurityUser().getSecurityUser()
										.getSecurityUserId())) {

					log.debug("Student Rep is already exists!");

					savedStatus = userService.updateSaveUser(getSecurityUser()
							.getSecurityUser());

					savedStatus = true;
				} else {
					log.debug("Student Rep is not already exists!");
					savedStatus = false;
					msg = new FacesMessage(
							FacesMessage.SEVERITY_WARN,
							"Please make sure that this group has atleast one Student Representative at the minimum except this!",
							"");
				}

			} else {
				log.debug("this user current role is STUDENT");
				savedStatus = true;
				savedStatus = userService.updateSaveUser(getSecurityUser()
						.getSecurityUser());

			}

		} else {
			savedStatus = false;
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Save Error",
					"Invalid credentials");
		}

		if (savedStatus) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated",
					"User details updated");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		context.addCallbackParam("savedStatus", savedStatus);

	}

	public void deleteUser(ActionEvent event) {
		log.debug("entered usm deleteUser");
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage msg = null;
		boolean deleteStatus = false;
		long tempRole = 0;

		for (EventSecurityUser evs : getSecurityUser().getSecurityUser()
				.getEventSecurityUsers()) {
			if (evs.getEvent().getEventId() == dashBean.getCurrentEventId()) {
				tempRole = evs.getRoleId();
				log.debug("got the role for the user!");
				break;
			}
		}
		if (tempRole == 2
				&& userService.checkUserExistsBasedOnRole(dashBean
						.getCurrentEventId(), 2, getSecurityUser()
						.getEventSecurityUserId())) {
			msg = new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"Please make sure that this group has atleast one Student Representative at the minimum except this!",
					"");
		} else {

			if (getSecurityUser().getSecurityUser().getUserProfile()
					.getFirstName() != null) {
				deleteStatus = userService.deleteUser(getSecurityUser()
						.getSecurityUser(), dashBean.getCurrentEventId());
				deleteStatus = true;
				securityUser.setUserStatusTypeId(3);
			} else {
				deleteStatus = false;
			}

			if (deleteStatus)
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete",
						"User data is deleted");
			else
				msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Delete Error", "Invalid credentials");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
		context.addCallbackParam("savedStatus", deleteStatus);
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public EventSecurityUser getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(EventSecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public String checkStatus(short statusId) {
		if (statusId == (short) 3) {
			return "hide";
		}
		return "";
	}

	public String sendEmail(EventSecurityUser user) {
		log.debug("send email is called! the user id is: " + user);
		FacesMessage msg = null;
		Map<String, Object> options = null;

		String mailUserMail = user.getSecurityUser().getUserName();
		String mailUserFirstName = user.getSecurityUser().getUserProfile()
				.getFirstName();
		String mailUserLastName = user.getSecurityUser().getUserProfile()
				.getLastName();

		boolean statusPending = false;

		// generating temp password to resend the credentials with this mail.
		String tempPass = userService.generateTempPassword();

		// checking the user status type, it should be pending.
		if (user.getUserStatusTypeId() == USER_STATUS_TYPE.PENDING.id()) {

			// validating user whether this user is exist or not different
			// events
			options = userService.validateResendMail(user.getSecurityUser()
					.getSecurityUserId(), dashBean.getCurrentEventId());

			SecurityUser securityUser = user.getSecurityUser();

			if ((boolean) options.get("existsAlready")) {

				List<String> eventWithStatus = (List<String>) options
						.get("existingGroups");

				for (String content : eventWithStatus) {

					String[] cont = content.split("~");

					log.debug("event Name: " + cont[0]);
					log.debug("status is: " + cont[1]);

					if (USER_STATUS_TYPE.PENDING.id() == Integer
							.parseInt(cont[1])) {
						log.debug("status pending exists for this user in group called, "
								+ cont[0]);
						statusPending = true;
						break;
					}
				}

			} else {

				// this scenario only happen when the user does not exists in
				// different events => here resetting new temporary hash
				// password for activation
				securityUser
						.setPasswordHash(userService.hashPassword(tempPass));

				// updating security user
				if (userService.updateSaveUser(securityUser)) {
					log.debug("user temp hash is updated properly!");

					String emailContent = "Hiya "
							+ mailUserFirstName
							+ " "
							+ mailUserLastName
							+ ",\n\n Hooray, you have been invited by "
							+ dashBean.getCurrentEventSecurityUser()
									.getSecurityUser().getUserName()
							+ " to be a part of your group, "
							+ dashBean.getCurrentEvent().getEventName()
							+ ".\n"
							+ "\n\n"
							+ "Coombu is a photo social application with web and mobile capabilities. You can upload your photos, and comment on and like the photos uploaded by the members of the group. If you think that any of the photo is offensive or inappropriate, you can flag and remove from the group dashboard."
							+ "\n\n"
							+ "Please click on the following link to sign up. You will be asked to set a new password the first time you login."
							+ "\n\n"
							+ "<a href=\""
							+ protocol
							+ "://"
							+ host
							+ ":"
							+ port
							+ "/coombu/login.xhtml\">Login</a>"
							+ "\n\n"
							+ "User Name: "
							+ mailUserMail
							+ "\n\n"
							+ "Password: "
							+ tempPass
							+ "\n"
							+ "\n\n"
							+ "If you have any questions, please feel free to send an email to support@coombu.com"
							+ "\n\n" + "Have fun!" + "\n\n" + "Cheers,"
							+ "Coombu" + "\n";

					// log.debug("here is this email content : " +
					// emailContent);
					// sending email
					emailService.sendEmail("coombu@caseopinion.com",
							"Registration - [Updated]", emailContent,
							mailUserMail);

					msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Mail sent!", "");
					log.debug("resend mail send");

				}

			}

			// if user is already exists in another group but he/she haven't
			// signed up yet.
			if (statusPending) {
				log.debug("resending the email with status pendings!");

				String emailContent = "Hiya "
						+ mailUserFirstName
						+ " "
						+ mailUserLastName
						+ ","
						+ "\n\n"
						+ "Yippe, you are getting popular! "
						+ dashBean.getCurrentEventSecurityUser()
								.getSecurityUser().getUserName()
						+ " has invited to join another group, "
						+ dashBean.getCurrentEvent().getEventName()
						+ ".\n"
						+ "\n\n"
						+ "As you might have already known, Coombu is a photo social application with web and mobile capabilities. You can upload your photos, and comment on and like the photos uploaded by the members of the group. If you think that any of the photo is offensive or inappropriate, you can flag and remove from the group dashboard."
						+ "\n\n"
						+ "Please click on the following link, You will be asked to set a new password the first time you login to Coombu. When you click on the link from the other email you can accept or decline to join the invited groups."
						+ "\n\n"
						+ "<a href=\""
						+ protocol
						+ "://"
						+ host
						+ ":"
						+ port
						+ "/coombu/login.xhtml\">Login</a>"
						+ "\n\n"
						+ "if you have any questions, please feel free to send an email to support@coombu.com\n"
						+ "\n\n" + "Have fun!\n" + "Cheers,\n" + "Coombu";

				// sending email!
				emailService.sendEmail("coombu@caseopinion.com",
						"Registration - [Updated]", emailContent, mailUserMail);

				msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Mail sent!", "");
				log.debug("resend mail send");

			} else {

				// this user already exists in some other in active status

				log.debug("resending the email without status pendings!");
				String emailContent = "Hiya "
						+ mailUserFirstName
						+ " "
						+ mailUserLastName
						+ ","
						+ "\n\n"
						+ "Yippe, you are getting popular! "
						+ dashBean.getCurrentEventSecurityUser()
								.getSecurityUser().getUserName()
						+ " has invited to join another group, "
						+ dashBean.getCurrentEvent().getEventName()
						+ ".\n"
						+ "\n\n"
						+ "As you might have already known, Coombu is a photo social application with web and mobile capabilities. You can upload your photos, and comment on and like the photos uploaded by the members of the group. If you think that any of the photo is offensive or inappropriate, you can flag and remove from the group dashboard."
						+ "\n\n"
						+ "Please click on the following link, You can accept or deny the invitation to join  "
						+ dashBean.getCurrentEvent().getEventName()
						+ ".\n"
						+ "\n\n"
						+ "<a href=\""
						+ protocol
						+ "://"
						+ host
						+ ":"
						+ port
						+ "/coombu/login.xhtml\">Login</a>"
						+ "\n\n"
						+ "if you have any questions, please feel free to send an email to support@coombu.com\n"
						+ "\n\n" + "Have fun!\n" + "Cheers,\n" + "Coombu";

				// sending email!
				emailService.sendEmail("coombu@caseopinion.com",
						"Registration - [Updated]", emailContent, mailUserMail);

				msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Mail sent!", "");
				log.debug("resend mail send");
			}

		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"Please make sure whether the status of the user is in pending mode!");
		}

		if (msg != null) {
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		return null;
	}

	public String block() {
		log.debug("Block is called");
		FacesMessage msg = null;
		if (securityUser.getSecurityUser().getUserStatusId() == (short) 1) {
			if (userService.blockOrUnBlockThisUser(
					securityUser.getSecurityUser(),
					dashBean.getCurrentEventId(), USER_STATUS_TYPE.BLOCKED)) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"User is blocked");
				securityUser.setUserStatusTypeId(5);

			} else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"Error while blocking the user!");
			}
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"This User not in Active mode!");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
	}

	public String unBlock() {
		log.debug("Unblock is called");
		FacesMessage msg = null;
		if (userService.blockOrUnBlockThisUser(securityUser.getSecurityUser(),
				dashBean.getCurrentEventId(), USER_STATUS_TYPE.ACTIVE)) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"User is unblocked");
			securityUser.setUserStatusTypeId(1);
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					"Error while blocking the user!");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
	}

	public String uploadAndImport() {
		log.debug("File name is:" + inputCsvFile);
		return null;
	}

	public void handleFileUpload(FileUploadEvent event) {

		FacesMessage msg = null;
		String role = null;
		String line = "";
		String cvsSplitBy = ",";
		boolean status = true;

		acknowledgeOfUpload = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					event.getFile().getInputstream()));
			while ((line = reader.readLine()) != null) {
				String[] contents = line.split(cvsSplitBy);
				if (contents.length > 4) {
					status = false;
					break;
				}
				System.out.println(Arrays.toString(contents));

				lastname = contents[1];
				firstname = contents[0];
				userName = contents[2];
				emailaddress = contents[2];
				role = contents[3];

				role = performClean(role);

				log.debug("role is: " + role);

				if (role == null) {
					log.debug("role is failed for this user!");
					msg = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"",
							"File is invalid! please make sure that you have provided proper role values for users!");
					break;
				} else {

					if (role.equals("SR")) {
						log.debug("role is ok for this user");
						userRoleId = 2;
					} else if (role.equals("SM")) {
						log.debug("role is ok for this user");
						userRoleId = 1;
					} else {
						log.debug("role is failed for this user!");
						msg = new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"",
								"File is invalid! please make sure that you have provided proper role values for users!");
						break;
					}
				}

				lastname = performClean(lastname);

				firstname = performClean(firstname);

				userName = performClean(userName);

				emailaddress = userName;

				log.debug("User Name: " + userName);
				log.debug("First Name:" + firstname);
				log.debug("Last Name:" + lastname);
				log.debug("Email :" + emailaddress);
				log.debug("User role:" + userRoleId);

				if (userName == null || userName.length() <= 0
						|| firstname == null || firstname.length() <= 0
						|| lastname == null || lastname.length() <= 0
						|| emailaddress == null || emailaddress.length() <= 0
						|| userRoleId == 0) {
					log.debug("values that you have provided for this user is invalid!");
					msg = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"",
							"File is invalid! please make sure that the file has only four columns with proper values.");
					break;
				} else {
					log.debug("values that you have provided for this user is valid!");

					if (createUser()) {
						acknowledgeOfUpload.add("<b>" + userName
								+ "</b> is added!");
					} else {
						acknowledgeOfUpload
								.add("<b>"
										+ userName
										+ "</b> is not added, please check the username or user existence in this group!");
					}
				}

			}
			/*
			 * msg = new FacesMessage("", event.getFile().getFileName() +
			 * " is uploaded and imported successfully!");
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error while extracting the contents from the file!", "");
		}
		if (!status && msg == null) {
			msg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"",
					"File is invalid! please make sure that the file has only four columns with proper values.");
		}

		if (msg != null) {

			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String performClean(String inputStr) {
		inputStr = inputStr.replaceAll("'", "");
		inputStr = inputStr.replaceAll("\"", "");
		inputStr = inputStr.trim();

		return inputStr;
	}

	public String getUserStatusByStatusId(int statusId) {

		if (statusId == USER_STATUS_TYPE.ACTIVE.id()) {
			return "ACT";
		} else if (statusId == USER_STATUS_TYPE.INACTIVE.id()) {
			return "INACT";
		} else if (statusId == USER_STATUS_TYPE.DELETED.id()) {
			return "DEL";
		} else if (statusId == USER_STATUS_TYPE.PENDING.id()) {
			return "PEN";
		} else if (statusId == USER_STATUS_TYPE.BLOCKED.id()) {
			return "BLO";
		}
		return null;
	}

}
