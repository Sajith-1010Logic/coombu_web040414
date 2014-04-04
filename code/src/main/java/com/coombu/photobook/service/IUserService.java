package com.coombu.photobook.service;

import java.util.List;
import java.util.Map;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.model.User;
import com.coombu.photobook.model.UserProfile;
import com.coombu.photobook.presentation.UserDataModel;
import com.coombu.photobook.presentation.search.UserSearchCriteria;
import com.coombu.photobook.util.Constants.USER_STATUS_TYPE;

public interface IUserService {

	public List<SecurityUser> findUser(UserSearchCriteria searchCriteria,
			int first, String sortField, boolean ascending);

	public int getNumberOfUsers(UserSearchCriteria searchCriteria);

	public void updateUserStatus(UserDataModel users);

	public SecurityUser findUser(String userid);

	public boolean updatePassword(SecurityUser securityUser, String userId);

	public boolean validateResetPassword(SecurityUser securityUser,
			String userType);

	public boolean resetPassword(SecurityUser securityUser);

	public void updateLoginHistory(User user, int loginStatusType,
			String sessionId);

	/**
	 * 
	 * @param user
	 * @return true if profile has been updated during the session, false
	 *         otherwise
	 */
	public void updateLogoutHistory(User user);

	public List<EventSecurityUser> getEventSecurityUser(long securityUserId);

	public EventSecurityUser getEventSecurityUser(long securityUserId,
			long eventId);

	public boolean isEventIdAllowedAccess(long eventId);

	/**
	 * Login method user by Web Services to valida userId and password
	 * 
	 * @param userId
	 * @param password
	 */
	public SecurityUser wsLogin(String userId, String password);

	public UserProfile getUserProfile(long securityUserId);

	public void saveUserProfile(UserProfile profile);

	public List<EventSecurityUser> getEventSecurityUsersByEventId(long eventId,
			boolean wantActive, boolean wantInActive, boolean wantDeleted,
			boolean wantPending, boolean wantBlocked);

	public boolean isUserLead(long eventId, Long securityUserId);

	public List<EventSecurityUser> userSeenPhoto(
			EventSecurityUser currentEventSecurityUser);

	public List<EventSecurityUser> loggedInUsers(long currentEventId);

	// boolean findUser(SecurityUser user, String userType);
	public List<EventSecurityUser> friends(
			EventSecurityUser currentEventSecurityUser);

	public List<SecurityUser> getUsers();

	public boolean updateSaveUser(SecurityUser user);

	public boolean deleteUser(SecurityUser user, long eventId);

	public Map<String, Object> checkUserExistenceByEmailId(long eventId,
			String email, String userName);

	public boolean blockOrUnBlockThisUser(SecurityUser user, long eventId,
			USER_STATUS_TYPE type);

	public SecurityUser getSecurityUser(long eventId, String email,
			String userName);

	public boolean checkUserExistsBasedOnRole(long eventId, long roleId,
			long eventSecUserId);

	public String generateTempPassword();

	public String hashPassword(String rawPassword);

	boolean isPasswordValid(String rawPassword, String encodedPassword);

	public void updateEventSecurityUser(EventSecurityUser userEvent);

	public Map<String, Object> validateResendMail(long securityUserId, long currentEventId);
	
}
