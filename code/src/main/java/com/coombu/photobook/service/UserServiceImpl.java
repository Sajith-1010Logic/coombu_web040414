package com.coombu.photobook.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coombu.photobook.dao.ICommentDao;
import com.coombu.photobook.dao.IEventDao;
import com.coombu.photobook.dao.IEventSecurityUserDao;
import com.coombu.photobook.dao.ILoginHistoryDao;
import com.coombu.photobook.dao.ISecurityUserDao;
import com.coombu.photobook.dao.IUserProfileDao;
import com.coombu.photobook.model.Event;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.LoginHistory;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.SecurityUserRole;
import com.coombu.photobook.model.User;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.presentation.UserDataModel;
import com.coombu.photobook.presentation.search.UserSearchCriteria;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;
import com.coombu.photobook.util.CoombuUtil;

/**
 * @author Fekade Aytaged
 * 
 */
@Transactional
@Service("userService")
public class UserServiceImpl implements IUserService, UserDetailsService,
		Serializable {
	private static final long serialVersionUID = 1L;

	private final static Logger log = LoggerFactory
			.getLogger(UserServiceImpl.class);

	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+={}|\\:<>,/?";

	private static Random rnd = new Random();

	private static int passwordLength = 8;

	@Autowired
	private transient BCryptPasswordEncoder passwordEncoder;

	@Autowired
	ISecurityUserDao securityUserDao = null;

	@Autowired
	private ILoginHistoryDao loginHistoryDao;

	@Autowired
	private IReferenceData referenceData;

	@Autowired
	private IEventSecurityUserDao eventSecurityUserDao;

	@Autowired
	private IUserProfileDao userProfileDao;

	@Autowired
	private IEventDao eventDao;

	@Autowired
	private ICommentDao commentDao;

	public IUserProfileDao getUserProfileDao() {
		return userProfileDao;
	}

	public void setUserProfileDao(IUserProfileDao userProfileDao) {
		this.userProfileDao = userProfileDao;
	}

	/**
	 * @param passwordEncoder
	 *            the passwordEncoder to set
	 */
	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * @return the passwordEncoder
	 */
	public BCryptPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobookservice.UserService#findUser(com.coombu.photobooksearch
	 * . UserSearchCriteria, int, java.lang.String, boolean)
	 */
	@Override
	@Transactional
	public List<SecurityUser> findUser(UserSearchCriteria searchCriteria,
			int firstResult, String sortField, boolean ascending) {
		Order ascDesc = null;
		if (sortField == null) {
			if (searchCriteria.getSortBy() == null
					|| searchCriteria.getSortBy().isEmpty())
				sortField = "lastName"; //$NON-NLS-1$
			else
				sortField = searchCriteria.getSortBy();
		}
		if (ascending)
			ascDesc = Order.asc(sortField);
		else
			ascDesc = Order.desc(sortField);

		return securityUserDao.findUser(searchCriteria, firstResult, sortField,
				ascDesc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coombu.photobookservice.UserService#getNumberOfUsers(com.coombu.photobook
	 * search.UserSearchCriteria)
	 */
	@Override
	@Transactional
	public int getNumberOfUsers(UserSearchCriteria searchCriteria) {
		return securityUserDao.getNumberOfUsers(searchCriteria);
	}

	@Override
	@Transactional
	public void updateUserStatus(UserDataModel users) {
	}

	@Override
	@Transactional
	public SecurityUser findUser(String userId) {
		return securityUserDao.findUser(userId);
	}

	@Override
	@Transactional
	public boolean updatePassword(SecurityUser user, String userId) {
		SecurityUser dbUser = findUser(userId);
		boolean passValidation = true;
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			FacesMessage fmsg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					Messages.getString("UserService.password.confirm.not.match"), ""); //$NON-NLS-1$ //$NON-NLS-2$
			FacesContext.getCurrentInstance().addMessage(null, fmsg);
			passValidation = false;
		}
		if (dbUser == null) {
			FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Messages.getString("UserService.account.not.found"), ""); //$NON-NLS-1$ //$NON-NLS-2$
			FacesContext.getCurrentInstance().addMessage(null, fmsg);
			passValidation = false;
		}
		if (!passwordEncoder.matches(user.getCurrentPassword(),
				dbUser.getPasswordHash())) {
			FacesMessage fmsg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					Messages.getString("UserService.password.not.match.database"), ""); //$NON-NLS-1$ //$NON-NLS-2$
			FacesContext.getCurrentInstance().addMessage(null, fmsg);
			passValidation = false;
		}
		if (user.getPassword().contains(user.getCurrentPassword())) {
			FacesMessage fmsg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					Messages.getString("UserService.same.password.not.allowed"), ""); //$NON-NLS-1$ //$NON-NLS-2$
			FacesContext.getCurrentInstance().addMessage(null, fmsg);
			passValidation = false;
		}
		if (passValidation == false)
			return false;
		// dbUser.setSecurityQuestionId(user.getSecurityQuestionId());
		// dbUser.setUserResponse(user.getUserResponse());
		String passwordHash = passwordEncoder.encode(user.getPassword());
		dbUser.setPasswordHash(passwordHash);
		securityUserDao.update(dbUser);
		FacesMessage fmsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				Messages.getString("UserService.password.updated"), ""); //$NON-NLS-1$ //$NON-NLS-2$
		FacesContext.getCurrentInstance().addMessage(null, fmsg);
		return true;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder(12);
		String myhash = bpe.encode("testuser");
		log.debug("password hash = : {}", myhash);
		SecurityUser user = findUser(username);
		if (user == null) {
			log.debug("Login attempted for UserId: {} - not found", username); //$NON-NLS-1$
			throw new UsernameNotFoundException(
					"UserServiceImpl.notFound Username " + username + " not found"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		Timestamp lastLoggedIn = securityUserDao.getLastLoggedInTimestamp(user
				.getSecurityUserId());
		boolean isAccountActive = false;
		if (user.getUserStatusId() == USER_STATUS_TYPE.ACTIVE.id()
				|| user.getUserStatusId() == USER_STATUS_TYPE.PENDING.id())
			isAccountActive = true;
		User springUser = new User(
				username,
				user.getPasswordHash(),
				isAccountActive, // enabled - administrative
									// enablement/disablement
				true, // NonExpired
				true, // credentialsNonExpired
				true, // NonLocked - automatic suspension due to # number of
						// login Attempts
				user.getSecurityUserId(), user.getUserProfile() == null ? ""
						: user.getUserProfile().getFirstName(),
				user.getUserProfile() == null ? "" : user.getUserProfile()
						.getLastName(), user.getUserStatusId(), lastLoggedIn,
				getRoles(user));
		return springUser;
	}

	@Transactional
	public Set<GrantedAuthority> getRoles(SecurityUser user) {
		HashSet<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		// if Registration is pending, no access is given.
		if (user == null
				|| user.getUserStatusId() == USER_STATUS_TYPE.PENDING.id())
			return roles;
		for (SecurityUserRole role : user.getSecurityUserRoles()) {
			// String roleName = role.getSecurityRole().getRoleName();
			String roleName = referenceData.getRoleName(role.getRoleId());
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority(roleName);
			roles.add(auth);
		}
		return roles;
	}

	@Override
	@Transactional
	public boolean validateResetPassword(SecurityUser user, String userType) {

		SecurityUser dbUser = this.findUser(user.getUserName());
		if (user.getResetAnswer() == null
				|| !user.getResetAnswer().equals(dbUser.getUserResponse())) {
			FacesMessage fmsg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					Messages.getString("UserService.security.answer.notmatch"), ""); //$NON-NLS-1$ //$NON-NLS-2$
			FacesContext.getCurrentInstance().addMessage(null, fmsg);
			return false;
		}
		if (user.getUserProfile() == null)
			user.setUserProfile(new UserProfile());
		user.getUserProfile().setFirstName(
				dbUser.getUserProfile().getFirstName());
		user.getUserProfile()
				.setLastName(dbUser.getUserProfile().getLastName());

		return true;
	}

	@Override
	@Transactional
	public boolean resetPassword(SecurityUser user) {
		String passwordHash = passwordEncoder.encode(user.getPassword());
		return securityUserDao.resetPassword(user, passwordHash);
	}

	@Override
	@Transactional
	public void updateLoginHistory(User user, int loginStatusType,
			String sessionId) {
		LoginHistory history = new LoginHistory();
		Date now = new Date();
		history.setLoginTimestamp(new Timestamp(now.getTime()));
		history.setSecurityUserId(user.getSecurityUserId());
		history.setSessionId(sessionId);
		loginHistoryDao.save(history);
		user.setLoginHistory(history);
	}

	/**
	 * Updates the login history table when the user logs out or the session
	 * times out
	 * 
	 */
	@Override
	@Transactional
	public void updateLogoutHistory(User user) {
		LoginHistory history = user.getLoginHistory();
		if (history != null) {
			history.setLogoutTimestamp(new Timestamp((new Date()).getTime()));
			loginHistoryDao.saveOrUpdate(history);
		}
	}

	@Override
	@Transactional
	public List<EventSecurityUser> getEventSecurityUser(long securityUserId) {
		return eventSecurityUserDao.getEventSecurityUser(securityUserId);
	}

	@Override
	@Transactional
	public EventSecurityUser getEventSecurityUser(long securityUserId,
			long eventId) {
		return eventSecurityUserDao.getEventSecurityUser(securityUserId,
				eventId);
	}

	@Override
	@Transactional
	public boolean isEventIdAllowedAccess(long eventId) {
		long securityUserId = CoombuUtil.getSecurityUserId();
		List<EventSecurityUser> userEvents = getEventSecurityUser(securityUserId);
		for (EventSecurityUser esu : userEvents) {
			if (eventId == esu.getEvent().getEventId())
				return true;
		}
		return false;
	}

	@Override
	public SecurityUser wsLogin(String userId, String password) {
		int response = -1;
		SecurityUser user = securityUserDao.findUser(userId);
		if ( userId == null || password == null)
			return null;
		else if ( !userId.equals(user.getUserName()) || !password.equals(user.getPasswordHash()))
				return user;
		else 
			return null;
		
		}

	@Override
	public UserProfile getUserProfile(long securityUserId) {
		return userProfileDao.getUserProfileBySecurityUserId(securityUserId);
	}

	@Override
	public void saveUserProfile(UserProfile userProfile) {
		userProfileDao.merge(userProfile);
	}

	@Override
	public boolean isUserLead(long eventId, Long securityUserId) {
		boolean isUserLead = false;
		Event event = eventDao.findById(eventId);
		if (event.getSecurityUser().getSecurityUserId() == securityUserId) {
			isUserLead = true;
		}
		return isUserLead;
	}

	@Override
	public List<EventSecurityUser> userSeenPhoto(
			EventSecurityUser currentEventSecurityUser) {
		throw new UnsupportedOperationException();

	}

	@Override
	public List<EventSecurityUser> loggedInUsers(long eventId) {
		return loginHistoryDao.getLoggedInUsers(eventId);
	}

	@Override
	public List<EventSecurityUser> friends(
			EventSecurityUser currentEventSecurityUser) {
		throw new UnsupportedOperationException();

	}

	@Override
	public List<SecurityUser> getUsers() {

		return securityUserDao.getUsers();
	}

	@Override
	public boolean updateSaveUser(SecurityUser user) {
		SecurityUser result = null;
		result = securityUserDao.merge(user);
		if (result == null)
			return false;
		else
			return true;
	}

	@Override
	public boolean deleteUser(SecurityUser user, long eventId) {

		EventSecurityUser eveSecUser = null;
		Iterator iterator = user.getEventSecurityUsers().iterator();
		while (iterator.hasNext()) {
			EventSecurityUser usr = (EventSecurityUser) iterator.next();
			if (usr.getEvent().getEventId() == eventId) {
				eveSecUser = usr;
				log.debug("user object is found for this event!");
				break;
			}
		}

		// if this secuirty user has only one subscription
		if (user.getEventSecurityUsers().size() <= 1) {

			// if the event security user is in pending state
			if (eveSecUser.getUserStatusTypeId() == USER_STATUS_TYPE.PENDING
					.id()) {
				securityUserDao.delete(user);
				log.debug("security user is removed from the database!");
				return true;
			} else {
				return markThisUserAsDeleted(user, eventId);
			}
		} else {

			// if user has two or more subscriptions and the event security user
			// is in pending state for this event
			if (eveSecUser.getUserStatusTypeId() == USER_STATUS_TYPE.PENDING
					.id()) {
				eventSecurityUserDao.delete(eveSecUser);
				log.debug("event security user is removed from the database!");
				return true;
			} else {
				return markThisUserAsDeleted(user, eventId);
			}
		}
	}

	public boolean markThisUserAsDeleted(SecurityUser user, long eventId) {
		EventSecurityUser eveSecUser = eventSecurityUserDao
				.getEventSecurityUser(user.getSecurityUserId(), eventId);
		eveSecUser.setUserStatusTypeId((short) USER_STATUS_TYPE.DELETED.id());
		EventSecurityUser result = null;
		result = eventSecurityUserDao.merge(eveSecUser);
		if (result == null) {
			log.debug("user is marked as delete in event security user table!");
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Map<String, Object> checkUserExistenceByEmailId(long eventId,
			String email, String userName) {
		log.debug("Entered  checkUserExistenceByEmailId");
		Map<String, Object> result = securityUserDao
				.checkUserExistenceByEmailId(eventId, email, userName);
		log.debug("Exited  checkUserExistenceByEmailId");
		return result;
	}

	@Override
	public boolean blockOrUnBlockThisUser(SecurityUser user, long eventId,
			USER_STATUS_TYPE type) {
		// TODO Auto-generated method stub
		EventSecurityUser eveSecUser = eventSecurityUserDao
				.getEventSecurityUser(user.getSecurityUserId(), eventId);
		eveSecUser.setUserStatusTypeId((short) type.id());
		EventSecurityUser result = null;
		result = eventSecurityUserDao.merge(eveSecUser);
		if (result == null)
			return false;
		else
			return true;
	}

	@Override
	public SecurityUser getSecurityUser(long eventId, String email,
			String userName) {
		// TODO Auto-gensenull;
		return securityUserDao.getSecurityUser(eventId, email, userName);
	}

	@Override
	public boolean checkUserExistsBasedOnRole(long eventId, long roleId,
			long eventSecUserId) {
		// TODO Auto-generated method stub
		return eventSecurityUserDao.checkUserExistenceBasedOnRole(eventId,
				roleId, eventSecUserId);
	}

	@Override
	public String generateTempPassword() {
		StringBuilder sb = new StringBuilder(passwordLength);
		for (int i = 0; i < passwordLength; i++) {
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString();
	}

	@Override
	public String hashPassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean isPasswordValid(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);

	}

	@Override
	public void updateEventSecurityUser(EventSecurityUser userEvent) {
		eventSecurityUserDao.update(userEvent);
	}

	@Override
	public List<EventSecurityUser> getEventSecurityUsersByEventId(long eventId,
			boolean wantActive, boolean wantInActive, boolean wantDeleted,
			boolean wantPending, boolean wantBlocked) {
		return this.eventSecurityUserDao.getEventSecurityUsersByEventId(
				eventId, wantActive, wantInActive, wantDeleted, wantPending,
				wantBlocked);
	}

	@Override
	public Map<String, Object> validateResendMail(long securityUserId,
			long currentEventId) {
		return eventSecurityUserDao.validateResendEmail(securityUserId,
				currentEventId);
	}

}
