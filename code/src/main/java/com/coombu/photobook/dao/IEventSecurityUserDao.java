package com.coombu.photobook.dao;

import java.util.List;
import java.util.Map;

import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.SecurityUser;

public interface IEventSecurityUserDao extends IDao{

	public abstract void save(EventSecurityUser transientInstance);

	public abstract void delete(EventSecurityUser persistentInstance);

	public abstract EventSecurityUser merge(EventSecurityUser detachedInstance);

	public abstract EventSecurityUser findById(int id);

	public abstract List<EventSecurityUser> getEventSecurityUser(
			long securityUserId);

	public abstract EventSecurityUser getEventSecurityUser(long securityUserId,
			long eventId);

	public abstract List<EventSecurityUser> getEventSecurityUsersByEventId(
			long eventId, boolean wantActive, boolean wantInActive,
			boolean wantDeleted, boolean wantPending, boolean wantBlocked);

	public abstract SecurityUser getEventSecurityUserById(long eventSecUserId);

	public abstract boolean checkUserExistenceBasedOnRole(long eventId,
			long roleId, long eventSecUserId);

	public abstract boolean incrementImageVoteCount(long eventSecUserId);
	
	public abstract boolean decrementImageVoteCount(long eventSecUserId);
	
	public Map<String, Object> validateResendEmail(long securityUserId, long currentEventId);
	
}