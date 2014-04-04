package com.coombu.photobook.dao;

import com.coombu.photobook.model.UserProfile;

public interface IUserProfileDao {

	public abstract void save(UserProfile transientInstance);

	public abstract void delete(UserProfile persistentInstance);

	public abstract UserProfile merge(UserProfile detachedInstance);

	public abstract UserProfile findById(int id);

	public abstract UserProfile getUserProfileBySecurityUserId(long securityUserId);

}