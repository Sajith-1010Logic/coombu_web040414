package com.coombu.photobook.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.presentation.search.UserSearchCriteria;

public interface ISecurityUserDao {

	public abstract void save(SecurityUser transientInstance);

	public abstract void delete(SecurityUser persistentInstance);

	public abstract SecurityUser merge(SecurityUser detachedInstance);

	public abstract SecurityUser findById(int id);
	public abstract List<SecurityUser> findUser(
			UserSearchCriteria searchCriteria, int firstResult,
			String sortField, Order ascDesc);

	public abstract int getNumberOfUsers(UserSearchCriteria searchCriteria);

	public abstract SecurityUser findUser(String userId);

	public abstract Timestamp getLastLoggedInTimestamp(Long securityUserId);

	public abstract boolean resetPassword(SecurityUser user, String passwordHash);

	public abstract void update(SecurityUser dbUser);
	
	public abstract List<SecurityUser> getUsers();
	
	public abstract Map<String, Object> checkUserExistenceByEmailId(long eventId, String email,String userName);
	
	public abstract SecurityUser getSecurityUser(long eventId, String email,String userName);
}