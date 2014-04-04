package com.coombu.photobook.dao.lookup;

import java.util.List;

import com.coombu.photobook.model.lookup.Role;

public interface IRoleDao {

	public abstract void save(Role transientInstance);

	public abstract void delete(Role persistentInstance);

	public abstract Role merge(Role detachedInstance);

	public abstract Role findById(short id);
	
	public List<Role> getAll();
}