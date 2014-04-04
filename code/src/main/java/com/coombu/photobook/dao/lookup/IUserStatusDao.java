package com.coombu.photobook.dao.lookup;

import java.util.List;

import com.coombu.photobook.model.lookup.UserStatus;

public interface IUserStatusDao {

	public abstract void save(UserStatus transientInstance);

	public abstract void delete(UserStatus persistentInstance);

	public abstract UserStatus merge(UserStatus detachedInstance);

	public abstract UserStatus findById(short id);
	
	public List<UserStatus> getAll();

}