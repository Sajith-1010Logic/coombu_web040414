package com.coombu.photobook.dao.lookup;

import com.coombu.photobook.model.lookup.Privilege;

public interface IPrivilege {

	public abstract void save(Privilege transientInstance);

	public abstract void delete(Privilege persistentInstance);

	public abstract Privilege merge(Privilege detachedInstance);

	public abstract Privilege findById(short id);

}